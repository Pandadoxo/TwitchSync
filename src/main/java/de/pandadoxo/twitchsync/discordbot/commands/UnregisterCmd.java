// -----------------------
// Coded by Pandadoxo
// on 29.12.2020 at 13:49 
// -----------------------

package de.pandadoxo.twitchsync.discordbot.commands;

import de.pandadoxo.twitchsync.Main;
import de.pandadoxo.twitchsync.discordbot.commandtyes.PrivateCommand;
import de.pandadoxo.twitchsync.discordbot.config.Registered;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.Arrays;

public class UnregisterCmd implements PrivateCommand {

    @Override
    public void performCommand(User user, PrivateChannel channel, Message message) {
        String[] args = message.getContentRaw().split(" ");
        String finalMessage = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Registered registered = null;
        for (Registered registered1 : Main.getRegisteredConfig().registereds) {
            if (registered1.getDiscordID() == user.getIdLong()) {
                registered1.setDiscordName(user.getName());
                registered = registered1;
                break;
            }
        }
        if (registered == null) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Unregister Command");
            builder.setDescription("Du bist **nicht** registriert! Falls du deinen __Minecraft-Account__ mit dem falschen " +
                    "__Discord-Account__ vernüpft hast, führe den Befehl **/twitchsync unregister** auf `Pandadoxo.de` aus");
            builder.setColor(new Color(152, 23, 175));
            user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(builder.build())).queue();
            return;
        }

        Main.getRegisteredConfig().registereds.remove(registered);
        Main.getFilesUtil().save();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Unregister Command");
        builder.setDescription("Die **Verknüpfung** wurde aufgehoben");
        builder.setColor(new Color(152, 23, 175));
        user.openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessage(builder.build())).queue();
        return;
    }
}
