package com.bjeroken.nanobots.common.network.client;

import java.io.IOException;

import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.PlayerNanobotHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.FuelHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.IPerkCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.PerkHandler;
import com.bjeroken.nanobots.common.network.AbstractMessage.AbstractClientMessage;
import com.bjeroken.nanobots.common.network.PacketDispatcher;
import com.bjeroken.nanobots.common.stats.Perk;
import com.bjeroken.nanobots.common.util.EnumStats;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.Refs;
import com.google.common.base.Throwables;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

public class UpdateStatsMessage extends AbstractClientMessage<UpdateStatsMessage> {

  private NBTTagCompound data;

  public UpdateStatsMessage() {
    data = new NBTTagCompound();
    addStat(EnumStats.perks, new NBTTagList());
  }
  public UpdateStatsMessage(EnumStats stat, NBTBase value) {
    this();
    addStat(stat, value);
  }

  public UpdateStatsMessage addStat(EnumStats stat, NBTBase value) {
    data.setTag(stat.name(), value);
    return this;
  }

  public UpdateStatsMessage addPerk(Perk p) {
    NBTTagList list = (NBTTagList) data.getTag(EnumStats.perks.name());
    list.appendTag(new NBTTagString(p.getPerkName()));
    addStat(EnumStats.perks, list);
    return this;
  }

  /**
   * Merges this message with the given message, by merging the nbt. Returns this for chaining.
   */
  public UpdateStatsMessage merge(UpdateStatsMessage other) {
    data.merge(other.data);
    return this;
  }

  public void sendTo(EntityPlayer player) {
    // Don't sent empty data and don't send from client
    if (data.hasNoTags() || !(player instanceof EntityPlayerMP))
      return;
    PacketDispatcher.sendTo(this, (EntityPlayerMP) player);
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
    ModLogger.log("recieved NBT DATA");

    // Process data
    FuelHandler.processNBTData(Refs.getPlayerFuelCap(player), data);
    PlayerNanobotHandler.processNBTData(player, data);
    PerkHandler.processNBTData(player, data);
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