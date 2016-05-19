package com.bjeroken.nanobots.common.testing;

import com.bjeroken.nanobots.common.items.ModItems;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;

public final class MilkFluidBlock extends BlockFluidClassic {
  public static final MilkFluidBlock instance = new MilkFluidBlock();
  public static final String name = "MilkFluidBlock";

  private MilkFluidBlock() {
    super(ModItems.milkFluid, Material.water);
    setCreativeTab(Refs.TAB);
    setUnlocalizedName(Refs.MODID + ":" + name);
    setRegistryName(Refs.toResource(name));
  }
}