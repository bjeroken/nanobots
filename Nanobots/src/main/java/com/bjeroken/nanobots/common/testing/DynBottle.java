package com.bjeroken.nanobots.common.testing;

import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.UniversalBucket;

public class DynBottle extends UniversalBucket {
  public DynBottle() {
    super(250, new ItemStack(Items.glass_bottle), true);
    setUnlocalizedName("dynbottle");
    setRegistryName(new ResourceLocation(Refs.MODID, "dynbottle"));
    setMaxStackSize(16);
    setHasSubtypes(true);
    setCreativeTab(CreativeTabs.tabMisc);
  }
}
