package bspkrs.crystalwing.fml;

import java.util.EnumSet;

import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void onLoad()
    {
        new CWClient();
        TickRegistry.registerTickHandler(new CWTicker(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
    }
    
    @Override
    public boolean isEnabled()
    {
        return CWClient.instance().serverDetected;
    }
}
