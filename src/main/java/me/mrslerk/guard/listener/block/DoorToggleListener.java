package me.mrslerk.guard.listener.block;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.DoorToggleEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;

public class DoorToggleListener extends BlockListener implements Listener {


    public DoorToggleListener(@NonNull GuardManager plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull DoorToggleEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (isFlagDenied(event.getBlock(), "door", player)) {
            event.setCancelled(true);
        }
    }
}