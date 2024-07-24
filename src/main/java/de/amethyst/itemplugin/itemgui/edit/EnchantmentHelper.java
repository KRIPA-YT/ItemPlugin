package de.amethyst.itemplugin.itemgui.edit;

import org.apache.commons.lang.WordUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantmentHelper {
    public static ItemStack addEnchant(ItemStack itemStack, Enchantment enchantment, int level) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta instanceof EnchantmentStorageMeta editStorageMeta) {
            editStorageMeta.addStoredEnchant(enchantment, level, true);
        } else {
            itemMeta.addEnchant(enchantment, level, true);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack removeEnchant(ItemStack itemStack, Enchantment enchantment) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta instanceof EnchantmentStorageMeta editStorageMeta) {
            editStorageMeta.removeStoredEnchant(enchantment);
        } else {
            itemMeta.removeEnchant(enchantment);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack toggleEnchant(ItemStack itemStack, Enchantment enchantment, int level) {
        if (containsEnchant(itemStack, enchantment)) {
            return removeEnchant(itemStack, enchantment);
        } else {
            return addEnchant(itemStack, enchantment, level);
        }
    }

    public static boolean containsEnchant(ItemStack itemStack, Enchantment enchantment) {
        if (!itemStack.hasItemMeta()) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta.hasEnchant(enchantment) ||
                (itemMeta instanceof EnchantmentStorageMeta storageMeta && storageMeta.hasStoredEnchant(enchantment));
    }

    public static String getEnchantmentName(Enchantment enchantment) {
        return switch (enchantment.getKey().getKey().toUpperCase()) {
            case "VANISHING_CURSE" -> "Curse of Vanishing";
            case "BINDING_CURSE" -> "Curse of Binding";
            case "SWEEPING" -> "Sweeping Edge";
            default -> WordUtils.capitalizeFully(enchantment.getKey().getKey().replace('_', ' '));
        };
    }
}
