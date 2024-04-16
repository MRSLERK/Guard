package me.mrslerk.guard.listener.player;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerBucketEmptyEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;

public class PlayerBucketEmptyListener extends PlayerListener implements Listener {


    public PlayerBucketEmptyListener(@NonNull GuardManager plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull PlayerBucketEmptyEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        Block blockClicked = event.getBlockClicked();
        if (isFlagDenied(player, "bucket", blockClicked)) {
            event.setCancelled(true);
        }
    }
}
