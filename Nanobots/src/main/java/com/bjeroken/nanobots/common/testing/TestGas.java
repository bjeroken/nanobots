package com.bjeroken.nanobots.common.testing;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public final class TestGas extends Fluid {
  public static final String name = "testgas";
  public static final TestGas instance = new TestGas();

  private TestGas() {
    super(name, new ResourceLocation("blocks/lava_still"), new ResourceLocation("blocks/lava_flow"));
    density = -1000;
    isGaseous = true;
  }

  @Override
  public int getColor() {
    return 0xFFFF0000;
  }
}