package com.bjeroken.nanobots.common.capabilities.nanobots.fuel;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityFuelHandler {


  public static void register() {
    CapabilityManager.INSTANCE.register(IFuelCapability.class, new Capability.IStorage<IFuelCapability>() {

      @Override
      public NBTBase writeNBT(Capability<IFuelCapability> capability, IFuelCapability instance, EnumFacing side) {
        return FuelHandler.writeNBT(capability, instance);
      }

      @Override
      public void readNBT(Capability<IFuelCapability> capability, IFuelCapability instance, EnumFacing side, NBTBase nbt) {
       FuelHandler.readNBT(capability, instance, (NBTTagCompound) nbt);
      }
    }, new Callable<IFuelCapability>() {
      @Override
      public IFuelCapability call() throws Exception {
        return new FuelCapability();
      }
    });
  }
}
