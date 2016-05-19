package com.bjeroken.nanobots.common.util;

import com.bjeroken.nanobots.common.capabilities.extrasleep.IExtraSleeping;
import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.IPerkCapability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Refs {
  public static final boolean LOGGING = true;
  public static final String MODID = "nanobots";
  public static final String MODNAME = "Nanobots";
  public static final String VERSION = "1.9-0.0.1";
  public static final String CLIENT_PROXY_CLASS = "com.bjeroken.nanobots.client.ClientProxy";
  public static final String SERVER_PROXY_CLASS = "com.bjeroken.nanobots.common.CommonProxy";
  public static final float STEP_DEFAULT = 0.6f;
  public static final float STEP_ASSIST = 1f;
  public static final int FUEL_DEFAULT = 100;
  public static final int FUEL_LEVEL_MULT = 10;
  
  public static final NanobotTab TAB = new NanobotTab("Nanobots");

  private static int modGuiIndex = 10;
  public static final int GUI_SWAPPER = modGuiIndex++, GUI_CONTROLLER = modGuiIndex++, GUI_NANO_FURNACE = modGuiIndex++,
      GUI_NANO_CRUSHER = modGuiIndex++, GUI_NANO_TANK = modGuiIndex++, GUI_FUELGENERATOR = modGuiIndex++, GUI_WORKBENCH = modGuiIndex++,
      GUI_ENCHANTMENT = modGuiIndex++, GUI_PERKS = modGuiIndex++;

  @CapabilityInject(IExtraSleeping.class)
  public static final Capability<IExtraSleeping> SLEEP_CAP = null;
  @CapabilityInject(INanobotCapability.class)
  public static final Capability<INanobotCapability> NANOBOT_CAP = null;
  @CapabilityInject(IFuelCapability.class)
  public static final Capability<IFuelCapability> FUEL_CAP = null;
  @CapabilityInject(IPerkCapability.class)
  public static final Capability<IPerkCapability> PERK_CAP = null;

  // Simple static method just so I don't have to write the EnumFacing parameter.
  public static INanobotCapability getNanobotCap(EntityPlayer player) {
    return player.getCapability(Refs.NANOBOT_CAP, null);
  }
  
  public static IFuelCapability getPlayerFuelCap(EntityPlayer player){
    return player.getCapability(Refs.FUEL_CAP, null);
  }
  
  public static IFuelCapability getItemStackFuelCap(ItemStack stack){
    return stack.getCapability(Refs.FUEL_CAP, null);
  }
  
  public static IPerkCapability getPerkCap(EntityPlayer player){
    return player.getCapability(Refs.PERK_CAP, null);
  }
  public static NBTTagString toTagString(String s){
    return new NBTTagString(s);
  }
  public static NBTTagInt toTagInt(int i){
    return new NBTTagInt(i);
  }
  public static NBTTagFloat toTagFloat(float f){
    return new NBTTagFloat(f);
  }
  // Wrapper for items/blocks/etc to use
  public static ResourceLocation toResource(String path) {
    return new ResourceLocation(Refs.MODID, path);
  }
}
