package bspkrs.crystalwing;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import bspkrs.util.CommonUtils;

public class ItemCrystalWingBurnt extends Item
{
    private final Random rand;

    public ItemCrystalWingBurnt()
    {
        rand = new Random();

        if (!CommonUtils.isObfuscatedEnv())
            this.setCreativeTab(CreativeTabs.tabTransport);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityPlayer)
    {
        if (!world.isRemote)
        {
            if (entityPlayer.dimension == -1)
            {
                return CWSettings.crystalWing.onItemRightClick(itemstack, world, entityPlayer);
            }
            world.playSoundAtEntity(entityPlayer, "fire.ignite", 1.0F, 0.5F);
            int dX = (int) ((entityPlayer.posX + rand.nextInt(CWSettings.teleDistance * 2)) - CWSettings.teleDistance);
            int dZ = (int) ((entityPlayer.posZ + rand.nextInt(CWSettings.teleDistance * 2)) - CWSettings.teleDistance);
            BlockPos c = new BlockPos(dX, CommonUtils.getFirstNonAirBlockFromTop(world, new BlockPos(dX, 0, dZ)), dZ);

            IChunkProvider ichunkprovider = world.getChunkProvider();
            ichunkprovider.provideChunk(c.north(3).east(3));
            ichunkprovider.provideChunk(c.south(3).east(3));
            ichunkprovider.provideChunk(c.north(3).west(3));
            ichunkprovider.provideChunk(c.south(3).west(3));

            entityPlayer.rotationPitch = 0.0F;
            entityPlayer.rotationYaw = 0.0F;
            entityPlayer.setPositionAndUpdate(dX + 0.5D, c.getY() + 0.1D, dZ);

            while (!world.getCollidingBoundingBoxes(entityPlayer, entityPlayer.getEntityBoundingBox()).isEmpty())
            {
                entityPlayer.setPositionAndUpdate(entityPlayer.posX, entityPlayer.posY + 1.0D, entityPlayer.posZ);
            }

            world.playSoundAtEntity(entityPlayer, "mob.endermen.portal", 1.0F, 1.0F);
            CommonUtils.spawnExplosionParticleAtEntity(entityPlayer);

            itemstack.stackSize--;
        }
        return itemstack;
    }
}
