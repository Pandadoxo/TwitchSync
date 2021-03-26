// -----------------------
// Coded by Pandadoxo
// on 25.03.2021 at 20:48 
// -----------------------

package de.pandadoxo.twitchsync.discordbot.config;

import de.pandadoxo.twitchsync.Main;
import de.pandadoxo.twitchsync.util.FilesUtil;

public class BotConfig {

    @FilesUtil.Exclude
    public final String DEFAULT_TOKEN = "Insert token here";
    @FilesUtil.Exclude
    public final String DEFAULT_SUB_MESSAGE = "%prefix% §7Vielen Dank für dein §6Abonnement";
    @FilesUtil.Exclude
    public final String DEFAULT_SUB_LOSE_MESSAGE = "%prefix% §7Dein Abonnement ist abgelaufen";
    @FilesUtil.Exclude
    public final String DEFAULT_NAME = "TwitchBot";
    @FilesUtil.Exclude
    public final long DEFAULT_GUILD_ID = 0;
    @FilesUtil.Exclude
    public final long DEFAULT_ROLE_ID = 0;

    private String prefix;
    private String botPrefix;
    private String botName;
    private String botToken;
    private String subMessage;
    private String subLoseMessage;
    private long guildId;
    private long roleId;

    public BotConfig() {
        this.prefix = "§8| §dTwitchSync §8» §r";
        this.botPrefix = "| Botambus » ";
        this.botName = DEFAULT_NAME;
        this.botToken = DEFAULT_TOKEN;
        this.subMessage = DEFAULT_SUB_MESSAGE;
        this.subLoseMessage = DEFAULT_SUB_LOSE_MESSAGE;
        this.guildId = DEFAULT_GUILD_ID;
        this.roleId = DEFAULT_ROLE_ID;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getBotPrefix() {
        return botPrefix;
    }

    public void setBotPrefix(String botPrefix) {
        this.botPrefix = botPrefix;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getSubMessage() {
        return subMessage.replace("%prefix%", Main.PREFIX());
    }

    public void setSubMessage(String subMessage) {
        this.subMessage = subMessage;
    }

    public String getSubLoseMessage() {
        return subLoseMessage.replace("%prefix%", Main.PREFIX());
    }

    public void setSubLoseMessage(String subLoseMessage) {
        this.subLoseMessage = subLoseMessage;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
