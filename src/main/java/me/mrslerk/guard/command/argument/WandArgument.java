package me.mrslerk.guard.command.argument;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import me.mrslerk.guard.GuardManager;

public class WandArgument extends Argument{

    public WandArgument(GuardManager plugin){
        super(plugin, "wand");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args){
        GuardManager api = getPlugin();
        if(!(sender instanceof Player)){
            sender.sendMessage(api.getMessage("no_console"));
            return false;
        }

        Player player = ((Player) sender).getPlayer();
        if(!player.hasPermission("command.guard.rg.wand")){
            player.sendMessage(api.getMessage("no_permission"));
            return false;
        }

        Item wand = Item.get(Item.WOODEN_AXE);

        if(!player.getInventory().canAddItem(wand)){
            player.sendMessage(api.getMessage("inventory_oversize"));
            return false;
        }

        player.getInventory().addItem(wand);
        player.sendMessage(api.getMessage("got_wand"));
        return true;
    }
}
