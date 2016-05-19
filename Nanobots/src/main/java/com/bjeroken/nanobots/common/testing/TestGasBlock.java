package com.bjeroken.nanobots.common.testing;

import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;

public final class TestGasBlock extends BlockFluidClassic {
  public static final TestGasBlock instance = new TestGasBlock();
  public static final String name = "TestGasBlock";

  private TestGasBlock() {
    super(TestGas.instance, Material.lava);
    setCreativeTab(Refs.TAB);
    setUnlocalizedName(Refs.MODID + ":" + name);
    setRegistryName(Refs.toResource(name));
  }
}