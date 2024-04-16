package me.mrslerk.guard.event.flag;

import lombok.Getter;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;
import me.mrslerk.guard.event.RegionEvent;

class FlagCheckEvent extends RegionEvent {

    @Getter
    private final String flag;
    private boolean needCancel = false;

    public FlagCheckEvent(GuardManager plugin, Region region, String flag) {
        super(plugin, region);
        this.flag = flag.toLowerCase();
    }

    public boolean isMainEventCancelled() {
        return needCancel;
    }

    public void setMainEventCancelled(boolean value) {
        needCancel = value;
    }

    public void setMainEventCancelled() {
        needCancel = true;
    }
}