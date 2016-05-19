package com.bjeroken.nanobots.common.capabilities.nanobots;

import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.FuelUtils;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.IPerkCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.PerkUtils;
import com.bjeroken.nanobots.common.items.ModItems;
import com.bjeroken.nanobots.common.util.EnumStats;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class PlayerNanobotHandler {


  public static NBTBase writeNBT(Capability<INanobotCapability> capability, INanobotCapability instance) {
    NBTTagCompound tags = new NBTTagCompound();
    tags.setInteger(EnumStats.nanobotlevel.name(), instance.getNanobotLevel());
    tags.setInteger(EnumStats.nanobotxp.name(), instance.getNanobotXP());
    tags.setFloat(EnumStats.stepheight.name(), instance.getStepHeight());
    tags.setInteger(EnumStats.nanobotstatus.name(), instance.getNanobotStatus().ordinal());
    tags.setBoolean(EnumStats.drainxp.name(), instance.getXPDrain());
    tags.setInteger(EnumStats.fireresist.name(), instance.getFireResist());
    tags.setInteger(EnumStats.healresist.name(), instance.getHealResist());
    return tags;
  }

  public static void readNBT(Capability<INanobotCapability> capability, INanobotCapability instance, NBTTagCompound nbt) {
    if (nbt.hasKey(EnumStats.nanobotlevel.name()))
      instance.setNanobotLevel(nbt.getInteger(EnumStats.nanobotlevel.name()));
    if (nbt.hasKey(EnumStats.nanobotxp.name()))
      instance.setNanobotXP(nbt.getInteger(EnumStats.nanobotxp.name()));
    if (nbt.hasKey(EnumStats.stepheight.name()))
      instance.setStepHeight(nbt.getFloat(EnumStats.stepheight.name()));
    if (nbt.hasKey(EnumStats.nanobotstatus.name()))
      instance.setNanobotStatus(NanobotStatus.values()[nbt.getInteger(EnumStats.nanobotstatus.name())]);
    if (nbt.hasKey(EnumStats.drainxp.name()))
      instance.setXPDrain(nbt.getBoolean(EnumStats.drainxp.name()));

    if (nbt.hasKey(EnumStats.fireresist.name())) {
      instance.setFireResist(nbt.getInteger(EnumStats.fireresist.name()));
    }
    if (nbt.hasKey(EnumStats.healresist.name())) {
      instance.setFireResist(nbt.getInteger(EnumStats.healresist.name()));
    }
  }

  public static void onUpdate(EntityPlayer player) {
    if (!(player instanceof EntityPlayerMP))
      return;
    final INanobotCapability props = Refs.getNanobotCap(player);
    final IPerkCapability perks = Refs.getPerkCap(player);
    final IFuelCapability fuel = Refs.getPlayerFuelCap(player);
    if (props == null || perks == null || fuel == null)
      return;
    if(player.ticksExisted % (NanobotUtils.second * 10) == 0)
      FuelUtils.transferFromArmor(player, 100);
    PerkUtils.healPlayer(player);
    if(player.ticksExisted % NanobotUtils.second == 0)
      NanobotUtils.convertXp(player);
    props.update();
    PerkUtils.runSpeed(player);
    if (player.isSprinting())
      PerkUtils.sprintPlayer(player);
    IBlockState bs = player.worldObj
        .getBlockState(
            player.getPosition().offset(EnumFacing.DOWN));
    if(bs != null)
    if(bs.getBlock() != null){
     if(bs.getBlock() == ModItems.baseBlock){
       if(player.ticksExisted % (NanobotUtils.second / 4) == 0)
         FuelUtils.transferToArmor(player, 40);
     }
    }
  }

  public static void processNBTData(EntityPlayer player, NBTTagCompound nbt) {
    INanobotCapability instance = Refs.getNanobotCap(player);
    if (instance == null)
      return;
    if (nbt.hasKey(EnumStats.nanobotlevel.name())) {
      instance.setNanobotLevel(nbt.getInteger(EnumStats.nanobotlevel.name()));
      ModLogger.log("recieved nanobotLevel: " + instance.getNanobotLevel());
    }
    if (nbt.hasKey(EnumStats.nanobotxp.name())) {
      instance.setNanobotXP(nbt.getInteger(EnumStats.nanobotxp.name()));
      ModLogger.log("recieved nanobotXP: " + instance.getNanobotXP());
    }
    if (nbt.hasKey(EnumStats.stepheight.name())) {
      instance.setStepHeight(player, nbt.getFloat(EnumStats.stepheight.name()));
      ModLogger.log("recieved stepHeight: " + instance.getStepHeight());
    }
    if (nbt.hasKey(EnumStats.nanobotstatus.name())) {
      instance.setNanobotStatus(NanobotStatus.values()[nbt.getInteger(EnumStats.nanobotstatus.name())]);
      ModLogger.log("recieved nanobot status: " + instance.getNanobotStatus());
    }
    if (nbt.hasKey(EnumStats.drainxp.name())) {
      instance.setXPDrain(nbt.getBoolean(EnumStats.drainxp.name()));
      ModLogger.log("recieved xp drain: " + instance.getXPDrain());
    }
    if (nbt.hasKey(EnumStats.fireresist.name())) {
      instance.setFireResist(nbt.getInteger(EnumStats.fireresist.name()));
    }
    if (nbt.hasKey(EnumStats.healresist.name())) {
      instance.setHealResist(nbt.getInteger(EnumStats.healresist.name()));
    }
  }
}
