package com.bjeroken.nanobots.common.capabilities.nanobots;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class NanobotStorage implements IStorage<INanobotCapability> {

  @Override
  public NBTBase writeNBT(Capability<INanobotCapability> capability, INanobotCapability instance, EnumFacing side) {
    return PlayerNanobotHandler.writeNBT(capability, instance);
  }

  @Override
  public void readNBT(Capability<INanobotCapability> capability, INanobotCapability instance, EnumFacing side, NBTBase nbt) {
    PlayerNanobotHandler.readNBT(capability, instance, (NBTTagCompound) nbt);
  }
  
  /*
   * Is this necessary? or in the right place? 
   */
  public class Factory implements Callable<INanobotCapability> {
    @Override
    public INanobotCapability call() throws Exception {
      return new NanobotCapability();
    }
    
  }
}
