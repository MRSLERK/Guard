package me.mrslerk.guard.listener.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityCombustEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;

public class EntityCombustListener extends EntityListener implements Listener {


    public EntityCombustListener(@NonNull GuardManager plugin) {
        super(plugin);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(EntityCombustEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            if (isFlagDenied(entity, "combust", null)) {
                event.setCancelled(true);
            }
        }
    }
}