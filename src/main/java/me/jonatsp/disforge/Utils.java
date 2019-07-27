package me.jonatsp.disforge;

import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Utils {
    public static TextFormatting getTextFormattingByColor(Color color){
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
    public static long mean(long[] values)
    {
        long sum = 0L;
        for (long v : values)
        {
            sum += v;
        }
        return sum / values.length;
    }
}
