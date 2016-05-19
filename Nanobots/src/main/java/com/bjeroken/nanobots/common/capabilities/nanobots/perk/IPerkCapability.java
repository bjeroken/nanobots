package com.bjeroken.nanobots.common.capabilities.nanobots.perk;

import java.util.List;

import com.bjeroken.nanobots.common.stats.Perk;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public interface IPerkCapability {

  /**
   * Returns NBTBase for easy sync to client
   */
  NBTBase setPerkPoints(int points);
  int getPerkPoints();
  /**
   * Returns NBTBase for easy sync to client
   */
  NBTBase setUsedPoints(int usedPoints);
  int getUsedPoints();
  boolean hasPerk(Perk perk);
  boolean canAddPerk(EntityPlayer player, Perk perk);

  /**
   * Returns NBTBase for easy sync to client
   */
  NBTBase addPerk(Perk perk);
  List<Perk> getPerks();
  /**
   * Returns NBTBase for easy sync to client
   */
  NBTTagList setPerks(List<Perk> perks);
  NBTTagCompound writeNBT();
  void readNBT(NBTTagCompound nbt);
}
