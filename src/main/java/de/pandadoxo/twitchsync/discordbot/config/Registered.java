// -----------------------
// Coded by Pandadoxo
// on 29.12.2020 at 12:53 
// -----------------------

package de.pandadoxo.twitchsync.discordbot.config;

public class Registered {

    private String minecraftName;
    private String minecraftUUID;
    private String discordName;
    private long discordID;
    private boolean subscriber;

    public Registered(String minecraftName, String minecraftUUID, String discordName, long discordID) {
        this.minecraftName = minecraftName;
        this.minecraftUUID = minecraftUUID;
        this.discordName = discordName;
        this.discordID = discordID;
        this.subscriber = false;
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public void setMinecraftName(String minecraftName) {
        this.minecraftName = minecraftName;
    }

    public String getMinecraftUUID() {
        return minecraftUUID;
    }

    public void setMinecraftUUID(String minecraftUUID) {
        this.minecraftUUID = minecraftUUID;
    }

    public String getDiscordName() {
        return discordName;
    }

    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }

    public long getDiscordID() {
        return discordID;
    }

    public void setDiscordID(long discordID) {
        this.discordID = discordID;
    }

    public boolean isSubscriber() {
        return subscriber;
    }

    public void setSubscriber(boolean subscriber) {
        this.subscriber = subscriber;
    }
}
