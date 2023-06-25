package me.mrslerk.guard.event.flag;


import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import lombok.Getter;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;


public class FlagCheckByEntityEvent extends FlagCheckEvent implements Cancellable{


    @Getter
    private final Entity entity;

    @Getter
    private final Entity target;

    public FlagCheckByEntityEvent(GuardManager plugin, Region region, String flag, Entity entity, Entity target){
        super(plugin, region, flag);
        this.entity = entity;
        this.target = target;
    }
}