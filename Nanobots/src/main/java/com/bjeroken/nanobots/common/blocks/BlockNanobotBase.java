package com.bjeroken.nanobots.common.blocks;

import com.bjeroken.nanobots.Nanobots;
import com.bjeroken.nanobots.common.items.INanoTab;
import com.bjeroken.nanobots.common.items.IProxyReg;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockNanobotBase extends Block implements INanoTab, IProxyReg {

  public BlockNanobotBase(String name) {
    this(Material.rock, name);
  }

  public BlockNanobotBase(Material material, String name) {
    super(material);
    setTab(Refs.TAB);
    ResourceLocation loc = Refs.toResource(name);
    setUnlocalizedName(loc.toString());
    setRegistryName(loc);
  }

  @Override
  public void regProxy() {
    GameRegistry.register(new ItemBlock((Block) this).setRegistryName(this.getRegistryName()));
    Nanobots.proxy.regRender(Item.getItemFromBlock(this), getRegistryName());
  }

  /*
   * If I want subitems to not be in my tab, i just override this in subclass
   */
  @Override
  public void setTab(CreativeTabs tab) {
    setCreativeTab(tab);
  }

}
