package com.bjeroken.nanobots.common.capabilities.nanobots.perk;

import java.util.ArrayList;
import java.util.List;

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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.capabilities.Capability;

public class PerkHandler {

  public static NBTBase writeNBT(Capability<IPerkCapability> capability, IPerkCapability instance) {
    NBTTagCompound tags = new NBTTagCompound();
    List<Perk> perks = instance.getPerks();
    NBTTagList perksList = new NBTTagList();
    for (Perk p : perks)
      perksList.appendTag(Refs.toTagString(p.getPerkName()));
    tags.setTag(EnumStats.perks.name(), perksList);
    tags.setInteger(EnumStats.perkpoints.name(), instance.getPerkPoints());
    tags.setInteger(EnumStats.perkpointsused.name(), instance.getUsedPoints());
    return tags;
  }

  public static void readNBT(Capability<IPerkCapability> capability, IPerkCapability instance, NBTTagCompound data) {
    List<Perk> perks = new ArrayList<Perk>();
    if (data.hasKey(EnumStats.perks.name())) {
      NBTTagList perksList = (NBTTagList) data.getTag(EnumStats.perks.name());
      for (int i = 0; i < perksList.tagCount(); i++) {
        perks.add(PerkList.perkMap.get(perksList.getStringTagAt(i)));
      }
      instance.setPerks(perks);
    }
    if (data.hasKey(EnumStats.perkpoints.name()))
      instance.setPerkPoints(data.getInteger(EnumStats.perkpoints.name()));
    if (data.hasKey(EnumStats.perkpointsused.name()))
      instance.setUsedPoints(data.getInteger(EnumStats.perkpointsused.name()));

  }

  public static void onUpdate(EntityPlayer player) {
    if (!(player instanceof EntityPlayerMP))
      return;
    final INanobotCapability props = Refs.getNanobotCap(player);
    final IPerkCapability perks = Refs.getPerkCap(player);
    final IFuelCapability fuel = Refs.getPlayerFuelCap(player);
    if (props == null || perks == null)
      return;

    if (props.getNanobotStatus() == NanobotStatus.dead) {
      UpdateStatsMessage msg = new UpdateStatsMessage();
      boolean change = false;
      //TODO: Add more stuff that deactivates when dead queen.
      
      if (perks.hasPerk(PerkList.stepAssist) && props.getStepHeight() > Refs.STEP_DEFAULT) {
        msg.addStat(EnumStats.stepheight, props.setStepHeight(Refs.STEP_DEFAULT));
        change = true;
      }
      if(change)
      msg.sendTo(player);
      
    } else if (NanobotUtils.isActive(props)) {
      UpdateStatsMessage msg = new UpdateStatsMessage();
      boolean change = false;
      //TODO: Add more stuff that works when queen is alive.
      if (perks.hasPerk(PerkList.stepAssist) && props.getStepHeight() < Refs.STEP_ASSIST) {
        msg.addStat(EnumStats.stepheight, props.setStepHeight(Refs.STEP_ASSIST));
        change = true;
      }
      if(perks.hasPerk(PerkList.intel) && fuel.getMaxFuel() != Refs.FUEL_DEFAULT + (props.getNanobotLevel() * Refs.FUEL_LEVEL_MULT)){
        msg.merge(FuelUtils.setMaxFuelGetMessage(fuel, Refs.FUEL_DEFAULT + (props.getNanobotLevel() * Refs.FUEL_LEVEL_MULT)));
        change = true;
      }
      if(change)
      msg.sendTo(player);
    }
    

  }

  public static void processNBTData(EntityPlayer player, NBTTagCompound data) {
    IPerkCapability instance = Refs.getPerkCap(player);
    if (instance == null)
      return;
    // IF taglist
    if (data.hasKey(EnumStats.perks.name()) && data.getTagId(EnumStats.perks.name()) == 9) {
      NBTTagList perksList = (NBTTagList) data.getTag(EnumStats.perks.name());
      for (int i = 0; i < perksList.tagCount(); i++) {
        instance.addPerk(PerkList.perkMap.get(perksList.getStringTagAt(i)));
      }
      // If string
    } else if (data.hasKey(EnumStats.perks.name()) && data.getTagId(EnumStats.perks.name()) == 8) {
      instance.addPerk(PerkList.perkMap.get(data.getString(EnumStats.perks.name())));
    }
    if (data.hasKey(EnumStats.perkpoints.name()))
      instance.setPerkPoints(data.getInteger(EnumStats.perkpoints.name()));
    if (data.hasKey(EnumStats.perkpointsused.name()))
      instance.setUsedPoints(data.getInteger(EnumStats.perkpointsused.name()));
  }
}
