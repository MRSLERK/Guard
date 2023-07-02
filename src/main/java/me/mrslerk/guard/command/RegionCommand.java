package me.mrslerk.guard.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.command.argument.*;


public class RegionCommand extends Command{

    private final Argument[] argumentList;

    public RegionCommand(String name, String description){
        super(name, description);
        this.setPermission("command.guard.rg");
        GuardManager api = GuardManager.getInstance();

        this.commandParameters.clear();

        /* /rg pos <1|2> */
        commandParameters.put("pos", new CommandParameter[]{
                CommandParameter.newEnum("pos2", new CommandEnum("position", "pos")),
                CommandParameter.newEnum("pos", new CommandEnum("pos", "1", "2")),
        });

        /* /rg create <name> */
        commandParameters.put("create", new CommandParameter[]{
                CommandParameter.newEnum("create", new CommandEnum("create", "create")),
                CommandParameter.newType("name", CommandParamType.STRING)
        });

        /* /rg remove <name> */
        commandParameters.put("remove", new CommandParameter[]{
                CommandParameter.newEnum("remove", new CommandEnum("remove", "remove")),
                CommandParameter.newType("name", CommandParamType.STRING)
        });

        /* /rg wand */
        commandParameters.put("wand", new CommandParameter[]{
                CommandParameter.newEnum("wand", new CommandEnum("wand", "wand"))
        });

        /* /rg list */
        commandParameters.put("list", new CommandParameter[]{
                CommandParameter.newEnum("list", new CommandEnum("list", "list"))
        });

        /* /rg flag <regionName> <flagName> */
        commandParameters.put("flag", new CommandParameter[]{
                CommandParameter.newEnum("flag", new CommandEnum("flag", "flag")),
                CommandParameter.newType("regionName", CommandParamType.STRING),
                CommandParameter.newEnum("flagName", new CommandEnum("flagName", api.getDefaultFlags().keySet().stream().toList()))
        });

        /* /rg owner <regionName> <player> */
        commandParameters.put("owner", new CommandParameter[]{
                CommandParameter.newEnum("owner", new CommandEnum("owner", "owner")),
                CommandParameter.newType("regionName", CommandParamType.STRING),
                CommandParameter.newType("player", CommandParamType.TARGET),
        });

        /* /rg member <add|remove> <regionName> <player> */
        commandParameters.put("member", new CommandParameter[]{
                CommandParameter.newEnum("member2", new CommandEnum("member", "member")),
                CommandParameter.newEnum("member", new CommandEnum("memberType", "add", "remove")),
                CommandParameter.newType("regionName", CommandParamType.STRING),
                CommandParameter.newType("player", CommandParamType.TARGET),
        });


        setPermissionMessage(api.getMessage("no_permission"));
        argumentList = new Argument[]{
                new PositionArgument(api),
                new CreateArgument(api),
                new MemberArgument(api),
                new RemoveArgument(api),
                new OwnerArgument(api),
                new FlagArgument(api),
                new ListArgument(api),
                new WandArgument(api)
        };
    }

    private Argument getArgument(String name){
        for(Argument argument : argumentList){
            if(argument.getName().equalsIgnoreCase(name)){
                return argument;
            }
        }
        return null;
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args){

        GuardManager api = GuardManager.getInstance();

        if(!(sender instanceof Player)){
            sender.sendMessage(api.getMessage("no_console"));
            return false;
        }

        if(!testPermission(sender)){
            return false;
        }

        Player player = ((Player) sender).getPlayer();

        if(args.length == 0){
            player.sendMessage(api.getMessage("rg_help"));
            return false;
        }

        Argument argument = getArgument(args[0]);
        if(argument == null){
            player.sendMessage(api.getMessage("no_argument"));
            return false;
        }

        return argument.execute(sender, commandLabel, args);
    }
}