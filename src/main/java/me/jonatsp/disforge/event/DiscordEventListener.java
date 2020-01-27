package me.jonatsp.disforge.event;

import me.jonatsp.disforge.Configuration;
import me.jonatsp.disforge.Utils;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Arrays;


public class DiscordEventListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent e) {

        if(e.getAuthor() != e.getJDA().getSelfUser() && !e.getAuthor().isFake() && e.getChannel().getId().equals(Configuration.channelId)) {

            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            if(e.getMessage().getContentRaw().startsWith("!console") && Arrays.asList(Configuration.adminsIds).contains(e.getAuthor().getId())) {
                String command = e.getMessage().getContentRaw().replace("!console", "");
                server.getCommandManager().executeCommand(server, command);

            }else if(e.getMessage().getContentRaw().startsWith("!online")) {
                String[] onlinePlayers = server.getPlayerList().getOnlinePlayerNames();
                StringBuilder playerList = new StringBuilder("```\n=============== Online Players (" + onlinePlayers.length + ") ===============\n");
                for (String player : onlinePlayers) {
                    playerList.append("\n").append(player);
                }
                playerList.append("```");
                e.getChannel().sendMessage(playerList.toString()).queue();

            }else if (e.getMessage().getContentRaw().startsWith("!tps")) {
                StringBuilder tpss = new StringBuilder("```\n============= TPS per loaded dimension ==============\n");
                for (Integer id : DimensionManager.getIDs()) {
                    double worldTickTime = Utils.mean(server.worldTickTimes.get(id)) * 1.0E-6D;
                    double worldTPS = Math.min(1000.0 / worldTickTime, 20);
                    tpss.append("\n").append(DimensionManager.getProviderType(id).getName()).append(" (").append(id).append("): ").append(worldTPS).append("\n");
                }
                tpss.append("```");
                e.getChannel().sendMessage(tpss.toString()).queue();

            }else if(e.getMessage().getContentRaw().startsWith("!help")){
                String help = "```\n" + "=============== Commands ==============\n" +
                                 "\n" + "!online: list server online players" +
                                 "\n" + "!tps: shows loaded dimensions tps´s" +
                                 "\n" + "!console <command>: executes commands in the server console (admins only)\n```";
                e.getChannel().sendMessage(help).queue();

            }else
                server.getPlayerList().sendMessage(new TextComponentString(Utils.getTextFormattingByColor(e.getMember().getColor()) + "[Discord]" + TextFormatting.RESET + " <" + e.getMember().getEffectiveName() + "> " + e.getMessage().getContentDisplay() + ((e.getMessage().getAttachments().size() > 0) ? "<att>" : "") + ((e.getMessage().getEmbeds().size() > 0) ? "<embed>" : "")));

        }

    }

}
