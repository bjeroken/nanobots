package com.bjeroken.nanobots.common.network.bidirectional;
import java.io.IOException;

import com.bjeroken.nanobots.Nanobots;
import com.bjeroken.nanobots.common.network.AbstractMessage;
import com.bjeroken.nanobots.common.util.ModLogger;
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
public class OpenGuiMessage extends AbstractMessage<OpenGuiMessage> {
    // this will store the id of the gui to open
    private int id;

    // The basic, no-argument constructor MUST be included to use the new
    // automated handling
    public OpenGuiMessage() {
    }

    // if there are any class fields, be sure to provide a constructor that
    // allows
    // for them to be initialized, and use that constructor when sending the
    // packet
    public OpenGuiMessage(int id) {
      ModLogger.log("created gui message");
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
        // using the message instance gives access to 'this.id'
      ModLogger.log("process gui message");
      if(side.isClient())
        player.openGui(Nanobots.instance, this.id, player.worldObj, 0, 0, 0);
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