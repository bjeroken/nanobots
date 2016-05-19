package com.bjeroken.nanobots.common.testing;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class DynBucket extends Item {
  public DynBucket() {
    setUnlocalizedName("dynbucket");
    setMaxStackSize(1);
    setHasSubtypes(true);
    setCreativeTab(CreativeTabs.tabMisc);
  }

  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    for (int i = 0; i < 4; i++) {
      ItemStack bucket = new ItemStack(this, 1, i);
      if (FluidContainerRegistry.isFilledContainer(bucket))
        subItems.add(bucket);
    }
  }
}