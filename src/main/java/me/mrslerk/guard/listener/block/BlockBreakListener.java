package me.mrslerk.guard.listener.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.Position;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;

public class BlockBreakListener extends BlockListener implements Listener {


    public BlockBreakListener(@NonNull GuardManager plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }


        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (block == null) {
            return;
        }
        if (isFlagDenied(block, "break", player)) {
            event.setCancelled(true);
            return;
        }

        if (block instanceof BlockChest && isFlagDenied(block, "chest", player)) {
            event.setCancelled(true);
            return;
        }
        Item item = event.getItem();
        String nick = player.getName().toLowerCase();
        if (item != null) {
            if (item.getId() == ItemID.WOODEN_AXE & !player.isSneaking()) {
                Region region = getPlugin().getRegion(block);
                if (player.hasPermission("guard.access.wand")) {
                    if (region != null) {
                        if (!region.getOwner().equalsIgnoreCase(nick) && !player.hasPermission("guard.all")) {
                            getPlugin().sendWarning(player, "rg_override");
                            return;
                        }
                    }
                    getPlugin().firstPos.remove(nick);
                    if (getPlugin().secondPos.containsKey(nick)) {
                        Position second = getPlugin().secondPos.get(nick);
                        int size = getPlugin().calculateSize(second, block);
                        int maxSize = getPlugin().getGroupConfig().getInt("max-size", getPlugin().getPlayerGroupId(player));
                        if (size > maxSize) {
                            player.sendTip(getPlugin().getMessage("rg_oversize").replace("{max_size}", String.valueOf(maxSize)));
                            event.setCancelled(true);
                            return;
                        }
                        getPlugin().sendSelection(player, block, second);
                    }
                    getPlugin().firstPos.put(nick, block);
                    getPlugin().sendWarning(player, "pos_2_set");
                    event.setCancelled(true);
                }
            }
        }
    }
}