package com.bjeroken.nanobots.common.capabilities.manaexample;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

/*
 * Copied from http://www.minecraftforge.net/forum/index.php/topic,37763.msg198798.html#msg198798
 */
public class PlayerManaProvider implements ICapabilityProvider, INBTSerializable {
  @CapabilityInject(IBaseManaCapability.class)
  public static Capability<IBaseManaCapability> s_Mana = null;

  protected IBaseManaCapability m_Mana = null;

  public PlayerManaProvider() {
    m_Mana = new PlayerManaCapability();
  }

  public PlayerManaProvider(IBaseManaCapability mana) {
    this.m_Mana = mana;
  }

  public static IBaseManaCapability Get(EntityPlayer player) {
    if (player.hasCapability(s_Mana, null)) {
      return player.getCapability(s_Mana, null);
    }
    return null;
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    return s_Mana != null && capability == s_Mana;
  }

  @Override
  public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    if (s_Mana != null && capability == s_Mana)
      return (T) m_Mana;

    return null;
  }

  @Override
  public NBTBase serializeNBT() {
    return m_Mana.saveNBTData();
  }

  @Override
  public void deserializeNBT(NBTBase nbt) {
    m_Mana.loadNBTData((NBTTagCompound) nbt);
  }

}