// -----------------------
// Coded by Pandadoxo
// on 28.12.2020 at 23:38 
// -----------------------

package de.pandadoxo.twitchsync.discordbot.commandtyes;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface ServerCommand {

    void performCommand(Member member, TextChannel channel, Message message);

}
