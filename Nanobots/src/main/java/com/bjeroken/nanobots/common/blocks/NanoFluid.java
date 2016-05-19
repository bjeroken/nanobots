package com.bjeroken.nanobots.common.blocks;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public final class NanoFluid extends Fluid {
  public static final String name = "fluid_nano";

  public NanoFluid() {
    super(name, new ResourceLocation("blocks/lava_still"), new ResourceLocation("blocks/lava_flow"));
  }

  @Override
  public int getColor() {
    return 0xFF00FF00;
  }

}