package com.bjeroken.nanobots.common.capabilities.nanobots.perk;

import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PerkProvider implements ICapabilitySerializable<NBTTagCompound>  {
  
  public IPerkCapability cap = null;
  
  public PerkProvider() {
    cap = new PerkCapability();
  }
  
  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return capability == Refs.PERK_CAP;
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (Refs.PERK_CAP != null && Refs.PERK_CAP == capability)
      return (T) cap;
    return null;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    return (NBTTagCompound) PerkHandler.writeNBT(Refs.PERK_CAP, cap);
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    PerkHandler.readNBT(Refs.PERK_CAP, cap, nbt);
  }
}
