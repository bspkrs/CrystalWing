package net.minecraft.src;

import net.minecraft.client.Minecraft;
import bspkrs.crystalwing.CrystalWing;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;

public class mod_CrystalWing extends BaseMod
{
    public static boolean     allowUpdateCheck;
    private ModVersionChecker versionChecker;
    private String            versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/crystalWing.version";
    private String            mcfTopic   = "http://www.minecraftforum.net/topic/1009577-";
    
    public mod_CrystalWing()
    {
        allowUpdateCheck = mod_bspkrsCore.allowUpdateCheck;
        
        if (allowUpdateCheck)
            versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic);
    }
    
    @Override
    public String getName()
    {
        return "CrystalWing";
    }
    
    @Override
    public String getVersion()
    {
        return "ML " + CrystalWing.VERSION_NUMBER;
    }
    
    @Override
    public String getPriorities()
    {
        return "required-after:mod_bspkrsCore";
    }
    
    @Override
    public void load()
    {
        if (allowUpdateCheck)
            versionChecker.checkVersionWithLogging();
        ModLoader.setInGameHook(this, true, true);
        
        new CrystalWing(false);
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
