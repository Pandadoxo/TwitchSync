package de.pandadoxo.twitchsync;

import de.pandadoxo.twitchsync.commands.BotCmd;
import de.pandadoxo.twitchsync.commands.TwitchSyncCmd;
import de.pandadoxo.twitchsync.core.LuckPermsCore;
import de.pandadoxo.twitchsync.discordbot.Twitchbot;
import de.pandadoxo.twitchsync.discordbot.config.BotConfig;
import de.pandadoxo.twitchsync.discordbot.config.RegisteredConfig;
import de.pandadoxo.twitchsync.listener.PlayerListener;
import de.pandadoxo.twitchsync.util.FilesUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public final class Main extends Plugin {

    private static Main instance;
    private static Doxperm doxperm;
    private static Twitchbot twitchbot;
    private static BotConfig botConfig;
    private static RegisteredConfig registeredConfig;
    private static FilesUtil filesUtil;
    private static LuckPerms api;
    private static LuckPermsCore luckPermsCore;

    public static String PREFIX() {
        return botConfig.getPrefix();
    }

    public static String WRONG_SYNTAX() {
        return botConfig.getPrefix() + "§7Falscher Syntax! Benutze: §e";
    }

    public static BaseComponent[] toBaseComponent(String string) {
        ComponentBuilder componentBuilder = new ComponentBuilder();

        ChatColor color = ChatColor.RESET;
        boolean bold = false;
        boolean italic = false;
        boolean strike = false;
        boolean underline = false;
        boolean obfus = false;
        for (BaseComponent comp : TextComponent.fromLegacyText(string)) {
            if (comp.hasFormatting()) {
                if (comp.getColor() != null) {
                    color = comp.getColor();
                    bold = comp.isBold();
                    italic = comp.isItalic();
                    strike = comp.isStrikethrough();
                    underline = comp.isUnderlined();
                    obfus = comp.isObfuscated();
                }
            }
            componentBuilder.append(comp).reset().color(color).bold(bold).italic(italic).strikethrough(strike).underlined(underline).obfuscated(obfus);
        }
        return componentBuilder.create();
    }

    public static Main getInstance() {
        return instance;
    }

    public static Twitchbot getTwitchbot() {
        return twitchbot;
    }

    public static void setTwitchbot(Twitchbot twitchbot) {
        Main.twitchbot = twitchbot;
    }

    public static BotConfig getBotConfig() {
        return botConfig;
    }

    public static void setBotConfig(BotConfig botConfig) {
        Main.botConfig = botConfig;
    }

    public static RegisteredConfig getRegisteredConfig() {
        return registeredConfig;
    }

    public static void setRegisteredConfig(RegisteredConfig registeredConfig) {
        Main.registeredConfig = registeredConfig;
    }

    public static FilesUtil getFilesUtil() {
        return filesUtil;
    }

    public static void setFilesUtil(FilesUtil filesUtil) {
        Main.filesUtil = filesUtil;
    }

    public static Doxperm getDoxperm() {
        return doxperm;
    }

    public static LuckPerms getApi() {
        return api;
    }

    public static LuckPermsCore getLuckPermsCore() {
        return luckPermsCore;
    }

    @Override
    public void onEnable() {
        instance = this;
        botConfig = new BotConfig();
        registeredConfig = new RegisteredConfig();
        filesUtil = new FilesUtil();
        doxperm = new Doxperm(botConfig.getPrefix());
        filesUtil.saveConfig();

        try {
            api = LuckPermsProvider.get();
        } catch (IllegalStateException ignored) {
            getLogger().severe("LuckPerm API couldn't be loaded. Shutting down");
            return;
        }

        luckPermsCore = new LuckPermsCore(api);
        twitchbot = new Twitchbot();


        ProxyServer.getInstance().getPluginManager().registerCommand(this, new TwitchSyncCmd("twitchsync"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BotCmd("bot"));
        ProxyServer.getInstance().getPluginManager().registerListener(this, new PlayerListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
