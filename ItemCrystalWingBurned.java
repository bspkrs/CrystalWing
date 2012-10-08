// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Item, EntityPlayer, mod_crystalWing, World, 
//            IChunkProvider, ItemStack

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
