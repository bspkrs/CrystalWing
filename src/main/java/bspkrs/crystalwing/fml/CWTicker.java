package bspkrs.crystalwing.fml;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CWTicker
{
    private Minecraft      mcClient;
    private static boolean isRegistered = false;

    public CWTicker()
    {
        mcClient = FMLClientHandler.instance().getClient();
        isRegistered = true;
    }

    @SubscribeEvent
    public void onTick(ClientTickEvent event)
    {
        if (!event.phase.equals(Phase.START))
        {
            boolean keepTicking = !(mcClient != null && mcClient.thePlayer != null && mcClient.theWorld != null);

            if (!keepTicking && isRegistered)
            {
                if (bspkrsCoreMod.instance.allowUpdateCheck && CrystalWingMod.versionChecker != null)
                    if (!CrystalWingMod.versionChecker.isCurrentVersion())
                        for (String msg : CrystalWingMod.versionChecker.getInGameMessage())
                            mcClient.thePlayer.addChatMessage(new ChatComponentText(msg));

                FMLCommonHandler.instance().bus().unregister(this);
                isRegistered = false;
            }
        }
    }

    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
