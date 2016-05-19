package com.bjeroken.nanobots.common.util;

import com.bjeroken.nanobots.common.items.ItemNanobotArmor;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FuelHelper {
  private ItemStack itemStack;
  private ItemArmor.ArmorMaterial material;

  public FuelHelper() {
  }

  public static int getMaxFuel(Item repairItem) {
    if (repairItem == Items.leather)
      return 300;
    if (repairItem == Items.iron_ingot)
      return 500;
    if (repairItem == Items.gold_ingot)
      return 800;
    if (repairItem == Items.diamond)
      return 1000;
    return 400;
  }

}
