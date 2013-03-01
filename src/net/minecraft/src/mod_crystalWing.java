package net.minecraft.src;

import net.minecraft.client.Minecraft;
import bspkrs.crystalwing.CrystalWing;
import bspkrs.util.ModVersionChecker;

public class mod_crystalWing extends BaseMod
{
    @MLProp
    public static int         idCrystalWing = 3100;
    @MLProp
    public static int         idBurningWing = 3101;
    @MLProp
    public static int         idBurnedWing  = 3102;
    @MLProp(info = "Number of Crystal Wing uses. Set to 0 for infinite.")
    public static int         uses          = 8;
    @MLProp(info = "Maximum distance for the Burned Wing random teleportation.\n\n**ONLY EDIT WHAT IS BELOW THIS**")
    public static int         teleDistance  = 500;
    
    public static boolean     allowUpdateCheck;
    private ModVersionChecker versionChecker;
    private String            versionURL    = "https://dl.dropbox.com/u/20748481/Minecraft/1.4.6/crystalWing.version";
    private String            mcfTopic      = "http://www.minecraftforum.net/topic/1009577-";
    
    public mod_crystalWing()
    {
        allowUpdateCheck = mod_bspkrsCore.allowUpdateCheck;
        
        if (allowUpdateCheck)
            versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic, ModLoader.getLogger());
    }
    
    @Override
    public String getName()
    {
        return "CrystalWing";
    }
    
    @Override
    public String getVersion()
    {
        return "ML 1.4.6.r02";
    }
    
    @Override
    public String getPriorities()
    {
        return "after:mod_bspkrsCore";
    }
    
    @Override
    public void load()
    {
        if (allowUpdateCheck)
            versionChecker.checkVersionWithLogging();
        ModLoader.setInGameHook(this, true, true);
        
        new CrystalWing(false, idCrystalWing, idBurningWing, idBurnedWing, uses, teleDistance);
    }
    
    @Override
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if (allowUpdateCheck)
        {
            if (!versionChecker.isCurrentVersion())
                for (String msg : versionChecker.getInGameMessage())
                    mc.thePlayer.addChatMessage(msg);
        }
        return false;
    }
}
