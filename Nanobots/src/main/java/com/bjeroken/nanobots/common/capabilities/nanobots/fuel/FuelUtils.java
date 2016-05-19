package com.bjeroken.nanobots.common.capabilities.nanobots.fuel;

import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.IPerkCapability;
import com.bjeroken.nanobots.common.items.ItemNanobotArmor;
import com.bjeroken.nanobots.common.network.client.UpdateStatsMessage;
import com.bjeroken.nanobots.common.network.client.UpdateWornStatsMessage;
import com.bjeroken.nanobots.common.util.EnumStats;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.ModUtils;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;

public class FuelUtils {

  /**
   * Adds amount to current fuel, negative amount will subtract
   */
  public static NBTBase addFuel(IFuelCapability fuel, int amount) {
    if (amount < 0) {
      if (fuel.getCurrentFuel() + amount <= 0)
        return fuel.setCurrentFuel(0);
    }
    if (fuel.getCurrentFuel() + amount >= fuel.getMaxFuel())
      return fuel.setCurrentFuel(fuel.getMaxFuel());
    return fuel.setCurrentFuel(fuel.getCurrentFuel() + amount);
  }
  public static void transferToArmor(EntityPlayer player, int amount) {
    IItemHandler armor = new PlayerArmorInvWrapper(player.inventory);
    int fuelAdded = 0;
    for (int i = 0; i < armor.getSlots(); i++) {
      ItemStack stack = armor.getStackInSlot(i);
      if(stack != null && stack.getItem() instanceof ItemNanobotArmor){
        FuelCapability cap = new FuelCapability(stack, ((ItemNanobotArmor) stack.getItem()).getArmorMaterial().getRepairItem());
        if(amount > 0 && cap.getCurrentFuel() < cap.getMaxFuel()){
          if(cap.getCurrentFuel() + amount >= cap.getMaxFuel()){
            amount = cap.getMaxFuel() - cap.getCurrentFuel();
          }
          fuelAdded += amount;
          ModLogger.log("fuelAdded to armor: " + fuelAdded + ", i:" + i);
          FuelUtils.addFuel(cap, amount);
          amount = 0;
          stack.setTagCompound(cap.writeNBT());
        }
      }
//      if (stack != null && stack.hasCapability(Refs.FUEL_CAP, null)) {
//        IFuelCapability fuelA = stack.getCapability(Refs.FUEL_CAP, null);
//        if (amount > 0 && fuelA.getCurrentFuel() < fuelA.getMaxFuel()) {
//          if (fuelA.getCurrentFuel() + amount >= fuelA.getMaxFuel()) {
//            amount = fuelA.getMaxFuel() - fuelA.getCurrentFuel();
//          }
//            fuelAdded += amount;
//            ModLogger.log("fuelAdded to armor: " + fuelAdded + ", i:" + i);
//            FuelUtils.addFuel(fuelA, amount);
//            new UpdateWornStatsMessage(i, fuelA.getCurrentFuel()).sendTo(player);
//            amount = 0;
//        }
//      }
    }
  }
  
  public static void transferFromArmor(EntityPlayer player, int amount) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    final IPerkCapability perks = Refs.getPerkCap(player);
    final IFuelCapability fuel = Refs.getPlayerFuelCap(player);
    IItemHandler armor = new PlayerArmorInvWrapper(player.inventory);
    int fuelAdded = 0;
    if (fuel.getCurrentFuel() >= fuel.getMaxFuel())
      return;
    for (int i = 0; i < armor.getSlots(); i++) {
      ItemStack stack = armor.getStackInSlot(i);
      if (stack != null && stack.hasCapability(Refs.FUEL_CAP, null)) {
        IFuelCapability fuelA = stack.getCapability(Refs.FUEL_CAP, null);
        if (amount > 0 && fuelA.getCurrentFuel() > 0) {
          if (fuel.getCurrentFuel() + amount >= fuel.getMaxFuel()) {
            amount = fuel.getMaxFuel() - fuel.getCurrentFuel();
          }
          if (fuelA.getCurrentFuel() >= amount) {
            fuelAdded += amount;
            ModLogger.log("fuelAdded: " + fuelAdded + ", i:" + i);
            addFuel(fuelA, -amount);
            amount = 0;
            // Force stack to sync to player - too lazy to write my own packet for that
            new UpdateWornStatsMessage(i, fuelA.getCurrentFuel()).sendTo(player);
//            stack.setTagCompound(fuelA.writeNBT());
            break;
          } else {
            amount -= fuelA.getCurrentFuel();
            fuelAdded += amount;
            ModLogger.log("fuelAdded: " + fuelAdded + ", i:" + i);
            addFuel(fuelA, -amount);
            new UpdateWornStatsMessage(i, fuelA.getCurrentFuel()).sendTo(player);
            // Force stack to sync to player - too lazy to write my own packet for that
//            stack.setTagCompound(fuelA.writeNBT());
          }
        }
      }
    }

    if (fuelAdded > 0) {
      ModLogger.log("fuel added: " + fuelAdded);
      addFuelGetMessage(fuel, fuelAdded).sendTo(player);
    }
  }

  /**
   * Adds amount to current fuel, negative amount will subtract,
   * returns UpdateStatsMessage for syncing to client
   */
  public static UpdateStatsMessage addFuelGetMessage(IFuelCapability fuel, int amount) {
    return new UpdateStatsMessage(EnumStats.currentfuel, FuelUtils.addFuel(fuel, amount));
  }

  public static NBTBase setMaxFuel(IFuelCapability fuel, int amount) {
    return fuel.setMaxFuel(amount);
  }

  /**
   * Sets max fuel and returns UpdateStatsMessage for sync to client
   */
  public static UpdateStatsMessage setMaxFuelGetMessage(IFuelCapability fuel, int amount) {
    return new UpdateStatsMessage(EnumStats.maxfuel, FuelUtils.setMaxFuel(fuel, amount));
  }

  /**
   * Checks if amount is smaller or equal to current fuel amount
   * Negative amount is converted, for easier calling
   */
  public static boolean hasFuelForCost(IFuelCapability fuel, int amount) {
    return fuel.getCurrentFuel() >= Math.abs(amount);
  }
}
