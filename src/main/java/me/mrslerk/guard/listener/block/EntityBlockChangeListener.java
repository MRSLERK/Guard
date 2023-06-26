package me.mrslerk.guard.listener.block;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityBlockChangeEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;

public class EntityBlockChangeListener extends BlockListener implements Listener{


    public EntityBlockChangeListener(@NonNull GuardManager plugin){
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull EntityBlockChangeEvent event){
        if(event.isCancelled()){
            return;
        }

        Entity entity = event.getEntity();
        if(isFlagDenied(entity, "change", null)){
            event.setCancelled(true);
        }
    }
}