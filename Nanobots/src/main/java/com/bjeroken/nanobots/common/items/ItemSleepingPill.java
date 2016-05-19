package com.bjeroken.nanobots.common.items;

import com.bjeroken.nanobots.common.capabilities.extrasleep.IExtraSleeping;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/*
 * Adapted from https://github.com/MinecraftForge/MinecraftForge/blob/1.9/src/test/java/net/minecraftforge/test/NoBedSleepingTest.java
 */
public class ItemSleepingPill extends ItemNanobotBase {

  public ItemSleepingPill(String name) {
    super(name);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
    if (!world.isRemote) {
      final EnumStatus result = player.trySleep(player.getPosition());
      if (result == EnumStatus.OK) {
        final IExtraSleeping sleep = player.getCapability(Refs.SLEEP_CAP, null);
        if (sleep != null)
          sleep.setSleeping(true);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
      } else if(result == EnumStatus.NOT_POSSIBLE_NOW){
        player.addChatComponentMessage(new TextComponentString("nanobots.sleep.notpossiblenow"));
      } else if(result == EnumStatus.NOT_POSSIBLE_HERE){
        player.addChatComponentMessage(new TextComponentString("nanobots.sleep.notpossiblehere"));
      } else if(result == EnumStatus.NOT_SAFE){
        player.addChatComponentMessage(new TextComponentString("nanobots.sleep.notsafe"));
      }else if(result == EnumStatus.OTHER_PROBLEM){
        player.addChatComponentMessage(new TextComponentString("nanobots.sleep.otherproblem"));
      }
    }
    return ActionResult.newResult(EnumActionResult.PASS, stack);
  }
}