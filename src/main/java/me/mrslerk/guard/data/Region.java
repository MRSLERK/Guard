package me.mrslerk.guard.data;

import cn.nukkit.math.Vector3;
import lombok.Getter;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.event.region.RegionFlagChangeEvent;
import me.mrslerk.guard.event.region.RegionLoadEvent;
import me.mrslerk.guard.event.region.RegionMemberChangeEvent;
import me.mrslerk.guard.event.region.RegionOwnerChangeEvent;

import java.util.HashMap;
import java.util.List;

public class Region{

    @Getter
    private String name;

    @Getter
    private String owner;


    private final List<String> members;

    @Getter
    private String levelName;

    private final Vector3 min;
    private final Vector3 max;

    @Getter
    private HashMap<String, Boolean> flags;

    @Getter
    private boolean close = false;


    public Region(@NonNull String name, @NonNull String owner, @NonNull String levelName, @NonNull List<String> members, @NonNull HashMap<String, Boolean> flags, @NonNull Vector3 min, @NonNull Vector3 max){
        this.name = name;
        this.owner = owner;
        this.levelName = levelName;
        this.members = members;
        this.min = min;
        this.max = max;
        this.flags = flags;
        RegionLoadEvent event = new RegionLoadEvent(getPlugin(), this);
        getPlugin().getServer().getPluginManager().callEvent(event);
    }


    public void setOwner(@NonNull String nick){
        RegionOwnerChangeEvent event = new RegionOwnerChangeEvent(getPlugin(), this, owner, nick);
        getPlugin().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return;
        }
        owner = nick.toLowerCase();
        save();
    }

    public void setFlag(@NonNull String flag, boolean value){
        RegionFlagChangeEvent event = new RegionFlagChangeEvent(getPlugin(), this, flag, value);
        getPlugin().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return;
        }
        flags.put(flag, value);
        save();
    }


    public boolean removeMember(@NonNull String member){
        member = member.toLowerCase();
        RegionMemberChangeEvent event = new RegionMemberChangeEvent(getPlugin(), this, member, RegionMemberChangeEvent.TYPE_REMOVE);
        getPlugin().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return false;
        }
        if(!hasMember(member)){
            return false;
        }
        members.remove(member);
        save();
        return true;
    }

    public boolean addMember(@NonNull String member){
        member = member.toLowerCase();
        RegionMemberChangeEvent event = new RegionMemberChangeEvent(getPlugin(), this, member, RegionMemberChangeEvent.TYPE_ADD);
        getPlugin().getServer().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return false;
        }
        if(hasMember(member)){
            return false;
        }
        members.add(member);
        save();
        return true;
    }

    public boolean hasMember(@NonNull String member){
        return members.contains(member.toLowerCase());
    }

    public String[] getMembers(){
        if(members.size() == 0){
            return new String[]{};
        }
        return members.toArray(new String[members.size() - 1]);
    }

    public int getMinX(){
        return min.getFloorX();
    }

    public int getMinY(){
        return min.getFloorY();
    }

    public int getMinZ(){
        return min.getFloorZ();
    }

    public int getMaxX(){
        return max.getFloorX();
    }

    public boolean getFlagValue(@NonNull String flag){
        if(!flags.containsKey(flag)){
            return false;
        }
        return flags.get(flag);
    }

    public int getMaxY(){
        return max.getFloorY();
    }

    public int getMaxZ(){
        return max.getFloorZ();
    }

    public Vector3 getMax(){
        return max;
    }

    public Vector3 getMin(){
        return min;
    }

    public void save(){
        GuardManager.getInstance().saveRegion(this);
    }

    public void close(){
        close = true;
    }

    public GuardManager getPlugin(){
        return GuardManager.getInstance();
    }
}
