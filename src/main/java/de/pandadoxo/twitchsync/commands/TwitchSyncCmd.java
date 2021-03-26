// -----------------------
// Coded by Pandadoxo
// on 29.12.2020 at 13:10 
// -----------------------

package de.pandadoxo.twitchsync.commands;

import de.pandadoxo.twitchsync.Main;
import de.pandadoxo.twitchsync.core.Code;
import de.pandadoxo.twitchsync.discordbot.Twitchbot;
import de.pandadoxo.twitchsync.discordbot.config.Registered;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TwitchSyncCmd extends Command implements TabExecutor {

    private static final HashMap<ProxiedPlayer, Code> registering = new HashMap<>();
    private static final HashMap<ProxiedPlayer, ScheduledTask> messageTask = new HashMap<>();

    private final String syntaxmsg = Main.WRONG_SYNTAX() + "/twitchsync (unregister)";

    public TwitchSyncCmd(String name) {
        super(name);
    }

    public static HashMap<ProxiedPlayer, Code> getRegistering() {
        return registering;
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer))
            return new ArrayList<>();
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!Main.getDoxperm().has(p, "twitchsync.register", true)) {
            return new ArrayList<>();
        }

        List<String> tocomplete = new ArrayList<>();
        List<String> complete = new ArrayList<>();

        if (args.length == 1) {
            tocomplete.addAll(Arrays.asList("unregister"));
        }

        for (
                String tc : tocomplete) {
            if (tc.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                complete.add(tc);
            }
        }
        return complete;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer))
            return;
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (!Main.getDoxperm().has(p, "twitchsync.register", true)) {
            return;
        }

        if (args.length > 1) {
            p.sendMessage(Main.toBaseComponent(syntaxmsg));
            return;
        }

        if (args.length == 0) {
            if (registering.containsKey(p)) {
                p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Du bist bereits dabei dich zu §aregistrieren"));
                p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Dein Code: §b" + registering.get(p).getCode()));
                return;
            }
            for (Registered registered : Main.getRegisteredConfig().registereds) {
                if (registered.getMinecraftUUID().equals(p.getUniqueId().toString())) {
                    p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Du bist bereits registreirt"));
                    p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7DC-Name: §b" + registered.getDiscordName() + " §7| DC-ID: §b" + registered.getDiscordID()));
                    registered.setMinecraftName(p.getName());
                    return;
                }
            }
            Guild guild = Twitchbot.getJda().getGuildById(Main.getBotConfig().getGuildId());
            if (guild == null) {
                p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Es ist ein Fehler aufgetreten"));
                return;
            }
            TextChannel defaultChannel = guild.getDefaultChannel();
            if (defaultChannel == null) {
                p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Es ist ein Fehler aufgetreten"));
                return;
            }
            Member member = guild.getMember(Twitchbot.getJda().getSelfUser());
            if (member == null) {
                p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Es ist ein Fehler aufgetreten"));
                return;
            }
            if (!member.hasPermission(Permission.CREATE_INSTANT_INVITE)) {
                p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Der Bot kann keine Einladung erstellen. Bitte informiere den Server-Admin"));
                return;
            }

            Code code = new Code(System.currentTimeMillis());
            registering.put(p, code);

            //Build Step 2
            ComponentBuilder codemessage = new ComponentBuilder();
            codemessage.append(TextComponent.fromLegacyText(Main.PREFIX())).bold(false);
            codemessage.append(TextComponent.fromLegacyText("§eSchritt 3 §7» Schreibe nun folgende Nachricht an den §b" + Main.getBotConfig().getBotName() +
                    " §7Discord-Bot: §3"));
            codemessage.append(">register " + registering.get(p).getCode()).event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,
                    ">register " + registering.get(p).getCode())).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Klicken zum " +
                    "kopieren")));

            ComponentBuilder botmessage = new ComponentBuilder();
            botmessage.append(TextComponent.fromLegacyText(Main.PREFIX())).bold(false).event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https" +
                    "://discordapp.com/users/" + Twitchbot.getJda().getSelfUser().getId()));
            botmessage.append(TextComponent.fromLegacyText("§7[§aKlicke§7]")).bold(false);
            botmessage.append(TextComponent.fromLegacyText(" §oum den Chat mit dem Bot zu öffnen"));

            for (int i = 0; i < 20; i++) p.sendMessage();

            //Send Message
            p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§8§oPlugin provided by §7§oPandadoxo: "));
            p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Bitte befolge folgende Schritte, um dich zu synchronisieren: "));

            messageTask.put(p, ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), new Runnable() {

                int i = 0;

                @Override
                public void run() {

                    i++;
                    if (i == 3) {
                        p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§eSchritt 1 §7» Öffne Discord"));
                    } else if (i == 6) {
                        p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§eSchritt 2 §7» Um dich zu registrieren musst du" +
                                " dem Discord-Server beitreten §8-> §3" +
                                defaultChannel.createInvite().deadline(System.currentTimeMillis() + 5 * 60 * 1000).complete().getUrl()));
                    } else if (i == 9) {
                        p.sendMessage(codemessage.create());
                        p.sendMessage(botmessage.create());
                    } else if (i == 12) {
                        p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§eSchritt 4 §7» Falls noch nicht geschehen, Verbinde deinen Twitch-Acoount " +
                                "mit deinem Discord-Account. Gehe hierzu auf die §oBenutzereinstellungen §8-> §7§oVerknüpfungen §8-> §7§oTwitch"));
                        p.sendMessage(Main.toBaseComponent("§8(§7§oDer Code läuft nach 5 Minuten ab [§3§o" +
                                new SimpleDateFormat("HH:mm:ss").format(registering.get(p).getExireAt()) + " Uhr§o§7]§8)"));
                    } else return;
                    if (i == 18) {
                        messageTask.get(p).cancel();
                        messageTask.remove(p);
                    }
                }
            }, 0, 1, TimeUnit.SECONDS));


            ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> {
                registering.remove(p, code);
            }, 5, TimeUnit.MINUTES);
            return;
        }

        //Unregister
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("unregister")) {
                Registered registered = null;
                for (Registered registered1 : Main.getRegisteredConfig().registereds) {
                    if (registered1.getMinecraftUUID().equals(p.getUniqueId().toString())) {
                        registered1.setMinecraftName(p.getName());
                        registered = registered1;
                        break;
                    }
                }
                if (registered == null) {
                    p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Du bist §cnicht §7registriert! Falls du deinen §oDiscord-Account §7mit dem" +
                            " falschen §oMinecraft-Account §7vernüpft hast, sende dem Botambus-Bot §3>unregister§7, um die Verknüpfung aufzuheben"));
                    return;
                }
                Main.getRegisteredConfig().registereds.remove(registered);
                Main.getFilesUtil().save();
                p.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Die §oVerknüpfung §7wurde §aaufgehoben"));
                return;
            }
        }

        p.sendMessage(Main.toBaseComponent(syntaxmsg));
        return;
    }


}