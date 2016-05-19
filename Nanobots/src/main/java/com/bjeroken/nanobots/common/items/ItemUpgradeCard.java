package com.bjeroken.nanobots.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemUpgradeCard extends ItemNanobotBase {

  public ItemUpgradeCard(String name) {
    super(name);
    this.maxStackSize = 1;
  }

  @Override
  public boolean hasEffect(ItemStack stack) {
    if (stack.hasTagCompound()) {
      if (!stack.getTagCompound().hasNoTags())
        return true;
    }
    return false;
  }

  @Override
  public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
    itemStack.setTagCompound(new NBTTagCompound());
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player,
      EnumHand hand) {
    if (!player.worldObj.isRemote) {
      if (player.isSneaking()) {
        player.addChatMessage(new TextComponentString("Attuned to you."));
        setInfo(itemStackIn, player);
      }
    }
    return new ActionResult(EnumActionResult.PASS, itemStackIn);
  }

  private void setInfo(ItemStack itemStackIn, EntityPlayer player) {
//    NanobotPlayer props = NanobotPlayerUtils.get(player);
//    NBTTagCompound comp = props.getAttributes();
//    float processTimeReduction = 0F;
//    NBTTagCompound tags = itemStackIn.hasTagCompound() ? itemStackIn.getTagCompound() : new NBTTagCompound();
//    tags.setString("owner", player.getName());
//    tags.setBoolean("active", true);
//    if (props.hasPerkUnlocked(PerkList.fuelEff2)) {
//      tags.setInteger("fuelEff", 2);
//      processTimeReduction += 1F;
//    } else if (props.hasPerkUnlocked(PerkList.fuelEff1)) {
//      tags.setInteger("fuelEff", 1);
//      processTimeReduction += 0.5F;
//    }
//    if (props.hasPerkUnlocked(PerkList.machAcc)) {
//      tags.setBoolean("machAcc", true);
//      processTimeReduction += 0.5F;
//    }
//    if (props.hasPerkUnlocked(PerkList.machHopper)) {
//      tags.setBoolean("machHopper", true);
//      processTimeReduction += 0.5F;
//    }
//    if (props.hasPerkUnlocked(PerkList.machEff2)) {
//      tags.setInteger("machEff", 2);
//      processTimeReduction += 1F;
//    } else if (props.hasPerkUnlocked(PerkList.machEff1)) {
//      tags.setInteger("machEff", 1);
//      processTimeReduction += 0.5F;
//    } 
//    if (props.hasPerkUnlocked(PerkList.attackDmg1)) {
//      tags.setInteger("NanobotAttackDamage", 1);
//    }
//    tags.setFloat("timeReduction", processTimeReduction);

//    itemStackIn.setTagCompound(tags);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
    if (stack.hasTagCompound() && stack.getTagCompound().getTag("owner") != null) {
      NBTTagCompound tags = stack.getTagCompound();
      tooltip.add("Linked from: " + tags.getString("owner"));
      if (tags.hasKey("fuelEff"))
        tooltip.add("Fuel Efficiency bonus: " + (tags.getInteger("fuelEff") * 10) + " percent.");
      if (tags.hasKey("machAcc"))
        tooltip.add("Hopper extract enabled.");
      if (tags.hasKey("machHopper"))
        tooltip.add("Hopper input enabled.");
      if (tags.hasKey("machEff"))
        tooltip.add("Extra output chance: " + (tags.getInteger("machEff") * 15) + " percent.");
      if (tags.hasKey("timeReduction"))
        tooltip.add("Processing time reduction: " + tags.getFloat("timeReduction") + " seconds.");
      if (tags.hasKey("NanobotAttackDamage"))
        tooltip.add("Tool and Weapon damage upgrade.");

    } else  if (stack.hasTagCompound() && stack.getTagCompound().getTag("NanobotAttackDamage") != null) {
      tooltip.add("Tool and Weapon damage upgrade.");
    } else {
      tooltip.add("Sneak right-click in ");
      tooltip.add("the air to attune to you.");
    }
    super.addInformation(stack, playerIn, tooltip, advanced);
  }
}
