package me.mrslerk.guard.command.argument;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;

import java.util.List;
import java.util.stream.Collectors;

public class ListArgument extends Argument {

    public ListArgument(GuardManager plugin) {
        super(plugin, "list");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        GuardManager api = getPlugin();
        if (!(sender instanceof Player)) {
            sender.sendMessage(api.getMessage("no_console"));
            return false;
        }

        Player player = ((Player) sender).getPlayer();
        if (!player.hasPermission("command.guard.rg.list")) {
            player.sendMessage(api.getMessage("no_permission"));
            return false;
        }
        String nick = sender.getName().toLowerCase();

        List<Region> regions = api.getRegionList(nick, false);
        if (regions.isEmpty()) {
            player.sendMessage(api.getMessage("list_empty"));
            return false;
        }
        List<String> list = regions.stream().map(Region::getName).collect(Collectors.toList());
        player.sendMessage(api.getMessage("list_success").replace("{list}", String.join(", ", list)));
        return true;
    }
}
