package com.bjeroken.nanobots.common.capabilities.test;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerProvider implements ICapabilitySerializable<NBTBase> {
//  public static IItemHandler inst = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getDefaultInstance();
  public static IItemHandler inst = new ItemStackHandler(10);
  
  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.<T> cast(inst) : null;
  }

  @Override
  public NBTBase serializeNBT() {
    return (NBTBase) CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inst, null);
  }

  @Override
  public void deserializeNBT(NBTBase nbt) {
    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inst, null, nbt);
  }

}
