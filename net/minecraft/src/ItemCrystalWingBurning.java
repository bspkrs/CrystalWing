package net.minecraft.src;

public class ItemCrystalWingBurning extends Item
{

    public ItemCrystalWingBurning(int i)
    {
        super(i);
        maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTransport);
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
