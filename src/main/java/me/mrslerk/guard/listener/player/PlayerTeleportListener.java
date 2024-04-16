package me.mrslerk.guard.listener.player;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerTeleportEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;

public class PlayerTeleportListener extends PlayerListener implements Listener {


    public PlayerTeleportListener(@NonNull GuardManager plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull PlayerTeleportEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (isFlagDenied(player, "teleport", null)) {
            event.setCancelled(true);
        }
    }
}
