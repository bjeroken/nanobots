package com.bjeroken.nanobots.client.gui.container;

import com.bjeroken.nanobots.common.container.ContainerBlockSwapper;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBlockSwapper extends GuiContainer {

  private static final ResourceLocation machineGuiTextures = Refs.toResource("textures/gui/container/blockswapper.png");

  public GuiBlockSwapper(InventoryPlayer playerInv) {
    super(new ContainerBlockSwapper(playerInv));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(machineGuiTextures);
    int startX = this.guiLeft;
    int startY = this.guiTop;
    this.drawTexturedModalRect(startX, startY, 0, 0, this.xSize, this.ySize);
  }

}
