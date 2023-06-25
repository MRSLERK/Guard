package me.mrslerk.guard.event;


import cn.nukkit.event.plugin.PluginEvent;
import lombok.Getter;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;

public class RegionEvent extends PluginEvent{

    @Getter
    private final Region region;

    public RegionEvent(GuardManager plugin, Region region){
        super(plugin);
        this.region = region;
    }
}