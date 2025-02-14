package me.dave.activityrewarder.data;

import me.dave.activityrewarder.ActivityRewarder;
import me.dave.activityrewarder.api.event.RewardUserPlaytimeChangeEvent;
import me.dave.activityrewarder.module.ModuleData;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RewardUser {
    private final UUID uuid;
    private String username;
    private int minutesPlayed;

    private final ConcurrentHashMap<String, ModuleData> moduleDataMap = new ConcurrentHashMap<>();

    public RewardUser(@NotNull UUID uuid, String username, int minutesPlayed) {
        this.uuid = uuid;
        this.username = username;
        this.minutesPlayed = minutesPlayed;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
        ActivityRewarder.getDataManager().saveRewardUser(this);
    }

    public int getMinutesPlayed() {
        return this.minutesPlayed;
    }

    public int getHoursPlayed() {
        return this.minutesPlayed * 60;
    }

    public void setMinutesPlayed(int minutesPlayed) {
        ActivityRewarder.getMorePaperLib().scheduling().globalRegionalScheduler().run(() -> ActivityRewarder.getInstance().callEvent(new RewardUserPlaytimeChangeEvent(this, this.minutesPlayed, minutesPlayed)));

        this.minutesPlayed = minutesPlayed;
        ActivityRewarder.getDataManager().saveRewardUser(this);
    }

    @Nullable
    public ModuleData getModuleData(String module) {
        return moduleDataMap.get(module);
    }

    public void addModuleData(ModuleData moduleData) {
        moduleDataMap.put(moduleData.getId(), moduleData);
    }

    public void save() {
        ActivityRewarder.getDataManager().saveRewardUser(this);
    }
}
