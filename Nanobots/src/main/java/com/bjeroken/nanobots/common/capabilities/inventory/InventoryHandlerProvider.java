package com.bjeroken.nanobots.common.capabilities.inventory;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class InventoryHandlerProvider implements ICapabilitySerializable<NBTBase> {
  public static IInventoryHandler inst = CapabilityInventoryHandler.CAP.getDefaultInstance();

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return capability == CapabilityInventoryHandler.CAP;
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    return capability == CapabilityInventoryHandler.CAP ? CapabilityInventoryHandler.CAP.<T> cast(inst) : null;
  }

  @Override
  public NBTBase serializeNBT() {
    return (NBTBase) CapabilityInventoryHandler.CAP.getStorage().writeNBT(CapabilityInventoryHandler.CAP, inst, null);
  }

  @Override
  public void deserializeNBT(NBTBase nbt) {
    CapabilityInventoryHandler.CAP.getStorage().readNBT(CapabilityInventoryHandler.CAP, inst, null, nbt);
  }

}