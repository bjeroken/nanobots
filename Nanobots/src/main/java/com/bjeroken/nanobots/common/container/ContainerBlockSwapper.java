package com.bjeroken.nanobots.common.container;

import java.util.Set;

import com.bjeroken.nanobots.common.capabilities.inventory.CapabilityInventoryHandler;
import com.bjeroken.nanobots.common.capabilities.inventory.IInventoryHandler;
import com.bjeroken.nanobots.common.capabilities.inventory.InventoryHandler;
import com.bjeroken.nanobots.common.items.ModItems;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.ModUtils;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

public class ContainerBlockSwapper extends Container {
  private IInventoryHandler inventoryHandler;
  private PlayerInvWrapper playerInvWrapped;
  private InventoryPlayer playerInv;
  private ItemStack swapper;
  /** The current drag mode (0 : evenly split, 1 : one item by slot, 2 : not used ?) */
  private int dragMode = -1;
  /** The current drag event (0 : start, 1 : add slot : 2 : end) */
  private int dragEvent;
  private int numRows;
  private final Set<SlotItemHandler> dragSlots = Sets.<SlotItemHandler> newHashSet();

  public ContainerBlockSwapper(InventoryPlayer playerInv) {
    this.playerInv = playerInv;
    this.playerInvWrapped = new PlayerInvWrapper(playerInv);
    this.swapper = playerInv.getCurrentItem();
    this.inventoryHandler = new InventoryHandler(this.swapper);
    this.addSlotToContainer(new SlotItemHandler(this.inventoryHandler, 0, 8, 8) {
      
      @Override
      public boolean isItemValid(ItemStack stack) {
        if (stack == null)
          return false;
        if (!(stack.getItem() instanceof ItemBlock) || this.getItemHandler().getStackInSlot(0) != null)
          return false;
        Block block = ((ItemBlock) stack.getItem()).getBlock();
        return block.getMaterial(block.getStateFromMeta(stack.getMetadata())).isSolid() && super.isItemValid(stack);
      }

      @Override
      public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        super.onPickupFromSlot(playerIn, stack);
      }

      @Override
      public void onSlotChanged() {
        ContainerBlockSwapper.this.onCraftMatrixChanged(this.inventory);
        super.onSlotChanged();
      }

      @Override
      public ItemStack getStack() {
        return super.getStack();
      }

      @Override
      public int getItemStackLimit(ItemStack stack) {
        return 1;
      }
    });
    int rows = 1;
    for (int i = 1; i < this.inventoryHandler.getSlots(); i++) {
      this.addSlotToContainer(new SlotItemHandler(this.inventoryHandler, i, 8 + (i - 1) * 18, 28 + (rows-1) * 18){
        @Override
        public boolean isItemValid(ItemStack stack) {
          if(stack.getItem() == ModItems.blockReplacer)
            return false;
          return super.isItemValid(stack);
        }
      });
    }

    // Inventory slots
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlotToContainer(new SlotItemHandler(this.playerInvWrapped, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }
    // Hotbar slots
    for (int k = 0; k < 9; ++k) {
      this.addSlotToContainer(new SlotItemHandler(this.playerInvWrapped, k, 8 + k * 18, 142){
        @Override
        public boolean canTakeStack(EntityPlayer playerIn) {
//          if(ModUtils.equals(playerIn.getHeldItemMainhand(), getStack()))
//              return false;
          return super.canTakeStack(playerIn);
        }
      });
    }

  }
  /**
   * Take a stack from the specified inventory slot.
   */
  public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
    ItemStack itemstack = null;
    Slot slot = (Slot) this.inventorySlots.get(index);

    if (slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();

      if (index == 0) {
        if (!this.mergeItemStack(itemstack1, this.inventoryHandler.getSlots(), this.inventorySlots.size(), true)) {
          return null;
        }

        slot.onSlotChange(itemstack1, itemstack);
      } else if (index >= 1 && index < this.inventorySlots.size()) {
        if (!this.mergeItemStack(itemstack1, this.inventorySlots.size() - 9, this.inventorySlots.size(), false)) {
          return null;
        }
      } else if (!this.mergeItemStack(itemstack1, this.inventoryHandler.getSlots(), this.inventorySlots.size(), false)) {
        return null;
      }

      if (itemstack1.stackSize == 0) {
        slot.putStack((ItemStack) null);
      } else {
        slot.onSlotChanged();
      }

      if (itemstack1.stackSize == itemstack.stackSize) {
        return null;
      }

      slot.onPickupFromSlot(playerIn, itemstack1);
    }

    return itemstack;
  }
  
  @Override
  public void onContainerClosed(EntityPlayer playerIn) {
    this.inventoryHandler.writeToNBT();
//    swapper.setTagCompound(this.inventoryHandler.writeToNBT());
    super.onContainerClosed(playerIn);
  }


  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;
  }

}
