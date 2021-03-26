// -----------------------
// Coded by Pandadoxo
// on 28.12.2020 at 22:31 
// -----------------------

package de.pandadoxo.twitchsync.discordbot;

import de.pandadoxo.twitchsync.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class Twitchbot {

    private static CommandManager commandManager;
    private static JDA jda;

    private boolean shutdown;

    public Twitchbot() {
        commandManager = new CommandManager();

        start();
    }

    public static JDA getJda() {
        return jda;
    }

    public static ShardManager getShardManager() {
        if (jda == null) return null;
        return jda.getShardManager();
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public void start() {

        if (Main.getBotConfig().getBotToken() == null || Main.getBotConfig().getBotToken().equalsIgnoreCase(Main.getBotConfig().DEFAULT_TOKEN)
                || Main.getBotConfig().getGuildId() == Main.getBotConfig().DEFAULT_GUILD_ID || Main.getBotConfig().getRoleId() == Main.getBotConfig().DEFAULT_ROLE_ID) {
            Main.getInstance().getLogger().severe(Main.getBotConfig().getBotPrefix() + "Fehlende Angaben erkannt!");
            Main.getInstance().getLogger().severe(Main.getBotConfig().getBotPrefix() + "TOKEN: " + (Main.getBotConfig().getBotToken() == null ? "/" :
                    Main.getBotConfig().getBotToken()));
            Main.getInstance().getLogger().severe(Main.getBotConfig().getBotPrefix() + "GUILDID: " + Main.getBotConfig().getGuildId());
            Main.getInstance().getLogger().severe(Main.getBotConfig().getBotPrefix() + "ROLEID: " + Main.getBotConfig().getRoleId());
            Main.getInstance().getLogger().severe(Main.getBotConfig().getBotPrefix() + "-------------------------------");
            Main.getInstance().getLogger().severe(Main.getBotConfig().getBotPrefix() + "Der Bot wird NICHT gestartet!");
            shutdown = true;
            return;
        }

        try {
            JDABuilder builder = JDABuilder.createDefault(Main.getBotConfig().getBotToken());
            builder.setActivity(Activity.streaming("Pandadoxo :^)", "https://twitch.tv/pandadoxo"));
            builder.setStatus(OnlineStatus.ONLINE);
            builder.useSharding(0, 1);
            builder.setMemberCachePolicy(MemberCachePolicy.ALL);
            builder.enableIntents(Arrays.asList(GatewayIntent.values()));
            commandManager = new CommandManager();
            builder.addEventListeners(new CommandListener());

            jda = builder.build();
            shutdown = false;
            Main.getInstance().getLogger().info(Main.getBotConfig().getBotPrefix() + "Bot online");
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        shutdown = true;
        if (getJda() != null) {
            if (getShardManager() != null)
                getShardManager().setStatus(OnlineStatus.OFFLINE);
            getJda().shutdownNow();
        }
        Main.getInstance().getLogger().info(Main.getBotConfig().getBotPrefix() + "shutting down");
    }

    public boolean isShutdown() {
        return shutdown;
    }

}