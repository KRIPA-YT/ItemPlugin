package de.amethyst.itemplugin.itemgui.edit;

import de.amethyst.itemplugin.ItemBuilder;
import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import de.amethyst.itemplugin.engine.GUIManager;
import de.amethyst.itemplugin.engine.templates.*;
import de.amethyst.itemplugin.itemgui.AmountSelectFrame;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemEditFrame extends EmptyFrame {

    public ItemEditFrame(Player player, ItemStack edit) {
        super("Edit Item");
        ItemEditHistoryManager.getSingleton().clearHistory(player.getUniqueId());
        ItemEditHistoryManager.getSingleton().addEntry(player.getUniqueId(), edit.clone());

        this.setElement(4, 1, new AmountSelectButton());
        this.setElement(2, 1, new EditHistoryButton(true));
        this.setElement(6, 1, new EditHistoryButton(false));
        this.setElement(5, 4, new EnchantmentEditButton());
        this.setElement(1, 4, new NameEditButton());
        this.setElement(4, 5, new PreviousButton(new CloseButton()));
    }

    private static class AmountSelectButton implements GUIElement {
        @Override
        public ItemStack getIcon(Player player) {
            return new ItemBuilder(ItemEditHistoryManager.getSingleton().getCurrentEntry(player.getUniqueId()).clone())
                    .addLoreLines("", "§eClick to select amount")
                    .toItemStack();
        }

        @Override
        public void onClick(GUIElementClickEvent event) {
            Player player = event.getPlayer();
            this.playDing(player);
            GUIManager.getSingleton().nextGUI(player, new AmountSelectFrame(ItemEditHistoryManager.getSingleton().getCurrentEntry(player.getUniqueId())));
        }
    }

    protected static class EditItemDisplay implements GUIElement {
        @Override
        public ItemStack getIcon(Player player) {
            return ItemEditHistoryManager.getSingleton().getStaged(player.getUniqueId());
        }

        @Override
        public void onClick(GUIElementClickEvent event) {
            event.setRefresh(false);
        }
    }

    @AllArgsConstructor
    @Getter
    private static class EditHistoryButton implements GUIElement {
        private final boolean undo;
        @Override
        public ItemStack getIcon(Player player) {
            if (ItemEditHistoryManager.getSingleton().isStart(player.getUniqueId()) && this.undo) {
                return new EmptyElement().getIcon(player);
            }
            if (ItemEditHistoryManager.getSingleton().isEnd(player.getUniqueId()) && !this.undo) {
                return new EmptyElement().getIcon(player);
            }
            return new ItemBuilder(Material.ARROW)
                    .setName(this.undo ? "§aUndo" : "§aRedo")
                    .addLoreLines("", "§eClick to " + (this.undo ? "undo" : "redo"))
                    .toItemStack();
        }

        @Override
        public void onClick(GUIElementClickEvent event) {
            Player player = event.getPlayer();
            try {
                if (this.undo) {
                    ItemEditHistoryManager.getSingleton().undo(player.getUniqueId());
                    this.playDing(player);
                } else {
                    ItemEditHistoryManager.getSingleton().redo(player.getUniqueId());
                    this.playDing(player);
                }
            } catch (IndexOutOfBoundsException ignored) {
                // If undo or redo impossible
            }
        }
    }

    private static class EnchantmentEditButton implements GUIElement {
        @Override
        public ItemStack getIcon(Player player) {
            return new ItemBuilder(Material.ENCHANTED_BOOK)
                    .setName("§aEdit Enchantments")
                    .addLoreLines("", "§eClick to edit")
                    .toItemStack();
        }

        @Override
        public void onClick(GUIElementClickEvent event) {
            Player player = event.getPlayer();
            GUIManager.getSingleton().nextGUI(player, new EnchantmentEditFrame(player));
            this.playDing(player);
        }
    }

}
