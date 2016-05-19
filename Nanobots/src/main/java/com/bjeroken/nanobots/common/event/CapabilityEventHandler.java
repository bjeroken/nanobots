package com.bjeroken.nanobots.common.event;

import com.bjeroken.nanobots.common.capabilities.extrasleep.IExtraSleeping;
import com.bjeroken.nanobots.common.capabilities.extrasleep.SleepCapabilityHandler;
import com.bjeroken.nanobots.common.capabilities.inventory.InventoryHandlerProvider;
import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.PlayerNanobotHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.PlayerNanobotProvider;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.FuelProvider;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.IPerkCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.PerkHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.PerkProvider;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.PerkUtils;
import com.bjeroken.nanobots.common.items.ItemNanobotArmor;
import com.bjeroken.nanobots.common.items.ModItems;
import com.bjeroken.nanobots.common.network.PacketDispatcher;
import com.bjeroken.nanobots.common.network.client.FullSyncStatsMessage;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/*
 * Adapted from https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
 */
public class CapabilityEventHandler {

  @SubscribeEvent
  public void onAttachCapabilities(AttachCapabilitiesEvent.Entity evt) {
    if (evt.getEntity() instanceof EntityPlayer) {
      evt.addCapability(Refs.toResource("IExtraSleeping"), new SleepCapabilityHandler());
      evt.addCapability(Refs.toResource("INanobotCapability"), new PlayerNanobotProvider());
      evt.addCapability(Refs.toResource("IPerkCapability"), new PerkProvider());
      evt.addCapability(Refs.toResource("IFuelCapability"), new FuelProvider());

    }
  }

  @SubscribeEvent
  public void onAttachItemCapabilities(AttachCapabilitiesEvent.Item evt) {
    if (evt.getItem() == ModItems.blockReplacer) {
      evt.addCapability(Refs.toResource("InventoryHandler"), new InventoryHandlerProvider());
    }
    if (evt.getItem() instanceof ItemNanobotArmor) {
      // ItemNanobotArmor armor = (ItemNanobotArmor) evt.getItem();
      // evt.addCapability(Refs.toResource("IFuelCapability"), new
      // FuelProvider(evt.getItemStack(),
      // armor.getArmorMaterial().getRepairItem()));
    }
  }

  @SubscribeEvent
  public void onBedCheck(SleepingLocationCheckEvent evt) {
    final IExtraSleeping sleep = evt.getEntityPlayer().getCapability(Refs.SLEEP_CAP, null);
    if (sleep != null && sleep.isSleeping())
      evt.setResult(Result.ALLOW);
  }

  @SubscribeEvent
  public void onWakeUp(PlayerWakeUpEvent evt) {
    final IExtraSleeping sleep = evt.getEntityPlayer().getCapability(Refs.SLEEP_CAP, null);
    if (sleep != null)
      sleep.setSleeping(false);
  }

  @SubscribeEvent
  public void onClone(PlayerEvent.Clone event) {
    if (!event.isWasDeath())
      return;

    final INanobotCapability propsNew = Refs.getNanobotCap(event.getEntityPlayer());
    propsNew.read(event.getOriginal());
  }

  /*
   * Just in case a full sync is needed
   */
  @SubscribeEvent
  public void onJoin(EntityJoinWorldEvent event) {
    if (event.getEntity() instanceof EntityPlayerMP) {
      EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
      INanobotCapability nanoCap = Refs.getNanobotCap(player);
      IFuelCapability fuelCap = Refs.getPlayerFuelCap(player);
      IPerkCapability perkCap = Refs.getPerkCap(player);
      PacketDispatcher.sendTo(new FullSyncStatsMessage(nanoCap.writeNBT()), (EntityPlayerMP) event.getEntity());
      PacketDispatcher.sendTo(new FullSyncStatsMessage(fuelCap.writeNBT()), (EntityPlayerMP) event.getEntity());
      PacketDispatcher.sendTo(new FullSyncStatsMessage(perkCap.writeNBT()), (EntityPlayerMP) event.getEntity());
    }
  }

  @SubscribeEvent
  public void onLivingFallEvent(LivingFallEvent event) {
    if (event.getEntity() instanceof EntityPlayer) {
      if (!event.getEntity().worldObj.isRemote) {
        PerkUtils.fall(event);
      }
    }
  }

  @SubscribeEvent
  public void onLivingJumpEvent(LivingJumpEvent event) {
    if (event.getEntity() instanceof EntityPlayer) {
      PerkUtils.jump((EntityPlayer) event.getEntity());
    }

  }

  @SubscribeEvent
  public void onLivingUpdate(LivingUpdateEvent event) {
    if (event.getEntity() instanceof EntityPlayer) {
      PlayerNanobotHandler.onUpdate((EntityPlayer) event.getEntity());
      PerkHandler.onUpdate((EntityPlayer) event.getEntity());
    }
  }
}