package me.mrslerk.guard.event.region;

import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;
import me.mrslerk.guard.event.RegionEvent;

public class RegionLoadEvent extends RegionEvent{

    public RegionLoadEvent(GuardManager plugin, Region region){
        super(plugin, region);
    }
}