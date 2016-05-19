package com.bjeroken.nanobots.common.capabilities.nanobots;

import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class NanobotCapability implements INanobotCapability {
  private int nanobotLevel, nanobotXP, nanobotXPTotal, fireResist, healResist;
  private float stepHeight;
  private boolean xPDrain;
  private NanobotStatus nanobotStatus;
  public NanobotCapability() {
    nanobotLevel = nanobotXP = nanobotXPTotal = fireResist = healResist = 0;
    nanobotStatus = NanobotStatus.none;
    xPDrain = false;
  }
  
  @Override
  public void read(EntityPlayer player){
      INanobotCapability cap = Refs.getNanobotCap(player);
      setNanobotLevel(cap.getNanobotLevel());
      setNanobotXP(cap.getNanobotXP());
      setStepHeight(player, cap.getStepHeight());
  }
  
  @Override
  public void update() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int getNanobotLevel() {
    return nanobotLevel;
  }

  @Override
  public NBTBase setNanobotLevel(int nanobotLevel) {
    this.nanobotLevel = nanobotLevel;
    return Refs.toTagInt(this.nanobotLevel);
  }

  @Override
  public int getNanobotXP() {
    return nanobotXP;
  }

  @Override
  public NBTBase setNanobotXP(int nanobotXP) {
    this.nanobotXP = nanobotXP;
    return Refs.toTagInt(this.nanobotXP);
  }

  public int getNanobotXPTotal() {
    return nanobotXPTotal;
  }

  public NBTBase setNanobotXPTotal(int nanobotXPTotal) {
    this.nanobotXPTotal = nanobotXPTotal;
    return Refs.toTagInt(this.nanobotXPTotal);
  }

  @Override
  public boolean getXPDrain() {
    return xPDrain;
  }

  @Override
  public NBTBase setXPDrain(boolean xPDrain) {
    this.xPDrain = xPDrain;
    return Refs.toTagInt(this.xPDrain ? 1 : 0);
  }

  @Override
  public String toString() {
    return "NanobotCapability [nanobotLevel=" + nanobotLevel + ", nanobotXP=" + nanobotXP + ", stepHeight=" + stepHeight + ", xPDrain="
        + xPDrain + ", fireResist=" + fireResist + ", healResist=" + healResist + ", nanobotStatus=" + nanobotStatus + "]";
  }
  @Override
  public NanobotStatus getNanobotStatus() {
    return nanobotStatus;
  }

  @Override
  public NBTBase setNanobotStatus(NanobotStatus nanobotStatus) {
    this.nanobotStatus = nanobotStatus;
    return Refs.toTagInt(this.nanobotStatus.ordinal());
  }

  @Override
  public NBTBase setStepHeight(EntityPlayer player, float height) {
    player.stepHeight = height;
    return setStepHeight(height);
  }
  @Override
  public NBTBase setStepHeight(float height) {
    this.stepHeight = height;
    return Refs.toTagFloat(this.stepHeight);
  }
  @Override
  public float getStepHeight() {
    return this.stepHeight;
  }
  @Override
  public NBTTagCompound writeNBT() {
    return (NBTTagCompound) PlayerNanobotHandler.writeNBT(Refs.NANOBOT_CAP, this);
  }
  @Override
  public void readNBT(NBTTagCompound nbt) {
    PlayerNanobotHandler.readNBT(Refs.NANOBOT_CAP, this, nbt);
  }

  @Override
  public int getFireResist() {
    return fireResist;
  }

  @Override
  public NBTBase setFireResist(int fireResist) {
    this.fireResist = fireResist;
    return Refs.toTagInt(this.fireResist);
  }

  @Override
  public int getHealResist() {
    return healResist;
  }

  @Override
  public NBTBase setHealResist(int healResist) {
    this.healResist = healResist;
    return Refs.toTagInt(this.healResist);
  }

}
