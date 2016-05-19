package com.bjeroken.nanobots;

import com.bjeroken.nanobots.common.CommonProxy;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Refs.MODID, name = Refs.MODNAME, version = Refs.VERSION)
public class Nanobots {

  @Mod.Instance(Refs.MODID)
  public static Nanobots instance;

  @SidedProxy(modId = Refs.MODID, clientSide = Refs.CLIENT_PROXY_CLASS, serverSide = Refs.SERVER_PROXY_CLASS)
  public static CommonProxy proxy;

  static {
    FluidRegistry.enableUniversalBucket();
  }

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    proxy.preInit(event);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.init(event);
    NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    proxy.postInit(event);
  }
}
