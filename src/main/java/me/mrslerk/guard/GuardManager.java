package me.mrslerk.guard;


import cn.nukkit.Player;
import cn.nukkit.block.fake.FakeStructBlock;
import cn.nukkit.event.Listener;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;
import lombok.NonNull;
import me.mrslerk.guard.command.RegionCommand;
import me.mrslerk.guard.data.Region;
import me.mrslerk.guard.event.region.RegionCreateEvent;
import me.mrslerk.guard.event.region.RegionRemoveEvent;
import me.mrslerk.guard.event.region.RegionSaveEvent;
import me.mrslerk.guard.listener.block.*;
import me.mrslerk.guard.listener.entity.EntityCombustListener;
import me.mrslerk.guard.listener.entity.EntityDamageListener;
import me.mrslerk.guard.listener.entity.EntityExplodeListener;
import me.mrslerk.guard.listener.player.*;
import me.mrslerk.guard.task.RegionSaveTask;
import me.mrslerk.guard.utils.GroupConfig;
import me.mrslerk.guard.utils.Metrics;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class GuardManager extends PluginBase {

    @Getter
    public static GuardManager instance;
    public final HashMap<String, Position> firstPos = new HashMap<>();
    public final HashMap<String, Position> secondPos = new HashMap<>();
    @Getter
    public final HashMap<String, Boolean> defaultFlags = new HashMap<>();
    public final HashMap<String, FakeStructBlock> selections = new HashMap<>();
    private final HashMap<String, HashMap<String, Region>> regions = new HashMap<>();
    private Config messages;
    private Config config;
    private GroupConfig group;

    public static void metricsStart() {
        try {
            int pluginId = 18883;
            Metrics metrics = new Metrics(getInstance(), pluginId);
            metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> getInstance().getServer().getNukkitVersion()));
            getInstance().getLogger().info(TextFormat.GREEN + "Loaded Metrics");
        } catch (Exception e) {
            getInstance().getLogger().info(TextFormat.GREEN + "Can't load metrics");
        }
    }

    @Override
    public void onLoad() {
        instance = this;
        saveResource("config.yml");
        saveResource("group.yml");
        group = new GroupConfig(getDataFolder() + "/group.yml");
        config = new Config(getDataFolder() + "/config.yml");
        saveResource("messages-" + config.getString("language") + ".yml");
        messages = new Config(getDataFolder() + "/messages-" + config.getString("language") + ".yml");
        registerFlags();
    }

    @Override
    public void onEnable() {
        loadRegions();
        loadListener();
        getServer().getCommandMap().register(getName(), new RegionCommand("rg", "guard"));
        metricsStart();
    }

    private void registerFlags() {
        for (Map.Entry<String, Object> entry : config.getSection("default-flags").entrySet()) {
            String flag = entry.getKey();
            Object value = entry.getValue();
            defaultFlags.put(flag, (Boolean) value);
        }
    }

    private void loadListener() {
        Listener[] list = {
                new PlayerInteractListener(this),
                new BlockBreakListener(this),
                new BlockPlaceListener(this),
                new FarmLandDecayListener(this),
                new EntityDamageListener(this),
                new EntityExplodeListener(this),
                new LeavesDecayListener(this),
                new PlayerBucketEmptyListener(this),
                new PlayerBucketFillListener(this),
                new ItemFrameUseListener(this),
                new EntityBlockChangeListener(this),
                new DoorToggleListener(this),
                new PlayerTeleportListener(this),
                new EntityCombustListener(this),
                new PlayerDropListener(this),
                new BlockPistonListener(this)
        };
        for (Listener listener : list) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void loadRegions() {
        File regionsDir = new File(getDataFolder() + "/regions");
        if (!regionsDir.exists()) {
            regionsDir.mkdir();
        }
        for (File regionFile : Objects.requireNonNull(regionsDir.listFiles())) {
            if (!regionFile.isFile()) {
                continue;
            }
            Config data = new Config(regionFile);
            HashMap<String, Boolean> flags = new HashMap<>();
            data.getSection("flags").forEach((flag, value) -> flags.put(flag, (boolean) value));
            Vector3 min = new Vector3(data.getDouble("pos.min.x"), data.getDouble("pos.min.y"), data.getDouble("pos.min.z"));
            Vector3 max = new Vector3(data.getDouble("pos.max.x"), data.getDouble("pos.max.y"), data.getDouble("pos.max.z"));
            String level = data.getString("pos.level");
            String name = data.getString("name");
            if (!regions.containsKey(level)) {
                regions.put(level, new HashMap<>());
            }
            HashMap<String, Region> map = regions.get(level);
            map.put(name, new Region(name, data.getString("owner"), level, data.getStringList("members"), flags, min, max));
            regions.put(level, map);
        }
    }

    public HashMap<String, HashMap<String, Region>> getAllRegions() {
        return regions;
    }

    public Region getRegionByName(@NonNull String name) {
        return regions.values().stream().flatMap(data -> data.values().stream()).filter(region -> region.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }


    public List<Region> getRegionList(@NonNull String nick, boolean include_member) {
        List<Region> list = new ArrayList<>();
        for (HashMap<String, Region> data : regions.values()) {
            for (Region region : data.values()) {
                if (region.getOwner().equalsIgnoreCase(nick)) {
                    list.add(region);
                    continue;
                }
                if (include_member) {
                    continue;
                }
                if (Arrays.stream(region.getMembers()).anyMatch((member) -> member.equalsIgnoreCase(nick))) {
                    list.add(region);
                }
            }
        }
        return list;
    }

    public Region getRegion(@NonNull Position pos) {
        int x = pos.getFloorX();
        int y = pos.getFloorY();
        int z = pos.getFloorZ();
        if (!regions.containsKey(pos.getLevel().getFolderName())) {
            return null;
        }

        return regions.get(pos.getLevel().getFolderName()).values().stream().filter(region -> region.getMinX() <= x && x <= region.getMaxX() && region.getMinY() <= y && y <= region.getMaxY() && region.getMinZ() <= z && z <= region.getMaxZ()).findFirst().orElse(null);
    }

    public List<Region> getOverride(@NonNull Position min, @NonNull Position max) {
        String level = min.getLevel().getFolderName();
        List<Region> list = new ArrayList<>();

        if (!level.equals(max.getLevel().getFolderName())) {
            return list;
        }
        if (!regions.containsKey(min.getLevel().getFolderName())) {
            return list;
        }

        list = regions.get(min.getLevel().getFolderName()).values().stream().filter(region -> level.equals(region.getLevelName())).filter(region -> region.getMinX() <= max.getX() && min.getX() <= region.getMaxX() && region.getMinY() <= max.getY() && min.getY() <= region.getMaxY() && region.getMinZ() <= max.getZ() && min.getZ() <= region.getMaxZ()).collect(Collectors.toList());
        return list;
    }

    public GroupConfig getGroupConfig() {
        return group;
    }

    public String getMessage(String key) {
        if (!messages.exists(key)) {
            return key;
        }
        return String.valueOf(messages.get(key));
    }

    public String getPlayerGroupId(@NonNull Player player) {
        return "0";//сюда группу ебануть надо
    }


    public int calculateSize(Vector3 pos1, Vector3 pos2) {
        float[] x = new float[]{(float) Math.min(pos1.getX(), pos2.getX()), (float) Math.max(pos1.getX(), pos2.getX())};
        float[] y = new float[]{0F, 1F};
        if (!config.getBoolean("full-height")) {
            y = new float[]{(float) Math.min(pos1.getY(), pos2.getY()), (float) Math.max(pos1.getY(), pos2.getY())};
        }
        float[] z = new float[]{(float) Math.min(pos1.getZ(), pos2.getZ()), (float) Math.max(pos1.getZ(), pos2.getZ())};
        return (int) ((x[1] - x[0]) * (y[1] - y[0]) * (z[1] - z[0]));
    }

    public void createRegion(@NonNull Vector3 min, @NonNull Vector3 max, @NonNull Level level, @NonNull String name, @NonNull String owner) {
        Region region = new Region(name, owner, level.getFolderName(), new ArrayList<>(), defaultFlags, min, max);
        RegionCreateEvent event = new RegionCreateEvent(this, region);
        getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        if (config.getBoolean("full-height")) {
            max.y = level.getMaxHeight();
            min.y = level.getMinHeight();
        }

        if (!regions.containsKey(level.getFolderName())) {
            regions.put(level.getFolderName(), new HashMap<>());
        }
        HashMap<String, Region> data = regions.get(level.getFolderName());
        data.put(name, region);
        regions.put(level.getFolderName(), data);
        saveRegion(region);
    }

    public void saveRegion(@NonNull Region region, boolean async) {
        RegionSaveEvent event = new RegionSaveEvent(this, region);
        getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }

        RegionSaveTask task = new RegionSaveTask(region, this);
        if (async) {
            getServer().getScheduler().scheduleAsyncTask(this, task);
        } else {
            task.onRun();
        }
    }

    public boolean isIgnoredFlag(@NonNull String flag, @NonNull Level level) {
        return config.getStringList("disabled-flags." + level.getFolderName().toLowerCase()).stream().anyMatch(f -> f.equalsIgnoreCase(flag));
    }


    public void saveRegion(@NonNull Region region) {
        saveRegion(region, config.getBoolean("async-save", true));
    }

    public void sendWarning(@NonNull Player player, String key) {
        String message = TextFormat.RED + "An internal error has occurred! Key: " + key + " not found.";
        if (!Objects.equals(getMessage(key), key)) {
            message = getMessage(key);
        } else {
            getLogger().error(message);
        }
        switch (config.getInt("notification-type", 2)) {
            case 0 -> player.sendPopup(message);
            case 1 -> player.sendMessage(message);
            case 2 -> player.sendTip(message);
        }
    }


    public void removeRegion(@NonNull Region region) {
        RegionRemoveEvent event = new RegionRemoveEvent(this, region);
        getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        HashMap<String, Region> data = regions.get(region.getLevelName());
        data.remove(region.getName());
        regions.put(region.getLevelName(), data);
        File file = new File(getDataFolder() + "/regions/" + region.getName() + ".yml");
        file.delete();
    }

    public void sendSelection(@NonNull Player player, @NonNull Vector3 pos1, @NonNull Vector3 pos2) {
        if (!config.getBoolean("show-selection")) {
            return;
        }
        removeSelection(player);
        if (config.getBoolean("full-height")) {
            pos1.y = player.getLevel().getMaxHeight();
            pos2.y = player.getLevel().getMinHeight();
        }
        String nick = player.getName();
        FakeStructBlock fakeStructBlock = new FakeStructBlock();
        fakeStructBlock.create(pos1.asBlockVector3(), pos2.asBlockVector3(), player);
        selections.put(nick, fakeStructBlock);
    }

    public void removeSelection(@NonNull Player player) {
        String nick = player.getName();
        if (selections.containsKey(nick)) {
            selections.remove(nick).remove(player);
        }
    }


    @Override
    public void onDisable() {
        for (Map.Entry<String, FakeStructBlock> entry : selections.entrySet()) {
            String nick = entry.getKey();
            Player player = getServer().getPlayerExact(nick);
            if (player != null) {
                removeSelection(player);
            }
        }
    }

}