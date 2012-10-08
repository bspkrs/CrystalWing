// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            Item, EntityPlayer, World, ItemStack, 
//            mod_crystalWing, ChunkCoordinates, ModLoader, EntityPlayerSP, 
//            Entity

public class ItemCrystalWing extends Item
{

    private boolean playNotes;
    private int start;

    protected ItemCrystalWing(int i)
    {
        super(i);
        maxStackSize = 1;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if(entityplayer.dimension == -1)
        {
            itemstack = null;
            world.playSoundAtEntity(entityplayer, "fire.ignite", 1.0F, 1.0F);
            itemstack = new ItemStack(mod_crystalWing.crystalWingBurning, 1);
            return itemstack;
        }
        ChunkCoordinates chunkcoordinates = entityplayer.getSpawnChunk();
        if(chunkcoordinates == null)
        {
            chunkcoordinates = world.getSpawnPoint();
        }
        int i;
        for(i = 126; i > 16 && !world.isBlockNormalCube(chunkcoordinates.posX, i, chunkcoordinates.posZ) && !world.isBlockNormalCube(chunkcoordinates.posX, i + 1, chunkcoordinates.posZ); i--) { }
        entityplayer.addChatMessage("Magical winds brought you home");
        boolean flag = true;
        entityplayer.setPosition((double)chunkcoordinates.posX + 0.5D, i + 3, (double)chunkcoordinates.posZ + 0.5D);
        if(flag)
        {
            playEffects(entityplayer, world);
        }
        if(mod_crystalWing.uses > 0)
        {
            itemstack.damageItem(1, entityplayer);
        }
        return itemstack;
    }

    public void playEffects(EntityPlayer entityplayer, World world)
    {
        entityplayer.spawnExplosionParticle();
        playNotes = true;
        start = entityplayer.ticksExisted;
    }

    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        if(!playNotes)
        {
            return;
        }
        EntityPlayerSP entityplayersp = ModLoader.getMinecraftInstance().thePlayer;
        int j = entityplayersp.ticksExisted - start;
        switch(j)
        {
        case 1: // '\001'
            playAtPitch(8, world, entityplayersp);
            break;

        case 5: // '\005'
            playAtPitch(15, world, entityplayersp);
            break;

        case 7: // '\007'
            playAtPitch(19, world, entityplayersp);
            break;
        }
    }

    public void playAtPitch(int i, World world, EntityPlayer entityplayer)
    {
        float f = (float)Math.pow(2D, (double)(i - 12) / 12D);
        world.playSoundAtEntity(entityplayer, "note.pling", 0.5F, f);
    }
}
