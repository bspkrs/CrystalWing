package bspkrs.crystalwing.fml;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.crystalwing.CWSettings;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import bspkrs.util.config.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = "@MOD_VERSION@", dependencies = "required-after:bspkrsCore@[@BSCORE_VERSION@,)",
        useMetadata = true, guiFactory = Reference.GUI_FACTORY)
public class CrystalWingMod
{
    public static ModVersionChecker versionChecker;
    private String                  versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/crystalWingForge.version";
    private String                  mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";
    
    @Metadata(value = Reference.MODID)
    public static ModMetadata       metadata;
    
    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy       proxy;
    
    @Instance(value = Reference.MODID)
    public static CrystalWingMod    instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        CWSettings.initConfig(event.getSuggestedConfigurationFile());
        CWSettings.registerStuff();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerTickHandler();
        FMLCommonHandler.instance().bus().register(instance);
        
        if (bspkrsCoreMod.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(Reference.MODID, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLogging();
        }
    }
    
    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event)
    {
        if (event.modID.equals(Reference.MODID))
        {
            Reference.config.save();
            CWSettings.syncConfig();
        }
    }
}
