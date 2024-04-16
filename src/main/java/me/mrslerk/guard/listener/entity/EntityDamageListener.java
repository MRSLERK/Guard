package me.mrslerk.guard.listener.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;

public class EntityDamageListener extends EntityListener implements Listener {


    public EntityDamageListener(@NonNull GuardManager plugin) {
        super(plugin);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            if (isFlagDenied(entity, "damage", null)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (!(damager instanceof Player)) {
                return;
            }

            var flag = entity instanceof Player ? "pvp" : "mob";

            if (isFlagDenied(damager, flag, entity)) {
                event.setCancelled(true);
            }
        }
    }
}