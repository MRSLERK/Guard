package me.mrslerk.guard.event.flag;


import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.level.Position;
import lombok.Getter;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;


public class FlagCheckByBlockEvent extends FlagCheckEvent implements Cancellable{

    @Getter
    private final Player player;

    @Getter
    private final Position position;


    public FlagCheckByBlockEvent(GuardManager plugin, Region region, String flag, Position position, Player player){
        super(plugin, region, flag);
        this.player = player;
        this.position = position;
    }
}