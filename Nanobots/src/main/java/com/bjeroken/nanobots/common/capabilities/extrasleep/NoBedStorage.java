package com.bjeroken.nanobots.common.capabilities.extrasleep;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTBase.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

/*
 * Adapted from https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
 */
public class NoBedStorage implements IStorage<IExtraSleeping> {
  @Override
  public NBTBase writeNBT(Capability<IExtraSleeping> capability, IExtraSleeping instance, EnumFacing side) {
    return new NBTTagByte((byte) (instance.isSleeping() ? 1 : 0));
  }

  @Override
  public void readNBT(Capability<IExtraSleeping> capability, IExtraSleeping instance, EnumFacing side, NBTBase nbt) {
    instance.setSleeping(((NBTPrimitive) nbt).getByte() == 1);
  }
}