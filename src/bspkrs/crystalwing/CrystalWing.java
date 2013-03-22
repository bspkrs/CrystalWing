package bspkrs.crystalwing;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public final class CrystalWing
{
    public final static String VERSION_NUMBER   = "1.5.1.r01";
    public static int          idCrystalWing    = 3100;
    public static int          idBurningWing    = 3101;
    public static int          idBurnedWing     = 3102;
    public final static String usesDesc         = "Number of Crystal Wing uses. Set to 0 for infinite.";
    public static int          uses             = 8;
    public final static String teleDistanceDesc = "Maximum distance for the Burned Wing random teleportation.";
    public static int          teleDistance     = 500;
    
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
        crystalWing = (new ItemCrystalWing(idCrystalWing - 256)).setUnlocalizedName("crystalWing");
        crystalWingBurning = (new ItemCrystalWingBurning(idBurningWing - 256)).setUnlocalizedName("crystalWingBurning");
        crystalWingBurned = (new ItemCrystalWingBurned(idBurnedWing - 256, teleDistance)).setUnlocalizedName("crystalWingBurned");
        burnedWing = (new Achievement(1710, "burnedWing", 9, -5, crystalWingBurning, null)).registerAchievement();
        
        if (uses > 0)
        {
            crystalWing.setMaxDamage(uses - 1);
        }
        
        ModLoader.addName(crystalWing, "Crystal Wing");
        ModLoader.addName(crystalWingBurning, "Burning Wing");
        ModLoader.addName(crystalWingBurned, "Burned Wing");
        ModLoader.addAchievementDesc(burnedWing, "To Hell And Back", "Get a Burned Wing by entering in water with a Burning Wing.");
        ModLoader.addRecipe(new ItemStack(crystalWing, 1), new Object[] {
                "GGG", "EFF", Character.valueOf('G'), Item.ingotGold, Character.valueOf('E'), Item.enderPearl, Character.valueOf('F'), Item.feather
        });
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
        
        if (block != null && block.isBed(world, c.posX, c.posY, c.posZ, null))
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