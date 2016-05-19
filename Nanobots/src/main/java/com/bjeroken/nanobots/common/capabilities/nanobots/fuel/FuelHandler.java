package com.bjeroken.nanobots.common.capabilities.nanobots.fuel;

import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.util.EnumStats;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;

public class FuelHandler {

  public static NBTTagCompound writeNBT(Capability<IFuelCapability> capability, IFuelCapability instance) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger(EnumStats.maxfuel.name(), instance.getMaxFuel());
    tags.setInteger(EnumStats.currentfuel.name(), instance.getCurrentFuel());
    return tags;
  }

  public static void readNBT(Capability<IFuelCapability> capability, IFuelCapability instance, NBTTagCompound data) {
    if (data.hasKey(EnumStats.currentfuel.name())){
      instance.setCurrentFuel(data.getInteger(EnumStats.currentfuel.name()));
//      ModLogger.log("read currentFuel: " + instance.getCurrentFuel());
    }
    if (data.hasKey(EnumStats.maxfuel.name())){
      instance.setMaxFuel(data.getInteger(EnumStats.maxfuel.name()));
//      ModLogger.log("read maxFuel: " + instance.getMaxFuel());
    }
//    ModLogger.log(data.toString());
  }

  public static void processNBTData(IFuelCapability props, NBTTagCompound data) {
    if (props == null)
      return;
    if (data.hasKey(EnumStats.maxfuel.name())) {
      props.setMaxFuel(data.getInteger(EnumStats.maxfuel.name()));
      ModLogger.log("recieved maxFuel: " + props.getMaxFuel());
    }
    if (data.hasKey(EnumStats.currentfuel.name())) {
      props.setCurrentFuel(data.getInteger(EnumStats.currentfuel.name()));
      ModLogger.log("recieved currentFuel: " + props.getCurrentFuel());
    }
  }

}
