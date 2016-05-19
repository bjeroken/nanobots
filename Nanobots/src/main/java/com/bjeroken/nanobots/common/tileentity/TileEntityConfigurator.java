package com.bjeroken.nanobots.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;

public class TileEntityConfigurator extends TileEntity implements IInventory {

  private String inventoryTitle;
  protected int slotsCount;
  protected ItemStack[] inventoryContents;
  private boolean hasCustomName;
  
  public TileEntityConfigurator() {
    this.inventoryTitle = "nanobots.configurator";
    this.hasCustomName = true;
    this.slotsCount = 1;
    this.inventoryContents = new ItemStack[this.slotsCount];
  }

  @Override
  public void writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    if (this.hasCustomName()) {
      compound.setString("CustomName", this.inventoryTitle);
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    if (compound.hasKey("CustomName", 8)) {
      this.inventoryTitle = compound.getString("CustomName");
    }
  }

  /**
   * Get the name of this object. For players this returns their username
   */
  @Override
  public String getName() {
    return this.inventoryTitle;
  }

  /**
   * Returns true if this thing is named
   */
  @Override
  public boolean hasCustomName() {
    return this.hasCustomName;
  }

  public void setCustomName(String customNameIn) {
    this.hasCustomName = true;
    this.inventoryTitle = customNameIn;
  }
  

  /**
   * Get the formatted ChatComponent that will be used for the sender's username in chat
   */
  @Override
  public ITextComponent getDisplayName() {
    return (ITextComponent) (this.hasCustomName() ? new TextComponentTranslation(this.getName())
        : new TextComponentTranslation(this.getName(), new Object[0]));
  }

  @Override
  public int getSizeInventory() {
    return slotsCount;
  }

  @Override
  public ItemStack getStackInSlot(int index) {
    return index >= 0 && index < this.inventoryContents.length ? this.inventoryContents[index] : null;

  }

  @Override
  public ItemStack decrStackSize(int index, int count) {

    if (this.inventoryContents[index] != null) {
      if (this.inventoryContents[index].stackSize <= count) {
        ItemStack itemstack1 = this.inventoryContents[index];
        this.inventoryContents[index] = null;
        this.markDirty();
        return itemstack1;
      } else {
        ItemStack itemstack = this.inventoryContents[index].splitStack(count);

        if (this.inventoryContents[index].stackSize == 0)
          this.inventoryContents[index] = null;

        this.markDirty();
        return itemstack;
      }
    } else {
      return null;
    }
  }

  @Override
  public ItemStack removeStackFromSlot(int index) {
    if (this.inventoryContents[index] != null) {
      ItemStack itemstack = this.inventoryContents[index];
      this.inventoryContents[index] = null;
      return itemstack;
    } else {
      return null;
    }
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    this.inventoryContents[index] = stack;

    if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
      stack.stackSize = this.getInventoryStackLimit();
    }
    this.markDirty();

  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer player) {
    return true;
  }

  @Override
  public void openInventory(EntityPlayer player) {
  }

  @Override
  public void closeInventory(EntityPlayer player) {
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return true;
  }
  @Override
  public void clear() {
    for (int i = 0; i < this.inventoryContents.length; ++i) {
      this.inventoryContents[i] = null;
    }
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound nbtTagCompound = new NBTTagCompound();
    writeToNBT(nbtTagCompound);
    int metadata = getBlockMetadata();
    return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
  }
  
  @Override
  public int getField(int id) {
    return 0;
  }

  @Override
  public void setField(int id, int value) {
  }

  @Override
  public int getFieldCount() {
    return 0;
  }

}
