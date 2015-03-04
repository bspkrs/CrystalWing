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
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import bspkrs.crystalwing.fml.Reference;
import bspkrs.util.CommonUtils;

public final class CWSettings
{
    private final static boolean allowDebugLoggingDefault = false;
    public static boolean        allowDebugLogging        = allowDebugLoggingDefault;
    private final static int     usesDefault              = 8;
    public static int            uses                     = usesDefault;
    private final static int     teleDistanceDefault      = 500;
    public static int            teleDistance             = teleDistanceDefault;

    public static Item           crystalWing;
    public static Item           crystalWingBurning;
    public static Item           crystalWingBurnt;
    public static Item           enderScepter;
    public static Achievement    burntWing;

    public static void initConfig(File file)
    {
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            if (file.exists())
          //                file.delete();
        }

        Reference.config = new Configuration(file);

        syncConfig();
    }

    public static void syncConfig()
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;

        Reference.config.load();

        Reference.config.setCategoryComment(ctgyGen, "ATTENTION: Editing this file manually is no longer necessary. \n" +
                "On the Mods list screen select the entry for CrystalWing, then click the Config button to modify these settings.");

        allowDebugLogging = Reference.config.getBoolean(ConfigElement.ALLOW_DEBUG_LOGGING.key(), ctgyGen, allowDebugLoggingDefault, ConfigElement.ALLOW_DEBUG_LOGGING.desc(), ConfigElement.ALLOW_DEBUG_LOGGING.languageKey());
        uses = Reference.config.getInt(ConfigElement.USES.key(), ctgyGen, usesDefault, 0, 5280, ConfigElement.USES.desc(), ConfigElement.USES.languageKey());
        teleDistance = Reference.config.getInt(ConfigElement.TELE_DISTANCE.key(), ctgyGen, teleDistanceDefault, 100, 50000, ConfigElement.TELE_DISTANCE.desc(), ConfigElement.TELE_DISTANCE.languageKey());

        Reference.config.save();
    }

    public static void registerStuff()
    {
        crystalWing = (new ItemCrystalWing()).setUnlocalizedName("crystalWing");
        crystalWingBurning = (new ItemCrystalWingBurning()).setUnlocalizedName("crystalWingBurning");
        crystalWingBurnt = (new ItemCrystalWingBurnt()).setUnlocalizedName("crystalWingBurnt");
        enderScepter = (new ItemEnderScepter()).setUnlocalizedName("enderScepter");

        GameRegistry.registerItem(crystalWing, "crystalWing");
        GameRegistry.registerItem(crystalWingBurning, "crystalWingBurning");
        GameRegistry.registerItem(crystalWingBurnt, "crystalWingBurnt");
        GameRegistry.registerItem(enderScepter, "enderScepter");

        GameRegistry.addRecipe(new ItemStack(crystalWing, 1), new Object[] {
                "GGG", "EFF", Character.valueOf('G'), Items.gold_ingot, Character.valueOf('E'), Items.ender_pearl, Character.valueOf('F'), Items.feather
        });

        burntWing = (Achievement) (new Achievement("burntWing", "burntWing", 9, -5, crystalWingBurning, null)).initIndependentStat().registerStat();

        ChestGenHooks.addItem(PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 3));
        ChestGenHooks.addItem(PYRAMID_DESERT_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWingBurnt, 1), 1, 1, 2));
        ChestGenHooks.addItem(PYRAMID_JUNGLE_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 2));
        ChestGenHooks.addItem(STRONGHOLD_LIBRARY, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 2));
        ChestGenHooks.addItem(VILLAGE_BLACKSMITH, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 2));
        ChestGenHooks.addItem(BONUS_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 2));
        ChestGenHooks.addItem(DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWing, 1), 1, 1, 3));
        ChestGenHooks.addItem(DUNGEON_CHEST, new WeightedRandomChestContent(new ItemStack(crystalWingBurnt, 1), 1, 1, 2));
    }

    /**
     * Ensure that a block enabling respawning exists at the specified coordinates and find an empty space nearby to spawn.
     */
    public static BlockPos verifyRespawnCoordinates(World world, BlockPos c, boolean par2)
    {
        IChunkProvider ichunkprovider = world.getChunkProvider();
        ichunkprovider.provideChunk(c.north(3).east(3));
        ichunkprovider.provideChunk(c.south(3).east(3));
        ichunkprovider.provideChunk(c.north(3).west(3));
        ichunkprovider.provideChunk(c.south(3).west(3));

        IBlockState state = world.getBlockState(c);
        Block block = state.getBlock();

        if (block.equals(Blocks.bed) || block.isBed(world, c, null))
        {
            return block.getBedSpawnPosition(world, c, null);
        }
        else
        {
            Material material = block.getMaterial();
            Material material1 = world.getBlockState(c.up()).getBlock().getMaterial();
            boolean flag1 = !material.isSolid() && !material.isLiquid();
            boolean flag2 = !material1.isSolid() && !material1.isLiquid();
            return par2 && flag1 && flag2 ? c : null;
        }
    }
}
