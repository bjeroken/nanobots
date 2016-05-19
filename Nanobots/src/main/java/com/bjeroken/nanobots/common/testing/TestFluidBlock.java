package com.bjeroken.nanobots.common.testing;

import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;

public final class TestFluidBlock extends BlockFluidClassic {
  public static final TestFluidBlock instance = new TestFluidBlock();
  public static final String name = "TestFluidBlock";

  private TestFluidBlock() {
    super(TestFluid.instance, Material.water);
    setCreativeTab(Refs.TAB);
    setUnlocalizedName(Refs.MODID + ":" + name);
    setRegistryName(Refs.toResource(name));
  }
}