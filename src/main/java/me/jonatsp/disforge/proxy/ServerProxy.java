package me.jonatsp.disforge.proxy;

import me.jonatsp.disforge.Configuration;
import me.jonatsp.disforge.DisForge;
import me.jonatsp.disforge.Utils;
import me.jonatsp.disforge.event.DiscordEventListener;
import me.jonatsp.disforge.event.MinecraftEventListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.Level;

import javax.security.auth.login.LoginException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerProxy extends CommonProxy{
    private JDA jda;
    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        MinecraftForge.EVENT_BUS.register(MinecraftEventListener.class);

    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);

        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(Configuration.botToken)
                    .addEventListener(new DiscordEventListener())
                    .build();
            jda.awaitReady();
            MinecraftEventListener.setJda(jda);
        } catch (LoginException ex) {
            DisForge.logger.error("Unable to login!", ex);
        } catch (InterruptedException ex) {
            DisForge.logger.error(ex);
        }
    }
    @Override
    public void serverStarted(FMLServerStartedEvent e) {
        super.serverStarted(e);
        jda.getTextChannelById(Configuration.channelId).sendMessage("**Server started!**").queue();
        Runnable updateTopic = () -> {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            Runtime runtime = Runtime.getRuntime();

            String free = String.valueOf(runtime.freeMemory() / 1024 / 1024);
            String max = String.valueOf(runtime.totalMemory() / 1024 / 1024);
            String using = String.valueOf((runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024);
            double meanTickTime = Utils.mean(server.tickTimeArray) * 1.0E-6D;
            double meanTPS = Math.min(1000.0 / meanTickTime, 20);

            String topic = "TPS: " + meanTPS
                    + "\nOnline Players: " + server.getOnlinePlayerNames().length  + "/" + server.getMaxPlayers()
                    + "\nUptime: " + (System.currentTimeMillis() - DisForge.startedTime) / 1000 / 60 + " mins"
                    + "\nMemory: "+ using + "/" + free + "/" + max + " MBs using/free/max";
            jda.getTextChannelById(Configuration.channelId).getManager().setTopic(topic).queue();
        };
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(updateTopic, 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public void serverStopped(FMLServerStoppedEvent e) {
        super.serverStopped(e);
        jda.getTextChannelById(Configuration.channelId).sendMessage("**Server stopped!**").queue();
    }
}
