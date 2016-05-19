package com.bjeroken.nanobots.common;

import com.bjeroken.nanobots.client.gui.GuiPerks;
import com.bjeroken.nanobots.client.gui.container.GuiBlockSwapper;
import com.bjeroken.nanobots.common.capabilities.extrasleep.IExtraSleeping;
import com.bjeroken.nanobots.common.capabilities.extrasleep.NoBedStorage;
import com.bjeroken.nanobots.common.capabilities.extrasleep.SleepDefaultImpl;
import com.bjeroken.nanobots.common.capabilities.inventory.CapabilityInventoryHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.NanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.NanobotStorage;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.CapabilityFuelHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.CapabilityPerkHandler;
import com.bjeroken.nanobots.common.container.ContainerBlockSwapper;
import com.bjeroken.nanobots.common.event.CapabilityEventHandler;
import com.bjeroken.nanobots.common.event.ModEventHandler;
import com.bjeroken.nanobots.common.items.ModItems;
import com.bjeroken.nanobots.common.network.PacketDispatcher;
import com.bjeroken.nanobots.common.stats.PerkList;
import com.bjeroken.nanobots.common.testing.BlockSimpleTank;
import com.bjeroken.nanobots.common.testing.MilkFluidBlock;
import com.bjeroken.nanobots.common.testing.TestFluid;
import com.bjeroken.nanobots.common.testing.TestFluidBlock;
import com.bjeroken.nanobots.common.testing.TestGas;
import com.bjeroken.nanobots.common.testing.TestGasBlock;
import com.bjeroken.nanobots.common.testing.TestItem;
import com.bjeroken.nanobots.common.testing.TileSimpleTank;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {
  public void preInit(FMLPreInitializationEvent event) {

    // FLUID
    FluidRegistry.registerFluid(TestFluid.instance);
    FluidRegistry.registerFluid(TestGas.instance);
    FluidRegistry.registerFluid(ModItems.milkFluid);
    GameRegistry.register(TestFluidBlock.instance);
    GameRegistry
        .register(new ItemBlock(TestFluidBlock.instance).setRegistryName(TestFluidBlock.instance.getRegistryName()));
    GameRegistry.register(TestGasBlock.instance);
    GameRegistry
        .register(new ItemBlock(TestGasBlock.instance).setRegistryName(TestGasBlock.instance.getRegistryName()));
    GameRegistry.register(MilkFluidBlock.instance);
    GameRegistry
        .register(new ItemBlock(MilkFluidBlock.instance).setRegistryName(MilkFluidBlock.instance.getRegistryName()));

    // DYNBUCKET
    GameRegistry.register(new TestItem(), ModItems.testItemName);
    Block tank = new BlockSimpleTank();
    GameRegistry.register(tank, ModItems.simpleTankName);
    GameRegistry.register(new ItemBlock(tank), ModItems.simpleTankName);
    GameRegistry.registerTileEntity(TileSimpleTank.class, "simpletank");

    FluidRegistry.addBucketForFluid(FluidRegistry.getFluid(TestFluid.name));
    FluidRegistry.addBucketForFluid(FluidRegistry.getFluid(TestGas.name));

    // GameRegistry.registerItem(dynBucket, "dynbucket");
    GameRegistry.register(ModItems.dynBottle);
    FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluid("milk"), new ItemStack(Items.milk_bucket),
        FluidContainerRegistry.EMPTY_BUCKET);

    ModItems.init();
    PerkList.init();

    CapabilityInventoryHandler.register();

    CapabilityManager.INSTANCE.register(IExtraSleeping.class, new NoBedStorage(), SleepDefaultImpl.class);
    CapabilityManager.INSTANCE.register(INanobotCapability.class, new NanobotStorage(), NanobotCapability.class);
    CapabilityFuelHandler.register();
    CapabilityPerkHandler.register();

    MinecraftForge.EVENT_BUS.register(new CapabilityEventHandler());
    MinecraftForge.EVENT_BUS.register(new ModEventHandler());

    PacketDispatcher.registerPackets();
    registerRender();
  }

  public void init(FMLInitializationEvent event) {
  }

  public void postInit(FMLPostInitializationEvent event) {
  }

  protected void registerRender() {
  }

  public void regRender(Item object, ResourceLocation... loc) {
  }

  public void regFluidRender(Block fluidBlock, ModelResourceLocation model) {

  }

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    if (ID == Refs.GUI_SWAPPER)
      return new ContainerBlockSwapper(player.inventory);
    if (ID == Refs.GUI_PERKS) {
      ModLogger.log("registered gui open on server");
    }
    return null;
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    ModLogger.log("registered gui open on client" + ID);
    if (ID == Refs.GUI_PERKS) {
      return new GuiPerks(player);
    } else if (ID == Refs.GUI_SWAPPER) {
      return new GuiBlockSwapper(player.inventory);
    } else {
      return null;
    }
  }

  // From CoolAlias tutorial
  /**
   * Returns a side-appropriate EntityPlayer for use during message handling
   */
  public EntityPlayer getPlayerEntity(MessageContext ctx) {
    return ctx.getServerHandler().playerEntity;
  }

  // From CoolAlias tutorial
  /**
   * Returns the current thread based on side during message handling, used for
   * ensuring that the message is being handled by the main thread
   */
  public IThreadListener getThreadFromContext(MessageContext ctx) {
    return ctx.getServerHandler().playerEntity.getServerForPlayer();
  }

}