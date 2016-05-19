package com.bjeroken.nanobots.common.items;

import java.util.List;

import com.bjeroken.nanobots.Nanobots;
import com.bjeroken.nanobots.common.stats.PerkList;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLevelTrophy extends ItemPerk {
  public static final String[] TROPHY_NAMES = { "level5", "level10", "level15", "level20", "level25", "level30", "level35", "level40", "level45",
      "level50" };

  public ItemLevelTrophy(String name) {
    super(name);
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {
    int i = stack.getMetadata();
    return "trophy." + Refs.toResource("trophy_" + TROPHY_NAMES[i]);
  }

  @Override
  public void regProxy() {
    ResourceLocation[] trophyLocs = new ResourceLocation[ItemLevelTrophy.TROPHY_NAMES.length];
    for (int i = 0; i < ItemLevelTrophy.TROPHY_NAMES.length; i++) {
      trophyLocs[i] = Refs.toResource("trophy/trophy_" + ItemLevelTrophy.TROPHY_NAMES[i]);
    }
    Nanobots.proxy.regRender(this, trophyLocs);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    for (int i = 0; i < TROPHY_NAMES.length; ++i) {
      subItems.add(new ItemStack(itemIn, 1, i));
    }
  }
}