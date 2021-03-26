// -----------------------
// Coded by Pandadoxo
// on 29.12.2020 at 13:11 
// -----------------------

package de.pandadoxo.twitchsync.core;

import java.util.Random;

public class Code {

    private String code;
    private long createdAt;
    private long exireAt;

    public Code(long createdAt) {
        this.code = createCode();
        this.createdAt = createdAt;
        this.exireAt = createdAt + 5 * 60 * 1000;
    }

    public String createCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        int length = 20;

        Random random = new Random();
        char[] chars = new char[length];

        for (int i = 0; i < length; i++) {
            chars[i] = characters.charAt(random.nextInt(characters.length()));
        }
        for (char aChar : chars) {
            code.append(aChar);
        }

        return code.toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getExireAt() {
        return exireAt;
    }

    public void setExireAt(long exireAt) {
        this.exireAt = exireAt;
    }
}
