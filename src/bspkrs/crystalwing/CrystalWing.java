package bspkrs.crystalwing;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.stats.Achievement;

public final class CrystalWing
{
    public static int          idCrystalWing    = 3100;
    public static int          idBurningWing    = 3101;
    public static int          idBurnedWing     = 3102;
    public final static String usesDesc         = "Number of Crystal Wing uses. Set to 0 for infinite.";
    public static int          uses             = 8;
    public final static String teleDistanceDesc = "Maximum distance for the Burned Wing random teleportation.";
    public static int          teleDistance     = 500;
    
    public final int           indexCrystalWing;
    public final int           indexBurningWing;
    public final int           indexBurnedWing;
    public final Item          crystalWing;
    public final Item          crystalWingBurning;
    public final Item          crystalWingBurned;
    public final Achievement   burnedWing;
    
    public static CrystalWing  instance;
    public final boolean       isForgeVersion;
    
    public CrystalWing(boolean isForgeVersion, int idCrystalWing, int idBurningWing, int idBurnedWing, int uses, int teleDistance)
    {
        instance = this;
        this.isForgeVersion = isForgeVersion;
        this.idCrystalWing = idCrystalWing;
        this.idBurningWing = idBurningWing;
        this.idBurnedWing = idBurnedWing;
        this.uses = uses;
        this.teleDistance = teleDistance;
        indexCrystalWing = ModLoader.addOverride("/gui/items.png", "/daftpvf/crystalWing.png");
        indexBurningWing = ModLoader.addOverride("/gui/items.png", "/daftpvf/crystalWingBurning.png");
        indexBurnedWing = ModLoader.addOverride("/gui/items.png", "/daftpvf/crystalWingBurned.png");
        crystalWing = (new ItemCrystalWing(idCrystalWing - 256)).setItemName("crystalWing").setIconIndex(indexCrystalWing);
        crystalWingBurning = (new ItemCrystalWingBurning(idBurningWing - 256)).setItemName("burningWing").setIconIndex(indexBurningWing);
        crystalWingBurned = (new ItemCrystalWingBurned(idBurnedWing - 256, teleDistance)).setItemName("burnedWing").setIconIndex(indexBurnedWing);
        burnedWing = (new Achievement(1710, "burnedWing", 9, -5, crystalWingBurning, null)).registerAchievement();
        
        ModLoader.addCommand(new CommandServerCw());
        
        if (uses > 0)
        {
            crystalWing.setMaxDamage(uses - 1);
        }
        
        ModLoader.addName(crystalWing, "Crystal Wing");
        ModLoader.addName(crystalWingBurning, "BURNING WING");
        ModLoader.addName(crystalWingBurned, "Burned Wing");
        ModLoader.addAchievementDesc(burnedWing, "To Hell And Back", "Get a Burned Wing by entering in water with a Burning Wing.");
        ModLoader.addRecipe(new ItemStack(crystalWing, 1), new Object[] {
                "GGG", "EFF", Character.valueOf('G'), Item.ingotGold, Character.valueOf('E'), Item.enderPearl, Character.valueOf('F'), Item.feather
        });
    }
}
