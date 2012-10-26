// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;


// Referenced classes of package net.minecraft.src:
//            BaseMod, ModLoader, ItemStack, Item, 
//            ItemCrystalWing, ItemCrystalWingBurning, ItemCrystalWingBurned, Achievement

public class mod_crystalWing extends BaseMod
{

    @MLProp
    public static int idCrystalWing = 3100;
    @MLProp
    public static int idBurningWing = 3101;
    @MLProp
    public static int idBurnedWing = 3102;
    @MLProp(info="Number of Crystal Wing uses. Set to 0 for infinite.")
    public static int uses = 8;
    @MLProp(info="Maximum distance for the Burned Wing teleportation.\n\n**ONLY EDIT WHAT IS BELOW THIS**")
    public static int teleDistance = 500;
    
    static int indexCrystalWing = ModLoader.addOverride("/gui/items.png", "/daftpvf/crystalWing.png");
    static int indexBurningWing = ModLoader.addOverride("/gui/items.png", "/daftpvf/crystalWingBurning.png");
    static int indexBurnedWing = ModLoader.addOverride("/gui/items.png", "/daftpvf/crystalWingBurned.png");
    public static final Item crystalWing = (new ItemCrystalWing(idCrystalWing - 256)).setItemName("crystalWing").setIconIndex(indexCrystalWing);
    public static final Item crystalWingBurning = (new ItemCrystalWingBurning(idBurningWing - 256)).setItemName("burningWing").setIconIndex(indexBurningWing);
    public static final Item crystalWingBurned = (new ItemCrystalWingBurned(idBurnedWing - 256, teleDistance)).setItemName("burnedWing").setIconIndex(indexBurnedWing);
    public static final Achievement burnedWing = (new Achievement(1710, "burnedWing", 9, -5, crystalWingBurning, null)).registerAchievement();

    public mod_crystalWing()
    {
        ModLoader.addName(crystalWing, "Crystal Wing");
        ModLoader.addName(crystalWingBurning, "BURNING WING");
        ModLoader.addName(crystalWingBurned, "Burned Wing");
        ModLoader.addAchievementDesc(burnedWing, "To Hell And Back", "Get a Burned Wing by entering in water with a Burning Wing.");
        ModLoader.addRecipe(new ItemStack(crystalWing, 1), new Object[] {
            "GGG", "EFF", Character.valueOf('G'), Item.ingotGold, Character.valueOf('E'), Item.enderPearl, Character.valueOf('F'), Item.feather
        });
        if(uses > 0)
        {
            crystalWing.setMaxDamage(uses - 1);
        }
        ModLoader.setInGameHook(this, true, true);
    }

    public String getVersion()
    {
        return "1.3.2.d updated by bspkrs";
    }

    public void load()
    {
        if(!isCurrentVersion(ModLoader.getMinecraftInstance()))
            ModLoader.getLogger().log(Level.INFO, "Your version of CrystalWing is out of date! Visit http://www.minecraftforum.net/topic/1009577- for the latest version.");
    }
    
    public boolean onTickInGame(float f, Minecraft minecraft)
    {
        if(!isCurrentVersion(minecraft) && minecraft.inGameHasFocus)
        {
            minecraft.thePlayer.addChatMessage("&cYour version of CrystalWing is out of date!");
            minecraft.thePlayer.addChatMessage("&cVisit http://www.minecraftforum.net/topic/1009577- for the latest version.");
        }
        return !minecraft.inGameHasFocus;
    }
    
    private boolean isCurrentVersion(Minecraft minecraft)
    {
        try
        {
            String[] version = loadTextFromURL(new URL("https://dl.dropbox.com/u/20748481/Minecraft/1.3.1/crystalWing.version"));
            return version[0].equalsIgnoreCase(getVersion());
        }
        catch(Exception e)
        {
            ModLoader.getLogger().log(Level.WARNING, "Error getting current version info: " + e.getStackTrace());
            return true;
        }
    }

    private String[] loadTextFromURL(URL url)
    {
        ArrayList arraylist = new ArrayList();
        Scanner scanner = null;
        try
        {
            scanner = new Scanner(url.openStream(), "UTF-8");
        }
        catch(FileNotFoundException filenotfoundexception) { } catch (IOException e) {
            ModLoader.getLogger().log(Level.WARNING, "Error getting current version info: " + e.getStackTrace());
        }
        while(scanner.hasNextLine())
        {
            arraylist.add(scanner.nextLine());
        }
        scanner.close();
        return (String[])arraylist.toArray(new String[arraylist.size()]);
    }
}
