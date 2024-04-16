package me.mrslerk.guard.event.region;

import cn.nukkit.event.Cancellable;
import lombok.Getter;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;
import me.mrslerk.guard.event.RegionEvent;

public class RegionMemberChangeEvent extends RegionEvent implements Cancellable {

    public static final int TYPE_ADD = 0;
    public static final int TYPE_REMOVE = 1;

    private final int type;

    @Getter
    private final String member;

    public RegionMemberChangeEvent(GuardManager plugin, Region region, String member, int type) {
        super(plugin, region);
        this.member = member;
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
