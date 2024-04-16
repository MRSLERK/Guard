package me.mrslerk.guard.command.argument;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Position;
import me.mrslerk.guard.GuardManager;

public class PositionArgument extends Argument {

    public PositionArgument(GuardManager plugin) {
        super(plugin, "pos");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        GuardManager api = getPlugin();
        if (!(sender instanceof Player)) {
            sender.sendMessage(api.getMessage("no_console"));
            return false;
        }

        Player player = ((Player) sender).getPlayer();

        if (!player.hasPermission("command.guard.rg.pos")) {
            player.sendMessage(api.getMessage("no_permission"));
            return false;
        }
        if (args.length == 1 || args.length >= 3) {
            player.sendMessage(api.getMessage("pos_help"));
            return true;
        }

        Position pos = player.getPosition();
        String nick = sender.getName().toLowerCase();

        if (args[1].equalsIgnoreCase("1")) {
            if (api.secondPos.containsKey(nick)) {
                if (api.secondPos.get(nick).getLevel() != pos.getLevel()) {
                    player.sendMessage(api.getMessage("pos_another_world"));
                    return false;
                }
                Position second = api.secondPos.get(player.getName().toLowerCase());
                int size = api.calculateSize(second, pos);
                int maxSize = api.getGroupConfig().getInt("max-size", api.getPlayerGroupId(player));
                if (size > maxSize) {
                    player.sendTip(api.getMessage("rg_oversize").replace("{max_size}", String.valueOf(maxSize)));
                    return false;
                }
                api.sendSelection(player, pos, second);
            }

            api.firstPos.put(nick, pos);
            player.sendMessage(api.getMessage("pos_1_set"));
            return true;
        }

        if (args[1].equalsIgnoreCase("2")) {
            if (api.firstPos.containsKey(nick)) {
                if (api.firstPos.get(nick).getLevel() != pos.getLevel()) {
                    player.sendMessage(api.getMessage("pos_another_world"));
                    return false;
                }
                Position first = api.firstPos.get(player.getName().toLowerCase());
                int size = api.calculateSize(first, pos);
                int maxSize = api.getGroupConfig().getInt("max-size", api.getPlayerGroupId(player));
                if (size > maxSize) {
                    player.sendTip(api.getMessage("rg_oversize").replace("{max_size}", String.valueOf(maxSize)));
                    return false;
                }
                api.sendSelection(player, pos, first);
            }
            api.secondPos.put(nick, pos);
            player.sendMessage(api.getMessage("pos_2_set"));
            return true;
        }
        player.sendMessage(api.getMessage("pos_help"));
        return false;
    }
}
