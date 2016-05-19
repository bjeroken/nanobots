package com.bjeroken.nanobots.common.capabilities.nanobots;

import java.util.UUID;

import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.FuelUtils;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.IPerkCapability;
import com.bjeroken.nanobots.common.network.PacketDispatcher;
import com.bjeroken.nanobots.common.network.client.UpdateStatsMessage;
import com.bjeroken.nanobots.common.stats.PerkList;
import com.bjeroken.nanobots.common.util.EnumStats;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class NanobotUtils {

  public static int XP_MULTIPLIER_BASE = 10;
  public final static int XP_MULTIPLIER_LEVEL_1 = 12;
  public final static int XP_MULTIPLIER_LEVEL_2 = 14;

  private int cooldownSeconds = 45;
  private int cooldownTicks = cooldownSeconds * 20;
  protected int waterBreathingCooldown = 5 * 20;

  public static int second = 20;

  /**
   * Returns true if status is active
   */
  public static boolean isActive(INanobotCapability props) {
    return props.getNanobotStatus() == NanobotStatus.active;
  }

  public static void convertXp(EntityPlayer player) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    final IPerkCapability perks = Refs.getPerkCap(player);
    final IFuelCapability fuel = Refs.getPlayerFuelCap(player);
    if (props.getXPDrain() && props.getNanobotStatus() == NanobotStatus.active) {
      if (player.experienceLevel > 0 && fuel.getCurrentFuel() >= player.experienceLevel + props.getNanobotLevel()) {
        if (player.experienceLevel > 1 && player.experience == 0) {
          addExperience(player, xpBarCap(player.experienceLevel - 1));
          player.removeExperienceLevel(1);
        } else {
          if (player.experience > 0) {
            addExperience(player, (int) (player.experience * xpBarCap(player.experienceLevel)));
            player.experience = 0F;
            player.removeExperienceLevel(1);
            player.addExperienceLevel(1);
          }
        }
      }
      if (props.getNanobotXP() >= xpNeeded(player)) {
        new UpdateStatsMessage().addStat(EnumStats.nanobotlevel, props.setNanobotLevel(props.getNanobotLevel() + 1))
            .addStat(EnumStats.nanobotxp, props.setNanobotXP(props.getNanobotXP() - xpNeeded(props.getNanobotLevel() - 1))).sendTo(player);
      }
    }
  }

  public static void addExperience(EntityPlayer player, int amount) {
    addExperience(player, amount, true);
  }

  public static void addExperience(EntityPlayer player, int amount, boolean consumeFuel) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    final IPerkCapability perks = Refs.getPerkCap(player);
    final IFuelCapability fuel = Refs.getPlayerFuelCap(player);
    int j = Integer.MAX_VALUE - props.getNanobotXPTotal();
    if (perks.hasPerk(PerkList.xpConv1))
      amount *= XP_MULTIPLIER_LEVEL_1;
    else if (perks.hasPerk(PerkList.xpConv2))
      amount *= XP_MULTIPLIER_LEVEL_2;
    else
      amount *= XP_MULTIPLIER_BASE;
    UpdateStatsMessage msg = new UpdateStatsMessage();
    if (amount > j) {
      amount = j;
    }
    if (!player.worldObj.isRemote) {
      if (consumeFuel)
        msg.addStat(EnumStats.currentfuel, fuel.setCurrentFuel(fuel.getCurrentFuel() - (player.experienceLevel + props.getNanobotLevel())));
    }
    msg.addStat(EnumStats.nanobotxp, props.setNanobotXP(props.getNanobotXP() + amount));
    for (msg.addStat(EnumStats.nanobotxptotal,
        props.setNanobotXPTotal(props.getNanobotXPTotal() + amount)); props.getNanobotXP() >= xpNeeded(player); msg
            .addStat(EnumStats.nanobotxp, props.setNanobotXP(props.getNanobotXP() - xpNeeded(props.getNanobotLevel() - 1)))) {
      msg.addStat(EnumStats.nanobotlevel, props.setNanobotLevel(props.getNanobotLevel() + 1));
      player.addChatComponentMessage(new TextComponentTranslation("nanobots.levelup", props.getNanobotLevel()));
    }
    // Update the client
    msg.sendTo(player);
  }

  public static int xpBarCap(int level) {
    return level >= 30 ? 112 + (level - 30) * 9 : (level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2);
  }

  public static boolean counter(EntityPlayer player, int amount) {
    return player.ticksExisted % amount == 0;
  }

  public static boolean canHeal(EntityPlayer player) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    if (props.getHealResist() >= 200) {
      props.setHealResist(0);
    } else {
      props.setHealResist(props.getHealResist() + 1);
    }
    //Do not sync to player - triggers every tick!
    return props.getHealResist() == 0;
  }

  protected boolean canResistFire(EntityPlayer player) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    if (props.getFireResist() >= cooldownTicks){
      props.setFireResist(0);
    } else{
      props.setFireResist(props.getFireResist() + 1);
    }
    //Do not sync to player - triggers every tick!
    return props.getFireResist() == 0;
  }

  public static int xpNeeded(EntityPlayer player) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    return xpNeeded(props.getNanobotLevel());
  }

  public static int xpNeeded(int level) {
    return (level * 2 + XP_MULTIPLIER_BASE + (XP_MULTIPLIER_BASE * ((level / 5) + 1))) * XP_MULTIPLIER_BASE;
  }

  public static void setDrain(EntityPlayer player, boolean bool) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    PacketDispatcher.sendTo(new UpdateStatsMessage().addStat(EnumStats.drainxp, props.setXPDrain(bool)), (EntityPlayerMP) player);
  }

  public static void toggleDrain(EntityPlayer player) {
    final INanobotCapability props = Refs.getNanobotCap(player);
    setDrain(player, !props.getXPDrain());
  }

}
