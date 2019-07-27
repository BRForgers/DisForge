package me.jonatsp.disforge.proxy;

import me.jonatsp.disforge.Configuration;
import me.jonatsp.disforge.DisForge;
import me.jonatsp.disforge.event.DiscordEventListener;
import me.jonatsp.disforge.event.MinecraftEventListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

import javax.security.auth.login.LoginException;

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
    }

    @Override
    public void serverStopped(FMLServerStoppedEvent e) {
        super.serverStopped(e);
        jda.getTextChannelById(Configuration.channelId).sendMessage("**Server stopped!**").queue();
    }
}
