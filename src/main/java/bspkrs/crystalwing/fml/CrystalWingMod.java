package bspkrs.crystalwing.fml;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.crystalwing.CWSettings;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = "@MOD_VERSION@", dependencies = "required-after:bspkrsCore@[@BSCORE_VERSION@,)",
        useMetadata = true, guiFactory = Reference.GUI_FACTORY)
public class CrystalWingMod
{
    public static ModVersionChecker versionChecker;
    private final String            versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/crystalWingForge.version";
    private final String            mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";

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
        proxy.registerItemModels();
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
