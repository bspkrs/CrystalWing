package bspkrs.crystalwing;

import static net.minecraftforge.common.config.Property.Type.BOOLEAN;
import static net.minecraftforge.common.config.Property.Type.INTEGER;
import net.minecraftforge.common.config.Property;

public enum ConfigElement
{
    ALLOW_DEBUG_LOGGING("allowDebugLogging", "bspkrs.cw.configgui.allowDebugLogging", "", BOOLEAN),
    USES("uses", "bspkrs.cw.configgui.uses", "Number of Crystal Wing uses. Set to 0 for infinite.", INTEGER),
    TELE_DISTANCE("teleDistance", "bspkrs.cw.configgui.teleDistance", "Maximum distance for the Burned Wing random teleportation.", INTEGER);

    private String        key;
    private String        langKey;
    private String        desc;
    private Property.Type propertyType;
    private String[]      validStrings;

    private ConfigElement(String key, String langKey, String desc, Property.Type propertyType, String[] validStrings)
    {
        this.key = key;
        this.langKey = langKey;
        this.desc = desc;
        this.propertyType = propertyType;
        this.validStrings = validStrings;
    }

    private ConfigElement(String key, String langKey, String desc, Property.Type propertyType)
    {
        this(key, langKey, desc, propertyType, new String[0]);
    }

    public String key()
    {
        return key;
    }

    public String languageKey()
    {
        return langKey;
    }

    public String desc()
    {
        return desc;
    }

    public Property.Type propertyType()
    {
        return propertyType;
    }

    public String[] validStrings()
    {
        return validStrings;
    }
}