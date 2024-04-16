package me.mrslerk.guard.listener.entity;

import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityExplodeEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.listener.block.BlockListener;

public class EntityExplodeListener extends BlockListener implements Listener {

    public EntityExplodeListener(@NonNull GuardManager plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        for (Block block : event.getBlockList()) {
            if (isFlagDenied(block, "explode", null)) {
                event.setCancelled(true);
                break;
            }
        }

    }
}