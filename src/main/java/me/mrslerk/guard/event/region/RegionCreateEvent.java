package me.mrslerk.guard.event.region;

import cn.nukkit.event.Cancellable;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;
import me.mrslerk.guard.event.RegionEvent;

public class RegionCreateEvent extends RegionEvent implements Cancellable{

    public RegionCreateEvent(GuardManager plugin, Region region){
        super(plugin, region);
    }
}