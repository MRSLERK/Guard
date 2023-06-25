package me.mrslerk.guard.listener.player;

import cn.nukkit.Player;
import cn.nukkit.block.*;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.*;
import cn.nukkit.level.Position;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;

public class PlayerInteractListener extends PlayerListener implements Listener{


    public PlayerInteractListener(@NonNull GuardManager plugin){
        super(plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEvent(@NonNull PlayerInteractEvent event){
        if(event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();

        Block block = event.getBlock();
        if(block == null){
            return;
        }

        if(!event.getAction().equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)){
            return;
        }


        Item item = event.getItem();

        Region region = getPlugin().getRegion(block);
        String nick = player.getName().toLowerCase();

        if(item != null){
            if(item.isFertilizer()){ // BoneMeal
                if(isFlagDenied(player, "grow", block)){
                    event.setCancelled(true);
                }
            }
            if(player.hasPermission("guard.access.stick") && !player.isSneaking()){
                if(item.getId() == ItemID.STICK){
                    if(region == null){
                        getPlugin().sendWarning(player, "rg_not_exist");
                        event.setCancelled(true);
                        return;
                    }
                    player.sendTip(getPlugin().getMessage("rg_info")
                            .replace("{region}", region.getName())
                            .replace("{owner}", region.getOwner())
                            .replace("{member}", String.join(", ", region.getMembers())));
                    event.setCancelled(true);
                    return;
                }
            }


            if(item.getId() == ItemID.WOODEN_AXE & !player.isSneaking()){
                if(player.hasPermission("guard.access.wand")){
                    if(region != null){
                        if(!region.getOwner().equalsIgnoreCase(player.getName()) && !player.hasPermission("guard.all")){
                            getPlugin().sendWarning(player, "rg_override");
                            event.setCancelled(true);
                            return;
                        }
                    }
                    getPlugin().secondPos.remove(nick);
                    if(getPlugin().firstPos.containsKey(player.getName().toLowerCase())){
                        Position first = getPlugin().firstPos.get(player.getName().toLowerCase());
                        int size = getPlugin().calculateSize(first, block);
                        int maxSize = getPlugin().getGroupConfig().getInt("max-size", getPlugin().getPlayerGroupId(player));
                        if(size > maxSize){
                            player.sendTip(getPlugin().getMessage("rg_oversize").replace("{max_size}", String.valueOf(maxSize)));
                            event.setCancelled(true);
                            return;
                        }

                        getPlugin().sendSelection(player, block, first);
                    }
                    getPlugin().secondPos.put(nick, block);
                    getPlugin().sendWarning(player, "pos_1_set");
                    event.setCancelled(true);
                    return;
                }
            }
        }


        if(region == null){
            return;
        }


        String flag = null;


        if(block instanceof BlockChest){
            flag = "chest";

        }

        if(block instanceof BlockUndyedShulkerBox){
            flag = "shulker";

        }

        if(block instanceof BlockSmoker || block instanceof BlockBlastFurnace || block instanceof BlockFurnace){
            flag = "furnace";
        }

        if(block instanceof BlockDispenser){
            flag = "dispenser";
        }

        if(block instanceof BlockBarrel){
            flag = "barrel";
        }

        if(block instanceof BlockHopper){
            flag = "hopper";
        }

        if(block instanceof BlockBeacon){
            flag = "beacon";
        }

        if(block instanceof BlockBrewingStand){
            flag = "brewing_stand";
        }

        if(block instanceof BlockGrindstone || block instanceof BlockCraftingTable || block instanceof BlockSmithingTable || block instanceof BlockAnvil || block instanceof BlockEnchantingTable || block instanceof BlockStonecutterBlock){
            flag = "table";
        }

        if(item != null){
            if(item instanceof ItemTool && (block instanceof BlockDirt || block instanceof BlockLog)){
                flag = "change";
            }
        }

        if(block instanceof BlockRedstoneComparator || block instanceof BlockRedstoneRepeater){
            flag = "redstone";
        }

        if(item instanceof ItemFlint ||
                item instanceof ItemFireCharge){
            flag = "fire";
        }

        if(
                block instanceof BlockLever ||
                        block instanceof BlockButton ||
                        block instanceof BlockJukebox ||
                        block instanceof BlockNoteblock ||
                        block instanceof BlockComposter ||
                        block instanceof BlockFenceGate ||
                        block instanceof BlockBell ||
                        block instanceof BlockDaylightDetector ||
                        block instanceof BlockMobSpawner ||
                        block instanceof BlockTNT ||
                        block instanceof BlockDragonEgg ||
                        block instanceof BlockSignPost ||
                        block instanceof BlockCampfire ||
                        block instanceof BlockRespawnAnchor

        ){
            flag = "interact";
        }

        if(block instanceof BlockEnderChest){
            flag = "ender_chest";
        }

        if(block instanceof BlockBed){
            flag = "sleep";
        }

        if(flag == null){
            return;
        }

        if(isFlagDenied(player, flag, block)){
            event.setCancelled(true);
        }
    }
}
