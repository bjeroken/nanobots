package com.bjeroken.nanobots.common.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class NanobotTab extends CreativeTabs{

  public NanobotTab(String label) {
    super(label);
  }
  @Override
  public Item getTabIconItem() {
    // TODO: custom item icon
    return Items.baked_potato;
  }
  
}
