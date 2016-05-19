package com.bjeroken.nanobots.common.capabilities.nanobots.fuel;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public interface IFuelCapability {


  /**
   * Returns NBTBase for easy sync to client
   */
  NBTBase setMaxFuel(int max);

  int getMaxFuel();

  /**
   * Returns NBTBase for easy sync to client
   */
  NBTBase setCurrentFuel(int current);

  int getCurrentFuel();

  NBTTagCompound writeNBT();
  void readNBT(NBTTagCompound nbt);
}
