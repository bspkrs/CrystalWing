package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class ItemCrystalWing extends Item
{

    private boolean playNotes;
    private int start;

    protected ItemCrystalWing(int i)
    {
        super(i);
        maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    @Override
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
            chunkcoordinates = world.getSpawnPoint();

        int i;
        for(i = 126; i > 16 && !world.isBlockNormalCube(chunkcoordinates.posX, i, chunkcoordinates.posZ) 
        		&& !world.isBlockNormalCube(chunkcoordinates.posX, i + 1, chunkcoordinates.posZ); i--) { }

        entityplayer.addChatMessage("Magical winds brought you home");
        entityplayer.setPosition((double)chunkcoordinates.posX + 0.5D, i + 3, (double)chunkcoordinates.posZ + 0.5D);
        playEffects(entityplayer, world);
        
        if(mod_crystalWing.uses > 0)
            itemstack.damageItem(1, entityplayer);
        
        return itemstack;
    }

    public void playEffects(EntityPlayer entityplayer, World world)
    {
        entityplayer.spawnExplosionParticle();
        playNotes = true;
        start = (int) world.worldInfo.getWorldTime();
    }

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {    	
        if(!playNotes)
            return;
        
        EntityClientPlayerMP player = ModLoader.getMinecraftInstance().thePlayer;
        int j = (int) world.worldInfo.getWorldTime() - start;
        switch(j)
        {
        case 1: // '\001'
            playAtPitch(8, world, player);
            break;

        case 5: // '\005'
            playAtPitch(15, world, player);
            break;

        case 7: // '\007'
            playAtPitch(19, world, player);
            break;
        }
    }

    public void playAtPitch(int i, World world, EntityPlayer entityplayer)
    {
        float f = (float)Math.pow(2D, (double)(i - 12) / 12D);
        world.playSoundAtEntity(entityplayer, "note.pling", 0.5F, f);
    }
}
