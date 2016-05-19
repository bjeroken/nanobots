package com.bjeroken.nanobots.common.stats;

import java.util.HashMap;
import java.util.Map;

import com.bjeroken.nanobots.common.items.ModItems;
import com.bjeroken.nanobots.common.util.ModLogger;

import net.minecraft.item.ItemStack;

public class PerkList {
  public static int minDisplayColumn;
  public static int minDisplayRow;
  public static int maxDisplayColumn;
  public static int maxDisplayRow;
  public static Perk intel, fuelEff1, fuelEff2, machEff1, machEff2, machAcc, machHopper, xpConv1, xpConv2;
  public static Perk stam, regen1, regen2, autoFeed, fireRes, miningSpeed1, miningSpeed2, attackDmg1;
  public static Perk agil, runSpeed1, waterbreathing, bowSpeed1, fallReduce1, fallReduce2, jumpBoost1, jumpBoost2, stepAssist;
  public static Map<String, Perk> perkMap;
  public static String[] PERK_NAMES = { "intel", "fuelEff1", "fuelEff2", "machEff1", "machEff2", "machAcc", "machHopper", "xpConv1",
      "xpConv2", "stam", "regen1", "regen2", "autoFeed", "fireRes", "miningSpeed1", "miningSpeed2", "attackDmg1", "agil", "runSpeed1",
      "waterbreathing", "bowSpeed1", "fallReduce1", "fallReduce2", "jumpBoost1", "jumpBoost2", "stepAssist" };
  
  public static void init() {
    perkMap = new HashMap<String, Perk>();
    PerkPage intelPage = new PerkPage("perks.intelligence");
    PerkPage stamPage = new PerkPage("perks.stamina");
    PerkPage agiPage = new PerkPage("perks.agility");

    intel = new Perk(EnumPerk.intelligence.name(), 0, 0, new ItemStack(ModItems.perk, 1, 0), (Perk) null, true).addToPage(intelPage);
    fuelEff1 = new Perk(EnumPerk.fuelefficiency1.name(), -1, -2, new ItemStack(ModItems.perk, 1, 1), intel, false).addToPage(intelPage);
    fuelEff2 = new Perk(EnumPerk.fuelefficiency2.name(), -3, -2, new ItemStack(ModItems.perk, 1, 2), fuelEff1, false).addToPage(intelPage);
    machEff1 = new Perk(EnumPerk.machineefficiency1.name(), 1, 2, new ItemStack(ModItems.perk, 1, 3), intel, false).addToPage(intelPage);
    machEff2 = new Perk(EnumPerk.machineefficiency2.name(), 3, 2, new ItemStack(ModItems.perk, 1, 4), machEff1, false).addToPage(intelPage);
    machAcc = new Perk(EnumPerk.machineaccess.name(), 2, 0, new ItemStack(ModItems.perk, 1, 5), intel, false).addToPage(intelPage);
    machHopper = new Perk(EnumPerk.machinehopper.name(), 4, 0, new ItemStack(ModItems.perk, 1, 6), machAcc, false).addToPage(intelPage);
    xpConv1 = new Perk(EnumPerk.xpconversion1.name(), -2, 0, new ItemStack(ModItems.perk, 1, 7), intel, false).addToPage(intelPage);
    xpConv2 = new Perk(EnumPerk.xpconversion2.name(), -4, 0, new ItemStack(ModItems.perk, 1, 8), xpConv1, false).addToPage(intelPage);

    stam = new Perk(EnumPerk.stamina.name(), 0, 0, new ItemStack(ModItems.perk, 1, 9), (Perk) null, true).addToPage(stamPage);
    regen1 = new Perk(EnumPerk.regeneration1.name(), -1, -2, new ItemStack(ModItems.perk, 1, 10), stam, false).addToPage(stamPage);
    regen2 = new Perk(EnumPerk.regeneration2.name(), -1, -4, new ItemStack(ModItems.perk, 1, 11), regen1, false).addToPage(stamPage);
    autoFeed = new Perk(EnumPerk.autofeed.name(), -3, -2, new ItemStack(ModItems.perk, 1, 12), regen1, false).addToPage(stamPage);
    fireRes = new Perk(EnumPerk.fireresistance.name(), 2, 0, new ItemStack(ModItems.perk, 1, 13), stam, false).addToPage(stamPage);
    miningSpeed1 = new Perk(EnumPerk.miningspeed1.name(), -1, 2, new ItemStack(ModItems.perk, 1, 14), stam, false).addToPage(stamPage);
    miningSpeed2 = new Perk(EnumPerk.miningspeed2.name(), -3, 2, new ItemStack(ModItems.perk, 1, 15), miningSpeed1, false)
        .addToPage(stamPage);
    attackDmg1 = new Perk(EnumPerk.attackdamage1.name(), 1, 2, new ItemStack(ModItems.perk, 1, 16), stam, false).addToPage(stamPage);

    agil = new Perk(EnumPerk.agility.name(), 0, 0, new ItemStack(ModItems.perk, 1, 17), (Perk) null, true).addToPage(agiPage);
    runSpeed1 = new Perk(EnumPerk.runspeed1.name(), -2, 0, new ItemStack(ModItems.perk, 1, 18), agil, false).addToPage(agiPage);
    waterbreathing = new Perk(EnumPerk.waterbreathing.name(), 2, 0, new ItemStack(ModItems.perk, 1, 19), agil, false).addToPage(agiPage);
    bowSpeed1 = new Perk(EnumPerk.bowspeed.name(), 1, 2, new ItemStack(ModItems.perk, 1, 20), agil, false).addToPage(agiPage);
    fallReduce1 = new Perk(EnumPerk.fallreduce1.name(), 1, -2, new ItemStack(ModItems.perk, 1, 21), agil, false).addToPage(agiPage);
    fallReduce2 = new Perk(EnumPerk.fallreduce2.name(), 3, -2, new ItemStack(ModItems.perk, 1, 22), fallReduce1, false).addToPage(agiPage);
    jumpBoost1 = new Perk(EnumPerk.jumpboost1.name(), -1, 2, new ItemStack(ModItems.perk, 1, 23), agil, false).addToPage(agiPage);
    jumpBoost2 = new Perk(EnumPerk.jumpboost2.name(), -3, 2, new ItemStack(ModItems.perk, 1, 24), jumpBoost1, false).addToPage(agiPage);
    stepAssist = new Perk(EnumPerk.stepassist.name(), -1, -2, new ItemStack(ModItems.perk, 1, 25), agil, false).setPerkCost(1)
        .addToPage(agiPage);

    PerkPage.registerPerkPage(intelPage);
    PerkPage.registerPerkPage(stamPage);
    PerkPage.registerPerkPage(agiPage);
    
    for(PerkPage pp: PerkPage.getPerkPages())
      for(Perk p : pp.getPerks()){
        perkMap.put(p.getPerkName(), p);
        ModLogger.log("p: " + p.getPerkName() + ", " + p);
      }
  }
}