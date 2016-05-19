package com.bjeroken.nanobots.common.capabilities.nanobots.perk;

import java.util.UUID;

import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.NanobotStatus;
import com.bjeroken.nanobots.common.capabilities.nanobots.NanobotUtils;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.FuelUtils;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.network.client.UpdateStatsMessage;
import com.bjeroken.nanobots.common.stats.Perk;
import com.bjeroken.nanobots.common.stats.PerkList;
import com.bjeroken.nanobots.common.util.EnumStats;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class PerkUtils {
  private static int reduceMult = 10;
  private static int sprintCost = -2;

  private static int second = 20;

  private static final UUID sprintingSpeedBoostModifierBonusUUID = UUID.fromString("962A6B8D-DA3E-4C1C-8813-96EA6097278D");
  private static final AttributeModifier sprintingSpeedBoostModifierBonus = (new AttributeModifier(sprintingSpeedBoostModifierBonusUUID,
      "Sprinting speed bonus boost", 0.30000001192092896D, 2)).setSaved(false);

  /**
   * Check if all perks listed are taken
   */
  public static boolean hasAllPerks(IPerkCapability perk, Perk... perks) {
    for (Perk p : perks) {
      if (!perk.hasPerk(p))
        return false;
    }
    return true;
  }

  /**
   * Check if one of the perks listed are taken
   */
  public static boolean hasPerk(IPerkCapability perk, Perk... perks) {
    boolean has = false;
    for (Perk p : perks) {
      if (perk.hasPerk(p)) {
        has = true;
        break;
      }
    }
    return has;
  }

  /*
   * Check for fall reduce perks and reduce distance accordingly
   */
  public static void fall(LivingFallEvent event) {
    EntityPlayer player = (EntityPlayer) event.getEntity();
    final INanobotCapability props = Refs.getNanobotCap(player);
    final IPerkCapability perks = Refs.getPerkCap(player);
    final IFuelCapability fuel = Refs.getPlayerFuelCap(player);

    if (!NanobotUtils.isActive(props) || !PerkUtils.hasPerk(perks, PerkList.fallReduce1, PerkList.fallReduce2))
      return;

    // Only act if fall distance is greater than 3 blocks
    if (event.getDistance() > 3.0F && fuel.getCurrentFuel() > 0) {
      float dist = event.getDistance() - 3.0F;
      int maxReduce = 0;
      if (perks.hasPerk(PerkList.fallReduce2))
        maxReduce = 6;
      else
        maxReduce = 3;
      float reduceby = maxReduce < dist ? maxReduce : dist;
      int reduceCost = (int) (reduceby * reduceMult);
      if (!FuelUtils.hasFuelForCost(fuel, reduceCost)) {
        reduceby = fuel.getCurrentFuel() / reduceMult;
        reduceCost = (int) (reduceby * reduceMult);
        player.addChatComponentMessage(new TextComponentTranslation("fuel.low.falldistance", new Object[0]));
      }
      event.setDistance(event.getDistance() - reduceby);
      FuelUtils.addFuelGetMessage(fuel, -reduceCost).sendTo(player);
    }
  }

  /*
   * Check for jump boost perks and act on it
   */
  public static void jump(EntityPlayer player) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    final IPerkCapability perks = Refs.getPerkCap(player);
    final IFuelCapability fuel = Refs.getPlayerFuelCap(player);
    if (!NanobotUtils.isActive(props) || !PerkUtils.hasPerk(perks, PerkList.jumpBoost2, PerkList.jumpBoost1))
      return;
    float motion = 0.2F;
    // Default motionY 0.42F
    boolean jumping = false;
    UpdateStatsMessage msg = new UpdateStatsMessage();
    int boost1 = -2;
    int boost2 = -4;
    if (perks.hasPerk(PerkList.jumpBoost2) && FuelUtils.hasFuelForCost(fuel, boost2)) {
      player.motionY = 0.75F;
      motion = 0.7F;
      jumping = true;
      msg = FuelUtils.addFuelGetMessage(fuel, boost2);

      // still get jumpboost 1 if low on fuel
    } else if (FuelUtils.hasFuelForCost(fuel, boost1)) {
      player.motionY = 0.6F;
      motion = 0.5F;
      jumping = true;
      msg = FuelUtils.addFuelGetMessage(fuel, boost1);
    }

    if (jumping) {
      if (!player.worldObj.isRemote)
        msg.sendTo(player);
      doJump(player, motion);
    }
  }

  /*
   * Add bonus effect if jump potion active
   */
  private static void doJump(EntityPlayer player, float motion) {
    if (player.isPotionActive(Potion.getPotionFromResourceLocation("jump"))) {
      player.motionY += (double) ((float) (player.getActivePotionEffect(Potion.getPotionFromResourceLocation("jump")).getAmplifier() + 1)
          * 0.1F);
    }

    if (player.isSprinting()) {
      float f = player.rotationYaw * 0.017453292F;
      player.motionX -= (double) (MathHelper.sin(f) * motion);
      player.motionZ += (double) (MathHelper.cos(f) * motion);
    }
  }
  
  /*
   * Add heals if player has regen perks
   */
  public static void healPlayer(EntityPlayer player) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    final IPerkCapability perks = Refs.getPerkCap(player);
    final IFuelCapability fuel = Refs.getPlayerFuelCap(player);
    int fuelCost = -1;
    if (!player.worldObj.isRemote && NanobotUtils.isActive(props) && PerkUtils.hasPerk(perks, PerkList.regen1, PerkList.regen2)
        && NanobotUtils.canHeal(player) && player.shouldHeal() && FuelUtils.hasFuelForCost(fuel, fuelCost)) {

      float healAmount = 2F;
      if (perks.hasPerk(PerkList.regen2)) {
        healAmount = 3F;
      }
      player.heal(healAmount);
      FuelUtils.addFuelGetMessage(fuel, fuelCost).sendTo(player);
      if (player.getHealth() < 10) {
        int duration = 100;
        if (perks.hasPerk(PerkList.regen2)) {
          duration = 200;
        }
        player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("absorption"), duration, 0));
        player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("regeneration"), duration, 0));
        player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("resistance"), duration, 0));
        FuelUtils.addFuelGetMessage(fuel, fuelCost).sendTo(player);
      }

    }
  }

  /*
   * Drain fuel while sprinting
   */
  public static void sprintPlayer(EntityPlayer player) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    final IPerkCapability perks = Refs.getPerkCap(player);
    final IFuelCapability fuel = Refs.getPlayerFuelCap(player);
    int ticks = second * 20;
    if (!player.worldObj.isRemote && NanobotUtils.isActive(props) && perks.hasPerk(PerkList.agil) && player.ticksExisted % ticks == 0) {
      if (player.getFoodStats().getSaturationLevel() < 15) {
        int amount = -10;
        if (FuelUtils.hasFuelForCost(fuel, amount)) {
          player.getFoodStats().addStats(1, 0.05F);
          FuelUtils.addFuelGetMessage(fuel, amount).sendTo(player);
        }
      }
    }
  }

  /*
   * Checks for runspeed perk and act on it
   */
  public static void runSpeed(EntityPlayer player) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    final IPerkCapability perks = Refs.getPerkCap(player);
    final IFuelCapability fuel = Refs.getPlayerFuelCap(player);

    if (perks.hasPerk(PerkList.runSpeed1) && NanobotUtils.isActive(props)) {
      IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

      if (iattributeinstance.getModifier(sprintingSpeedBoostModifierBonusUUID) != null) {
        iattributeinstance.removeModifier(sprintingSpeedBoostModifierBonus);
      }
      // Apply modifier is player is sprinting and has fuel && subtract fuel every second
      if (player.isSprinting() && FuelUtils.hasFuelForCost(fuel, sprintCost)) {
        iattributeinstance.applyModifier(sprintingSpeedBoostModifierBonus);
        if (!player.worldObj.isRemote && player.ticksExisted % second == 0) {
          FuelUtils.addFuelGetMessage(fuel, sprintCost).sendTo(player);
        }
      }
    }
  }
}
