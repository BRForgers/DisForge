package me.jonatsp.disforge.event;

import me.jonatsp.disforge.Configuration;
import me.jonatsp.disforge.Utils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Arrays;


public class DiscordEventListener extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent e){
        if(e.getAuthor() != e.getJDA().getSelfUser() && !e.getAuthor().isFake() && e.getChannel().getId().equals(Configuration.channelId)){
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            if(e.getMessage().getContentRaw().startsWith("!console") && Arrays.asList(Configuration.adminsIds).contains(e.getAuthor().getId())) {
                String msg = e.getMessage().getContentRaw();
                String command = msg.substring(msg.indexOf("d") + 1).trim();
                server.getCommandManager().executeCommand(server, command);
            }else if(e.getMessage().getContentRaw().startsWith("!online")) {
                String[] onlinePlayers = server.getPlayerList().getOnlinePlayerNames();
                StringBuilder playerList = new StringBuilder("```\n=============== Online Players (" + onlinePlayers.length + ") ===============\n");
                for (String player : onlinePlayers) {
                    playerList.append("\n").append(player);
                }
                playerList.append("```");
                e.getChannel().sendMessage(playerList.toString()).queue();
            } else if (e.getMessage().getContentRaw().startsWith("!tps")) {
                StringBuilder tpss = new StringBuilder("```\n============= TPS per loaded dimension ==============\n");
                for (Integer id : DimensionManager.getIDs()) {
                    double worldTickTime = Utils.mean(server.worldTickTimes.get(id)) * 1.0E-6D;
                    double worldTPS = Math.min(1000.0 / worldTickTime, 20);
                    tpss.append("\n").append(DimensionManager.getProviderType(id).getName()).append(" (" + id + "): ").append(worldTPS).append("\n");
                }
                tpss.append("```");

                e.getChannel().sendMessage(tpss.toString()).queue();
            }else if(e.getMessage().getContentRaw().startsWith("!help")){
                StringBuilder help = new StringBuilder().append("```\n").append("=============== Commands ==============\n")
                        .append("\n").append("!online: list server online players")
                        .append("\n").append("!tps: shows loaded dimensions tps´s")
                        .append("\n").append("!console <command>: executes commands in the server console (admins only)\n```");
                e.getChannel().sendMessage(help.toString()).queue();
            } else {
                server.getPlayerList().sendMessage(new TextComponentString(Utils.getTextFormattingByColor(e.getMember().getColor()) + "[Discord]" + TextFormatting.RESET + " <" + e.getMember().getEffectiveName() + "> " + e.getMessage().getContentDisplay() + ((e.getMessage().getAttachments().size() > 0) ? " <att>" : "") + ((e.getMessage().getEmbeds().size() > 0) ? " <embed>" : "")));
            }
        }
    }
}
