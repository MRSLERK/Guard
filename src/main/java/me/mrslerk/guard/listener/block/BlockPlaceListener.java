package me.mrslerk.guard.listener.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;

public class BlockPlaceListener extends BlockListener implements Listener {


    public BlockPlaceListener(@NonNull GuardManager plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (block == null) {
            return;
        }
        if (isFlagDenied(block, "place", player)) {
            event.setCancelled(true);
        }
    }
}