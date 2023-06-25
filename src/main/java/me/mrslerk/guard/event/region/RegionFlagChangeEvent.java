package me.mrslerk.guard.event.region;

import cn.nukkit.event.Cancellable;
import lombok.Getter;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;
import me.mrslerk.guard.event.RegionEvent;

public class RegionFlagChangeEvent extends RegionEvent implements Cancellable{

    @Getter
    private final String flag;

    @Getter
    private final Boolean newValue;

    public RegionFlagChangeEvent(GuardManager plugin, Region region, String flag, boolean newValue){
        super(plugin, region);
        this.flag = flag;
        this.newValue = newValue;
    }

    public boolean getOldValue(){
        return !getNewValue();
    }
}
