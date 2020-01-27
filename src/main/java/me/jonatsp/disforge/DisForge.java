package me.jonatsp.disforge;

import me.jonatsp.disforge.commands.ShrugCommand;
import me.jonatsp.disforge.proxy.CommonProxy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

}