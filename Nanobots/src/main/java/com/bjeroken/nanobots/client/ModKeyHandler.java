package com.bjeroken.nanobots.client;

import org.lwjgl.input.Keyboard;

import com.bjeroken.nanobots.Nanobots;
import com.bjeroken.nanobots.common.network.PacketDispatcher;
import com.bjeroken.nanobots.common.network.bidirectional.OpenGuiMessage;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class ModKeyHandler {
  /**
   * Storing an instance of Minecraft in a local variable saves having to get it
   * every time
   */
  private final Minecraft mc;

  /** Key index for easy handling */
  public static final int PERKS_KEY = 0;

  /** Key descriptions; use a language file to localize the description later */
  private static final String[] desc = { "key.tut_inventory.desc" };

  /** Default key values */
  private static final int[] keyValues = { Keyboard.KEY_P };

  /**
   * Make this public or provide a getter if you'll need access to the key
   * bindings from elsewhere
   */
  public static final KeyBinding[] keys = new KeyBinding[desc.length];

  public ModKeyHandler(Minecraft mc) {
    this.mc = mc;
    for (int i = 0; i < desc.length; ++i) {
      keys[i] = new KeyBinding(desc[i], keyValues[i], new TextComponentTranslation("key.nanobots.perks").toString());
      ClientRegistry.registerKeyBinding(keys[i]);
    }
  }

  /**
   * KeyInputEvent is in the FML package, so we must register to the FML event
   * bus
   */
  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {
    // checking inGameHasFocus prevents your keys from firing when the player is
    // typing a chat message
    // NOTE that the KeyInputEvent will NOT be posted when a gui screen such as
    // the inventory is open
    // so we cannot close an inventory screen from here; that should be done in
    // the GUI itself
    if (mc.inGameHasFocus) {
      if (keys[PERKS_KEY].isKeyDown()) {
        ModLogger.log("P pressed");
        mc.thePlayer.openGui(Nanobots.instance, Refs.GUI_PERKS, mc.theWorld, 0, 0, 0);
        // PacketDispatcher.sendToServer(new OpenGuiMessage(Refs.GUI_PERKS));
      }
    }
  }
}