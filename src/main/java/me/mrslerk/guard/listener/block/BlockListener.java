package me.mrslerk.guard.listener.block;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;
import lombok.Getter;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;
import me.mrslerk.guard.event.flag.FlagCheckByBlockEvent;
import me.mrslerk.guard.event.flag.FlagCheckByPlayerEvent;


abstract public class BlockListener {

    @Getter
    private final GuardManager plugin;

    public BlockListener(@NonNull GuardManager plugin) {
        this.plugin = plugin;
    }

    public boolean isFlagDenied(@NonNull Position position, @NonNull String flag, Player player) {
        GuardManager api = getPlugin();
        if (api.isIgnoredFlag(flag, position.getLevel())) {
            return false;
        }

        if (player != null) {
            if (player.hasPermission("guard.noflag")) {
                return false;
            }
        }

        Region region = api.getRegion(position);

        if (region == null) {
            return false;
        }

        if (region.getFlagValue(flag)) {
            return false;
        }

        FlagCheckByBlockEvent event = new FlagCheckByBlockEvent(api, region, flag, position, player);
        api.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return event.isMainEventCancelled();
        }

        if (player == null) {
            return true;
        }

        String nick = player.getName().toLowerCase();

        if (nick.equalsIgnoreCase(region.getOwner()) || region.hasMember(nick)) {
            FlagCheckByPlayerEvent event2 = new FlagCheckByPlayerEvent(api, region, flag, player, position);
            api.getServer().getPluginManager().callEvent(event2);

            if (event2.isCancelled()) {
                return event2.isMainEventCancelled();
            }
            return false;
        }

        if (flag.equals("break") || flag.equals("place")) {
            LevelEventPacket packet = new LevelEventPacket();
            packet.evid = LevelEventPacket.EVENT_PARTICLE_BLOCK_FORCE_FIELD;
            packet.data = 1;
            Vector3 pos = position.clone().add(0.5, 0.5, 0.5);
            packet.x = (float) pos.x;
            packet.y = (float) pos.y;
            packet.z = (float) pos.z;
            player.dataPacket(packet);
        }

        if (flag.equals("break")) {
            Position pos = player.getPosition().subtract(position.getX(), position.getY(), position.getZ());
            pos.y = Math.abs(pos.y + 2);
            pos = pos.divide(8);
            player.setMotion(pos);
        }

        api.sendWarning(player, "warn_flag_" + flag);
        return true;
    }
}
