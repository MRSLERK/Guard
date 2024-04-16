package me.mrslerk.guard.listener.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import lombok.Getter;
import lombok.NonNull;
import me.mrslerk.guard.GuardManager;
import me.mrslerk.guard.data.Region;
import me.mrslerk.guard.event.flag.FlagCheckByEntityEvent;


abstract public class EntityListener {

    @Getter
    private final GuardManager plugin;

    public EntityListener(@NonNull GuardManager plugin) {
        this.plugin = plugin;
    }


    public boolean isFlagDenied(Entity entity, @NonNull String flag, Entity target) {
        GuardManager api = getPlugin();

        boolean result = false;
        Region region;
        if (target != null) {
            if (api.isIgnoredFlag(flag, target.getLevel())) {
                return false;
            }
            region = api.getRegion(target.getPosition());

            if (region != null && !region.getFlagValue(flag)) {
                result = true;
            }
        }

        if (api.isIgnoredFlag(flag, entity.getLevel())) {
            return result;
        }

        region = api.getRegion(entity.getPosition());

        if (region == null) {
            return result;
        }

        if (!region.getFlagValue(flag)) {
            FlagCheckByEntityEvent event = new FlagCheckByEntityEvent(api, region, flag, entity, target);
            api.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return event.isMainEventCancelled();
            }
            if (entity instanceof Player) {
                api.sendWarning((Player) entity, "warn_flag_" + flag);
            }
            return true;
        }

        return result;
    }
}