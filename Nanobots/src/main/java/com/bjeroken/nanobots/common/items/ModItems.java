package com.bjeroken.nanobots.common.items;

import java.util.LinkedList;
import java.util.List;

import com.bjeroken.nanobots.common.blocks.BlockNanobotBase;
import com.bjeroken.nanobots.common.blocks.NanoFluid;
import com.bjeroken.nanobots.common.blocks.NanoFluidBlock;
import com.bjeroken.nanobots.common.testing.DynBottle;
import com.bjeroken.nanobots.common.testing.DynBucket;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public class ModItems {

  public static Item sleepingPill;

  public static Item perk, trophy, blockReplacer, debugItem;
  public static Block baseBlock;

  public static Fluid fluidNano;

  public static Block fluidNanoBlock;
  public static final Fluid milkFluid = new Fluid("milk", new ResourceLocation(Refs.MODID, "blocks/milk_still"),
      new ResourceLocation(Refs.MODID, "blocks/milk_flow"));

  public static List<ItemNanobotArmor> nanobotArmors;
  public static ItemArmor[] armors = { Items.leather_boots, Items.leather_leggings, Items.leather_chestplate,
      Items.leather_helmet, Items.chainmail_boots, Items.chainmail_leggings, Items.chainmail_chestplate,
      Items.chainmail_helmet, Items.iron_boots, Items.iron_leggings, Items.iron_chestplate, Items.iron_helmet,
      Items.diamond_boots, Items.diamond_leggings, Items.diamond_chestplate, Items.diamond_helmet, Items.golden_boots,
      Items.golden_leggings, Items.golden_chestplate, Items.golden_helmet };

  public static final Item dynBucket = (Item) new DynBucket();
  public static final Item dynBottle = new DynBottle();
  public static final ResourceLocation simpleTankName = new ResourceLocation(Refs.MODID, "simpletank");
  public static final ResourceLocation testItemName = new ResourceLocation(Refs.MODID, "testitem");

  private static void initItems() {

    sleepingPill = reg(new ItemSleepingPill("sleeping_pill"));

    perk = reg(new ItemPerk("perk"));

    trophy = reg(new ItemLevelTrophy("trophy"));

    blockReplacer = reg(new ItemBlockReplacer("block_replacer"));

    debugItem = reg(new ItemDebug("nanobot_debugger"));

    nanobotArmors = new LinkedList<ItemNanobotArmor>();
    for (ItemArmor armor : armors)
      nanobotArmors.add(reg(new ItemNanobotArmor(armor)));

  }

  private static void initBlocks() {

    baseBlock = reg(new BlockNanobotBase("base_block"));

    fluidNano = new NanoFluid().setDensity(500).setViscosity(500);
    FluidRegistry.registerFluid(fluidNano);
    fluidNanoBlock = reg(new NanoFluidBlock(fluidNano, Material.water));
    fluidNano.setBlock(fluidNanoBlock);
  }

  // Short wrapper for register, also registers render in proxy
  private static <K extends IForgeRegistryEntry<?>> K reg(K object) {
    if (object instanceof IProxyReg)
      ((IProxyReg) object).regProxy();
    return GameRegistry.register(object);
  }

  public static void init() {
    initItems();
    initBlocks();
  }
}
