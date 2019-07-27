package me.jonatsp.disforge.event;

import com.mashape.unirest.http.Unirest;
import me.jonatsp.disforge.Configuration;
import me.jonatsp.disforge.DisForge;
import me.jonatsp.disforge.Utils;
import net.dv8tion.jda.core.JDA;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.json.JSONObject;


public class MinecraftEventListener {
    private static JDA jda;

    public static void setJda(JDA jdaa){
        jda = jdaa;
    }
    @SubscribeEvent
    public static void onServerChat(ServerChatEvent e){
        JSONObject body = new JSONObject();
        body.put("username", e.getUsername());
        body.put("avatar_url", "https://mc-heads.net/avatar/" + e.getUsername());
        body.put("content", e.getMessage());
        try {
            Unirest.post(Configuration.webhookURL).header("Content-Type", "application/json").body(body).asJsonAsync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e){
        jda.getTextChannelById(Configuration.channelId).sendMessage("**" + e.player.getName() + " joined the server!**").queue();
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent e){
        jda.getTextChannelById(Configuration.channelId).sendMessage("**" + e.player.getName() + " left the server!**").queue();
    }
   /* private static int count = 0;
    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent e){
        count++;
        if(count == (20 * 5)){
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            Runtime runtime = Runtime.getRuntime();

            String free = String.valueOf(runtime.freeMemory() / 1024 / 1024);
            String max = String.valueOf(runtime.totalMemory() / 1024 / 1024);
            String using = String.valueOf((runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);
            count = 0;
            double meanTickTime = Utils.mean(server.tickTimeArray) * 1.0E-6D;
            double meanTPS = Math.min(1000.0/meanTickTime, 20);
            long time = server.getCurrentTime();
            String topic = "TPS: " + meanTPS
                    + "\nOnline Players: " + server.getOnlinePlayerNames().length  + "/" + server.getMaxPlayers()
                    + "\nUptime: " + String.valueOf((System.currentTimeMillis() - DisForge.startedTime) / 1000 / 60) + " mins"
                    + "\nMemory: "+ using + "/" + free + "/" + max + " MBs using/free/max";
            jda.getTextChannelById(Configuration.channelId).getManager().setTopic(topic).queue();
        }
    }*/


}
