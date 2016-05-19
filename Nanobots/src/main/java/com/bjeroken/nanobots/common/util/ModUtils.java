package com.bjeroken.nanobots.common.util;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModUtils {

  //Adapted from net.minecraft.block.Block.createStackedBlock(IBlockState)
  public static final ItemStack createStackedBlock(IBlockState state) {
    Item item = Item.getItemFromBlock(state.getBlock());
    if (item == null) {
      return null;
    } else {
      int i = 0;
      if (item.getHasSubtypes()) {
        i = state.getBlock().getMetaFromState(state);
      }
      return new ItemStack(item, 1, i);
    }
  }
  public static final void replaceBlock(World world, BlockPos pos, IBlockState newBlock) {
    world.setBlockToAir(pos);
    world.setBlockState(pos, newBlock, 3);
  }
  public static int slotHasItem(List<Slot> slots, ItemStack item){
    int index = -1;
    for(Slot slot : slots){
      if (slot.getHasStack() && ModUtils.equals(slot.getStack(), item)) {
        index = slot.getSlotIndex();
        break;
      }
    }
    return index;
  }
  public static boolean equals(ItemStack one, ItemStack two){
    if(one == null || two == null)
      return false;
    return one.getItem() == two.getItem() && one.getMetadata() == two.getMetadata();
  }
  //Method to avoid getting a null tag
  public static NBTTagCompound getTags(ItemStack stack){
    return stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
  }
}
