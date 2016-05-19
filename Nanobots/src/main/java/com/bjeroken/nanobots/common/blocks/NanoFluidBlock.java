package com.bjeroken.nanobots.common.blocks;

import com.bjeroken.nanobots.Nanobots;
import com.bjeroken.nanobots.common.items.INanoTab;
import com.bjeroken.nanobots.common.items.IProxyReg;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class NanoFluidBlock extends BlockFluidClassic implements INanoTab, IProxyReg {
  public static final String name = "fluid_nano_block";

  public NanoFluidBlock(Fluid fluid, Material material) {
    super(fluid, material);
    ResourceLocation loc = Refs.toResource(name);
    setUnlocalizedName(loc.toString());
    setRegistryName(loc);
    setTab(Refs.TAB);
  }

  @Override
  public void setTab(CreativeTabs tab) {
    setCreativeTab(tab);
  }

  @Override
  public void regProxy() {
    GameRegistry.register(new ItemBlock((Block) this).setRegistryName(this.getRegistryName()));
    Nanobots.proxy.regFluidRender(this, new ModelResourceLocation(this.getRegistryName(), "fluid"));
  }
}