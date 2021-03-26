// -----------------------
// Coded by Pandadoxo
// on 28.12.2020 at 23:38 
// -----------------------

package de.pandadoxo.twitchsync.discordbot.commandtyes;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public interface PrivateCommand {

    void performCommand(User user, PrivateChannel channel, Message message);

}
