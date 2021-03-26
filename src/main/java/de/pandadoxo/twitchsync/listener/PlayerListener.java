// -----------------------
// Coded by Pandadoxo
// on 23.12.2020 at 21:33 
// -----------------------

package de.pandadoxo.twitchsync.listener;

import de.pandadoxo.twitchsync.Main;
import de.pandadoxo.twitchsync.discordbot.Twitchbot;
import de.pandadoxo.twitchsync.discordbot.config.Registered;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(ServerConnectEvent event) {
        if (!event.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {
            return;
        }

        ProxiedPlayer p = event.getPlayer();
        boolean isSub = false;

        Guild guild = Twitchbot.getJda().getGuildById(Main.getBotConfig().getGuildId());
        if (guild == null) return;
        Role role = guild.getRoleById(Main.getBotConfig().getRoleId());
        if (role == null) return;

        for (Registered registered : Main.getRegisteredConfig().registereds) {
            if (registered.getMinecraftUUID().equals(p.getUniqueId().toString())) {
                registered.setMinecraftName(p.getName());
                Member member = guild.getMemberById(registered.getDiscordID());
                if (member == null) {
                    break;
                }
                registered.setDiscordName(member.getUser().getName());
                if (member.getRoles().contains(role)) {
                    isSub = true;
                }
                break;
            }
        }
        Main.getFilesUtil().save();
        if (!Main.getLuckPermsCore().isPlayerSub(p)) {
            if (isSub) {
                ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> {
                    p.sendMessage(Main.toBaseComponent(Main.getBotConfig().getSubMessage()));
                    Main.getLuckPermsCore().addPlayerSubscriber(p);
                }, 250, TimeUnit.MILLISECONDS);
                return;
            }

        } else {
            if (isSub) {
                return;
            }
            ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> {
                p.sendMessage(Main.toBaseComponent(Main.getBotConfig().getSubLoseMessage()));
                Main.getLuckPermsCore().removePlayerSubscriber(p);
            }, 250, TimeUnit.MILLISECONDS);

        }

    }
}
