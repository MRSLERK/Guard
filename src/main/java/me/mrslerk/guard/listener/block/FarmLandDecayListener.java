package me.mrslerk.guard.listener.block;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.FarmLandDecayEvent;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;

public class FarmLandDecayListener extends BlockListener implements Listener {


    public FarmLandDecayListener(@NonNull GuardManager plugin) {
        super(plugin);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull FarmLandDecayEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            if (isFlagDenied(event.getBlock(), "change", (Player) entity)) {
                event.setCancelled(true);
            }
        }
    }
}