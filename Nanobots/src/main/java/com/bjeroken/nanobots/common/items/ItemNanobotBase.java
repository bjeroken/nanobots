package com.bjeroken.nanobots.common.items;

import com.bjeroken.nanobots.Nanobots;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemNanobotBase extends Item implements INanoTab, IProxyReg{
  
  public ItemNanobotBase(String name) {
    setTab(Refs.TAB);
    ResourceLocation loc = Refs.toResource(name);
    setUnlocalizedName(loc.toString());
    setRegistryName(loc);
  }
  
  /*
   * If I want subitems to not be in my tab, i just override this in subclass
   */
  @Override
  public void setTab(CreativeTabs tab) {
    setCreativeTab(tab);
  }
  
  @Override
  public void regProxy(){
      Nanobots.proxy.regRender(this, getRegistryName());
  }
}
