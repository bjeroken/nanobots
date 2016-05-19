package com.bjeroken.nanobots.common.items;

import com.bjeroken.nanobots.Nanobots;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public interface IProxyReg {

  /**
   * 
   * @Items <code>
   * Nanobots.proxy.regRender(this, getRegistryName());</code>
   * 
   * @Blocks<code>
   * GameRegistry.register(new ItemBlock((Block) this).setRegistryName(this.getRegistryName()));
   * <br>
   * Nanobots.proxy.regRender(Item.getItemFromBlock(this), getRegistryName());</code>
   */
  public default void regProxy() {

  }
}
