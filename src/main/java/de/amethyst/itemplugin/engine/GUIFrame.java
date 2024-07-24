package de.amethyst.itemplugin.engine;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public interface GUIFrame {
    String getTitle();
    GUIElement getElement(int index);
    @NonNull GUIElement[] getContent();
    default void render(Player player) {
        Inventory guiInv = Bukkit.createInventory(null, this.getSize(), this.getTitle());
        ItemStack[] icons = Arrays.stream(this.getContent())
                .map(element -> element.getIcon(player))
                .toArray(ItemStack[]::new);
        guiInv.setContents(icons);
        player.openInventory(guiInv);
    }

    default int getXSize() {
        return 9;
    }

    default int getYSize() {
        return 6;
    }

    default int getSize() {
        return this.getXSize() * this.getYSize();
    }

    default GUIElement getElement(int x, int y) {
        if (x < 0 || x >= this.getXSize()) {
            throw new IndexOutOfBoundsException("x must be between 0 and %d!".formatted(this.getXSize()));
        }
        if (y < 0 || x >= this.getYSize()) {
            throw new IndexOutOfBoundsException("y must be between 0 and %d!".formatted(this.getYSize()));
        }
        int index = x + 9 * y;
        return this.getElement(index);
    }
}
