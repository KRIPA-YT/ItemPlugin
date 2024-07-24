package de.amethyst.itemplugin.itemgui.edit;

import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import de.amethyst.itemplugin.engine.templates.EmptyFrame;
import de.amethyst.itemplugin.engine.templates.PreviousButton;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.List;

public class EnchantmentLevelFrame extends EmptyFrame {
    private Player player;
    private final Enchantment enchantment;

    public EnchantmentLevelFrame(Player player, Enchantment enchantment) {
        super("Select Enchantment Level");
        this.player = player;
        this.enchantment = enchantment;

        this.setElement(4, 0, new ItemEditFrame.EditItemDisplay());
        this.setElement(3, 2, new PreviousButton());

        int[][] positions = {
                {3, 5},
                {2, 4, 6},
                {2, 3, 5, 6},
                {2, 3, 4, 5, 6}
        };

        int maxLevel = this.enchantment.getMaxLevel();
        int[] pos = maxLevel < 6 ? positions[maxLevel - 2] : positions[3];

        for (int i = 0; i < pos.length; i++) {
            this.setElement(pos[i], 1, new EnchantmentApplyButton(i + 1));
        }
    }

    @Override
    public int getYSize() {
        return 3;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private class EnchantmentApplyButton implements GUIElement {
        private int level;
        @Override
        public ItemStack getIcon(Player player) {
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) book.getItemMeta();
            bookMeta.setDisplayName("§aApply %s %d".formatted(EnchantmentHelper.getEnchantmentName(enchantment), this.level));

            ItemStack edit = ItemEditHistoryManager.getSingleton().getStaged(player.getUniqueId());
            if (EnchantmentHelper.containsEnchant(edit, enchantment)) {
                bookMeta.setDisplayName("§cRemove %s".formatted(EnchantmentHelper.getEnchantmentName(enchantment)));
                bookMeta.setLore(List.of("", "§cClick to remove", "§cEnchantment"));
            }
            book.setItemMeta(bookMeta);
            ItemEditHistoryManager.getSingleton().editStaged(player.getUniqueId(), edit);
            return book;
        }

        @Override
        public void onClick(GUIElementClickEvent event) {
            Player player = event.getPlayer();
            ItemStack edit = ItemEditHistoryManager.getSingleton().getStaged(player.getUniqueId());
            edit = EnchantmentHelper.toggleEnchant(edit, enchantment, this.level);
            ItemEditHistoryManager.getSingleton().editStaged(player.getUniqueId(), edit);
            this.playDing(player);
        }
    }
}
