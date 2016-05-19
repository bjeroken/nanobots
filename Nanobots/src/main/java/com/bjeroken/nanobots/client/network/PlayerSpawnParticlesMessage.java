package com.bjeroken.nanobots.client.network;

import java.io.IOException;

import com.bjeroken.nanobots.common.network.AbstractMessage.AbstractClientMessage;
import com.google.common.base.Throwables;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

public class PlayerSpawnParticlesMessage extends AbstractClientMessage<PlayerSpawnParticlesMessage> {

  private NBTTagCompound data;

  public PlayerSpawnParticlesMessage() {
  }

  public PlayerSpawnParticlesMessage(BlockPos from, BlockPos to) {
    data = new NBTTagCompound();
    data.setLong("from", from.toLong());
    data.setLong("to", to.toLong());
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
    BlockPos fromPos = BlockPos.fromLong(data.getLong("from"));
    BlockPos toPos = BlockPos.fromLong(data.getLong("to"));
    // TODO: actually create particles
    // AXPEffectRender.getInstance().createParticles(player.getEntityWorld(),
    // fromPos, toPos, false);
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