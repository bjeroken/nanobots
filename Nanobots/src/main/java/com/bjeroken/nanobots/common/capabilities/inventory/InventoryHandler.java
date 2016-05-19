package com.bjeroken.nanobots.common.capabilities.inventory;

import com.bjeroken.nanobots.common.util.ModUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;

/*
 * Adapted from ItemStackHandler.class
 */
public class InventoryHandler implements IInventoryHandler, INBTSerializable<NBTTagCompound> {
  protected ItemStack[] stacks;
  private static int defaultSize = 10;
  private static InventoryHandlerStorage storage = new InventoryHandlerStorage();
  private ItemStack InventoryItem;

  public InventoryHandler() {
    this(defaultSize);
  }

  public InventoryHandler(ItemStack stack) {
    this(ModUtils.getTags(stack));
    setInventoryStack(stack);
  }

  public InventoryHandler(NBTTagCompound nbt) {
    setSizeFromNBT(nbt);
    readInventoryFromNBT(nbt);
  }

  public InventoryHandler(int size) {
    stacks = new ItemStack[size];
  }

  public InventoryHandler(ItemStack[] stacks) {
    this.stacks = stacks;
  }

  public void setSizeFromNBT(NBTTagCompound nbt) {
    setSize(nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : defaultSize);
  }

  public void readInventoryFromNBT(NBTTagCompound nbt) {
    if (nbt.hasKey("Inventory"))
      readFromNBT(nbt);
  }

  @Override
  public void setInventoryStack(ItemStack stack) {
    this.InventoryItem = stack;
  }

  public void setSize(int size) {
    stacks = new ItemStack[size];
  }

  @Override
  public void setStackInSlot(int slot, ItemStack stack) {
    validateSlotIndex(slot);
    if (ItemStack.areItemStacksEqual(this.stacks[slot], stack))
      return;
    this.stacks[slot] = stack;
    onContentsChanged(slot);
  }

  @Override
  public int getSlots() {
    return stacks.length;
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    validateSlotIndex(slot);
    return this.stacks[slot];
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    if (stack == null || stack.stackSize == 0)
      return null;

    validateSlotIndex(slot);

    ItemStack existing = this.stacks[slot];

    int limit = getStackLimit(slot, stack);

    if (existing != null) {
      if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
        return stack;

      limit -= existing.stackSize;
    }

    if (limit <= 0)
      return stack;

    boolean reachedLimit = stack.stackSize > limit;

    if (!simulate) {
      if (existing == null) {
        this.stacks[slot] = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
      } else {
        existing.stackSize += reachedLimit ? limit : stack.stackSize;
      }
      onContentsChanged(slot);
    }

    return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - limit) : null;
  }

  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (amount == 0)
      return null;

    validateSlotIndex(slot);

    ItemStack existing = this.stacks[slot];

    if (existing == null)
      return null;

    int toExtract = Math.min(amount, existing.getMaxStackSize());

    if (existing.stackSize <= toExtract) {
      if (!simulate) {
        this.stacks[slot] = null;
        onContentsChanged(slot);
      }
      return existing;
    } else {
      if (!simulate) {
        this.stacks[slot] = ItemHandlerHelper.copyStackWithSize(existing, existing.stackSize - toExtract);
        onContentsChanged(slot);
      }

      return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
    }
  }

  protected int getStackLimit(int slot, ItemStack stack) {
    return stack.getMaxStackSize();
  }

  @Override
  public NBTTagCompound serializeNBT() {
    if (this.InventoryItem != null) {
      this.InventoryItem.setTagCompound(storage.writeNBT(CapabilityInventoryHandler.CAP, this, null));
    }

    return storage.writeNBT(CapabilityInventoryHandler.CAP, this, null);
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    if (this.InventoryItem != null) {
      nbt = ModUtils.getTags(InventoryItem);
    }
    if (nbt != null)
      storage.readNBT(CapabilityInventoryHandler.CAP, this, null, nbt);
    onLoad();
  }

  protected void validateSlotIndex(int slot) {
    if (slot < 0 || slot >= stacks.length)
      throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.length + ")");
  }

  protected void onLoad() {
  }

  protected void onContentsChanged(int slot) {
  }

  @Override
  public NBTTagCompound writeToNBT() {
    return serializeNBT();
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    deserializeNBT(tags);
  }
}