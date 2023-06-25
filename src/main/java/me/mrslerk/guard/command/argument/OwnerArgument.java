package me.mrslerk.guard.command.argument;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;

public class OwnerArgument extends Argument{

    public OwnerArgument(GuardManager plugin){
        super(plugin, "owner");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args){
        GuardManager api = getPlugin();
        if(!(sender instanceof Player)){
            sender.sendMessage(api.getMessage("no_console"));
            return false;
        }

        Player player = ((Player) sender).getPlayer();

        if(!player.hasPermission("command.guard.rg.owner")){
            player.sendMessage(api.getMessage("no_permission"));
            return false;
        }
        if(args.length != 3){
            player.sendMessage(api.getMessage("owner_help"));
            return false;
        }
        Region region = api.getRegionByName(args[1]);
        if(region == null){
            player.sendMessage(api.getMessage("rg_not_exist"));
            return false;
        }
        Player target = api.getServer().getPlayerExact(args[2]);
        if(target == null){
            player.sendMessage(api.getMessage("player_not_exist"));
            return false;
        }
        String nick = sender.getName().toLowerCase();

        if(!nick.equalsIgnoreCase(region.getOwner()) & !player.hasPermission("guard.all")){
            player.sendMessage(api.getMessage("player_not_owner"));
            return false;
        }
        if(target.getName().equalsIgnoreCase(region.getOwner())){
            player.sendMessage(api.getMessage("player_already_owner"));
            return false;
        }
        region.setOwner(target.getName());
        player.sendMessage(api.getMessage("owner_change").replace("{player}", target.getName()).replace("{region}", region.getName()));
        target.sendMessage(api.getMessage("owner_got_region").replace("{player}", region.getOwner()).replace("{region}", region.getName()));

        return true;
    }
}
