package me.mrslerk.guard.listener.player;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerDropItemEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;

public class PlayerDropListener extends PlayerListener implements Listener {


    public PlayerDropListener(@NonNull GuardManager plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull PlayerDropItemEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (isFlagDenied(player, "drop", null)) {
            event.setCancelled(true);
        }
    }
}
