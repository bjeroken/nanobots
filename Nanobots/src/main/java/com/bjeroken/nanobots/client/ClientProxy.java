package com.bjeroken.nanobots.client;

import com.bjeroken.nanobots.client.gui.GuiNanobotOverlay;
import com.bjeroken.nanobots.common.CommonProxy;
import com.bjeroken.nanobots.common.items.ModItems;
import com.bjeroken.nanobots.common.testing.MilkFluidBlock;
import com.bjeroken.nanobots.common.testing.TestFluidBlock;
import com.bjeroken.nanobots.common.testing.TestGasBlock;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class ClientProxy extends CommonProxy {
  private final Minecraft mc = Minecraft.getMinecraft();
  private static ModelResourceLocation fluidLocation = new ModelResourceLocation(
      Refs.MODID.toLowerCase() + ":" + TestFluidBlock.name, "fluid");
  private static ModelResourceLocation gasLocation = new ModelResourceLocation(
      Refs.MODID.toLowerCase() + ":" + TestFluidBlock.name, "gas");
  private static ModelResourceLocation milkLocation = new ModelResourceLocation(
      Refs.MODID.toLowerCase() + ":" + TestFluidBlock.name, "milk");

  @Override
  public void preInit(FMLPreInitializationEvent event) {
    super.preInit(event);

    ModLogger.log("Client preInit");

    // FLUID
    Item fluid = Item.getItemFromBlock(TestFluidBlock.instance);
    Item gas = Item.getItemFromBlock(TestGasBlock.instance);
    Item milk = Item.getItemFromBlock(MilkFluidBlock.instance);
    // no need to pass the locations here, since they'll be loaded by the block
    // model logic.
    ModelBakery.registerItemVariants(fluid);
    ModelBakery.registerItemVariants(gas);
    ModelBakery.registerItemVariants(milk);
    ModelLoader.setCustomMeshDefinition(fluid, new ItemMeshDefinition() {
      public ModelResourceLocation getModelLocation(ItemStack stack) {
        return fluidLocation;
      }
    });
    ModelLoader.setCustomMeshDefinition(gas, new ItemMeshDefinition() {
      public ModelResourceLocation getModelLocation(ItemStack stack) {
        return gasLocation;
      }
    });
    ModelLoader.setCustomMeshDefinition(milk, new ItemMeshDefinition() {
      public ModelResourceLocation getModelLocation(ItemStack stack) {
        return milkLocation;
      }
    });
    ModelLoader.setCustomStateMapper(TestFluidBlock.instance, new StateMapperBase() {
      protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return fluidLocation;
      }
    });
    ModelLoader.setCustomStateMapper(TestGasBlock.instance, new StateMapperBase() {
      protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return gasLocation;
      }
    });
    ModelLoader.setCustomStateMapper(MilkFluidBlock.instance, new StateMapperBase() {
      protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return milkLocation;
      }
    });

    // DYNBUCKET
    ModelLoader.setBucketModelDefinition(ModItems.dynBucket);

    final ModelResourceLocation bottle = new ModelResourceLocation(new ResourceLocation(Refs.MODID, "dynbottle"),
        "inventory");
    ModelLoader.setCustomMeshDefinition(ModItems.dynBottle, new ItemMeshDefinition() {
      @Override
      public ModelResourceLocation getModelLocation(ItemStack stack) {
        return bottle;
      }
    });
    ModelBakery.registerItemVariants(ModItems.dynBottle, bottle);
    ModelLoader.setCustomModelResourceLocation(Item.itemRegistry.getObject(ModItems.simpleTankName), 0,
        new ModelResourceLocation(ModItems.simpleTankName, "normal"));
    ModelLoader.setCustomModelResourceLocation(Item.itemRegistry.getObject(ModItems.testItemName), 0,
        new ModelResourceLocation(new ResourceLocation("minecraft", "stick"), "inventory"));

  }

  @Override
  protected void registerRender() {

  }

  @Override
  public void regFluidRender(Block fluidBlock, ModelResourceLocation model) {
    Item fluid = Item.getItemFromBlock(fluidBlock);
    ModelBakery.registerItemVariants(fluid);
    ModelLoader.setCustomMeshDefinition(fluid, new ItemMeshDefinition() {
      public ModelResourceLocation getModelLocation(ItemStack stack) {
        return model;
      }
    });
    ModelLoader.setCustomStateMapper(fluidBlock, new StateMapperBase() {
      protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return model;
      }
    });
  }

  // Called from regProxy() implemented in my item classes
  // the ModItems class calls regProxy() when registering new items
  @Override
  public void regRender(Item object, ResourceLocation... loc) {
    for (int i = 0; i < loc.length; i++) {
      ModelLoader.setCustomModelResourceLocation(object, i, new ModelResourceLocation(loc[i], "inventory"));
    }
  }

  @Override
  public void init(FMLInitializationEvent event) {
    super.init(event);
    ModLogger.log("Client init");
    MinecraftForge.EVENT_BUS.register(new ModKeyHandler(mc));
    MinecraftForge.EVENT_BUS.register(new GuiNanobotOverlay(mc));
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);
    ModLogger.log("Client postInit");

  }

  // From CoolAlias tutorial
  @Override
  public EntityPlayer getPlayerEntity(MessageContext ctx) {
    // Note that if you simply return 'Minecraft.getMinecraft().thePlayer', your
    // packets will not work as expected because you will be
    // getting a client player even when you are on the server! Sounds absurd,
    // but it's true.

    // Solution is to double-check side before returning the player:
    return (ctx.side.isClient() ? mc.thePlayer : super.getPlayerEntity(ctx));
  }

  // From CoolAlias tutorial
  @Override
  public IThreadListener getThreadFromContext(MessageContext ctx) {
    return (ctx.side.isClient() ? mc : super.getThreadFromContext(ctx));
  }
}