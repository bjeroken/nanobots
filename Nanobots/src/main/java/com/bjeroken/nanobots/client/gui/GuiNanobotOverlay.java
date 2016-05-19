package com.bjeroken.nanobots.client.gui;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.NanobotUtils;
import com.bjeroken.nanobots.common.capabilities.nanobots.PlayerNanobotHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.IPerkCapability;
import com.bjeroken.nanobots.common.util.ModUtils;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiNanobotOverlay extends Gui {
  private Minecraft mc;
  private int startX, startY;
  private static final ResourceLocation texture = Refs.toResource("textures/gui/fuel_bar.png");

  public GuiNanobotOverlay(Minecraft mc) {
    super();
    this.mc = mc;
  }

  @SubscribeEvent(priority = EventPriority.NORMAL)
  public void onRenderExperienceBar(RenderGameOverlayEvent.Post event) {
    if (event.getType() != ElementType.EXPERIENCE) {
      return;
    }

    ScaledResolution scaledresolution = new ScaledResolution(this.mc);
    int width = scaledresolution.getScaledWidth();
    int height = scaledresolution.getScaledHeight();

    // ItemStack heldItem = this.mc.thePlayer.getHeldItemMainhand();
    // if (heldItem != null && heldItem.getItem() == AXPItems.link_tool &&
    // heldItem.hasTagCompound()
    // && heldItem.getTagCompound().hasKey("FuelSourceBlock")) {
    // BlockPos pos =
    // BlockPos.fromLong(heldItem.getTagCompound().getLong("FuelSourceBlock"));
    // String linkText = String.format("Link x:%S, y:%S, z:%S.", pos.getX(),
    // pos.getY(), pos.getZ());
    //
    // int stringL = this.mc.fontRendererObj.getStringWidth(linkText);
    // this.mc.fontRendererObj.drawStringWithShadow(linkText, width - (width /
    // 2) - (stringL/ 2), height - 70, 8453920);
    // if(this.mc.thePlayer.ticksExisted % 10 == 0){
    // AXPEffectRender.getInstance().createParticles(this.mc.theWorld, pos,
    // pos.up(2), true);
    // }
    // }

    final INanobotCapability props = Refs.getNanobotCap(this.mc.thePlayer);
    final IPerkCapability perks = Refs.getPerkCap(this.mc.thePlayer);
    final IFuelCapability fuel = Refs.getPlayerFuelCap(this.mc.thePlayer);
    if (props == null) {
      return;
    }

    int guiLocX = 1; // AXPConfig.guiLocX.getInt();
    int guiLocY = 1; // AXPConfig.guiLocY.getInt();
    String nanobotXP = String.format("L: %S (%S/%S)", props.getNanobotLevel(), props.getNanobotXP(),
        NanobotUtils.xpNeeded(this.mc.thePlayer));
    int stringxpwidth = this.mc.fontRendererObj.getStringWidth(nanobotXP);
    String fuels = String.format("F: %S/%S", fuel.getCurrentFuel(), fuel.getMaxFuel());
    int stringfuelwidth = this.mc.fontRendererObj.getStringWidth(fuels);
    int maxX = width - stringxpwidth - 2;
    int maxY = height - 55;
    startX = 2 + (maxX * guiLocX) / 100;
    startY = 2 + (maxY * guiLocY) / 100;
    int maxArmorFuel = 0;
    int currentArmorFuel = 0;
    Iterable<ItemStack> iterable = this.mc.thePlayer.getArmorInventoryList();
    Iterator<ItemStack> iterator = iterable.iterator();
    while (iterator.hasNext()) {
      ItemStack armor = iterator.next();
      if (armor != null && armor.hasCapability(Refs.FUEL_CAP, null)) {
        IFuelCapability fuelC = armor.getCapability(Refs.FUEL_CAP, null);
        fuelC.readNBT(ModUtils.getTags(armor));
        maxArmorFuel += fuelC.getMaxFuel();
        currentArmorFuel += fuelC.getCurrentFuel();
      }
    }
    this.mc.getTextureManager().bindTexture(texture);

    // Add this block of code before you draw the section of your texture
    // containing transparency
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glDepthMask(false);

    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
    GL11.glDisable(GL11.GL_ALPHA_TEST);

    // Here we draw the background bar which contains a transparent section;
    // note the new size
    drawBars(startX, startY + 2, (int) (((float) props.getNanobotXP() / NanobotUtils.xpNeeded(this.mc.thePlayer)) * 49),
        texture);
    drawBars(startX, startY + 21, (int) (((float) fuel.getCurrentFuel() / fuel.getMaxFuel()) * 49), texture);

    drawText(startX, startY + 12, nanobotXP);
    drawText(startX, startY + 32, fuels);
    if (maxArmorFuel > 0) {
      String Armorfuel = String.format("A: %S/%S", currentArmorFuel, maxArmorFuel);
      drawText(startX, startY + 42, Armorfuel);
    }

    GL11.glDisable(GL11.GL_BLEND);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glDepthMask(true);

  }

  private void drawBars(int xPos, int yPos, int width, ResourceLocation texture) {
    this.mc.getTextureManager().bindTexture(texture);
    drawTexturedModalRect(xPos, yPos, 0, 0, 56, 9);
    drawTexturedModalRect(xPos + 3, yPos + 3, 0, 9, width, 3);

  }

  private void drawText(int xPos, int yPos, String s) {
    this.mc.fontRendererObj.drawStringWithShadow(s, xPos, yPos, 8453920);
  }
}