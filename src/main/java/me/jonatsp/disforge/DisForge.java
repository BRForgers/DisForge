package me.jonatsp.disforge;

import javafx.util.Pair;
import me.jonatsp.disforge.commands.ShrugCommand;
import me.jonatsp.disforge.proxy.CommonProxy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Mod(modid = DisForge.MOD_ID, name = DisForge.MOD_NAME, version = DisForge.VERSION, acceptableRemoteVersions = "*")
public class DisForge {

    public static final String MOD_ID = "disforge", MOD_NAME = "DisForge", VERSION = "1.12.2-1.0.1.0";
    public static long startedTime;

    public static Logger logger = LogManager.getLogger(MOD_NAME);
    public static JDA jda;
    public static TextChannel textChannel;

    @SidedProxy(clientSide = "me.jonatsp.disforge.proxy.ClientProxy", serverSide = "me.jonatsp.disforge.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
        startedTime = System.currentTimeMillis();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ShrugCommand());
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent e){
        proxy.serverStarted(e);
    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent e){
        proxy.serverStopped(e);
    }

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
            for (Member m : textChannel.getMembers()) {
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

}