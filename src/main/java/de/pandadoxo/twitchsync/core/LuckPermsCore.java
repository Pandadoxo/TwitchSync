// -----------------------
// Coded by Pandadoxo
// on 26.03.2021 at 08:56 
// -----------------------

package de.pandadoxo.twitchsync.core;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LuckPermsCore {

    private final LuckPerms api;

    public LuckPermsCore(LuckPerms api) {
        this.api = api;
    }

    public boolean isPlayerInGroup(ProxiedPlayer player, String group) {
        return player.hasPermission("group." + group);
    }

    public boolean isPlayerSub(ProxiedPlayer player) {
        return player.hasPermission("group.subscriber");
    }

    public User getUser(ProxiedPlayer player) {
        return api.getPlayerAdapter(ProxiedPlayer.class).getUser(player);
    }

    public Group getSubscriber() {
        return api.getGroupManager().getGroup("subscriber");
    }

    public boolean addPlayerGroup(ProxiedPlayer player, Group group) {
        User user = getUser(player);
        if (user == null) return false;

        Node node = InheritanceNode.builder(group).build();
        user.data().add(node);
        api.getUserManager().saveUser(user);
        return true;
    }

    public boolean addPlayerSubscriber(ProxiedPlayer player) {
        Group subscriber = getSubscriber();
        if (subscriber == null) return false;

        return addPlayerGroup(player, subscriber);
    }

    public boolean removePlayerGroup(ProxiedPlayer player, Group group) {
        User user = getUser(player);
        if (user == null) return false;

        Node node = InheritanceNode.builder(group).build();
        user.data().remove(node);
        api.getUserManager().saveUser(user);
        return true;
    }

    public boolean removePlayerSubscriber(ProxiedPlayer player) {
        Group subscriber = getSubscriber();
        if (subscriber == null) return false;

        return removePlayerGroup(player, subscriber);
    }

}
