package de.amethyst.itemplugin;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/**
 * Easily create itemstacks, without messing your hands.
 * <i>Note that if you do use this in one of your projects, leave this notice.</i>
 * <i>Please do credit me if you do use this in one of your projects.</i>
 * @author NonameSL
 */
public class ItemBuilder {
    @Setter @Getter
    protected ItemStack itemStack;
    /**
     * Create a new ItemBuilder from scratch.
     * @param m The material to create the ItemBuilder with.
     */
    public ItemBuilder(Material m){
        this(m, 1);
    }
    /**
     * Create a new ItemBuilder over an existing itemstack.
     * @param itemStack The itemstack to create the ItemBuilder over.
     */
    public ItemBuilder(ItemStack itemStack){
        this.itemStack = itemStack;
    }
    /**
     * Create a new ItemBuilder from scratch.
     * @param m The material of the item.
     * @param amount The amount of the item.
     */
    public ItemBuilder(Material m, int amount){
        itemStack = new ItemStack(m, amount);
    }
    /**
     * Create a new ItemBuilder from scratch.
     * @param m The material of the item.
     * @param amount The amount of the item.
     * @param durability The durability of the item.
     */
    public ItemBuilder(Material m, int amount, byte durability){
        itemStack = new ItemStack(m, amount, durability);
    }
    /**
     * Clone the ItemBuilder into a new one.
     * @return The cloned instance.
     */
    public ItemBuilder clone(){
        return new ItemBuilder(itemStack);
    }
    /**
     * Change the durability of the item.
     * @param dur The durability to set it to.
     */
    public ItemBuilder setDurability(short dur){
        itemStack.setDurability(dur);
        return this;
    }
    /**
     * Set the displayname of the item.
     * @param name The name to change it to.
     */
    public ItemBuilder setName(String name){
        ItemMeta im = itemStack.getItemMeta();
        im.setDisplayName(name);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Set the amount of the item
     * @param amount The amount to set it to.
     */
    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Set itemflags of the item.
     * @param itemFlags The itemflags to set.
     */
    public ItemBuilder setItemFlags(ItemFlag... itemFlags) {
        ItemMeta im = itemStack.getItemMeta();
        im.addItemFlags(itemFlags);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Removes itemflags of the item.
     * @param itemFlags The itemflags to remove.
     */
    public ItemBuilder removeItemFlags(ItemFlag... itemFlags) {
        ItemMeta im = itemStack.getItemMeta();
        im.removeItemFlags(itemFlags);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Add an unsafe enchantment.
     * @param ench The enchantment to add.
     * @param level The level to put the enchant on.
     */
    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level){
        if (itemStack.getType() != Material.ENCHANTED_BOOK) {
            itemStack.addUnsafeEnchantment(ench, level);
            return this;
        }

        EnchantmentStorageMeta im = (EnchantmentStorageMeta) itemStack.getItemMeta();
        im.addStoredEnchant(ench, level, true);
        itemStack.setItemMeta(im);
        return this;
    }
    /**
     * Remove a certain enchant from the item.
     * @param ench The enchantment to remove
     */
    public ItemBuilder removeEnchantment(Enchantment ench){
        if (itemStack.getType() != Material.ENCHANTED_BOOK) {
            itemStack.removeEnchantment(ench);
            return this;
        }
        EnchantmentStorageMeta im = (EnchantmentStorageMeta) itemStack.getItemMeta();
        im.removeStoredEnchant(ench);
        itemStack.setItemMeta(im);
        return this;
    }

    /**
     * Check if a certain enchant exists on the item.
     * @param ench The enchantment to check
     * @return If it exists
     */
    public boolean containsEnchantment(Enchantment ench) {
        if (itemStack.getType() != Material.ENCHANTED_BOOK) {
            return itemStack.containsEnchantment(ench);
        }
        EnchantmentStorageMeta im = (EnchantmentStorageMeta) itemStack.getItemMeta();
        return im.hasStoredEnchant(ench);
    }
    /**
     * Set the skull owner for the item. Works on skulls only.
     * @param owner The name of the skull's owner.
     */
    public ItemBuilder setSkullOwner(String owner){
        try{
            SkullMeta im = (SkullMeta) itemStack.getItemMeta();
            im.setOwner(owner);
            itemStack.setItemMeta(im);
        }catch(ClassCastException expected){}
        return this;
    }
    /**
     * Add an enchant to the item.
     * @param ench The enchant to add
     * @param level The level
     */
    public ItemBuilder addEnchant(Enchantment ench, int level){
        if (itemStack.getType() != Material.ENCHANTED_BOOK) {
            ItemMeta im = itemStack.getItemMeta();
            im.addEnchant(ench, level, true);
            itemStack.setItemMeta(im);
            return this;
        }

        EnchantmentStorageMeta im = (EnchantmentStorageMeta) itemStack.getItemMeta();
        im.addStoredEnchant(ench, level, true);
        itemStack.setItemMeta(im);
        return this;
    }
    /**
     * Add multiple enchants at once.
     * @param enchantments The enchants to add.
     */
    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments){
        itemStack.addEnchantments(enchantments);
        return this;
    }
    /**
     * Sets infinity durability on the item by setting the durability to Short.MAX_VALUE.
     */
    public ItemBuilder setInfinityDurability(){
        itemStack.setDurability(Short.MAX_VALUE);
        return this;
    }
    /**
     * Re-sets the lore.
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(String... lore){
        ItemMeta im = itemStack.getItemMeta();
        im.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(im);
        return this;
    }
    /**
     * Re-sets the lore.
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = itemStack.getItemMeta();
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }
    public List<String> getLore() {
        ItemMeta im = itemStack.getItemMeta();
        return im.hasLore() ? im.getLore() : Collections.emptyList();
    }
    /**
     * Remove a lore line.
     * @param line The lore to remove.
     */
    public ItemBuilder removeLoreLine(String line){
        ItemMeta im = itemStack.getItemMeta();
        if (!im.hasLore()) return this;
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line)) return this;
        lore.remove(line);
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }
    /**
     * Remove a lore line.
     * @param index The index of the lore line to remove.
     */
    public ItemBuilder removeLoreLine(int index){
        ItemMeta im = itemStack.getItemMeta();
        if (!im.hasLore()) return this;
        List<String> lore = new ArrayList<>(im.getLore());
        if(index<0||index>lore.size())return this;
        lore.remove(index);
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }
    /**
     * Add a lore line.
     * @param line The lore line to add.
     */
    public ItemBuilder addLoreLine(String line){
        ItemMeta im = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(im.getLore());
        lore.add(line);
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }
    /**
     * Add a lore line.
     * @param pos The index of where to put it.
     * @param line The lore line to add.
     */
    public ItemBuilder addLoreLine(int pos, String line){
        ItemMeta im = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(im.getLore());
        lore.add(pos, line);
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }
    /**
     * Add a multiple lore lines.
     * @param lines The lore lines to add.
     */
    public ItemBuilder addLoreLines(String... lines) {
        ItemMeta im = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(im.getLore());
        lore.addAll(List.of(lines));
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }
    /**
     * Add a multiple lore lines.
     * @param lines The lore lines to add.
     * @param pos The index of where to put it.
     */
    public ItemBuilder addLoreLines(int pos, String... lines) {
        ItemMeta im = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(im.getLore());
        lore.addAll(pos, List.of(lines));
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }
    /**
     * Set a lore line
     * @param line The lore line to set.
     * @param pos The index of where to put it.
     */
    public ItemBuilder setLoreLine(int pos, String line) {
        ItemMeta im = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) {
            lore = new ArrayList<>(im.getLore());
        }
        if (pos <= lore.size()) {
            for (int i = 0; i < (pos + 1) - lore.size(); i++) {
                lore.add("");
            }
        }
        lore.set(pos, line);
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }
    public String getLoreLine(int pos) {
        ItemMeta im = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) {
            lore = new ArrayList<>(im.getLore());
        }
        if (pos >= lore.size()) {
            return "";
        }
        return im.getLore().get(pos);
    }
    /**
     * Sets the armor color of a leather armor piece. Works only on leather armor pieces.
     * @param color The color to set it to.
     */
    public ItemBuilder setLeatherArmorColor(Color color){
        try{
            LeatherArmorMeta im = (LeatherArmorMeta) itemStack.getItemMeta();
            im.setColor(color);
            itemStack.setItemMeta(im);
        }catch(ClassCastException expected){}
        return this;
    }
    /**
     * Retrieves the itemstack from the ItemBuilder.
     * @return The itemstack created/modified by the ItemBuilder instance.
     */
    public ItemStack toItemStack(){
        return itemStack;
    }
}