package com.bjeroken.nanobots.common.capabilities.manaexample;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
/*
 * Copied from http://www.minecraftforge.net/forum/index.php/topic,37763.msg198798.html#msg198798
 */
public interface IBaseManaCapability {

  void SetMana(int mana, EntityPlayer player);

  int GetMana();

  void SetManaNoUpdate(int mana);

  NBTTagCompound saveNBTData();

  void loadNBTData(NBTTagCompound compound);
}
