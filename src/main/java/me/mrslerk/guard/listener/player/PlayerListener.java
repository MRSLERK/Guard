package me.mrslerk.guard.listener.player;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import lombok.Getter;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;


abstract public class PlayerListener{

    @Getter
    private final GuardManager plugin;

    public PlayerListener(@NonNull GuardManager plugin){
        this.plugin = plugin;
    }

    public boolean isFlagDenied(@NonNull Player player, @NonNull String flag, Position position){
        GuardManager api = getPlugin();
        position = position == null ? player.getPosition() : position;
        if(api.isIgnoredFlag(flag, position.getLevel())){
            return false;
        }
        if(player.hasPermission("guard.noflag")){
            return false;
        }

        Region region = api.getRegion(position);
        if(region == null){
            return false;
        }
        if(region.getFlagValue(flag)){
            return false;
        }
        String nick = player.getName().toLowerCase();
        if(nick.equals(region.getOwner()) || region.hasMember(nick)){
            return false;
        }
        api.sendWarning(player, "warn_flag_" + flag);
        return true;
    }
}