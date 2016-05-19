package com.bjeroken.nanobots.common.items;

import java.util.ArrayList;
import java.util.List;

import com.bjeroken.nanobots.Nanobots;
import com.bjeroken.nanobots.common.capabilities.inventory.IInventoryHandler;
import com.bjeroken.nanobots.common.capabilities.inventory.InventoryHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.FuelUtils;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.ModUtils;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemBlockReplacer extends ItemNanobotBase {

  public ItemBlockReplacer(String name) {
    super(name);
    setMaxStackSize(1);
  }

  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumHand hand,
      EnumFacing facing, float hitX, float hitY, float hitZ) {
    EnumActionResult result = EnumActionResult.FAIL;
    if (world.isRemote)
      return result;
    if (world.getTileEntity(pos) != null || world.getBlockState(pos).getBlock() == Blocks.bedrock
        || !world.getBlockState(pos).getBlock().isNormalCube(world.getBlockState(pos), world, pos)) {
      playerIn.addChatComponentMessage(new TextComponentTranslation("itemblockswapper.invalid"));
      return result;
    }
    IBlockState toReplace = world.getBlockState(pos);

    IInventoryHandler handler = new InventoryHandler(stack);
    if (playerIn.isSneaking()) {
      playerIn.openGui(Nanobots.instance, Refs.GUI_SWAPPER, world, 0, 0, 0);
    } else {

      if (handler.getStackInSlot(0) != null) {
        IBlockState newBlock = Block.getBlockFromItem(handler.getStackInSlot(0).getItem())
            .getStateFromMeta(handler.getStackInSlot(0).getMetadata());
        float hardness = toReplace.getBlock().getBlockHardness(toReplace, world, pos);

        ModLogger.log("hardness: " + hardness);

        List<BlockPos> valids = new ArrayList<BlockPos>();
        int itemsInInventory = 0;
        int sourceSlotIndex = -1;
        ItemStack newStack = new ItemStack(Item.getItemFromBlock(newBlock.getBlock()), 1,
            newBlock.getBlock().getMetaFromState(newBlock));
        for (int i = handler.getSlots() - 1; i > 0; i--) {
          if (ModUtils.equals(handler.getStackInSlot(i), newStack)) {
            sourceSlotIndex = i;
            break;
          }
        }

        // TODO: pull from main inv if not found in bag
        // if(sourceSlotIndex < 0){
        // ContainerPlayer cont = new ContainerPlayer(playerIn.inventory, false,
        // playerIn);
        // sourceSlotIndex = ModUtils.slotHasItem(cont.inventorySlots,
        // newStack);
        // }
        if (sourceSlotIndex < 0) {
          playerIn.addChatComponentMessage(new TextComponentTranslation("itemblockswapper.noitemininventory"));
        } else if (newBlock == toReplace) {
        } else {
          toReplace.getBlock().harvestBlock(world, playerIn, pos, toReplace, world.getTileEntity(pos), stack);
          ModUtils.replaceBlock(world, pos, newBlock);
          handler.extractItem(sourceSlotIndex, 1, false);
          result = EnumActionResult.PASS;
          stack.setTagCompound(handler.writeToNBT());
        }
      } else {
        playerIn.addChatComponentMessage(new TextComponentTranslation("itemblockswapper.nosourceregistered"));
      }
    }
    return result;
  }
  // @Override
  // public boolean canItemEditBlocks() {
  // return true;
  // }

  @Override
  public String getHighlightTip(ItemStack stack, String displayName) {
    IInventoryHandler handler = new InventoryHandler(stack);
    if (handler.getStackInSlot(0) == null) {
      return super.getHighlightTip(stack, displayName);
    } else {
      return super.getHighlightTip(stack,
          displayName + " " + handler.getStackInSlot(0).getChatComponent().getFormattedText());
    }
  }

  @Override
  public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
    IInventoryHandler handler = new InventoryHandler(oldStack);
    IInventoryHandler handler1 = new InventoryHandler(newStack);
    if (handler.getStackInSlot(0) == null || handler1.getStackInSlot(0) == null)
      return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    return !ModUtils.equals(handler.getStackInSlot(0), handler1.getStackInSlot(0));
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    IInventoryHandler handler = new InventoryHandler(stack);
    if (handler.getStackInSlot(0) == null) {
      tooltip.add(new TextComponentTranslation("itemblockswapper.empty").getFormattedText());
    } else {
      tooltip.add(new TextComponentTranslation("itemblockswapper.swapsto").getFormattedText());
      tooltip.add(handler.getStackInSlot(0).getDisplayName());
    }
  }

}
