package me.jonatsp.disforge;

import me.jonatsp.disforge.proxy.CommonProxy;
import net.dv8tion.jda.core.JDA;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(
        modid = DisForge.MOD_ID,
        name = DisForge.MOD_NAME,
        version = DisForge.VERSION,
        acceptableRemoteVersions = "*"
)
public class DisForge {
    public static final String MOD_ID = "disforge";
    public static final String MOD_NAME = "DisForge";
    public static final String VERSION = "1.12.2-1.0.1.0";
    public static long startedTime;
    @Mod.Instance(MOD_ID)
    public static DisForge INSTANCE;


    public static Logger logger = LogManager.getLogger(MOD_NAME);

    private JDA jda;

    @SidedProxy(
            clientSide = "me.jonatsp.disforge.proxy.ClientProxy",
            serverSide = "me.jonatsp.disforge.proxy.ServerProxy"
    )
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
    public void onServerStopped(FMLServerStoppedEvent e){
        proxy.serverStopped(e);
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent e){
        proxy.serverStarted(e);
    }
}