package com.bjeroken.nanobots.common.items;

import java.util.List;

import com.bjeroken.nanobots.Nanobots;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.FuelHandler;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.FuelProvider;
import com.bjeroken.nanobots.common.capabilities.nanobots.fuel.IFuelCapability;
import com.bjeroken.nanobots.common.util.EnumStats;
import com.bjeroken.nanobots.common.util.FuelHelper;
import com.bjeroken.nanobots.common.util.ModLogger;
import com.bjeroken.nanobots.common.util.ModUtils;
import com.bjeroken.nanobots.common.util.Refs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemNanobotArmor extends ItemArmor implements ISpecialArmor, INanoTab, IProxyReg {

  /** Stores the armor type: 0 is helmet, 1 is plate, 2 is legs and 3 is boots */
  public EntityEquipmentSlot armorType;
  /** Holds the amount of damage that the armor reduces at full durability. */
  public int damageReduceAmount;
  /**
   * Used on RenderPlayer to select the correspondent armor to be rendered on the player: 0 is cloth, 1 is chain, 2 is iron, 3 is diamond
   * and 4 is gold.
   */
  public int renderIndex;
  /** The EnumArmorMaterial used for this ItemArmor */
  private ItemArmor.ArmorMaterial material;

  public ItemNanobotArmor(ItemArmor armor) {
    super(armor.getArmorMaterial(), armor.renderIndex, armor.armorType);
    copy(armor);
    setTab(Refs.TAB);
    ResourceLocation loc = Refs.toResource("nanobot_" + armor.getRegistryName().getResourcePath());
    setUnlocalizedName(loc.toString());
    setRegistryName(loc);
  }

  /*
   * If I want subitems to not be in my tab, i just override this in subclass
   */
  @Override
  public void setTab(CreativeTabs tab) {
    setCreativeTab(tab);
  }

  @Override
  public void regProxy() {
    Nanobots.proxy.regRender(this, getRegistryName());
  }

  public ItemNanobotArmor copy(ItemArmor armor) {
    this.material = armor.getArmorMaterial();
    this.renderIndex = armor.renderIndex;
    this.armorType = armor.armorType;
    this.damageReduceAmount = armor.getArmorMaterial().getDamageReductionAmount(armorType);
    this.setMaxDamage(material.getDurability(armorType));
    this.maxStackSize = 1;
    return this;
  }

  public ItemNanobotArmor getCopy(ItemArmor armor) {
    return new ItemNanobotArmor(armor);
  }

  // @Override
  // public void onCreated(ItemStack stack, World world, EntityPlayer player) {
  // if (stack.hasCapability(Refs.FUEL_CAP, null)) {
  // IFuelCapability fuels = stack.getCapability(Refs.FUEL_CAP, null);
  // fuels.setMaxFuel(FuelHelper.getMaxFuel(this.material.getRepairItem()));
  // }
  // }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
    FuelProvider prov = new FuelProvider(stack, this.material.getRepairItem());

    return prov;
  }

  @Override
  public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
   
  }

  @Override
  public boolean getShareTag() {
    return true;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
    super.addInformation(stack, playerIn, tooltip, advanced);
    tooltip.add(ModUtils.getTags(stack).toString());
    if (stack.hasCapability(Refs.FUEL_CAP, null)) {
      IFuelCapability fuels = stack.getCapability(Refs.FUEL_CAP, null);
      fuels.readNBT(ModUtils.getTags(stack));
      // FuelHandler.readNBT(Refs.FUEL_CAP, stack.getCapability(Refs.FUEL_CAP, null), ModUtils.getTags(stack));
      tooltip.add("Current fuel: " + fuels.getCurrentFuel() + ", Max fuel: " + fuels.getMaxFuel());
    }
  }

  @Override
  public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
    // TODO Auto-generated method stub
    ArmorProperties armorProps = new ArmorProperties(0, damageReduceAmount / 25D, Integer.MAX_VALUE);
    return armorProps;
    // return null;
  }

  @Override
  public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
    ItemArmor theArmor = (ItemArmor) armor.getItem();
    return theArmor.damageReduceAmount;
  }

  @Override
  public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
    // TODO damage armor here
  }
}