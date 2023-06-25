package me.mrslerk.guard.listener.block;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPistonEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;

public class BlockPistonListener extends BlockListener implements Listener{


    public BlockPistonListener(@NonNull GuardManager plugin){
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull BlockPistonEvent event){
        if(event.isCancelled()){
            return;
        }
        event.getBlocks().stream().filter(block -> isFlagDenied(block, "piston", null)).map(block -> true).forEach(event::setCancelled);
    }
}