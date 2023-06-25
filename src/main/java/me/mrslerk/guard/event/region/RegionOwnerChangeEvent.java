package me.mrslerk.guard.event.region;

import cn.nukkit.event.Cancellable;
import lombok.Getter;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;
import me.mrslerk.guard.event.RegionEvent;

public class RegionOwnerChangeEvent extends RegionEvent implements Cancellable{


    @Getter
    private final String newOwner;

    @Getter
    private final String oldOwner;

    public RegionOwnerChangeEvent(GuardManager plugin, Region region, String oldOwner, String newOwner){
        super(plugin, region);
        this.oldOwner = oldOwner.toLowerCase();
        this.newOwner = newOwner.toLowerCase();
    }
}
