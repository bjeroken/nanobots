package com.bjeroken.nanobots.common.capabilities.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class InventoryHandlerStorage implements Capability.IStorage<IInventoryHandler> {
  @Override
  public NBTTagCompound writeNBT(Capability<IInventoryHandler> capability, IInventoryHandler instance,
      EnumFacing side) {
    NBTTagList nbtTagList = new NBTTagList();
    NBTTagCompound tags = new NBTTagCompound();
    int size = instance.getSlots();
    for (int i = 0; i < size; i++) {
      ItemStack stack = instance.getStackInSlot(i);
      if (stack != null) {
        NBTTagCompound itemTag = new NBTTagCompound();
        itemTag.setInteger("Slot", i);
        stack.writeToNBT(itemTag);
        nbtTagList.appendTag(itemTag);
      }
    }
    tags.setTag("Inventory", nbtTagList);
    return tags;
  }

  @Override
  public void readNBT(Capability<IInventoryHandler> capability, IInventoryHandler instance, EnumFacing side,
      NBTBase base) {
    if (!(instance instanceof IInventoryHandler))
      throw new RuntimeException("IItemHandler instance does not implement IInventoryHandler");
    IInventoryHandler itemHandlerModifiable = (IInventoryHandler) instance;
    if (!(base instanceof NBTTagCompound))
      return;
    NBTTagCompound tags = (NBTTagCompound) base;
    NBTTagList tagList = (NBTTagList) tags.getTag("Inventory");
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
      int j = itemTags.getInteger("Slot");

      if (j >= 0 && j < instance.getSlots()) {
        itemHandlerModifiable.setStackInSlot(j, ItemStack.loadItemStackFromNBT(itemTags));
      }
    }
  }

}
