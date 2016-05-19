package com.bjeroken.nanobots.common.blocks;

import net.minecraft.util.IStringSerializable;

public enum EnumNanoPipe implements IStringSerializable {
  NONE(0, "none"),
  INPUT(1, "input"),
  OUTPUT(2, "output");
  private final int index;
  private final String name;

  private EnumNanoPipe(int indexIn, String nameIn) {
    this.index = indexIn;
    this.name = nameIn;
  }

  /**
   * Get the Index of this Property. Order is None, Input, Output, Relay
   */
  public int getIndex() {
    return this.index;
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public String getName() {
    return this.name;
  }

}