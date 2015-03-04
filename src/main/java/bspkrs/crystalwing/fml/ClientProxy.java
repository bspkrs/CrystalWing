package bspkrs.crystalwing.fml;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import bspkrs.crystalwing.CWSettings;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerItemModels()
    {
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        mesher.register(CWSettings.crystalWing, 0, new ModelResourceLocation("crystalwing:crystalWing", "inventory"));
        mesher.register(CWSettings.crystalWingBurning, 0, new ModelResourceLocation("crystalwing:crystalWingBurning", "inventory"));
        mesher.register(CWSettings.crystalWingBurnt, 0, new ModelResourceLocation("crystalwing:crystalWingBurnt", "inventory"));
        mesher.register(CWSettings.enderScepter, 0, new ModelResourceLocation("crystalwing:enderScepter", "inventory"));
    }

    @Override
    public void registerTickHandler()
    {
        if (!CWTicker.isRegistered())
            FMLCommonHandler.instance().bus().register(new CWTicker());
    }
}
