package com.pubnub.api.integration.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenerator {

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String newValue(int length) {
        final Random random = new SecureRandom();
        if (length <= 0) {
            throw new IllegalArgumentException("String length must be a positive integer");
        }

        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }

    public static String emoji() {
        final Random random = new SecureRandom();
        final List<String> emojiSet = new ArrayList<>();
        emojiSet.add("😀");
        emojiSet.add("😁");
        emojiSet.add("😂");
        emojiSet.add("🤣");
        emojiSet.add("😃");
        emojiSet.add("😄");
        emojiSet.add("😅");
        emojiSet.add("😆");
        emojiSet.add("😉");
        emojiSet.add("😊");
        emojiSet.add("😋");
        emojiSet.add("😎");
        emojiSet.add("😍");
        emojiSet.add("😘");
        emojiSet.add("🥰");
        emojiSet.add("😗");
        emojiSet.add("😙");
        emojiSet.add("😚");
        emojiSet.add("☺️");
        emojiSet.add("🙂");
        emojiSet.add("🤗");
        emojiSet.add("🤩");
        emojiSet.add("🤔");
        emojiSet.add("🤨");
        emojiSet.add("😐");
        emojiSet.add("😑");
        emojiSet.add("😶");
        emojiSet.add("🙄");
        emojiSet.add("😏");
        emojiSet.add("😣");
        emojiSet.add("😥");
        emojiSet.add("😮");
        emojiSet.add("🤐");
        emojiSet.add("😯");
        emojiSet.add("😪");
        emojiSet.add("😫");
        emojiSet.add("😴");
        emojiSet.add("😌");
        emojiSet.add("😛");
        emojiSet.add("😜");
        emojiSet.add("😝");
        emojiSet.add("🤤");
        emojiSet.add("😒");
        emojiSet.add("😓");
        emojiSet.add("😔");
        emojiSet.add("😕");
        emojiSet.add("🙃");
        emojiSet.add("🤑");
        emojiSet.add("😲");
        emojiSet.add("☹️");
        emojiSet.add("🙁");
        emojiSet.add("😖");
        emojiSet.add("😞");
        emojiSet.add("😟");
        emojiSet.add("😤");
        emojiSet.add("😢");
        emojiSet.add("😭");
        emojiSet.add("😦");
        emojiSet.add("😧");
        emojiSet.add("😨");
        emojiSet.add("😩");
        emojiSet.add("🤯");
        emojiSet.add("😬");
        emojiSet.add("😰");
        emojiSet.add("😱");
        emojiSet.add("🥵");
        emojiSet.add("🥶");
        emojiSet.add("😳");
        emojiSet.add("🤪");
        emojiSet.add("😵");
        emojiSet.add("😡");
        emojiSet.add("😠");
        emojiSet.add("🤬");
        emojiSet.add("😷");
        emojiSet.add("🤒");
        emojiSet.add("🤕");
        emojiSet.add("🤢");
        emojiSet.add("🤮");
        emojiSet.add("🤧");
        emojiSet.add("😇");
        emojiSet.add("🤠");
        emojiSet.add("🤡");
        emojiSet.add("🥳");
        emojiSet.add("🥴");
        emojiSet.add("🥺");
        emojiSet.add("🤥");
        emojiSet.add("🤫");
        emojiSet.add("🤭");
        emojiSet.add("🧐");
        emojiSet.add("🤓");
        emojiSet.add("😈");
        emojiSet.add("👿");
        emojiSet.add("👹");
        emojiSet.add("👺");
        emojiSet.add("💀");
        emojiSet.add("👻");
        emojiSet.add("👽");
        emojiSet.add("🤖");
        emojiSet.add("💩");
        emojiSet.add("😺");
        emojiSet.add("😸");
        emojiSet.add("😹");
        emojiSet.add("😻");
        emojiSet.add("😼");
        emojiSet.add("😽");
        emojiSet.add("🙀");
        emojiSet.add("😿");
        emojiSet.add("😾");
        return emojiSet.get(random.nextInt(emojiSet.size())) + "";
    }

    public static String unicode(int length) {
        final String unicodeChars = "!?+-=";

        final Random random = new SecureRandom();

        if (length <= 0) {
            length = unicodeChars.length();
        }

        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(unicodeChars.charAt(random.nextInt(unicodeChars.length())));
        }

        return sb.toString();
    }

    public static String get() {
        return newValue(5).concat(unicode(5)).concat(newValue(5));
    }

    public static int randomNumber(int min, int max) {
        final Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
