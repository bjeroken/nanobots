package com.bjeroken.nanobots.common.testing;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public final class TestFluid extends Fluid {
  public static final String name = "testfluid";
  public static final TestFluid instance = new TestFluid();

  private TestFluid() {
    super(name, new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"));
  }

  @Override
  public int getColor() {
    return 0xFF00FF00;
  }
}