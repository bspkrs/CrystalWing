// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            Item, Entity, EntityPlayer, World, 
//            mod_crystalWing, InventoryPlayer, ItemStack

public class ItemCrystalWingBurning extends Item
{

    public ItemCrystalWingBurning(int i)
    {
        super(i);
        maxStackSize = 1;
    }

    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        if(entity.inWater)
        {
            if(entity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                world.playSoundAtEntity(entity, "random.fizz", 1.0F, 1.0F);
                entityplayer.addStat(mod_crystalWing.burnedWing, 1);
                replaceWings(entityplayer.inventory);
            }
            return;
        }
        if(!entity.isBurning())
        {
            entity.setFire(2);
        }
    }

    private void replaceWings(InventoryPlayer inventoryplayer)
    {
        for(int i = 0; i < inventoryplayer.getSizeInventory(); i++)
        {
            if(inventoryplayer.getStackInSlot(i) == null)
            {
                continue;
            }
            ItemStack itemstack = inventoryplayer.getStackInSlot(i);
            if(itemstack.getItem() instanceof ItemCrystalWingBurning)
            {
                itemstack.itemID = mod_crystalWing.crystalWingBurned.shiftedIndex;
                inventoryplayer.setInventorySlotContents(i, itemstack);
            }
        }

    }
}
