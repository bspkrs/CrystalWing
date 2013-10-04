package bspkrs.crystalwing.localization;

import java.net.URL;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class LocalizationHandler
{
    private enum Localization
    {
        US("en_US"),
        GERMAN("de_DE"),
        FRENCH("fr_FR"),
        PORTUGAL("pt_PT"),
        BRAZIL("pt_BR"),
        ITALIAN("it_IT"),
        RUSSIAN("ru_RU"),
        CHINESE_SC("zh_CN"),
        CHINESE_TC("zh_TW"),
        SPANISH_AR("es_AR"),
        SPANISH_ES("es_ES"),
        SPANISH_MX("es_MX"),
        SPANISH_UY("es_UY"),
        SPANISH_VE("es_VE"),
        CZECH("cs_CZ"),
        WELSH("cy_GB"),
        JAPANESE("ja_JP"),
        DUTCH("nl_NL"),
        POLISH("pl_PL"),
        SERBIAN("sr_RS"),
        SWEDISH("sv_SE");
        
        private final String locale;
        
        Localization(String locale)
        {
            this.locale = locale;
        }
        
        public String filename()
        {
            return String.format("/bspkrs/crystalwing/lang/%s.xml", locale);
        }
        
        public String locale()
        {
            return locale;
        }
    }
    
    public static final LocalizationHandler instance = new LocalizationHandler();
    
    public void loadLanguages()
    {
        for (final Localization localeFile : Localization.values())
        {
            URL urlResource = this.getClass().getResource(localeFile.filename());
            if (urlResource != null)
                LanguageRegistry.instance().loadLocalization(urlResource, localeFile.locale(), true);
        }
    }
}