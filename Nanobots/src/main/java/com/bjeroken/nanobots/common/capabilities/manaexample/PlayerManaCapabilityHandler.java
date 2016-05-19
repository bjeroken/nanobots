package com.bjeroken.nanobots.common.capabilities.manaexample;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

/*
 * Copied from http://www.minecraftforge.net/forum/index.php/topic,37763.msg198798.html#msg198798
 */
public class PlayerManaCapabilityHandler implements IStorage<IBaseManaCapability> {
  public static final PlayerManaCapabilityHandler s_ManaHandler = new PlayerManaCapabilityHandler();

  @Override
  public NBTBase writeNBT(Capability<IBaseManaCapability> capability, IBaseManaCapability instance, EnumFacing side) {
    return instance.saveNBTData();
  }

  @Override
  public void readNBT(Capability<IBaseManaCapability> capability, IBaseManaCapability instance, EnumFacing side, NBTBase nbt) {
    instance.loadNBTData((NBTTagCompound) nbt);
  }
}