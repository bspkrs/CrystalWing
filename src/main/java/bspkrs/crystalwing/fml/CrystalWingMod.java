package bspkrs.crystalwing.fml;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import bspkrs.crystalwing.CWSettings;
import bspkrs.util.Const;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = "@MOD_VERSION@", dependencies = "required-after:bspkrsCore@[@BSCORE_VERSION@,)",
        useMetadata = true, guiFactory = Reference.GUI_FACTORY, updateJSON = Const.VERSION_URL_BASE + Reference.MODID + Const.VERSION_URL_EXT)
public class CrystalWingMod
{
    @Metadata(value = Reference.MODID)
    public static ModMetadata       metadata;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_COMMON)
    public static CommonProxy       proxy;

    @Instance(value = Reference.MODID)
    public static CrystalWingMod    instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        CWSettings.initConfig(event.getSuggestedConfigurationFile());
        CWSettings.registerStuff();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerTickHandler();
        proxy.registerItemModels();
        MinecraftForge.EVENT_BUS.register(instance);
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
