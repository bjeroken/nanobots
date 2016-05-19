package com.bjeroken.nanobots.common.capabilities.inventory;

import java.util.concurrent.Callable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

/*
 * Adapted from CapabilityItemHandler.class
 */
public class CapabilityInventoryHandler {

  @CapabilityInject(IInventoryHandler.class)
  public static Capability<IInventoryHandler> CAP = null;

  public static void register() {

    CapabilityManager.INSTANCE.register(IInventoryHandler.class, new InventoryHandlerStorage(),
        new Callable<InventoryHandler>() {
          @Override
          public InventoryHandler call() throws Exception {
            return new InventoryHandler();
          }
        });
  }
}
