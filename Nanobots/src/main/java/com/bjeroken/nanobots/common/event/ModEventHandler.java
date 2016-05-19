package com.bjeroken.nanobots.common.event;

import com.bjeroken.nanobots.common.items.ModItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class ModEventHandler {
  @SubscribeEvent
  public void onHarvest(BlockEvent.HarvestDropsEvent event) {
    if (event.getHarvester() != null && event.getHarvester().getHeldItemMainhand() != null
        && event.getHarvester().getHeldItemMainhand().getItem() == ModItems.blockReplacer) {
      for (ItemStack stack : event.getDrops()) {
        ItemHandlerHelper.giveItemToPlayer(event.getHarvester(), stack);
      }
      event.getDrops().clear();
    }
  }

  /*
   * Just to test nano
   */
  // @SubscribeEvent
  // public void onJump(LivingJumpEvent event) {
  // if (!(event.getEntity() instanceof EntityPlayerMP))
  // return;
  // EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
  // if(player.isOnLadder())
  // return;
  // IFuelCapability fuelCap = Refs.getPlayerFuelCap(player);
  // boolean changed = false;
  //
  // UpdateStatsMessage stats = new UpdateStatsMessage();
  //
  // if (fuelCap.getCurrentFuel() < fuelCap.getMaxFuel()) {
  // fuelCap.setCurrentFuel(fuelCap.getCurrentFuel() + 1);
  // player.addChatComponentMessage(new TextComponentString("Current nano: " +
  // fuelCap.getCurrentFuel()));
  // stats.addStat(EnumStats.currentfuel, new
  // NBTTagInt(fuelCap.getCurrentFuel()));
  // changed = true;
  // } else if (fuelCap.getCurrentFuel() >= fuelCap.getMaxFuel()) {
  // fuelCap.setCurrentFuel(0);
  // player.addChatComponentMessage(new TextComponentString("Nano reset"));
  // stats.addStat(EnumStats.currentfuel, new
  // NBTTagInt(fuelCap.getCurrentFuel()));
  // changed = true;
  // }
  //
  // INanobotCapability nanoCap = Refs.getNanobotCap(player);
  //
  // if(fuelCap.getCurrentFuel() % 2 == 0){
  // nanoCap.setStepHeight(player, 1f);
  // stats.addStat(EnumStats.stepheight, new NBTTagFloat(1f));
  // } else {
  // nanoCap.setStepHeight(player, 0.6f);
  // stats.addStat(EnumStats.stepheight, new NBTTagFloat(0.6f));
  // }
  //
  // if (changed){
  //
  // stats.sendTo(player);
  // }
  // }
  @SubscribeEvent
  public void onBucketFill(FillBucketEvent event) {
    IBlockState state = event.getWorld().getBlockState(event.getTarget().getBlockPos());
    if (state.getBlock() instanceof IFluidBlock) {
      Fluid fluid = ((IFluidBlock) state.getBlock()).getFluid();
      FluidStack fs = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);

      ItemStack filled = FluidContainerRegistry.fillFluidContainer(fs, event.getEmptyBucket());
      if (filled != null) {
        event.setFilledBucket(filled);
        event.setResult(Result.ALLOW);
      }
    }
  }
}
