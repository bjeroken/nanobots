package com.bjeroken.nanobots.common.testing;

import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class TestItem extends Item {
  public TestItem() {
    setCreativeTab(Refs.TAB);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
    if (worldIn.isRemote)
      return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);

    ItemStackHandler handler = new ItemStackHandler(5);
    ItemStackHandler handler2 = new ItemStackHandler(5);
    IItemHandler joined = new CombinedInvWrapper(handler, handler2);

    handler.setStackInSlot(0, new ItemStack(Blocks.stone));
    handler.setStackInSlot(1, new ItemStack(Blocks.grass));
    handler.setStackInSlot(2, new ItemStack(Blocks.dirt));
    handler.setStackInSlot(3, new ItemStack(Blocks.glass));
    handler.setStackInSlot(4, new ItemStack(Blocks.sand));

    handler2.setStackInSlot(0, new ItemStack(Blocks.slime_block));
    handler2.setStackInSlot(1, new ItemStack(Blocks.tnt));
    handler2.setStackInSlot(2, new ItemStack(Blocks.planks));
    handler2.setStackInSlot(3, new ItemStack(Blocks.log));
    handler2.setStackInSlot(4, new ItemStack(Blocks.diamond_block));

    for (int i = 0; i < handler.getSlots(); i++) {
      System.out.println("Expected 1: " + handler.getStackInSlot(i));
    }

    for (int i = 0; i < handler2.getSlots(); i++) {
      System.out.println("Expected 2: " + handler2.getStackInSlot(i));
    }

    for (int i = 0; i < joined.getSlots(); i++) {
      System.out.println("Joined: " + joined.getStackInSlot(i));
    }

    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
  }
}