package de.amethyst.itemplugin.itemgui.edit;

import de.amethyst.itemplugin.ItemBuilder;
import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import de.amethyst.itemplugin.engine.GUIManager;
import de.amethyst.itemplugin.engine.templates.PreviousButton;
import de.amethyst.itemplugin.itemgui.Scroller;
import de.amethyst.itemplugin.itemgui.SelectionFrame;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class EnchantmentEditFrame extends SelectionFrame {
    @Getter(AccessLevel.PROTECTED)
    private Category category;
    private final Player player;

    public EnchantmentEditFrame(Player player) {
        super("", new Scroller<>(new ArrayList<>(), 4*6, 0, new ItemStack(Material.AIR), 6));
        this.player = player;
        this.setTitle("Edit Enchantments");
        this.setSorter((first, second) -> {
            if (first.getType() != Material.ENCHANTED_BOOK || second.getType() != Material.ENCHANTED_BOOK) {
                return first.getType() == second.getType() ? 0 : 1;
            }
            Enchantment firstEnchant = ((EnchantmentStorageMeta) first.getItemMeta()).getStoredEnchants().keySet().toArray(Enchantment[]::new)[0];
            Enchantment secondEnchant = ((EnchantmentStorageMeta) second.getItemMeta()).getStoredEnchants().keySet().toArray(Enchantment[]::new)[0];
            return String.CASE_INSENSITIVE_ORDER.compare(EnchantmentHelper.getEnchantmentName(firstEnchant), EnchantmentHelper.getEnchantmentName(secondEnchant));
        });
        this.setCategory(Category.COMPATIBLE);

        this.setElement(0, 0, new CategoryButton(new ItemBuilder(Material.NETHER_STAR).setName("Compatible").toItemStack(), Category.COMPATIBLE));
        this.setElement(0, 1, new CategoryButton(new ItemBuilder(Material.DIAMOND_SWORD).setName("Weapons").toItemStack(), Category.WEAPONS));
        this.setElement(0, 2, new CategoryButton(new ItemBuilder(Material.DIAMOND_PICKAXE).setName("Tools").toItemStack(), Category.TOOLS));
        this.setElement(0, 3, new CategoryButton(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setName("Armor").toItemStack(), Category.ARMOR));
        this.setElement(0, 4, new CategoryButton(new ItemBuilder(Material.ENCHANTED_BOOK).setName("All Enchants").toItemStack(), Category.ALL));
        this.setElement(0, 5, new CategoryButton(new ItemBuilder(Material.ENCHANTED_GOLDEN_APPLE).setName("Max Enchants").toItemStack(), Category.MAX));
        this.setElement(4, 0, new ItemEditFrame.EditItemDisplay());
        this.setElement(3, 5, new PreviousButton() {
            @Override
            public void onClick(GUIElementClickEvent event) {
                super.onClick(event);
                ItemEditHistoryManager.getSingleton().push(event.getPlayer().getUniqueId());
            }
        });
        this.setElement(4, 5, new SearchButton());
        this.setElement(5, 5, new SortModeSelectButton());
        this.setElement(8, 0, new ScrollButton(false));
        this.setElement(8, 5, new ScrollButton(true));

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 6; x++) {
                int index = y * 6 + x;
                this.setElement(x + 2, y + 1, new EnchantEditButton(index));
            }
        }
        this.setColor("GRAY");
    }

    private void setCategory(Category category) {
        this.category = category;
        this.updateValidEntities();
    }

    @Override
    protected List<ItemStack> search() {
        List<ItemStack> results = new ArrayList<>();
        for (Enchantment enchant : Registry.ENCHANTMENT) {
            if (this.player == null || !this.enchantmentMatchesCategory(ItemEditHistoryManager.getSingleton().getStaged(this.player.getUniqueId()), enchant)) {
                continue;
            }
            String enchantName = enchant.getKey().getKey();
            if (!enchantName.toLowerCase().contains(this.getSearchString().toLowerCase())) {
                continue;
            }

            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) book.getItemMeta();
            bookMeta.setDisplayName(EnchantmentHelper.getEnchantmentName(enchant));
            bookMeta.addStoredEnchant(enchant, this.category == Category.MAX ? enchant.getMaxLevel() : enchant.getStartLevel(), false);
            book.setItemMeta(bookMeta);
            results.add(book);
        }
        return results;
    }

    private boolean enchantmentMatchesCategory(ItemStack edit, Enchantment enchant) {
        switch (this.category) {
            case COMPATIBLE -> {
                if (edit.getType() == Material.ENCHANTED_BOOK) {
                    return true;
                }
                return enchant.canEnchantItem(edit);
            }
            case WEAPONS -> {
                return !Stream.of(Material.DIAMOND_SWORD, Material.BOW, Material.CROSSBOW, Material.TRIDENT, Material.MACE)
                        .map(ItemStack::new)
                        .filter(enchant::canEnchantItem)
                        .toList().isEmpty();
            }
            case TOOLS -> {
                return !Stream.of(Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE)
                        .map(ItemStack::new)
                        .filter(enchant::canEnchantItem)
                        .toList().isEmpty();
            }
            case ARMOR -> {
                return !Stream.of(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS)
                        .map(ItemStack::new)
                        .filter(enchant::canEnchantItem)
                        .toList().isEmpty();
            }
            case ALL, MAX -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Getter
    @AllArgsConstructor
    private enum Category {
        COMPATIBLE(ChatColor.GRAY, "GRAY"),
        WEAPONS(ChatColor.RED, "RED"),
        TOOLS(ChatColor.BLUE, "BLUE"),
        ARMOR(ChatColor.AQUA, "CYAN"),
        ALL(ChatColor.LIGHT_PURPLE, "PURPLE"),
        MAX(ChatColor.GOLD, "ORANGE");

        private final ChatColor chatColor;
        private final String paneColor;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    private class CategoryButton implements GUIElement {
        private ItemStack icon;
        private Category target;

        @Override
        public ItemStack getIcon(Player player) {
            return new ItemBuilder(icon.clone())
                    .setName(target.getChatColor() + icon.getItemMeta().getDisplayName())
                    .addLoreLines("§8Category", "", "§eClick to view", "§eenchantments")
                    .setItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .toItemStack();
        }

        @Override
        public void onClick(GUIElementClickEvent event) {
            Player player = event.getPlayer();
            this.playDing(player);
            setCategory(target);
            setColor(category.getPaneColor());
        }
    }

    protected class ScrollButton extends IndexButton {
        public ScrollButton(boolean increment) {
            super(increment);
        }

        @Override
        public ItemStack getButtonIcon(Player player) {
            int targetPage = selector.getIndex() + (increment ? 2 : 0);
            ItemBuilder iconBuilder = new ItemBuilder(Material.ARROW, targetPage);

            if (this.increment) {
                iconBuilder
                        .setName("§aDown")
                        .addLoreLine("§8§o" + (selector.getIndex() + 2))
                        .addLoreLine("")
                        .addLoreLine("§bRight click to go to end")
                        .addLoreLine("§eLeft click to scroll down");
            } else {
                iconBuilder
                        .setName("§aUp")
                        .addLoreLine("§8§o" + selector.getIndex())
                        .addLoreLine("")
                        .addLoreLine("§bRight click to go to start")
                        .addLoreLine("§eLeft click to scroll up");
            }
            return iconBuilder.toItemStack();
        }

    }

    @AllArgsConstructor
    protected class EnchantEditButton implements GUIElement {
        public final int index;
        @Override
        public ItemStack getIcon(Player player) {
            ItemStack icon = selector.getResults().get(this.index).clone();
            if (icon.getType() == Material.AIR) {
                return icon;
            }
            ItemMeta iconMeta = icon.getItemMeta();
            Enchantment enchantment = ((EnchantmentStorageMeta) iconMeta).getStoredEnchants().keySet().stream().toList().get(0);
            iconMeta.setDisplayName(category.getChatColor() + iconMeta.getDisplayName());

            ItemStack edit = ItemEditHistoryManager.getSingleton().getStaged(player.getUniqueId());
            if (edit.containsEnchantment(enchantment) ||
                    (edit.getItemMeta() instanceof EnchantmentStorageMeta storageMeta && storageMeta.hasStoredEnchant(enchantment))) {
                iconMeta.setLore(List.of("", "§cClick to remove", "§cEnchantment"));
            }

            icon.setItemMeta(iconMeta);
            return icon;
        }

        @Override
        public void onClick(GUIElementClickEvent event) {
            Player player = event.getPlayer();
            Enchantment enchantment = ((EnchantmentStorageMeta) selector.getResults().get(this.index).getItemMeta()).getStoredEnchants().keySet().stream().toList().get(0);
            int maxLevel = enchantment.getMaxLevel();
            this.playDing(player);
            if (maxLevel > 1 && category != Category.MAX) {
                GUIManager.getSingleton().nextGUI(player, new EnchantmentLevelFrame(player, enchantment));
                return;
            }

            ItemStack edit = ItemEditHistoryManager.getSingleton().getStaged(player.getUniqueId());
            edit = EnchantmentHelper.toggleEnchant(edit, enchantment, maxLevel);
            ItemEditHistoryManager.getSingleton().editStaged(player.getUniqueId(), edit);
        }
    }
}
