package bspkrs.crystalwing.fml;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerTickHandler()
    {
        if (!CWTicker.isRegistered())
            FMLCommonHandler.instance().bus().register(new CWTicker());
    }
}
