package com.bjeroken.nanobots.common.stats;

import java.util.ArrayList;
import java.util.List;

import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.IPerkCapability;
import com.bjeroken.nanobots.common.network.client.UpdateStatsMessage;
import com.bjeroken.nanobots.common.util.EnumStats;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Perk {
  public final int displayColumn;
  public final int displayRow;
  public final Perk parentPerk;
  private final String perkName;
  private final String perkDescription;
  private final boolean isSpecial;
  private List<Perk> childPerks;
  public final ItemStack theItemStack;
  private int perkCost;

  public Perk(String perkName, int column, int row, Item item, Perk parent, boolean isSpecial) {
    this(perkName, column, row, new ItemStack(item), parent, isSpecial);
  }

  public Perk(String perkName, int column, int row, Block block, Perk parent, boolean isSpecial) {
    this(perkName, column, row, new ItemStack(block), parent, isSpecial);
  }

  public Perk(String perkName, int column, int row, ItemStack itemstack, Perk parent, boolean isSpecial) {
    this.perkName = "perk." + perkName;
    this.theItemStack = itemstack;
    this.perkDescription = "perk." + perkName + ".desc";
    this.displayColumn = column;
    this.displayRow = row;
    this.isSpecial = isSpecial;
    childPerks = new ArrayList<Perk>();
    if (column < PerkList.minDisplayColumn)
      PerkList.minDisplayColumn = column;
    if (row < PerkList.minDisplayRow)
      PerkList.minDisplayRow = row;
    if (column > PerkList.maxDisplayColumn)
      PerkList.maxDisplayColumn = column;
    if (row > PerkList.maxDisplayRow)
      PerkList.maxDisplayRow = row;
    this.parentPerk = parent;
    if (parent != null)
      this.parentPerk.addChild(this);
    perkCost = 1 + countParents();
  }

  public boolean isSpecial() {
    return this.isSpecial;
  }

  public Perk addToPerkList(PerkPage perkPage) {
    perkPage.add(this);
    return this;
  }

  public UpdateStatsMessage activatePerk(EntityPlayer player, UpdateStatsMessage update) {
    IPerkCapability pc = Refs.getPerkCap(player);
    IFuelCapability fuel = Refs.getPlayerFuelCap(player);
    INanobotCapability nanoc = Refs.getNanobotCap(player);

    update.addStat(EnumStats.perks, pc.addPerk(this));
    update.addStat(EnumStats.perkpoints, pc.setPerkPoints(pc.getPerkPoints() - this.getPerkCost()));
    update.addStat(EnumStats.perkpointsused, pc.setUsedPoints(pc.getUsedPoints() + this.getPerkCost()));

    return update;
  }

  public int countParents() {
    return countParents(this);
  }

  public int countParents(Perk perk) {
    int c = 0;
    while (perk.parentPerk != null) {
      c++;
      perk = perk.parentPerk;
    }
    return c;
  }

  public int getPerkCost() {
    return perkCost;
  }

  public Perk setPerkCost(int cost) {
    if (cost < 1)
      cost = 1;
    perkCost = cost;
    return this;
  }

  public void addChild(Perk perk) {
    if (perk.parentPerk.equals(this))
      childPerks.add(perk);
  }

  public Perk addToPage(PerkPage page) {
    page.add(this);
    return this;
  }

  public String getPerkName() {
    return this.perkName;
  }

  @SideOnly(Side.CLIENT)
  public String getDescription() {
    return I18n.format(this.perkDescription, new Object[0]);
  }

  @SideOnly(Side.CLIENT)
  public String getName() {
    return I18n.format(this.perkName, new Object[0]);
  }

  @Override
  public String toString() {
    return "Perk [perkName=" + perkName.substring(5) + "]";
  }

}