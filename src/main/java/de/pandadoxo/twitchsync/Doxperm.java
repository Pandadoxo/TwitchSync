// -----------------------
// Coded by Pandadoxo
// on 08.12.2020 at 07:51 
// -----------------------

package de.pandadoxo.twitchsync;


import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Doxperm {

    private final String noPermission;

    public Doxperm(String prefix) {
        this.noPermission = prefix + "§7Du hast keine ausreichende §cBerechtigung §7für diesen Befehl! \n" +
                prefix + "§7Benötigte Berechtigung: §e";
    }

    public boolean has(ProxiedPlayer player, String permission) {
        return has(player, permission, false);
    }

    public boolean has(ProxiedPlayer player, String permission, boolean message) {
        if (hasRootPerm(player, permission)) return true;
        if (message)
            player.sendMessage(TextComponent.fromLegacyText(noPermission + permission));
        return false;
    }

    public boolean hasRootPerm(ProxiedPlayer player, String permission) {
        if (permission == null || permission.equals("")) return false;
        if (player.hasPermission(permission)) return true;
        return hasRootPerm(player, getRootPerm(permission));
    }

    private String getRootPerm(String permission) {
        StringBuilder builder = new StringBuilder();
        String[] args = permission.replace(".*", "").split("\\.");
        if (args.length == 0) return null;
        int i = 0;
        for (String arg : args) {
            i++;
            if (args.length == i) break;
            builder.append(arg).append(".");
            if (arg.length() + 1 != i) {
                builder.append("*");
                break;
            }
        }
        return builder.toString();
    }
}
