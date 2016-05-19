package com.bjeroken.nanobots.common.capabilities.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IInventoryHandler extends IItemHandlerModifiable {

  void setInventoryStack(ItemStack stack);

  NBTTagCompound writeToNBT();

  void readFromNBT(NBTTagCompound tags);
}
