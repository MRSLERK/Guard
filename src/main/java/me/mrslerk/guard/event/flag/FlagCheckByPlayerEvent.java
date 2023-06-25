package me.mrslerk.guard.event.flag;


import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.level.Position;
import lombok.Getter;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;


public class FlagCheckByPlayerEvent extends FlagCheckEvent implements Cancellable{

    @Getter
    private final Player player;

    @Getter
    private final Position position;


    public FlagCheckByPlayerEvent(GuardManager plugin, Region region, String flag, Player player, Position position){
        super(plugin, region, flag);
        this.player = player;
        this.position = position;
    }
}