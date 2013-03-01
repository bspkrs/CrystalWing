package bspkrs.crystalwing;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class ItemCrystalWing extends Item
{
    private int playNotes = 19;
    
    public ItemCrystalWing(int i)
    {
        super(i);
        maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        if (!world.isRemote)
        {
            if (entityPlayer.dimension == -1)
            {
                itemStack = null;
                world.playSoundAtEntity(entityPlayer, "fire.ignite", 1.0F, 1.0F);
                itemStack = new ItemStack(CrystalWing.instance.crystalWingBurning, 1);
                return itemStack;
            }
            else if (world.provider.dimensionId > 0)
                return itemStack;
            
            ChunkCoordinates chunkCoords = entityPlayer.getBedLocation();
            
            if (chunkCoords == null)
                chunkCoords = world.getSpawnPoint();
            
            // EntityPlayer.verifyRespawnCoordinates(ModLoader.getMinecraftServerInstance().worldServerForDimension(entityplayer.dimension),
            // chunkCoords, isSpawnForced);
            chunkCoords = EntityPlayer.verifyRespawnCoordinates(world, chunkCoords, true);
            
            if (chunkCoords == null)
                chunkCoords = world.getSpawnPoint();
            
            // int i;
            // for (i = 126; i > 16 && !world.isBlockNormalCube(chunkcoordinates.posX, i, chunkcoordinates.posZ) &&
            // !world.isBlockNormalCube(chunkcoordinates.posX, i + 1, chunkcoordinates.posZ); i--)
            // {}
            
            entityPlayer.addChatMessage("Magical winds brought you home");
            ModLoader.getMinecraftServerInstance().executeCommand("cw " + entityPlayer.username + " " +
                    (chunkCoords.posX + 0.5D) + " " + (chunkCoords.posY + 0.1D) + " " + (chunkCoords.posZ + 0.5D));
            // entityplayer.setLocationAndAngles(chunkCoords.posX + 0.5D, chunkCoords.posY + 0.1D, chunkCoords.posZ + 0.5D, 0.0F, 0.0F);
            playEffects(entityPlayer, world);
            
            if (CrystalWing.instance.uses > 0)
                itemStack.damageItem(1, entityPlayer);
        }
        return itemStack;
    }
    
    public void playEffects(EntityPlayer entityPlayer, World world)
    {
        entityPlayer.spawnExplosionParticle();
        playNotes = 0;
    }
    
    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int i, boolean flag)
    {
        if (playNotes >= 19)
            return;
        
        EntityClientPlayerMP player = ModLoader.getMinecraftInstance().thePlayer;
        switch (playNotes)
        {
            case 1: // '\001'
                playAtPitch(8, world, player);
                break;
            
            case 10: // '\005'
                playAtPitch(15, world, player);
                break;
            
            case 18: // '\007'
                playAtPitch(19, world, player);
                break;
        }
        playNotes++;
    }
    
    public void playAtPitch(int i, World world, EntityPlayer entityplayer)
    {
        float f = (float) Math.pow(2D, (i - 12) / 12D);
        world.playSoundAtEntity(entityplayer, "note.pling", 0.5F, f);
    }
}
