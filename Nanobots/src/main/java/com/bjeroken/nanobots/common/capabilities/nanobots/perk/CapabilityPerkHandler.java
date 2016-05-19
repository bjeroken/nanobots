package com.bjeroken.nanobots.common.capabilities.nanobots.perk;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityPerkHandler {

  public static void register() {
    CapabilityManager.INSTANCE.register(IPerkCapability.class, new Capability.IStorage<IPerkCapability>() {

      @Override
      public NBTBase writeNBT(Capability<IPerkCapability> capability, IPerkCapability instance, EnumFacing side) {
        return PerkHandler.writeNBT(capability, instance);
      }

      @Override
      public void readNBT(Capability<IPerkCapability> capability, IPerkCapability instance, EnumFacing side, NBTBase nbt) {
       PerkHandler.readNBT(capability, instance, (NBTTagCompound) nbt);
      }
    }, new Callable<IPerkCapability>() {
      @Override
      public IPerkCapability call() throws Exception {
        return new PerkCapability();
      }
    });
  }
}
