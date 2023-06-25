package me.mrslerk.guard.listener.block;

import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.LeavesDecayEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;

public class LeavesDecayListener extends BlockListener implements Listener{


    public LeavesDecayListener(@NonNull GuardManager plugin){
        super(plugin);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull LeavesDecayEvent event){
        if(event.isCancelled()){
            return;
        }
        Block block = event.getBlock();
        if(isFlagDenied(block, "decay", null)){
            event.setCancelled(true);
        }
    }
}