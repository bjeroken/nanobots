package com.bjeroken.nanobots.common.capabilities.nanobots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public interface INanobotCapability {

  /**
   * Returns NBTBase for easy sync to client
   */
  NBTBase setNanobotLevel(int level);

  int getNanobotLevel();

  NBTBase setNanobotXP(int amount);

  int getNanobotXP();
  
  int getNanobotXPTotal();
  NBTBase setNanobotXPTotal(int nanobotXPTotal);
  /**
   * Returns NBTBase for easy sync to client
   */
  NBTBase setNanobotStatus(NanobotStatus status);

  NanobotStatus getNanobotStatus();

  /**
   * Returns NBTBase for easy sync to client
   */
  NBTBase setXPDrain(boolean drain);

  boolean getXPDrain();

  void read(EntityPlayer player);

  void update();

  float getStepHeight();

  /**
   * Returns NBTBase for easy sync to client
   */
  NBTBase setStepHeight(EntityPlayer player, float height);
  
  int getFireResist();
  NBTBase setFireResist(int fireResist);
  
  int getHealResist();
  NBTBase setHealResist(int healResist);
  
  /**
   * Returns NBTBase for easy sync to client
   */
  NBTBase setStepHeight(float height);

  NBTTagCompound writeNBT();

  void readNBT(NBTTagCompound nbt);
}
