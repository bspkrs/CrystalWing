package bspkrs.crystalwing;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import bspkrs.util.CommonUtils;

public class ItemEnderScepter extends Item
{
    private int coolDown = 0;

    public ItemEnderScepter()
    {
        maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
        this.setMaxDamage(-1);
    }

    @Override
    public Item setUnlocalizedName(String par1Str)
    {
        super.setUnlocalizedName(par1Str);
        return this;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        if (!world.isRemote && (coolDown == 0))
        {
            MovingObjectPosition lookingSpot = CommonUtils.getPlayerLookingSpot(entityPlayer, false);
            if ((lookingSpot != null) && lookingSpot.typeOfHit.equals(MovingObjectType.BLOCK))
            {
                BlockPos chunkCoords;
                chunkCoords = lookingSpot.getBlockPos();

                if (entityPlayer.capabilities.isFlying)
                {

                    chunkCoords = new BlockPos(chunkCoords.getX(),
                            Math.max((int) entityPlayer.posY, CommonUtils.getHighestGroundBlock(world, chunkCoords)),
                            chunkCoords.getZ());
                }

                entityPlayer.setPositionAndUpdate(chunkCoords.getX() + 0.5D, chunkCoords.getY() + 0.1D, chunkCoords.getZ());

                while (!world.getCollidingBoundingBoxes(entityPlayer, entityPlayer.getEntityBoundingBox()).isEmpty())
                {
                    entityPlayer.setPositionAndUpdate(entityPlayer.posX, entityPlayer.posY + 1.0D, entityPlayer.posZ);
                }

                world.playSoundAtEntity(entityPlayer, "mob.endermen.portal", 1.0F, 1.0F);
                CommonUtils.spawnExplosionParticleAtEntity(entityPlayer);

                coolDown = 40;
            }
        }

        return itemStack;
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (coolDown > 0)
            coolDown--;
    }

    public static MovingObjectPosition getPlayerLookingSpot(EntityPlayer player, boolean restrict)
    {
        float scale = 1.0F;
        float pitch = player.prevRotationPitch + ((player.rotationPitch - player.prevRotationPitch) * scale);
        float yaw = player.prevRotationYaw + ((player.rotationYaw - player.prevRotationYaw) * scale);
        double x = player.prevPosX + ((player.posX - player.prevPosX) * scale);
        double y = (player.prevPosY + ((player.posY - player.prevPosY) * scale) + 1.62D);
        double z = player.prevPosZ + ((player.posZ - player.prevPosZ) * scale);
        Vec3 vector1 = new Vec3(x, y, z);
        float cosYaw = MathHelper.cos((-yaw * 0.017453292F) - (float) Math.PI);
        float sinYaw = MathHelper.sin((-yaw * 0.017453292F) - (float) Math.PI);
        float cosPitch = -MathHelper.cos(-pitch * 0.017453292F);
        float sinPitch = MathHelper.sin(-pitch * 0.017453292F);
        float pitchAdjustedSinYaw = sinYaw * cosPitch;
        float pitchAdjustedCosYaw = cosYaw * cosPitch;
        double distance = 1000D;
        if ((player instanceof EntityPlayerMP) && restrict)
        {
            distance = ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vector2 = vector1.addVector(pitchAdjustedSinYaw * distance, sinPitch * distance, pitchAdjustedCosYaw * distance);
        return player.worldObj.rayTraceBlocks(vector1, vector2);
    }
}
