package com.bjeroken.nanobots.common.network.client;

import java.io.IOException;

import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.network.AbstractMessage;
import com.bjeroken.nanobots.common.network.PacketDispatcher;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.Refs;
import com.google.common.base.Throwables;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;

public class UpdateWornStatsMessage extends AbstractMessage<UpdateWornStatsMessage> {
  private int id, value;

  public UpdateWornStatsMessage() {
  }

  public UpdateWornStatsMessage(int id, int value) {
    ModLogger.log("created update stats message: id: " + id + ", value: " + value);
    this.id = id;
    this.value = value;
  }

  @Override
  protected void read(PacketBuffer buffer) throws IOException {
    // basic Input/Output operations, very much like DataInputStream
    id = buffer.readInt();
    value = buffer.readInt();
  }

  @Override
  protected void write(PacketBuffer buffer) throws IOException {
    // basic Input/Output operations, very much like DataOutputStream
    buffer.writeInt(id);
    buffer.writeInt(value);
  }

  @Override
  public void process(EntityPlayer player, Side side) {
    if (side.isClient()) {
      ModLogger.log("process update worn message: id: " + id + ", value: " + value);
      IItemHandler armor = new PlayerArmorInvWrapper(player.inventory);
      ItemStack stack = armor.getStackInSlot(id);
      if (stack != null && stack.hasCapability(Refs.FUEL_CAP, null)) {
        IFuelCapability fuel = stack.getCapability(Refs.FUEL_CAP, null);
        fuel.setCurrentFuel(value);
        stack.setTagCompound(fuel.writeNBT());
      }
    }
  }

  @Override
  public void fromBytes(ByteBuf buffer) {
    try {
      read(new PacketBuffer(buffer));
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  @Override
  public void toBytes(ByteBuf buffer) {
    try {
      write(new PacketBuffer(buffer));
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  public void sendTo(EntityPlayer player) {
    if (player instanceof EntityPlayerMP)
      PacketDispatcher.sendTo(this, (EntityPlayerMP) player);
  }
}
