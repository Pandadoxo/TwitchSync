package de.pandadoxo.twitchsync.discordbot;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class CommandListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().equals(e.getJDA().getSelfUser()) || e.getAuthor().isFake())
            return;
        String msg = e.getMessage().getContentDisplay();
        String prefix = ">";
        String[] args = msg.split(" ");


        if (args[0].startsWith(prefix) && e.isFromType(ChannelType.TEXT)) {
            args[0] = args[0].substring(prefix.length());
            if (!args[0].equalsIgnoreCase(""))
                if (!Twitchbot.getCommandManager().sPerform(args[0], e.getMember(), e.getTextChannel(), e.getMessage())) {
                    e.getTextChannel().sendMessage("Command nicht vorhanden").complete().delete().queueAfter(5, TimeUnit.SECONDS);
                    return;
                }
        }

        if (args[0].startsWith(prefix) && e.isFromType(ChannelType.PRIVATE)) {
            args[0] = args[0].substring(prefix.length());
            if (!args[0].equalsIgnoreCase(""))
                if (!Twitchbot.getCommandManager().pPerform(args[0], e.getAuthor(), e.getPrivateChannel(), e.getMessage())) {
                    e.getAuthor().openPrivateChannel().flatMap(channel -> channel.sendMessage("Command nicht vorhanden")).complete().delete().queueAfter(5, TimeUnit.SECONDS);
                    return;
                }
        }


    }

}
