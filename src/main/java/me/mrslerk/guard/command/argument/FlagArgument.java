package me.mrslerk.guard.command.argument;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;

public class FlagArgument extends Argument {

    public FlagArgument(GuardManager plugin) {
        super(plugin, "flag");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        GuardManager api = getPlugin();
        if (!(sender instanceof Player)) {
            sender.sendMessage(api.getMessage("no_console"));
            return false;
        }

        Player player = ((Player) sender).getPlayer();
        if (!player.hasPermission("command.guard.rg.flag")) {
            player.sendMessage(api.getMessage("no_permission"));
            return false;
        }
        if (args.length != 3) {
            player.sendMessage(api.getMessage("flag_help").replace("{flag_list}", String.join(", ", api.getDefaultFlags().keySet())));
            return false;
        }
        Region region = api.getRegionByName(args[1]);
        if (region == null) {
            player.sendMessage(api.getMessage("rg_not_exist"));
            return false;
        }
        if (!player.getName().equalsIgnoreCase(region.getOwner()) & !player.hasPermission("guard.all")) {
            player.sendMessage(api.getMessage("player_not_owner"));
            return false;
        }
        String flag = args[2].toLowerCase();
        if (!api.getDefaultFlags().containsKey(flag)) {
            player.sendMessage(api.getMessage("flag_not_exist"));
            return false;
        }
        boolean value = region.getFlagValue(flag);
        region.setFlag(flag, !value);
        player.sendMessage(api.getMessage(value ? "flag_off" : "flag_on").replace("{flag}", flag));
        return true;
    }
}
