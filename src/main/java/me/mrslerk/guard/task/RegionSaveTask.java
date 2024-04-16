package me.mrslerk.guard.task;

import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;

public class RegionSaveTask extends AsyncTask {

    private final Region region;
    private final GuardManager plugin;

    public RegionSaveTask(Region region, GuardManager plugin) {
        this.region = region;
        this.plugin = plugin;
    }

    @Override
    public void onRun() {
        ConfigSection section = new ConfigSection();
        section.set("owner", region.getOwner());
        section.set("members", region.getMembers());
        section.set("flags", region.getFlags());
        section.set("name", region.getName());
        section.set("pos.min.x", region.getMinX());
        section.set("pos.min.y", region.getMinY());
        section.set("pos.min.z", region.getMinZ());
        section.set("pos.max.x", region.getMaxX());
        section.set("pos.max.y", region.getMaxY());
        section.set("pos.max.z", region.getMaxZ());
        section.set("pos.level", region.getLevelName());
        Config config = new Config(plugin.getDataFolder() + "/regions/" + region.getName() + ".yml", Config.YAML);
        config.setAll(section);
        config.save();
    }
}
