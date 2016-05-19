package com.bjeroken.nanobots.common.capabilities.nanobots.perk;

import java.util.ArrayList;
import java.util.List;

import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.NanobotStatus;
import com.bjeroken.nanobots.common.stats.Perk;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PerkCapability implements IPerkCapability {

  private List<Perk> perks;
  private int perkPoints, usedPoints;

  public PerkCapability() {
    ModLogger.log("initiated perk capability");
    usedPoints = perkPoints = 0;
    perks = new ArrayList<Perk>();
  }

  @Override
  public boolean hasPerk(Perk perk) {
    return perks.contains(perk);
  }

  @Override
  public NBTBase addPerk(Perk perk) {
    if (!hasPerk(perk)){
      perks.add(perk);
    }
    return Refs.toTagString(perk.getPerkName());
  }

  @Override
  public List<Perk> getPerks() {
    return perks;
  }

  @Override
  public NBTTagList setPerks(List<Perk> perks) {
    this.perks = perks;
    NBTTagList list = new NBTTagList();
    for(Perk p : perks)
      list.appendTag(Refs.toTagString(p.getPerkName()));
    return list;
  }

  @Override
  public String toString() {
    return "PerkCapability [perkPoints=" + perkPoints + ", usedPoints=" + usedPoints + ", perks=" + perks + "]";
  }

  @Override
  public NBTTagCompound writeNBT() {
    return (NBTTagCompound) PerkHandler.writeNBT(Refs.PERK_CAP, this);
  }

  @Override
  public void readNBT(NBTTagCompound nbt) {
    PerkHandler.readNBT(Refs.PERK_CAP, this, nbt);
  }

  @Override
  public NBTBase setPerkPoints(int points) {
    this.perkPoints = points;
    return Refs.toTagInt(this.perkPoints);
  }

  @Override
  public int getPerkPoints() {
    return this.perkPoints;
  }

  @Override
  public int getUsedPoints() {
    return this.usedPoints;
  }

  @Override
  public NBTBase setUsedPoints(int usedPoints) {
    this.usedPoints = usedPoints;
    return Refs.toTagInt(this.usedPoints);
  }

  @Override
  public boolean canAddPerk(EntityPlayer player, Perk perk) {
    INanobotCapability nanoc = Refs.getNanobotCap(player);
    if(nanoc.getNanobotStatus() != NanobotStatus.active)
      return false;
    if (perk.parentPerk == null)
      return !hasPerk(perk) && this.perkPoints >= perk.getPerkCost();
    return !hasPerk(perk) && this.perkPoints >= perk.getPerkCost() && hasPerk(perk.parentPerk);
  }
  
}
