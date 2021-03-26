// -----------------------
// Coded by Pandadoxo
// on 28.12.2020 at 22:59 
// -----------------------

package de.pandadoxo.twitchsync.commands;

import de.pandadoxo.twitchsync.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BotCmd extends Command implements TabExecutor {

    private static final String syntaxmsg = Main.WRONG_SYNTAX() + "/bot (start | stop | rlconf)";

    public BotCmd(String name) {
        super(name);
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (!Main.getDoxperm().has(p, "twitchsync.bot", true)) {
                return;
            }
        }

        if (args.length > 1) {
            sender.sendMessage(Main.toBaseComponent(syntaxmsg));
            return;
        }

        if (args.length == 0) {
            sender.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Status: " + (Main.getTwitchbot().isShutdown() ?
                    "§cOffline" : "§aOnline")));
            sender.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Token: " +
                    (Main.getBotConfig().getBotToken().equals(Main.getBotConfig().DEFAULT_TOKEN) ? "§cNicht gesetzt" : "§agesetzt")));
            sender.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7GuildId: §b" + Main.getBotConfig().getGuildId()));
            sender.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7RoleId: §b" + Main.getBotConfig().getRoleId()));
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("start")) {
                if (!Main.getTwitchbot().isShutdown()) {
                    sender.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Der §eBot §7ist bereits §agestartet"));
                    return;
                }
                Main.getTwitchbot().start();
                sender.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Der §eBot §7wird gestartet"));
                ProxyServer.getInstance().getScheduler().schedule(Main.getInstance(), () -> {
                    if (Main.getTwitchbot().isShutdown()) {
                        sender.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Der Bot konnte §cnicht §7gestartet werden"));
                    }
                }, 1, TimeUnit.SECONDS);
                return;
            }

            if (args[0].equalsIgnoreCase("stop")) {
                if (Main.getTwitchbot().isShutdown()) {
                    sender.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Der §eBot §7ist bereits §cgestoppt"));
                    return;
                }
                Main.getTwitchbot().stop();
                sender.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Der §eBot §7wird gestoppt"));
                return;
            }
            if (args[0].equalsIgnoreCase("rlconf")) {
                Main.getFilesUtil().load();
                sender.sendMessage(Main.toBaseComponent(Main.PREFIX() + "§7Die §eConfig §7wurde reloaded"));
                return;
            }
        }

    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if (!Main.getDoxperm().has(p, "twitchsync.bot")) {
                return new ArrayList<>();
            }
        }
        List<String> tocomplete = new ArrayList<>();
        List<String> complete = new ArrayList<>();

        if (args.length == 1) {
            tocomplete.addAll(Arrays.asList("stop", "start", "rlconf"));
        }

        for (String tc : tocomplete) {
            if (tc.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                complete.add(tc);
            }
        }
        return complete;
    }
}
  
  