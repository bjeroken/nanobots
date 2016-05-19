package com.bjeroken.nanobots.common.network.client;

import java.io.IOException;

import com.bjeroken.nanobots.common.capabilities.nanobots.PlayerNanobotHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.FuelHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.PerkHandler;
import com.bjeroken.nanobots.common.network.AbstractMessage.AbstractClientMessage;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.Refs;
import com.google.common.base.Throwables;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class FullSyncStatsMessage extends AbstractClientMessage<FullSyncStatsMessage> {

  private NBTTagCompound data;

  public FullSyncStatsMessage() {
  }

  public FullSyncStatsMessage(NBTTagCompound tags) {
    data = tags;
  }

  @Override
  protected void read(PacketBuffer buffer) throws IOException {
    data = buffer.readNBTTagCompoundFromBuffer();
  }

  @Override
  protected void write(PacketBuffer buffer) throws IOException {
    buffer.writeNBTTagCompoundToBuffer(data);
  }

  @Override
  public void process(EntityPlayer player, Side side) {
    if (side.isServer()) // Probably unnecessary but here it is anyways.
      return;
    ModLogger.log("Recieved full sync from server");
    PlayerNanobotHandler.readNBT(Refs.NANOBOT_CAP, Refs.getNanobotCap(player), data);
    PerkHandler.readNBT(Refs.PERK_CAP, Refs.getPerkCap(player), data);
    FuelHandler.readNBT(Refs.FUEL_CAP, Refs.getPlayerFuelCap(player), data);
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
}