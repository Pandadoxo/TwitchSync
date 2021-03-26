// -----------------------
// Coded by Pandadoxo
// on 28.12.2020 at 23:07 
// -----------------------

package de.pandadoxo.twitchsync.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import de.pandadoxo.twitchsync.Main;
import de.pandadoxo.twitchsync.discordbot.Twitchbot;
import de.pandadoxo.twitchsync.discordbot.config.BotConfig;
import de.pandadoxo.twitchsync.discordbot.config.Registered;
import de.pandadoxo.twitchsync.discordbot.config.RegisteredConfig;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class FilesUtil {

    private static final File botFile = new File(Main.getInstance().getDataFolder(), "botConfig.json");
    private static final File registeredFile = new File(Main.getInstance().getDataFolder(), "registered.json");

    public FilesUtil() {
        create();
        load();
    }

    public static File getBotFile() {
        return botFile;
    }

    public void create() {
        try {
            if (!botFile.exists()) {
                botFile.getParentFile().mkdirs();
                botFile.createNewFile();
            }
            if (!registeredFile.exists()) {
                registeredFile.getParentFile().mkdirs();
                registeredFile.createNewFile();
            }
        } catch (IOException ignored) {
        }
    }

    public void save() {
        saveConfig();
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            PrintWriter registerW = new PrintWriter(registeredFile);
            registerW.println(builder.create().toJson(Main.getRegisteredConfig()));
            registerW.flush();
            registerW.close();

            Guild guild = Twitchbot.getJda().getGuildById(Main.getBotConfig().getGuildId());
            if (guild == null) return;
            Role role = guild.getRoleById(Main.getBotConfig().getRoleId());
            if (role == null) return;

            for (Registered registered : Main.getRegisteredConfig().registereds) {
                Member member = guild.getMemberById(registered.getDiscordID());
                if (member == null) {
                    registered.setSubscriber(false);
                    continue;
                }
                registered.setSubscriber(member.getRoles().contains(role));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();

            builder.setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    return fieldAttributes.getAnnotation(Exclude.class) != null;
                }

                @Override
                public boolean shouldSkipClass(Class<?> aClass) {
                    return false;
                }
            });

            PrintWriter botW = new PrintWriter(botFile);
            botW.println(builder.create().toJson(Main.getBotConfig()));
            botW.flush();
            botW.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {

            JsonReader botR = new JsonReader(new FileReader(botFile));
            Main.setBotConfig(new Gson().fromJson(botR, BotConfig.class));
            botR.close();
            if (Main.getBotConfig() == null) Main.setBotConfig(new BotConfig());

            JsonReader registerR = new JsonReader(new FileReader(registeredFile));
            Main.setRegisteredConfig(new Gson().fromJson(registerR, RegisteredConfig.class));
            registerR.close();
            if (Main.getRegisteredConfig() == null) Main.setRegisteredConfig(new RegisteredConfig());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Exclude {
    }
}
