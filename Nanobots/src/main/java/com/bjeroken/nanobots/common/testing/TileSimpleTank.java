package com.bjeroken.nanobots.common.testing;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileSimpleTank extends TileEntity implements IFluidHandler {
  FluidTank tank = new FluidTank(4000);

  @Override
  public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
    int filled = tank.fill(resource, doFill);
    if (doFill && filled > 0) {
      IBlockState state = worldObj.getBlockState(pos);
      worldObj.notifyBlockUpdate(pos, state, state, 8); // TODO check flag
    }
    return filled;
  }

  @Override
  public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
    // not used in this test
    return null;
  }

  @Override
  public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
    FluidStack drained = tank.drain(maxDrain, doDrain);
    if (doDrain && drained != null) {
      IBlockState state = worldObj.getBlockState(pos);
      worldObj.notifyBlockUpdate(pos, state, state, 8); // TODO check flag
    }
    return drained;
  }

  @Override
  public boolean canFill(EnumFacing from, Fluid fluid) {
    return tank.getFluidAmount() == 0 || (tank.getFluid().getFluid() == fluid && tank.getFluidAmount() < tank.getCapacity());
  }

  @Override
  public boolean canDrain(EnumFacing from, Fluid fluid) {
    return tank.getFluidAmount() > 0;
  }

  @Override
  public FluidTankInfo[] getTankInfo(EnumFacing from) {
    return new FluidTankInfo[] { new FluidTankInfo(tank) };
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    tank.readFromNBT(tags);
  }

  @Override
  public void writeToNBT(NBTTagCompound tags) {
    super.writeToNBT(tags);
    tank.writeToNBT(tags);
  }

  @Override
  public Packet<?> getDescriptionPacket() {
    NBTTagCompound tag = new NBTTagCompound();
    writeToNBT(tag);
    return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    super.onDataPacket(net, pkt);
    readFromNBT(pkt.getNbtCompound());
  }
}