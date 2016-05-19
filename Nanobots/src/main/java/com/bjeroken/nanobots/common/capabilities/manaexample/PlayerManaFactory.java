package com.bjeroken.nanobots.common.capabilities.manaexample;

import java.util.concurrent.Callable;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/*
 * Copied from http://www.minecraftforge.net/forum/index.php/topic,37763.msg198798.html#msg198798
 */
public class PlayerManaFactory implements Callable<IBaseManaCapability> {
  @Override
  public PlayerManaCapability call() throws Exception {
    return new PlayerManaCapability();
  }
}