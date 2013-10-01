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
import bspkrs.util.CommonUtils;
import bspkrs.util.Configuration;
import bspkrs.util.Const;

public final class CWSettings
{
    public final static String  VERSION_NUMBER    = Const.MCVERSION + ".r02";
    
    public static int           idCrystalWing     = 3100;
    public static int           idBurningWing     = 3101;
    public static int           idBurnedWing      = 3102;
    public static int           idAchievement     = 1710;
    public static int           uses              = 8;
    public static int           teleDistance      = 500;
    
    public final Item           crystalWing;
    public final Item           crystalWingBurning;
    public final Item           crystalWingBurned;
    public final Achievement    burnedWing;
    
    public static CWSettings    instance;
    
    public static Configuration config;
    public static boolean       allowDebugLogging = true;
    public final boolean        isForgeVersion;
    
    public CWSettings(boolean isForgeVersion)
    {
        instance = this;
        this.isForgeVersion = isForgeVersion;
        crystalWing = (new ItemCrystalWing(idCrystalWing - 256)).setUnlocalizedName("crystalWing");
        crystalWingBurning = (new ItemCrystalWingBurning(idBurningWing - 256)).setUnlocalizedName("crystalWingBurning");
        crystalWingBurned = (new ItemCrystalWingBurned(idBurnedWing - 256, teleDistance)).setUnlocalizedName("crystalWingBurned");
        burnedWing = (new Achievement(idAchievement, "burnedWing", 9, -5, crystalWingBurning, null)).registerAchievement();
        
        ModLoader.addName(crystalWing, "Crystal Wing");
        ModLoader.addName(crystalWingBurning, "Burning Wing");
        ModLoader.addName(crystalWingBurned, "Burned Wing");
        ModLoader.addAchievementDesc(burnedWing, "To Hell And Back", "Get a Burned Wing by entering water with a Burning Wing.");
        ModLoader.addRecipe(new ItemStack(crystalWing, 1), new Object[] {
                "GGG", "EFF", Character.valueOf('G'), Item.ingotGold, Character.valueOf('E'), Item.enderPearl, Character.valueOf('F'), Item.feather
        });
    }
    
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
        uses = config.getInt("uses", ctgyGen, uses, 0, 5280, "Number of Crystal Wing uses. Set to 0 for infinite.");
        teleDistance = config.getInt("teleDistance", ctgyGen, teleDistance, 50, 50000, "Maximum distance for the Burned Wing random teleportation.");
        
        config.save();
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
