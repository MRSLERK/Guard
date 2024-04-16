package me.mrslerk.guard.command.argument;

import cn.nukkit.command.CommandSender;
import lombok.Getter;
import me.mrslerk.guard.GuardManager;

public abstract class Argument {

    private final String name;

    @Getter
    private final GuardManager plugin;

    public Argument(GuardManager plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean execute(CommandSender sender, String commandLabel, String[] args);
}
