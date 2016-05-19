package com.bjeroken.nanobots.common.blocks;

import com.bjeroken.nanobots.common.tileentity.TileEntityConfigurator;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockConfigurator extends BlockNanobotBase implements ITileEntityProvider {

  public BlockConfigurator(String name) {
    super(name);
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
      ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    // TODO Auto-generated method stub
    return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
  }

  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    // TODO Auto-generated method stub
    return new TileEntityConfigurator();
  }
}
