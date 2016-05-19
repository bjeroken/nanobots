package com.bjeroken.nanobots.common.capabilities.nanobots.fuel;

import com.bjeroken.nanobots.common.util.FuelHelper;
import com.bjeroken.nanobots.common.util.ModUtils;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class FuelCapability implements IFuelCapability {
  private int currentFuel, maxFuel;

  private ItemStack stack;
  private NBTTagCompound tags;

  public FuelCapability() {
    currentFuel = 0;
    maxFuel = 100;
  }

  public FuelCapability(ItemStack stack) {
    this();
    this.stack = stack;
  }

  public FuelCapability(ItemStack stack, Item repairItem) {
    this(stack);
    maxFuel = FuelHelper.getMaxFuel(repairItem);
    this.tags = ModUtils.getTags(stack);
    readNBT(this.tags);
  }

  public FuelCapability(ItemStack stack, Item repairItem, NBTTagCompound tags) {
    this(stack, repairItem);
    if (tags != null)
      this.tags = tags;
    else
      this.tags = new NBTTagCompound();
    readNBT(this.tags);
  }
  public void setStack(ItemStack stack){
    this.stack = stack;
    this.tags = ModUtils.getTags(stack);
    readNBT(this.tags);
  }
  @Override
  public int getCurrentFuel() {
    return currentFuel;
  }

  @Override
  public NBTBase setCurrentFuel(int currentFuel) {
    this.currentFuel = currentFuel;
    return Refs.toTagInt(this.currentFuel);
  }

  @Override
  public int getMaxFuel() {
    return maxFuel;
  }

  @Override
  public NBTBase setMaxFuel(int maxFuel) {
    this.maxFuel = maxFuel;
    return Refs.toTagInt(this.maxFuel);
  }

  @Override
  public String toString() {
    return "FuelCapability [currentFuel=" + currentFuel + ", maxFuel=" + maxFuel + "]";
  }

  @Override
  public NBTTagCompound writeNBT() {
//     return (NBTTagCompound) FuelHandler.writeNBT(Refs.FUEL_CAP, this);
    if (stack != null) {
      stack.setTagCompound((NBTTagCompound) FuelHandler.writeNBT(Refs.FUEL_CAP, this));
      return stack.getTagCompound();
    } else {
      return (NBTTagCompound) FuelHandler.writeNBT(Refs.FUEL_CAP, this);
    }

  }

  @Override
  public void readNBT(NBTTagCompound nbt) {
//     FuelHandler.readNBT(Refs.FUEL_CAP, this, nbt);
    if (stack != null) {
      FuelHandler.readNBT(Refs.FUEL_CAP, this, ModUtils.getTags(stack));
    } else {
      FuelHandler.readNBT(Refs.FUEL_CAP, this, nbt);
    }
  }

}
