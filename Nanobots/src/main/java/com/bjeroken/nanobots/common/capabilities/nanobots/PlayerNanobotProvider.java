package com.bjeroken.nanobots.common.capabilities.nanobots;

import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerNanobotProvider implements ICapabilitySerializable<NBTTagCompound> {
  public INanobotCapability instance = null;

  public PlayerNanobotProvider() {
    instance = new NanobotCapability();
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return capability == Refs.NANOBOT_CAP;
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (Refs.NANOBOT_CAP != null && Refs.NANOBOT_CAP == capability)
      return (T) instance;
    return null;
  }
  
  @Override
  public NBTTagCompound serializeNBT() {
    return (NBTTagCompound) Refs.NANOBOT_CAP.getStorage().writeNBT(Refs.NANOBOT_CAP, instance, null);
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    Refs.NANOBOT_CAP.getStorage().readNBT(Refs.NANOBOT_CAP, instance, null, nbt);
  }

}
