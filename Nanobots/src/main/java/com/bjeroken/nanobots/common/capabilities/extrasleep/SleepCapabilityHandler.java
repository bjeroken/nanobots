package com.bjeroken.nanobots.common.capabilities.extrasleep;

import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.nbt.NBTBase.NBTPrimitive;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/*
 * Adapted from https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
 */
public class SleepCapabilityHandler implements ICapabilitySerializable<NBTPrimitive> {

  IExtraSleeping inst = Refs.SLEEP_CAP.getDefaultInstance();

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return capability == Refs.SLEEP_CAP;
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    return capability == Refs.SLEEP_CAP ? Refs.SLEEP_CAP.<T> cast(inst) : null;
  }

  @Override
  public NBTPrimitive serializeNBT() {
    return (NBTPrimitive) Refs.SLEEP_CAP.getStorage().writeNBT(Refs.SLEEP_CAP, inst, null);
  }

  @Override
  public void deserializeNBT(NBTPrimitive nbt) {
    Refs.SLEEP_CAP.getStorage().readNBT(Refs.SLEEP_CAP, inst, null, nbt);
  }

}
