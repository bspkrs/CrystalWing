package bspkrs.crystalwing;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import bspkrs.crystalwing.localization.LocalizationHandler;
import bspkrs.util.CommonUtils;
import bspkrs.util.Configuration;
import bspkrs.util.Const;
import bspkrs.util.ForgeUtils;
import cpw.mods.fml.common.registry.GameRegistry;

public final class CWSettings
{
    public final static String  VERSION_NUMBER    = Const.MCVERSION + ".r02";
    
    public static int           idCrystalWing     = 23100;
    public static int           idBurningWing     = 23101;
    public static int           idBurnedWing      = 23102;
    public static int           idAchievement     = 1710;
    public static int           uses              = 8;
    public static int           teleDistance      = 500;
    
    public static Item          crystalWing;
    public static Item          crystalWingBurning;
    public static Item          crystalWingBurned;
    public static Achievement   burnedWing;
    
    public static Configuration config;
    public static boolean       allowDebugLogging = true;
    public static boolean       isForgeVersion    = ForgeUtils.isForgeEnv();
    
    public static void loadConfig(File file)
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;
        
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            if (file.exists())
          //                file.delete();
        }
        
        config = new Configuration(file);
        
        config.load();
        
        idCrystalWing = config.getItem("idCrystalWing", idCrystalWing).getInt(idCrystalWing);
        idBurningWing = config.getItem("idBurningWing", idBurningWing).getInt(idBurningWing);
        idBurnedWing = config.getItem("idBurnedWing", idBurnedWing).getInt(idBurnedWing);
        idAchievement = config.getInt("idAchievement", ctgyGen, idAchievement, 1, 2000, "");
        allowDebugLogging = config.getBoolean("allowDebugLogging", ctgyGen, allowDebugLogging, "");
        uses = config.getInt("uses", ctgyGen, uses, 0, 5280, "Number of Crystal Wing uses. Set to 0 for infinite.");
        teleDistance = config.getInt("teleDistance", ctgyGen, teleDistance, 50, 50000, "Maximum distance for the Burned Wing random teleportation.");
        
        config.save();
    }
    
    public static void registerStuff()
    {
        String prefix = "minecraft:";
        if (isForgeVersion)
            prefix = "crystalwing:";
        
        crystalWing = (new ItemCrystalWing(idCrystalWing - 256)).setUnlocalizedName(prefix + "crystalWing");
        crystalWingBurning = (new ItemCrystalWingBurning(idBurningWing - 256)).setUnlocalizedName(prefix + "crystalWingBurning");
        crystalWingBurned = (new ItemCrystalWingBurned(idBurnedWing - 256, teleDistance)).setUnlocalizedName(prefix + "crystalWingBurned");
        
        if (isForgeVersion)
        {
            //            LanguageRegistry.addName(crystalWing, "Crystal Wing");
            //            LanguageRegistry.addName(crystalWingBurning, "Burning Wing");
            //            LanguageRegistry.addName(crystalWingBurned, "Burned Wing");
            //            LanguageRegistry.instance().addStringLocalization("achievement.burnedWing", "en_US", "To Hell And Back");
            //            LanguageRegistry.instance().addStringLocalization("achievement.burnedWing.desc", "en_US", "Get a Burned Wing by entering water with a Burning Wing.");
            
            GameRegistry.addRecipe(new ItemStack(crystalWing, 1), new Object[] {
                    "GGG", "EFF", Character.valueOf('G'), Item.ingotGold, Character.valueOf('E'), Item.enderPearl, Character.valueOf('F'), Item.feather
            });
            
            burnedWing = (new Achievement(idAchievement, "burnedWing", 9, -5, crystalWingBurning, null)).registerAchievement();
            
            LocalizationHandler.instance.loadLanguages();
        }
        else
        {
            burnedWing = (new Achievement(idAchievement, "burnedWing", 9, -5, crystalWingBurning, null)).registerAchievement();
            
            ModLoader.addName(crystalWing, "Crystal Wing");
            ModLoader.addName(crystalWingBurning, "Burning Wing");
            ModLoader.addName(crystalWingBurned, "Burned Wing");
            ModLoader.addAchievementDesc(burnedWing, "To Hell And Back", "Get a Burned Wing by entering water with a Burning Wing.");
            ModLoader.addRecipe(new ItemStack(crystalWing, 1), new Object[] {
                    "GGG", "EFF", Character.valueOf('G'), Item.ingotGold, Character.valueOf('E'), Item.enderPearl, Character.valueOf('F'), Item.feather
            });
        }
    }
    
    /**
     * Ensure that a block enabling respawning exists at the specified coordinates and find an empty space nearby to spawn.
     */
    public static ChunkCoordinates verifyRespawnCoordinates(World world, ChunkCoordinates chunkCoords, boolean par2)
    {
        if (!world.isRemote)
        {
            IChunkProvider ichunkprovider = world.getChunkProvider();
            ichunkprovider.loadChunk(chunkCoords.posX - 3 >> 4, chunkCoords.posZ - 3 >> 4);
            ichunkprovider.loadChunk(chunkCoords.posX + 3 >> 4, chunkCoords.posZ - 3 >> 4);
            ichunkprovider.loadChunk(chunkCoords.posX - 3 >> 4, chunkCoords.posZ + 3 >> 4);
            ichunkprovider.loadChunk(chunkCoords.posX + 3 >> 4, chunkCoords.posZ + 3 >> 4);
        }
        
        ChunkCoordinates c = chunkCoords;
        Block block = Block.blocksList[world.getBlockId(c.posX, c.posY, c.posZ)];
        
        if (block != null && block.blockID == Block.bed.blockID)
        {
            return block.getBedSpawnPosition(world, c.posX, c.posY, c.posZ, null);
        }
        else
        {
            Material material = world.getBlockMaterial(chunkCoords.posX, chunkCoords.posY, chunkCoords.posZ);
            Material material1 = world.getBlockMaterial(chunkCoords.posX, chunkCoords.posY + 1, chunkCoords.posZ);
            boolean flag1 = !material.isSolid() && !material.isLiquid();
            boolean flag2 = !material1.isSolid() && !material1.isLiquid();
            return par2 && flag1 && flag2 ? chunkCoords : null;
        }
    }
}
