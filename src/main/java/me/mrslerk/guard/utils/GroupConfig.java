package me.mrslerk.guard.utils;

import cn.nukkit.utils.Config;

public class GroupConfig extends Config {
    public GroupConfig(String file) {
        super(file, Config.YAML);
    }

    public int getInt(String key, String id) {
        if (!this.exists("group." + id + "." + key)) {
            id = "default";
        }
        return super.getInt("group." + id + "." + key);
    }
}
