package me.jonatsp.disforge;

import javafx.util.Pair;
import net.dv8tion.jda.api.entities.Member;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static Pair<String, String> convertMentionsFromNames(String message) {

        if (!message.contains("@")) return new Pair<>(message, message);

        List<String> messageList = Arrays.asList(message.split("@[\\S]+"));
        if(messageList.size() == 0) {
            messageList = new ArrayList<>();
            messageList.add("");
        }

        StringBuilder discordString = new StringBuilder(), mcString = new StringBuilder();
        Pattern pattern = Pattern.compile("@[\\S]+");
        Matcher matcher = pattern.matcher(message);

        int x = 0;
        while(matcher.find()) {
            Member member = null;
            for (Member m : DisForge.textChannel.getMembers()) {
                String name = matcher.group().substring(1);
                if (m.getUser().getName().toLowerCase().equals(name.toLowerCase()) || (m.getNickname() != null && m.getNickname().toLowerCase().equals(name.toLowerCase()))) {
                    member = m;
                }
            }
            if (member == null) {
                discordString.append(messageList.get(x)).append(matcher.group());
                mcString.append(messageList.get(x)).append(matcher.group());
            } else {
                discordString.append(messageList.get(x)).append(member.getAsMention());
                mcString.append(messageList.get(x)).append(TextFormatting.YELLOW.toString()).append("@").append(member.getEffectiveName()).append(TextFormatting.WHITE.toString());
            }
            x++;
        }
        if(x < messageList.size()) {
            discordString.append(messageList.get(x));
            mcString.append(messageList.get(x));
        }
        return new Pair<>(discordString.toString(), mcString.toString());
    }

    public static TextFormatting getTextFormattingByColor(Color color) {
        if(color == null) return TextFormatting.BLUE;

        HashMap<TextFormatting, Color> mcColors = new HashMap<>();
        mcColors.put(TextFormatting.BLACK, new Color(0x000000));
        mcColors.put(TextFormatting.DARK_BLUE, new Color(0x0000AA));
        mcColors.put(TextFormatting.DARK_GREEN, new Color(0x00AA00));
        mcColors.put(TextFormatting.DARK_AQUA, new Color(0x00AAAA));
        mcColors.put(TextFormatting.DARK_RED, new Color(0xAA0000));
        mcColors.put(TextFormatting.DARK_PURPLE, new Color(0xAA00AA));
        mcColors.put(TextFormatting.GOLD, new Color(0xFFAA00).brighter());
        mcColors.put(TextFormatting.GRAY, new Color(0xAAAAAA));
        mcColors.put(TextFormatting.DARK_GRAY, new Color(0x555555));
        mcColors.put(TextFormatting.BLUE, new Color(0x5555ff).brighter());
        mcColors.put(TextFormatting.GREEN, new Color(0x55ff55).brighter());
        mcColors.put(TextFormatting.AQUA, new Color(0x55ffff).brighter());
        mcColors.put(TextFormatting.RED, new Color(0xff5555).brighter());
        mcColors.put(TextFormatting.LIGHT_PURPLE, new Color(0xff55ff).brighter().brighter());
        mcColors.put(TextFormatting.YELLOW, new Color(0xffff55).brighter());
        mcColors.put(TextFormatting.WHITE, new Color(0xffffff));

        HashMap<Integer, TextFormatting>  distances = new HashMap<>();
        for(Map.Entry<TextFormatting, Color> colorr: mcColors.entrySet()){
          int distance = Math.abs(color.getRed() - colorr.getValue().getRed()) +
                    Math.abs(color.getGreen() - colorr.getValue().getGreen()) +
                    Math.abs(color.getBlue() - colorr.getValue().getBlue());

          distances.put(distance, colorr.getKey());
        }

        Integer[] dis = distances.keySet().toArray(new Integer[distances.size()]);
        Arrays.sort(dis);

        return distances.get(dis[0]);
    }

    public static long mean(long[] values) {
        long sum = 0L;
        for (long v : values) {
            sum += v;
        }
        return sum / values.length;
    }

}
