package com.bjeroken.nanobots.common.network.server;

import java.io.IOException;

import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.IPerkCapability;
import com.bjeroken.nanobots.common.network.AbstractMessage.AbstractServerMessage;
import com.bjeroken.nanobots.common.network.client.UpdateStatsMessage;
import com.bjeroken.nanobots.common.stats.EnumPerk;
import com.bjeroken.nanobots.common.stats.Perk;
import com.bjeroken.nanobots.common.stats.PerkList;
import com.bjeroken.nanobots.common.util.EnumStats;
import com.bjeroken.nanobots.common.util.Refs;
import com.google.common.base.Throwables;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 
 * A simple message telling the server that the client wants to open a GUI.
 * 
 */
public class PerkClickMessage extends AbstractServerMessage<PerkClickMessage> {
  // this will store the id of the gui to open
  private int id;

  // The basic, no-argument constructor MUST be included to use the new
  // automated handling
  public PerkClickMessage() {
  }

  // if there are any class fields, be sure to provide a constructor that
  // allows
  // for them to be initialized, and use that constructor when sending the
  // packet
  public PerkClickMessage(int id) {
    this.id = id;
  }

  @Override
  protected void read(PacketBuffer buffer) throws IOException {
    // basic Input/Output operations, very much like DataInputStream
    id = buffer.readInt();
  }

  @Override
  protected void write(PacketBuffer buffer) throws IOException {
    // basic Input/Output operations, very much like DataOutputStream
    buffer.writeInt(id);
  }

  @Override
  public void process(EntityPlayer player, Side side) {
    if (side.isClient())
      return;
    Perk p = PerkList.perkMap.get("perk." + EnumPerk.values()[id].name());
    if (p != null) {
      IPerkCapability pc = Refs.getPerkCap(player);
      if (pc.canAddPerk(player, p)) {
        // Activate perk and send change to client
        p.activatePerk(player, new UpdateStatsMessage()).sendTo(player);
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

}