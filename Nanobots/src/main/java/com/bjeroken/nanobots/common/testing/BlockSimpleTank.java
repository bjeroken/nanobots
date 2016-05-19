package com.bjeroken.nanobots.common.testing;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;

// simple tank copied from tinkers construct
public class BlockSimpleTank extends BlockContainer {

  public BlockSimpleTank() {
    super(Material.rock);
    setCreativeTab(CreativeTabs.tabMisc);
    setUnlocalizedName("simpletank");
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileSimpleTank();
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
      EnumFacing side, float hitX, float hitY, float hitZ) {
    TileEntity te = worldIn.getTileEntity(pos);
    if (!(te instanceof IFluidHandler)) {
      return false;
    }
    IFluidHandler tank = (IFluidHandler) te;
    side = side.getOpposite();

    if (heldItem == null) {
      sendText(playerIn, tank, side);
      return false;
    }

    // do the thing with the tank and the buckets
    if (FluidUtil.interactWithTank(heldItem, playerIn, tank, side)) {
      return true;
    } else {
      sendText(playerIn, tank, side);
    }

    // prevent interaction of the item if it's a fluidcontainer. Prevents placing liquids when interacting with the tank
    return FluidContainerRegistry.isFilledContainer(heldItem) || heldItem.getItem() instanceof IFluidContainerItem;
  }

  private void sendText(EntityPlayer player, IFluidHandler tank, EnumFacing side) {
    if (player.worldObj.isRemote) {
      String text;
      if (tank.getTankInfo(side).length > 0 && tank.getTankInfo(side)[0] != null && tank.getTankInfo(side)[0].fluid != null) {
        text = tank.getTankInfo(side)[0].fluid.amount + "x " + tank.getTankInfo(side)[0].fluid.getLocalizedName();
      } else {
        text = "empty";
      }
      player.addChatMessage(new TextComponentString(text));
    }
  }
}