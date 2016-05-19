package com.bjeroken.nanobots.common.capabilities.nanobots.fuel;

import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class FuelProvider implements ICapabilitySerializable<NBTTagCompound> {
  
  public IFuelCapability cap = null;
  
  public FuelProvider() {
    cap = new FuelCapability();
  }
  
  public FuelProvider(ItemStack stack){
    cap = new FuelCapability(stack);
  }
  
  public FuelProvider(ItemStack stack, Item repairItem){
    cap = new FuelCapability(stack, repairItem);
  }
  
  public FuelProvider(ItemStack stack, Item repairItem, NBTTagCompound tags){
    cap = new FuelCapability(stack, repairItem, tags);
  }
  
  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return capability == Refs.FUEL_CAP;
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (Refs.FUEL_CAP != null && Refs.FUEL_CAP == capability)
      return (T) cap;
    return null;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    return (NBTTagCompound) FuelHandler.writeNBT(Refs.FUEL_CAP, cap);
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    FuelHandler.readNBT(Refs.FUEL_CAP, cap, nbt);
  }

}
