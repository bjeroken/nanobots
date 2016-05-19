package com.bjeroken.nanobots.common.capabilities.extrasleep;

/*
 * Adapted from https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
 */
public interface IExtraSleeping {
  boolean isSleeping();

  void setSleeping(boolean value);
}
