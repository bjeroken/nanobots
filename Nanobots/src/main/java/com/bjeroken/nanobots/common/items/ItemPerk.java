package com.bjeroken.nanobots.common.items;

import java.util.List;

import com.bjeroken.nanobots.Nanobots;
import com.bjeroken.nanobots.common.stats.PerkList;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPerk extends ItemNanobotBase {

  public ItemPerk(String name) {
    super(name);
    this.setHasSubtypes(true);
    this.setMaxDamage(0);
    this.maxStackSize = 1;
  }
  
  @Override
  public void setTab(CreativeTabs tab) {}
  
  @Override
  public String getUnlocalizedName(ItemStack stack) {
    int i = stack.getMetadata();
    return "perk." + Refs.toResource(PerkList.PERK_NAMES[i]);
  }
  @Override
  public void regProxy() {
    ResourceLocation[] perkLocs = new ResourceLocation[PerkList.PERK_NAMES.length];
    for (int i = 0; i < PerkList.PERK_NAMES.length; i++) {
      perkLocs[i] = Refs.toResource("perk/perk_" + PerkList.PERK_NAMES[i]);
    }
    Nanobots.proxy.regRender(this, perkLocs);
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    for (int i = 0; i < PerkList.PERK_NAMES.length; ++i) {
      subItems.add(new ItemStack(itemIn, 1, i));
    }
  }

}