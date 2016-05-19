package com.bjeroken.nanobots.client.gui;

import com.bjeroken.nanobots.common.stats.Perk;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PerkButton extends GuiButton {
  protected static final ResourceLocation perkTextures = Refs.toResource("textures/gui/gui_background.png");

  private Perk perk;
  public boolean canClick;

  public PerkButton(int buttonId, int x, int y, String string) {
    this(buttonId, x, y, 26, 26, string);
  }

  public PerkButton(int buttonId, int x, int y, int widthIn, int heightIn, String string) {
    this(buttonId, x, y, widthIn, heightIn, string, null);
  }

  public PerkButton(int buttonId, int x, int y, Perk perk) {
    this(buttonId, x, y, 26, 26, "", perk);
  }

  public PerkButton(int buttonId, int x, int y, int widthIn, int heightIn, String string, Perk perk) {
    super(buttonId, x, y, widthIn, heightIn, string);
    this.perk = perk;
    this.canClick = true;
  }

  @Override
  protected int getHoverState(boolean mouseOver) {
    byte b0 = 1;

    if (!this.enabled) {
      b0 = 0;
    } else if (mouseOver) {
      b0 = 2;
    }

    return b0;
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    if (this.visible) {
      FontRenderer fontrenderer = mc.fontRendererObj;
      mc.getTextureManager().bindTexture(perkTextures);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width
          && mouseY < this.yPosition + this.height;

      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.blendFunc(770, 771);
      if (this.perk != null)
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, perk.isSpecial() ? 1 : 0 * this.width, this.width,
            this.height);
      this.mouseDragged(mc, mouseX, mouseY);
      int l = 14737632;

      if (packedFGColour != 0) {
        l = packedFGColour;
      } else if (!this.enabled) {
        l = 10526880;
      } else if (this.hovered) {
        l = -16711936;
        // l = 16777120;
      }
      if (this.perk == null)
        this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2,
            this.yPosition + (this.height - 8) / 2, l);
    }
  }

  /**
   * Returns true if the mouse has been pressed on this control. Equivalent of
   * MouseListener.mousePressed(MouseEvent e).
   */
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    if (canClick)
      return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition
          && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    else
      return false;
  }

}