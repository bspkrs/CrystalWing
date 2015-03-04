package bspkrs.crystalwing;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import bspkrs.util.CommonUtils;

public class ItemCrystalWing extends Item
{
    public ItemCrystalWing()
    {
        maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);

        if (CWSettings.uses > 0)
        {
            this.setMaxDamage(CWSettings.uses - 1);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
        if (!world.isRemote && isCooledDown(itemStack))
        {
            if (entityPlayer.dimension == -1)
            {
                itemStack = null;
                world.playSoundAtEntity(entityPlayer, "fire.ignite", 1.0F, 1.0F);
                itemStack = new ItemStack(CWSettings.crystalWingBurning, 1);
                return itemStack;
            }

            BlockPos chunkCoords = entityPlayer.getBedLocation(world.provider.getDimensionId());

            if (chunkCoords == null)
                chunkCoords = world.getSpawnPoint();

            chunkCoords = CWSettings.verifyRespawnCoordinates(world, chunkCoords, false);

            if (chunkCoords == null)
                chunkCoords = world.getSpawnPoint();

            entityPlayer.addChatMessage(new ChatComponentTranslation("crystalwing.teleporthome.chatmessage"));

            entityPlayer.rotationPitch = 0.0F;
            entityPlayer.rotationYaw = 0.0F;
            entityPlayer.setPositionAndUpdate(chunkCoords.getX(), chunkCoords.getY() + 0.1D, chunkCoords.getZ());

            while (!world.getCollidingBoundingBoxes(entityPlayer, entityPlayer.getEntityBoundingBox()).isEmpty())
            {
                entityPlayer.setPositionAndUpdate(entityPlayer.posX, entityPlayer.posY + 1.0D, entityPlayer.posZ);
            }

            world.playSoundAtEntity(entityPlayer, "mob.endermen.portal", 1.0F, 1.0F);
            CommonUtils.spawnExplosionParticleAtEntity(entityPlayer);

            if (CWSettings.uses > 0)
                itemStack.damageItem(1, entityPlayer);

            setCoolDown(itemStack, (short) 40);
        }

        return itemStack;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int i, boolean flag)
    {
        if (itemStack.hasTagCompound())
        {
            NBTTagCompound tag = itemStack.getTagCompound();
            short cooldown = tag.getShort("cooldown");
            if (cooldown >= 0)
                tag.setShort("cooldown", (short) (cooldown - 1));
        }
        else
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setShort("cooldown", (short) 0);
            itemStack.setTagCompound(tag);
        }
    }

    private void setCoolDown(ItemStack itemStack, short cooldown)
    {
        if (itemStack.hasTagCompound())
        {
            NBTTagCompound tag = itemStack.getTagCompound();
            tag.setShort("cooldown", cooldown);
        }
        else
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setShort("cooldown", cooldown);
            itemStack.setTagCompound(tag);
        }
    }

    private boolean isCooledDown(ItemStack itemStack)
    {
        short cooldown = (short) 0;
        if (itemStack.hasTagCompound())
        {
            NBTTagCompound tag = itemStack.getTagCompound();
            cooldown = tag.getShort("cooldown");
        }
        else
        {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setShort("cooldown", (short) 0);
            itemStack.setTagCompound(tag);
        }
        return cooldown <= 0;
    }
}
