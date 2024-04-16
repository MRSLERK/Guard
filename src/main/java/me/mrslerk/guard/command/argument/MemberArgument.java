package me.mrslerk.guard.command.argument;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;

public class MemberArgument extends Argument {

    public MemberArgument(GuardManager plugin) {
        super(plugin, "member");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        GuardManager api = getPlugin();
        if (!(sender instanceof Player)) {
            sender.sendMessage(api.getMessage("no_console"));
            return false;
        }

        Player player = ((Player) sender).getPlayer();
        String nick = sender.getName().toLowerCase();

        if (!player.hasPermission("command.guard.rg.member")) {
            player.sendMessage(api.getMessage("no_permission"));
            return false;
        }
        if (args.length != 4) {
            player.sendMessage(api.getMessage("member_help"));
            return false;
        }
        //---add---\\
        if (args[1].equalsIgnoreCase("add")) {
            Region region = api.getRegionByName(args[2].toLowerCase());

            if (region == null) {
                player.sendMessage(api.getMessage("rg_not_exist"));
                return false;
            }

            if (!player.hasPermission("guard.all") & !region.getOwner().equalsIgnoreCase(nick)) {
                player.sendMessage(api.getMessage("player_not_owner"));
                return false;
            }

            String target = args[3];
            if (target.equalsIgnoreCase(nick)) {
                player.sendMessage(api.getMessage("player_already_member"));
                return false;
            }
            if (region.addMember(target)) {
                player.sendMessage(api.getMessage("member_add").replace("{player}", target));
            } else {
                player.sendMessage(api.getMessage("player_already_member"));
            }
            return true;
        }
        //---remove---\\
        if (args[1].equalsIgnoreCase("remove")) {
            Region region = api.getRegionByName(args[2].toLowerCase());

            if (region == null) {
                player.sendMessage(api.getMessage("rg_not_exist"));
                return false;
            }

            if (!player.hasPermission("guard.all") & !region.getOwner().equalsIgnoreCase(nick)) {
                player.sendMessage(api.getMessage("player_not_owner"));
                return false;
            }

            String target = args[3];

            if (region.removeMember(target)) {
                player.sendMessage(api.getMessage("member_remove").replace("{player}", target));
            } else {
                player.sendMessage(api.getMessage("player_not_exist"));
            }
        }
        return true;
    }
}
