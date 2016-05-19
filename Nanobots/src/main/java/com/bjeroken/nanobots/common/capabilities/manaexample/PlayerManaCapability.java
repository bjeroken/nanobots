package com.bjeroken.nanobots.common.capabilities.manaexample;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/*
 * Copied from http://www.minecraftforge.net/forum/index.php/topic,37763.msg198798.html#msg198798
 */
public class PlayerManaCapability implements IBaseManaCapability {
  protected int m_Mana = 0;

  @Override
  public int GetMana() {
    return m_Mana;
  }

  @Override
  public void SetMana(int mana, EntityPlayer player) {
    // ItemStack itemStack = PlayerUtils.FindWand(player);
    // if(itemStack == null)
    // return;

    // ItemBaseWand wand = (ItemBaseWand)itemStack.getItem();

    // m_Mana = Math.min(wand.GetMaxMana(), mana);
    // MoMMod.s_NetworkChannel.sendTo(new PlayerManaChangePacket(Integer.toString(m_Mana)), (EntityPlayerMP)player);
  }

  @Override
  public void SetManaNoUpdate(int mana) {
    m_Mana = mana;
  }

  @Override
  public NBTTagCompound saveNBTData() {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setInteger("mana", m_Mana);
    return nbt;
  }

  @Override
  public void loadNBTData(NBTTagCompound compound) {
    m_Mana = compound.getInteger("mana");
  }
}