package bspkrs.crystalwing;

import static net.minecraftforge.common.ChestGenHooks.BONUS_CHEST;
import static net.minecraftforge.common.ChestGenHooks.DUNGEON_CHEST;
import static net.minecraftforge.common.ChestGenHooks.PYRAMID_DESERT_CHEST;
import static net.minecraftforge.common.ChestGenHooks.PYRAMID_JUNGLE_CHEST;
import static net.minecraftforge.common.ChestGenHooks.STRONGHOLD_LIBRARY;
import static net.minecraftforge.common.ChestGenHooks.VILLAGE_BLACKSMITH;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ChestGenHooks;
import bspkrs.util.CommonUtils;
import bspkrs.util.Configuration;
import bspkrs.util.Const;
import cpw.mods.fml.common.registry.GameRegistry;

public final class CWSettings
{
    public final static String  VERSION_NUMBER    = Const.MCVERSION + ".r04";
    
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
        teleDistance = config.getInt("teleDistance", ctgyGen, teleDistance, 100, 50000, "Maximum distance for the Burned Wing random teleportation.");
        
        config.save();
    }
    
    public static void registerStuff()
    {
        crystalWing = (new ItemCrystalWing(idCrystalWing - 256)).setUnlocalizedName("crystalwing.crystalWing");
        crystalWingBurning = (new ItemCrystalWingBurning(idBurningWing - 256)).setUnlocalizedName("crystalwing.crystalWingBurning");
        crystalWingBurned = (new ItemCrystalWingBurned(idBurnedWing - 256, teleDistance)).setUnlocalizedName("crystalwing.crystalWingBurned");
        
        GameRegistry.addRecipe(new ItemStack(crystalWing, 1), new Object[] {
                "GGG", "EFF", Character.valueOf('G'), Item.ingotGold, Character.valueOf('E'), Item.enderPearl, Character.valueOf('F'), Item.feather
        });
        
        burnedWing = (new Achievement(idAchievement, "burnedWing", 9, -5, crystalWingBurning, null)).registerAchievement();
        
        ChestGenHooks.addItem(PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 3));
        ChestGenHooks.addItem(PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWingBurned, 1), 1, 1, 2));
        ChestGenHooks.addItem(PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 2));
        ChestGenHooks.addItem(STRONGHOLD_LIBRARY, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 2));
        ChestGenHooks.addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 2));
        ChestGenHooks.addItem(BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 2));
        ChestGenHooks.addItem(DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 3));
        ChestGenHooks.addItem(DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWingBurned, 1), 1, 1, 2));
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
