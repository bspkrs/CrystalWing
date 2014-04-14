package bspkrs.crystalwing.fml.gui;

import java.lang.reflect.Method;

import net.minecraft.client.gui.GuiScreen;
import bspkrs.crystalwing.CWSettings;
import bspkrs.crystalwing.ConfigElement;
import bspkrs.util.config.ConfigCategory;
import bspkrs.util.config.ConfigProperty;
import bspkrs.util.config.Configuration;
import bspkrs.util.config.gui.GuiConfig;
import bspkrs.util.config.gui.IConfigProperty;

public class GuiCWConfig extends GuiConfig
{
    public GuiCWConfig(GuiScreen parent) throws NoSuchMethodException, SecurityException
    {
        super(parent, getProps(), Configuration.class.getDeclaredMethod("save"), CWSettings.getConfig(),
                CWSettings.class.getDeclaredMethod("syncConfig"), null);
    }
    
    public GuiCWConfig(GuiScreen par1GuiScreen, IConfigProperty[] properties, Method saveAction, Object configObject, Method afterSaveAction, Object afterSaveObject)
    {
        super(par1GuiScreen, properties, saveAction, configObject, afterSaveAction, afterSaveObject);
    }
    
    private static IConfigProperty[] getProps()
    {
        ConfigCategory cc = CWSettings.getConfig().getCategory(Configuration.CATEGORY_GENERAL);
        IConfigProperty[] props = new IConfigProperty[ConfigElement.values().length];
        for (int i = 0; i < ConfigElement.values().length; i++)
        {
            bspkrs.crystalwing.ConfigElement ce = ConfigElement.values()[i];
            props[i] = new ConfigProperty(cc.get(ce.key()), ce.propertyType());
        }
        
        return props;
    }
}
