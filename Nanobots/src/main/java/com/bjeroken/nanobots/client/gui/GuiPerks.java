package com.bjeroken.nanobots.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.bjeroken.nanobots.client.ModKeyHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.INanobotCapability;
import com.bjeroken.nanobots.common.capabilities.nanobots.perk.IPerkCapability;
import com.bjeroken.nanobots.common.network.PacketDispatcher;
import com.bjeroken.nanobots.common.network.server.PerkClickMessage;
import com.bjeroken.nanobots.common.stats.EnumPerk;
import com.bjeroken.nanobots.common.stats.Perk;
import com.bjeroken.nanobots.common.stats.PerkList;
import com.bjeroken.nanobots.common.stats.PerkPage;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPerks extends GuiScreen {
  private static int iconInnerSize = 24;
  private static int iconOuterSize = 26;
  private static final int minColumn = PerkList.minDisplayColumn * iconInnerSize - 112;
  private static final int minRow = PerkList.minDisplayRow * iconInnerSize - 112;
  private static final int maxColumn = PerkList.maxDisplayColumn * iconInnerSize - 77;
  private static final int maxRow = PerkList.maxDisplayRow * iconInnerSize - 77;
  private static final ResourceLocation foregr = Refs.toResource("textures/gui/perk_frame.png");
  private static final ResourceLocation backgr = new ResourceLocation("minecraft", "textures/environment/end_sky.png");
  protected int totalWidth = 256;
  protected int totalHeight = 202;
  protected int field_146563_h;
  protected int field_146564_i;
  protected float zoom = 0.80F;
  protected double displayColumn2;
  protected double displayRow2;
  protected double displayColumn1;
  protected double displayRow1;
  protected double displayColumn;
  protected double displayRow;
  private int field_146554_D;
  private INanobotCapability props;
  private IPerkCapability perks;

  private EntityPlayer player;
  private int pageIndex;
  private List<Perk> minecraftPerks = new ArrayList<Perk>();
  private PerkButton info;
  // private Container container;
  private GuiButton selectedButton;
  private boolean rightClickDown, rightClickWasDown;
  private int tickK, tickL;

  public GuiPerks(EntityPlayer player) {
    // super(new ContainerPerks(player, world));
    // this.container = (ContainerPerks) inventorySlots;
    this.player = player;
    this.pageIndex = 0;
    this.props = Refs.getNanobotCap(player);
    this.perks = Refs.getPerkCap(player);
    this.rightClickDown = this.rightClickWasDown = false;
    short short1 = 141;
    short short2 = 141;
    this.displayColumn2 = this.displayColumn1 = this.displayColumn = (double) (PerkPage.getPerkPage(0).getPerks()
        .get(0).displayColumn * iconInnerSize - short1 / 2 - 12);
    this.displayRow2 = this.displayRow1 = this.displayRow = (double) (PerkPage.getPerkPage(0).getPerks()
        .get(0).displayRow * iconInnerSize - short2 / 2);
    minecraftPerks.clear();
    for (Perk Perk : PerkPage.getPerkPage(0).getPerks()) {
      minecraftPerks.add(Perk);
    }
    ModLogger.log("guiperks open");
  }

  // @Override
  // public void onGuiClosed() {
  // if (this.mc.thePlayer != null) {
  // this.container.onContainerClosed(this.mc.thePlayer);
  // }
  // }

  /**
   * Adds the buttons (and other controls) to the screen in question.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void initGui() {
    // this.mc.thePlayer.openContainer = this.container;
    // this.guiLeft = (this.width - this.xSize) / 2;
    // this.guiTop = (this.height - this.ySize) / 2;

    info = new PerkButton(3, (width - totalWidth) / 2 + 20, height / 2 + 74, 16, 16, "" + perks.getPerkPoints());
    info.canClick = false;
    this.buttonList.clear();
    this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20,
        I18n.format("gui.done", new Object[0])));
    this.buttonList.add(new GuiButton(2, (width - totalWidth) / 2 + 40, height / 2 + 74, 100, 20,
        I18n.format(PerkPage.getPerkPage(0).getName())));
    this.buttonList.add(info);
    // this.container.detectAndSendChanges();

  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == 1) {
      this.mc.displayGuiScreen((GuiScreen) null);
    } else if (button.id == 2) {
      pageIndex++;
      if (pageIndex >= PerkPage.getPerkPages().size())
        pageIndex = 0;
      minecraftPerks.clear();
      for (Perk Perk : PerkPage.getPerkPage(pageIndex).getPerks()) {
        minecraftPerks.add(Perk);
      }
      button.displayString = I18n.format(PerkPage.getPerkPage(pageIndex).getName());
    }
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)
        || keyCode == ModKeyHandler.keys[ModKeyHandler.PERKS_KEY].getKeyCode()) {
      this.mc.thePlayer.closeScreen();
    } else {
      super.keyTyped(typedChar, keyCode);
    }
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    int k;
    info.displayString = "" + perks.getPerkPoints();
    if (Mouse.isButtonDown(0)) {
      k = (this.width - this.totalWidth) / 2;
      int l = (this.height - this.totalHeight) / 2;
      int i1 = k + 8;
      int j1 = l + 17;

      if ((this.field_146554_D == 0 || this.field_146554_D == 1) && mouseX >= i1 && mouseX < i1 + 224 && mouseY >= j1
          && mouseY < j1 + 155) {
        if (this.field_146554_D == 0) {
          this.field_146554_D = 1;
        } else {
          this.displayColumn1 -= (double) ((float) (mouseX - this.field_146563_h) * this.zoom);
          this.displayRow1 -= (double) ((float) (mouseY - this.field_146564_i) * this.zoom);
          this.displayColumn = this.displayColumn2 = this.displayColumn1;
          this.displayRow = this.displayRow2 = this.displayRow1;
        }

        this.field_146563_h = mouseX;
        this.field_146564_i = mouseY;
      }
    } else {
      this.field_146554_D = 0;
    }

    k = Mouse.getDWheel();
    float f4 = this.zoom;

    if (k < 0) {
      this.zoom += 0.20F;
    } else if (k > 0) {
      this.zoom -= 0.20F;
    }

    this.zoom = MathHelper.clamp_float(this.zoom, 0.80F, 2.0F);

    if (this.zoom != f4) {
      float f5 = f4 * (float) this.totalWidth;
      float f1 = f4 * (float) this.totalHeight;
      float f2 = this.zoom * (float) this.totalWidth;
      float f3 = this.zoom * (float) this.totalHeight;
      this.displayColumn1 -= (double) ((f2 - f5) * 0.5F);
      this.displayRow1 -= (double) ((f3 - f1) * 0.5F);
      this.displayColumn = this.displayColumn2 = this.displayColumn1;
      this.displayRow = this.displayRow2 = this.displayRow1;
    }

    if (this.displayColumn < (double) minColumn) {
      this.displayColumn = (double) minColumn;
    }

    if (this.displayRow < (double) minRow) {
      this.displayRow = (double) minRow;
    }

    if (this.displayColumn >= (double) maxColumn) {
      this.displayColumn = (double) (maxColumn - 1);
    }

    if (this.displayRow >= (double) maxRow) {
      this.displayRow = (double) (maxRow - 1);
    }

    this.drawDefaultBackground();
    this.drawPerkScreen(mouseX, mouseY, partialTicks);
    GlStateManager.disableLighting();
    GlStateManager.disableDepth();
    this.drawTitle();
    GlStateManager.enableLighting();
    GlStateManager.enableDepth();
  }

  /**
   * Called from the main game loop to update the screen.
   */
  @Override
  public void updateScreen() {
    this.displayColumn2 = this.displayColumn1;
    this.displayRow2 = this.displayRow1;
    double d0 = this.displayColumn - this.displayColumn1;
    double d1 = this.displayRow - this.displayRow1;

    if (d0 * d0 + d1 * d1 < 4.0D) {
      this.displayColumn1 += d0;
      this.displayRow1 += d1;
    } else {
      this.displayColumn1 += d0 * 0.85D;
      this.displayRow1 += d1 * 0.85D;
    }

  }

  protected void drawTitle() {
    int i = (this.width - this.totalWidth) / 2;
    int j = (this.height - this.totalHeight) / 2;
    this.fontRendererObj.drawString(I18n.format("gui.perks", new Object[0]), i + 15, j + 5, 4210752);
    // <--
    // this.fontRendererObj.drawString("" + this.perkPoints,(width - totalWidth)
    // / 2 + 20, height / 2 + 80, 0);
  }

  protected void drawPerkScreen(int mouseX, int mouseY, float partialTicks) {
    // this.drawTexturedModalRect(k1, l1, cornerX - 50, cornerY - 50, 224, 154);
    int col = MathHelper
        .floor_double(this.displayColumn2 + (this.displayColumn1 - this.displayColumn2) * (double) partialTicks);
    int row = MathHelper.floor_double(this.displayRow2 + (this.displayRow1 - this.displayRow2) * (double) partialTicks);
    tickK = col;
    tickL = row;
    if (col < minColumn) {
      col = minColumn;
    }

    if (row < minRow) {
      row = minRow;
    }

    if (col >= maxColumn) {
      col = maxColumn - 1;
    }

    if (row >= maxRow) {
      row = maxRow - 1;
    }

    int startX = (this.width - this.totalWidth) / 2;
    int startY = (this.height - this.totalHeight) / 2;
    int innerStartX = startX + 16;
    int innerStartY = startY + 17;
    this.zLevel = 0.0F;
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(backgr);

    GlStateManager.depthFunc(518);
    GlStateManager.pushMatrix();
    GlStateManager.enableTexture2D();
    GlStateManager.disableLighting();
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableColorMaterial();
    int iterator1;
    int lineStartX;
    int lineStartY;
    GlStateManager.translate(0, 0, -200.0F);
    // this.drawTexturedModalRect(k1, l1, (int)this.displayColumn , (int)
    // this.displayRow, 224, 154);
    float scale = 0.5F;
    GlStateManager.scale(scale, scale, 1.0F);
    this.drawTexturedModalRect((int) (innerStartX / scale), (int) (innerStartY / scale), col, row, (int) (224 / scale),
        (int) (154 / scale));

    GlStateManager.disableDepth();
    GlStateManager.enableBlend();
    GlStateManager.popMatrix();

    GlStateManager.pushMatrix();
    GlStateManager.enableTexture2D();
    GlStateManager.disableLighting();
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableColorMaterial();

    GlStateManager.translate((float) innerStartX, (float) innerStartY, -200.0F);
    // FIXES models rendering weirdly in the acheivements pane
    // see
    // https://github.com/MinecraftForge/MinecraftForge/commit/1b7ce7592caafb760ec93066184182ae0711e793#commitcomment-10512284
    GlStateManager.scale(1.0F / this.zoom, 1.0F / this.zoom, 1.0F);

    GlStateManager.enableDepth();
    GlStateManager.depthFunc(515);
    this.mc.getTextureManager().bindTexture(foregr);
    int parents;
    int color;
    int lineStopY;
    // Rendering Lines backwards from child to parent perks
    List<Perk> PerkList = PerkPage.getPerkPage(pageIndex).getPerks();
    for (iterator1 = 0; iterator1 < PerkList.size(); ++iterator1) {
      Perk perk1 = PerkList.get(iterator1);

      if (perk1.parentPerk != null && PerkList.contains(perk1.parentPerk)) {
        lineStartX = perk1.displayColumn * iconInnerSize - col + 11;
        lineStartY = perk1.displayRow * iconInnerSize - row + 11;
        int lineStopX = perk1.parentPerk.displayColumn * iconInnerSize - col + 11;
        lineStopY = perk1.parentPerk.displayRow * iconInnerSize - row + 11;

        parents = perk1.countParents();

        if (parents <= 14) {
          color = -16777216;

          if (this.perks.hasPerk(perk1)) {
            color = -6250336;
          } else if (this.perks.canAddPerk(player, perk1)) {
            color = -16711936;
          }
          this.drawHorizontalLine(lineStartX, lineStopX, lineStartY, color);
          this.drawVerticalLine(lineStopX, lineStartY, lineStopY, color);

          if (lineStartX > lineStopX) {
            this.drawTexturedModalRect(lineStartX - 11 - 7, lineStartY - 5, 114, 234, 7, 11);
          } else if (lineStartX < lineStopX) {
            this.drawTexturedModalRect(lineStartX + 11, lineStartY - 5, 107, 234, 7, 11);
          } else if (lineStartY > lineStopY) {
            this.drawTexturedModalRect(lineStartX - 5, lineStartY - 11 - 7, 96, 234, 11, 7);
          } else if (lineStartY < lineStopY) {
            this.drawTexturedModalRect(lineStartX - 5, lineStartY + 11, 96, 241, 11, 7);
          }
        }
      }
    }
    // Lines end

    Perk perk = null;
    float adjustedMouseX = (float) (mouseX - innerStartX) * this.zoom;
    float adjustedMouseY = (float) (mouseY - innerStartY) * this.zoom;
    RenderHelper.enableGUIStandardItemLighting();
    GlStateManager.disableLighting();
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableColorMaterial();
    int perk2Row;
    int parentPerks;
    // All perks rendering
    for (iterator1 = 0; iterator1 < PerkList.size(); ++iterator1) {
      Perk perk2 = (Perk) PerkList.get(iterator1);
      lineStopY = perk2.displayColumn * iconInnerSize - col;
      perk2Row = perk2.displayRow * iconInnerSize - row;

      if (lineStopY >= -iconInnerSize && perk2Row >= -iconInnerSize && (float) lineStopY <= 224.0F * this.zoom
          && (float) perk2Row <= 155.0F * this.zoom) {
        parentPerks = perk2.countParents();
        float colorBase;

        if (this.perks.hasPerk(perk2)) {
          colorBase = 0.75F;
          GlStateManager.color(colorBase, colorBase, colorBase, 1.0F);
        } else if (this.perks.canAddPerk(player, perk2)) {
          colorBase = 1.0F;
          GlStateManager.color(colorBase, colorBase, colorBase, 1.0F);
        } else if (parentPerks < 14) {
          colorBase = 0.6F;
          GlStateManager.color(colorBase, colorBase, colorBase, 1.0F);
        } else if (parentPerks == 15) {
          colorBase = 0.5F;
          GlStateManager.color(colorBase, colorBase, colorBase, 1.0F);
        } else {
          if (parentPerks != 16) {
            continue;
          }
          colorBase = 0.4F;
          GlStateManager.color(colorBase, colorBase, colorBase, 1.0F);
        }

        this.mc.getTextureManager().bindTexture(foregr);

        GlStateManager.enableBlend();

        if (perk2.parentPerk == null) {
          this.drawTexturedModalRect(lineStopY - 2, perk2Row - 2, iconOuterSize, 202, iconOuterSize, iconOuterSize);
        } else {
          this.drawTexturedModalRect(lineStopY - 2, perk2Row - 2, 0, 202, iconOuterSize, iconOuterSize);
        }

        this.mc.getTextureManager().bindTexture(foregr);
        GlStateManager.disableBlend();

        if (!this.perks.canAddPerk(player, perk2)) {
          colorBase = 0.1F;
          GlStateManager.color(colorBase, colorBase, colorBase, 1.0F);
          this.itemRender.isNotRenderingEffectsInGUI(false);
        }

        GlStateManager.disableLighting();
        GlStateManager.enableCull();
        /*
         * Renders Item!
         */
        this.itemRender.renderItemAndEffectIntoGUI(perk2.theItemStack, lineStopY + 3, perk2Row + 3);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableLighting();

        if (!this.perks.canAddPerk(player, perk2)) {
          this.itemRender.isNotRenderingEffectsInGUI(true);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (adjustedMouseX >= (float) lineStopY && adjustedMouseX <= (float) (lineStopY + 22)
            && adjustedMouseY >= (float) perk2Row && adjustedMouseY <= (float) (perk2Row + 22)) {
          perk = perk2;
        }

      }
    }
    // Perks rendering end

    GlStateManager.disableDepth();
    GlStateManager.enableBlend();
    GlStateManager.popMatrix();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(foregr);
    this.drawTexturedModalRect(startX, startY, 0, 0, this.totalWidth, this.totalHeight);
    this.zLevel = 0.0F;
    GlStateManager.depthFunc(515);
    GlStateManager.disableDepth();
    GlStateManager.enableTexture2D();
    super.drawScreen(mouseX, mouseY, partialTicks);
    int stringWidth;

    // Render hover over perk
    if (perk != null) {
      String perkName = perk.getName();
      String perkDesc = perk.getDescription();

      lineStopY = mouseX + 12;
      perk2Row = mouseY - 4;
      parentPerks = perk.countParents();

      // Handle clicks on perks
      if (this.perks.canAddPerk(player, perk)
          /* && props.getNanobotStatus() == NanobotStatus.active */ || this.perks.hasPerk(perk)) {
        rightClickWasDown = rightClickDown;
        rightClickDown = Mouse.isButtonDown(1);
        if (rightClickWasDown && !rightClickDown && !this.perks.hasPerk(perk) && perks.canAddPerk(player, perk)) {
          ModLogger.log(
              "registered click " + perk.getPerkName() + ", Enum: " + EnumPerk.valueOf(perk.getPerkName().substring(5))
                  + ", ordinal: " + EnumPerk.valueOf(perk.getPerkName().substring(5)).ordinal());
          if (this.perks.canAddPerk(player, perk))
            PacketDispatcher
                .sendToServer(new PerkClickMessage(EnumPerk.valueOf(perk.getPerkName().substring(5)).ordinal()));

        }
        stringWidth = Math.max(this.fontRendererObj.getStringWidth(perkName), 140);
        color = this.fontRendererObj.splitStringWidth(perkDesc, stringWidth) + 12;
        int activ = 0;
        if (!perks.hasPerk(perk) && this.perks.getPerkPoints() >= perk.getPerkCost())
          activ += 12;
        this.drawGradientRect(lineStopY - 3, perk2Row - 3, lineStopY + stringWidth + 3,
            perk2Row + color + 3 + 12 + activ, -1073741824, -1073741824);
        this.fontRendererObj.drawSplitString(perkDesc, lineStopY, perk2Row + 12, stringWidth, -6250336);

        if (this.perks.hasPerk(perk)) {
          this.fontRendererObj.drawStringWithShadow(new TextComponentTranslation("perk.taken").getFormattedText(),
              (float) lineStopY, (float) (perk2Row + color + 4), -7302913);
        } else {
          if (this.perks.getPerkPoints() >= perk.getPerkCost())
            this.fontRendererObj.drawStringWithShadow(
                new TextComponentTranslation("perk.activate", new Object[0]).getFormattedText(), (float) lineStopY,
                (float) (perk2Row + color + 4), -7302913);
          this.fontRendererObj
              .drawStringWithShadow(new TextComponentTranslation("perk.pointcost", new Object[0]).getFormattedText()
                  + " " + perk.getPerkCost(), (float) lineStopY, (float) (perk2Row + color + 4) + activ, -7302913);
        }
      } else {
        int j4;
        String requires;
        stringWidth = Math.max(this.fontRendererObj.getStringWidth(perkName), 120);
        requires = I18n.format(perkDesc, new Object[0]);
        String req = new TextComponentTranslation("perk.perkpoint", new Object[0]).getFormattedText();
        if (perk.parentPerk != null)
          req = perk.parentPerk.getName();
        requires += new TextComponentTranslation("perk.requires", new Object[0]).getFormattedText();
        requires += " ";
        requires += req;
        j4 = this.fontRendererObj.splitStringWidth(requires, stringWidth);
        this.drawGradientRect(lineStopY - 3, perk2Row - 3, lineStopY + stringWidth + 3, perk2Row + j4 + 12 + 3,
            -1073741824, -1073741824);
        this.fontRendererObj.drawSplitString(requires, lineStopY, perk2Row + 12, stringWidth, -9416624);
      }

      if (perkName != null) {
        this.fontRendererObj.drawStringWithShadow(perkName, (float) lineStopY, (float) perk2Row,
            this.perks.canAddPerk(player, perk) ? -1 : -8355712);
      }
    }
    // Hover perk end

    if (info.isMouseOver()) {
      hoverPerkPoint(mouseX, mouseY);
    }
    GlStateManager.enableDepth();
    GlStateManager.enableLighting();
    RenderHelper.disableStandardItemLighting();
  }

  private void hoverPerkPoint(int mouseX, int mouseY) {
    int posx = mouseX + 12;
    int posy = mouseY - 4;
    int stringWidth;
    String desc = I18n.format("perk.amount.desc");
    stringWidth = 140;
    int i4 = this.fontRendererObj.splitStringWidth(desc, stringWidth);
    this.drawGradientRect(posx - 3, posy - 3, posx + stringWidth + 3, posy + i4 + 3, -1073741824, -1073741824);
    this.fontRendererObj.drawSplitString(desc, posx, posy, stringWidth, -1);

  }

  @Override
  public boolean doesGuiPauseGame() {
    return false;
  }

}