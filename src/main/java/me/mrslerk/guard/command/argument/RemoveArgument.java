package me.mrslerk.guard.command.argument;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;

public class RemoveArgument extends Argument{

    public RemoveArgument(GuardManager plugin){
        super(plugin, "remove");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args){
        GuardManager api = getPlugin();
        if(!(sender instanceof Player)){
            sender.sendMessage(api.getMessage("no_console"));
            return false;
        }

        Player player = ((Player) sender).getPlayer();
        String nick = sender.getName().toLowerCase();

        if(!player.hasPermission("command.guard.rg.remove")){
            player.sendMessage(api.getMessage("no_permission"));
            return false;
        }
        if(args.length == 1){
            player.sendMessage(api.getMessage("remove_help"));
            return false;
        }

        String name = args[1].toLowerCase();
        if(name.matches("[^A-Za-z0-9]+")){
            player.sendMessage(api.getMessage("bad_name"));
            return false;
        }
        if(name.length() < 4){
            player.sendMessage(api.getMessage("long_name"));
            return false;
        }

        if(name.length() > 10){
            player.sendMessage(api.getMessage("short_name"));
            return false;
        }
        Region region = api.getRegionByName(name);
        if(region == null){
            player.sendMessage(api.getMessage("rg_not_exist"));
            return false;
        }

        if(!player.hasPermission("guard.all") & !region.getOwner().equalsIgnoreCase(nick)){
            player.sendMessage(api.getMessage("player_not_owner"));
            return false;
        }

        api.removeRegion(region);
        player.sendMessage(api.getMessage("rg_remove"));
        return true;
    }
}
