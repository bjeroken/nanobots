package com.bjeroken.nanobots.common.items;

import java.util.List;

import com.bjeroken.nanobots.Nanobots;
import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.NanobotStatus;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.IPerkCapability;
import com.bjeroken.nanobots.common.network.PacketDispatcher;
import com.bjeroken.nanobots.common.network.bidirectional.OpenGuiMessage;
import com.bjeroken.nanobots.common.network.client.UpdateStatsMessage;
import com.bjeroken.nanobots.common.stats.EnumPerk;
import com.bjeroken.nanobots.common.stats.Perk;
import com.bjeroken.nanobots.common.stats.PerkList;
import com.bjeroken.nanobots.common.util.EnumStats;
import com.bjeroken.nanobots.common.util.ModUtils;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemDebug extends ItemNanobotBase {
  private int mode = -1;
  private static String md = "mode";
  private static ResourceLocation[] states = { new ResourceLocation("minecraft:beacon"),
      new ResourceLocation("minecraft:arrow"), new ResourceLocation("minecraft:birch_boat"),
      new ResourceLocation("minecraft:bookshelf"), new ResourceLocation("minecraft:cake"),
      new ResourceLocation("minecraft:clay"), new ResourceLocation("minecraft:apple"),
      new ResourceLocation("minecraft:farmland") };

  public ItemDebug(String name) {
    super(name);
  }

  @Override
  public void regProxy() {
    Nanobots.proxy.regRender(this, states);
  }

  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    subItems.add(new ItemStack(itemIn, 1, 0));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
    if (!world.isRemote) {
      int mod = stack.getMetadata();
      if (player.isSneaking()) {
        mod++;
        if (mod > 7)
          mod = 0;
        stack.setItemDamage(mod);
        switch (mod) {
        case -1:
          player.addChatMessage(new TextComponentTranslation("Set to show stats and fuel."));
          break;
        case 0:
          player.addChatMessage(new TextComponentTranslation("Set to show Perks."));
          break;
        case 1:
          player.addChatMessage(new TextComponentTranslation("Set to toggle xp drain."));
          break;
        case 2:
          player.addChatMessage(new TextComponentTranslation("Set to show perks window."));
          break;
        case 3:
          player.addChatMessage(new TextComponentTranslation("Set to add perk point."));
          break;
        case 4:
          player.addChatMessage(new TextComponentTranslation("Set to add nanobot level."));
          break;
        case 5:
          player.addChatMessage(new TextComponentTranslation("Set to switch NanobotStatus."));
          break;
        case 6:
          player.addChatMessage(new TextComponentTranslation("Set to add fuel."));
          break;
        default:
          break;
        }
      } else {
        INanobotCapability nanoCap = Refs.getNanobotCap(player);
        IFuelCapability fuelCap = Refs.getPlayerFuelCap(player);
        IPerkCapability perkCap = Refs.getPerkCap(player);
        UpdateStatsMessage msg = new UpdateStatsMessage();
        switch (mod) {
        case -1:
          player.addChatMessage(new TextComponentString(nanoCap.toString() + " " + fuelCap.toString()));
          break;
        case 0:
          if (perkCap != null)
            player.addChatMessage(new TextComponentString(perkCap.toString()));
          break;
        case 1:
          msg.addStat(EnumStats.drainxp, nanoCap.setXPDrain(!nanoCap.getXPDrain()));
          player.addChatMessage(new TextComponentString("set drain :" + nanoCap.getXPDrain()));
          break;
        case 2:
          PacketDispatcher.sendTo(new OpenGuiMessage(Refs.GUI_PERKS), (EntityPlayerMP) player);
          break;
        case 3:
          msg.addStat(EnumStats.perkpoints, perkCap.setPerkPoints(perkCap.getPerkPoints() + 1));
          player.addChatMessage(new TextComponentTranslation("Added perk point: " + perkCap.getPerkPoints()));
          break;
        case 4:
          msg.addStat(EnumStats.nanobotlevel, nanoCap.setNanobotLevel(nanoCap.getNanobotLevel() + 1));
          player.addChatMessage(new TextComponentTranslation("Added level: " + nanoCap.getNanobotLevel()));
          break;
        case 5:
          int current = nanoCap.getNanobotStatus().ordinal();
          current++;
          if (current >= NanobotStatus.values().length)
            current = 0;
          msg.addStat(EnumStats.nanobotstatus, nanoCap.setNanobotStatus(NanobotStatus.values()[current]));
          player.addChatMessage(new TextComponentTranslation("Set status: " + nanoCap.getNanobotStatus().name()));
          break;
        case 6:
          if (fuelCap.getCurrentFuel() < fuelCap.getMaxFuel()) {
            if (fuelCap.getCurrentFuel() + 100 <= fuelCap.getMaxFuel())
              msg.addStat(EnumStats.currentfuel, fuelCap.setCurrentFuel(fuelCap.getCurrentFuel() + 100));
            else
              msg.addStat(EnumStats.currentfuel, fuelCap.setCurrentFuel(fuelCap.getMaxFuel()));
            player.addChatMessage(new TextComponentTranslation("Set fuel: " + fuelCap.getCurrentFuel()));
          }
          break;
        default:
          break;
        }
        msg.sendTo(player);
      }
    }
    return new ActionResult(EnumActionResult.PASS, stack);
  }
}
