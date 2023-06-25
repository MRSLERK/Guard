package me.mrslerk.guard.command.argument;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;

public class CreateArgument extends Argument{

    public CreateArgument(GuardManager plugin){
        super(plugin, "create");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args){
        GuardManager api = getPlugin();
        if(!(sender instanceof Player)){
            sender.sendMessage(api.getMessage("no_console"));
            return false;
        }

        Player player = ((Player) sender).getPlayer();

        if(!player.hasPermission("command.guard.rg.create")){
            player.sendMessage(api.getMessage("no_permission"));
            return false;
        }
        if(args.length == 1){
            player.sendMessage(api.getMessage("create_help"));
            return false;
        }

        String nick = sender.getName().toLowerCase();

        if(!api.firstPos.containsKey(nick)){
            player.sendMessage(api.getMessage("pos_1_not_set"));
            return false;
        }
        if(!api.secondPos.containsKey(nick)){
            player.sendMessage(api.getMessage("pos_2_not_set"));
            return false;
        }

        String name = args[1].toLowerCase();
        if(name.matches("[^A-Za-z0-9]+")){
            player.sendMessage(api.getMessage("bad_name"));
            return false;
        }
        if(name.length() < 4){
            player.sendMessage(api.getMessage("short_name"));
            return false;
        }

        if(name.length() > 10){
            player.sendMessage(api.getMessage("long_name"));
            return false;
        }

        if(api.getRegionByName(name) != null){
            player.sendMessage(api.getMessage("rg_exist"));
            return false;
        }

        Position first = api.firstPos.get(nick);
        Position second = api.secondPos.get(nick);

        double xMin = Math.min(first.getX(), second.getX());
        double yMin = Math.min(first.getY(), second.getY());
        double zMin = Math.min(first.getZ(), second.getZ());

        double xMax = Math.max(first.getX(), second.getX());
        double yMax = Math.max(first.getY(), second.getY());
        double zMax = Math.max(first.getZ(), second.getZ());

        Level level = first.getLevel();

        Position min = new Position(xMin, yMin, zMin, level);
        Position max = new Position(xMax, yMax, zMax, level);

        if(!player.hasPermission("guard.all")){
            for(Region region : api.getOverride(min, max)) {
                if(!region.getOwner().equalsIgnoreCase(nick)){
                    player.sendMessage(api.getMessage("rg_override"));
                    return false;
                }
            }
            if(api.getRegionList(nick, true).size() > api.getGroupConfig().getInt("max-count", api.getPlayerGroupId(player))){
                player.sendMessage(api.getMessage("rg_overcount").replace("{max_count}", String.valueOf(api.getGroupConfig().getInt("max-count", api.getPlayerGroupId(player)))));
                return false;
            }
        }

        api.firstPos.remove(nick);
        api.secondPos.remove(nick);

        api.createRegion(min, max, sender.getPosition().getLevel(), name, nick);
        player.sendMessage(api.getMessage("rg_create").replace("{region}", name));
        api.removeSelection(player);
        return true;
    }
}
