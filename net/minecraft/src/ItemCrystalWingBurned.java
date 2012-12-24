package net.minecraft.src;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCrystalWingBurned extends Item
{

    private Random rand;
    private int teleDistance;

    protected ItemCrystalWingBurned(int i, int j)
    {
        super(i);
        teleDistance = 1;
        teleDistance = j;
        rand = new Random();
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if(entityplayer.dimension == -1)
        {
            return mod_crystalWing.crystalWing.onItemRightClick(itemstack, world, entityplayer);
        }
        world.playSoundAtEntity(entityplayer, "fire.ignite", 1.0F, 0.5F);
        double d = (entityplayer.posX + (double)rand.nextInt(teleDistance * 2)) - (double)teleDistance;
        double d1 = (entityplayer.posZ + (double)rand.nextInt(teleDistance * 2)) - (double)teleDistance;
        int i;
        for(i = 127; i > 16 && !world.isBlockNormalCube((int)d, i, (int)d1); i--) { }
        int j = entityplayer.chunkCoordX;
        int k = entityplayer.chunkCoordZ;
        world.getChunkProvider().provideChunk(j, k);
        entityplayer.setPosition(d, i + 3, d1);
        itemstack.stackSize--;
        return itemstack;
    }
}
