package com.bjeroken.nanobots.common.capabilities.extrasleep;

/*
 * Adapted from https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
 */
public class SleepDefaultImpl implements IExtraSleeping {
  private boolean isSleeping = false;

  @Override
  public boolean isSleeping() {
    return isSleeping;
  }

  @Override
  public void setSleeping(boolean value) {
    this.isSleeping = value;
  }
}