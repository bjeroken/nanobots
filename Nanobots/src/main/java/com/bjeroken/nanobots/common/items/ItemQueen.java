package com.bjeroken.nanobots.common.items;

import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.NanobotStatus;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemQueen extends ItemNanobotBase {
  private boolean isActive;

  public ItemQueen(String name, boolean isActive) {
    super(name);
    this.isActive = isActive;
  }

  @Override
  public boolean hasEffect(ItemStack stack) {
    return isActive;
  }

  public void setActive() {
    isActive = true;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemstack, World world, EntityPlayer player, EnumHand hand) {
    if (!world.isRemote
    // && itemstack.getItem() == AXPItems.nanobot_queen_active
    ) {
      INanobotCapability props = Refs.getNanobotCap(player);
      switch (props.getNanobotStatus()) {
      case active:
        world.createExplosion(player, player.posX, player.posY - 1, player.posZ, 1, true);
        player.addChatComponentMessage(new TextComponentTranslation("queen.unhappy", new Object[0]));
        break;
      case none:
        player.addChatComponentMessage(new TextComponentTranslation("queen.happy", new Object[0]));
        player.addChatComponentMessage(new TextComponentTranslation("queen.sting", new Object[0]));
        props.setNanobotStatus(NanobotStatus.active);
        break;
      case dead:
        player.addChatComponentMessage(new TextComponentTranslation("queen.takeover", new Object[0]));
        player.addChatComponentMessage(new TextComponentTranslation("queen.sting", new Object[0]));
        props.setNanobotStatus(NanobotStatus.active);
        break;
      default:
        break;
      }
      // player.addStat(AXPEventHandler.ach.get(AXPRef.ACTIVATED_NANOBOTS));

      // props.setIntValue(AXPRef.ACTIVATED_NANOBOTS, 1);
      // NanobotPlayerUtils.syncToPlayer(player, AXPRef.ACTIVATED_NANOBOTS, props.getIntValue(AXPRef.ACTIVATED_NANOBOTS));

      // NanobotPlayerUtils.syncToPlayer(player, AXPRef.CURRENT_FUEL, props.getIntValue(AXPRef.CURRENT_FUEL));

      itemstack.stackSize--;
    }
    return new ActionResult(EnumActionResult.PASS, itemstack);
  }
}