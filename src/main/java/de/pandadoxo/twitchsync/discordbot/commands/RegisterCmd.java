// -----------------------
// Coded by Pandadoxo
// on 29.12.2020 at 12:52 
// -----------------------

package de.pandadoxo.twitchsync.discordbot.commands;

import de.pandadoxo.twitchsync.Main;
import de.pandadoxo.twitchsync.commands.TwitchSyncCmd;
import de.pandadoxo.twitchsync.core.Code;
import de.pandadoxo.twitchsync.discordbot.Twitchbot;
import de.pandadoxo.twitchsync.discordbot.commandtyes.PrivateCommand;
import de.pandadoxo.twitchsync.discordbot.config.Registered;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.util.Arrays;

public class RegisterCmd implements PrivateCommand {

    @Override
    public void performCommand(User user, PrivateChannel channel, Message message) {
        String[] args = message.getContentRaw().split(" ");
        String finalMessage = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        if (args.length != 2) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Register Command");
            builder.setDescription("Falscher Syntax! Benutze: `>register <Code>`");
            builder.setColor(new Color(152, 23, 175));
            user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(builder.build())).queue();
            return;
        }

        Registered registered = null;
        for (Registered registered1 : Main.getRegisteredConfig().registereds) {
            if (registered1.getDiscordID() == user.getIdLong()) {
                registered1.setDiscordName(user.getName());
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Register Command");
                builder.setDescription("Du bist bereits registriert!\nMC-Name: `" + registered1.getMinecraftName() + "` | MC-ID: `" + registered1.getMinecraftUUID() + "`");
                builder.setColor(new Color(152, 23, 175));
                user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(builder.build())).queue();
                return;
            }
        }

        String code = args[1];
        ProxiedPlayer player = null;
        Code code1 = null;
        for (ProxiedPlayer all : TwitchSyncCmd.getRegistering().keySet()) {
            Code c = TwitchSyncCmd.getRegistering().get(all);
            if (c.getCode().equals(code)) {
                if (c.getExireAt() < System.currentTimeMillis()) {
                    TwitchSyncCmd.getRegistering().remove(all);
                    break;
                }
                player = all;
                code1 = c;
                break;
            }
        }

        if (player == null) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Register Command");
            builder.setDescription("Dieser Code existiert nicht! Bitte joine **Pandadoxo.de** und gib `/tiwtchsync` ein, um einen Code zu " +
                    "bekommen");
            builder.setColor(new Color(152, 23, 175));
            user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(builder.build())).queue();
            return;
        }

        Main.getRegisteredConfig().registereds.add(new Registered(player.getName(), player.getUniqueId().toString(), user.getName(),
                user.getIdLong()));
        Main.getFilesUtil().save();

        TwitchSyncCmd.getRegistering().remove(player);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Register Command");
        builder.setDescription("Dein Account wurde erfolgreich mit `" + player.getName() + "` verknüpft");
        builder.setColor(new Color(152, 23, 175));
        user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(builder.build())).queue();

        if (player.isConnected()) {
            player.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Dein Account wurde erfolgreich mit §e" + user.getAsTag() +
                    " §7verknüpft"));
        }

        Guild guild = Twitchbot.getJda().getGuildById(Main.getBotConfig().getGuildId());
        if (guild == null) return;
        Member member = guild.getMember(user);

        if (member == null) {
            for (Member m : guild.getMembers()) {
                System.out.println(m.getUser().getName() + " - " + m.getUser().getIdLong());
            }
            EmbedBuilder memberMessage = new EmbedBuilder();
            memberMessage.setTitle("Register Command");
            memberMessage.setDescription("Du bist noch nicht auf dem Discord Server oder? Um eine Einladung zu bekommen, schreibe mir `>server`");
            memberMessage.setColor(new Color(152, 23, 175));
            user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(memberMessage.build())).queue();
            return;
        }
        Role role = guild.getRoleById(Main.getBotConfig().getRoleId());
        if (role == null) return;

        if (member.getRoles().contains(role)) {
            if (player.isConnected()) {
                player.sendMessage();
                player.sendMessage(Main.toBaseComponent(Main.getBotConfig().getSubMessage()));
            }
            Main.getLuckPermsCore().addPlayerSubscriber(player);
        }
    }
}
