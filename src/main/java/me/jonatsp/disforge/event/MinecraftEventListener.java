package me.jonatsp.disforge.event;

import com.mashape.unirest.http.Unirest;
import javafx.util.Pair;
import me.jonatsp.disforge.Configuration;
import me.jonatsp.disforge.DisForge;
import me.jonatsp.disforge.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.json.JSONObject;

public class MinecraftEventListener {

    @SubscribeEvent
    public static void onServerChat(ServerChatEvent e){
        Pair<String, String> convertedPair = Utils.convertMentionsFromNames(e.getMessage());
        JSONObject body = new JSONObject();
        body.put("username", e.getUsername());
        body.put("avatar_url", "https://mc-heads.net/avatar/" + e.getUsername());
        body.put("content", convertedPair.getKey());
        try {
            Unirest.post(Configuration.webhookURL).header("Content-Type", "application/json").body(body).asJsonAsync();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JSONObject newComponent = new JSONObject(ITextComponent.Serializer.componentToJson(e.getComponent()));
        newComponent.getJSONArray("with").getJSONObject(1).put("text", convertedPair.getValue());
        e.setComponent(ITextComponent.Serializer.jsonToComponent(newComponent.toString()));
    }

    @SubscribeEvent
    public static void onPlayerAdvancement(AdvancementEvent e) {
        if(e.getAdvancement().getDisplay() != null) {
            DisForge.textChannel.sendMessage(e.getEntityPlayer().getName()+" has made the advancement **["+e.getAdvancement().getDisplay().getTitle().getUnformattedText()+"]**").queue();
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent e) {
        if( e.getEntity() instanceof EntityPlayer ) {
            DisForge.textChannel.sendMessage("**"+e.getSource().getDeathMessage(e.getEntityLiving()).getUnformattedText()+"**").queue();
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent e){
        DisForge.textChannel.sendMessage("**" + e.player.getName() + " joined the server!**").queue();
    }

    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent e){
        DisForge.textChannel.sendMessage("**" + e.player.getName() + " left the server!**").queue();
    }

}
