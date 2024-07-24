package de.amethyst.itemplugin.itemgui;

import de.amethyst.itemplugin.ItemBuilder;
import de.amethyst.itemplugin.engine.templates.CloseButton;
import de.amethyst.itemplugin.engine.templates.PreviousButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemSelectFrame extends SelectionFrame {
    public ItemSelectFrame(String searchString) {
        super(searchString, new Pager<>(new ItemStack(Material.AIR)));

        this.setElement(0, 5, new PageButton(false));          // Backwards
        this.setElement(8, 5, new PageButton(true));           // Forwards
        this.setElement(3, 5, new PreviousButton(new CloseButton()));   // Close Button
        this.setElement(4, 5, new SearchButton());                      // Search Button
        this.setElement(5, 5, new SortModeSelectButton());              // Mode Selection

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 7; x++) {
                int index = y * 7 + x;
                this.setElement(x + 1, y + 1, new ItemButton(index));
            }
        }
    }

    public ItemSelectFrame() {
        this("");
    }

    @Override
    public void render(Player player) {
        this.setTitle("Select Item [%d/%d]".formatted(this.selector.getIndex() + 1, this.selector.getMaxIndex()));
        super.render(player);
    }

    @Override
    protected List<ItemStack> search() {
        List<ItemStack> results = new ArrayList<>();
        for (Material material : Material.values()) {
            if (!material.isItem()) {
                continue;
            }
            if (material.isAir()) {
                continue;
            }
            if (!material.toString().toLowerCase().contains(this.getSearchString().toLowerCase())) {
                continue;
            }
            results.add(new ItemStack(material));
        }
        return results;
    }

    protected class PageButton extends IndexButton {
        public PageButton(boolean increment) {
            super(increment);
        }

        @Override
        public ItemStack getButtonIcon(Player player) {
            int targetPage = selector.getIndex() + (increment ? 2 : 0);
            ItemBuilder iconBuilder = new ItemBuilder(Material.ARROW, targetPage);

            if (this.increment) {
                iconBuilder
                        .setName("§aNext Page")
                        .addLoreLine("§8§o" + (selector.getIndex() + 2))
                        .addLoreLine("")
                        .addLoreLine("§bRight click to go to end")
                        .addLoreLine("§eLeft click for next page");
            } else {
                iconBuilder
                        .setName("§aPrevious Page")
                        .addLoreLine("§8§o" + selector.getIndex())
                        .addLoreLine("")
                        .addLoreLine("§bRight click to go to start")
                        .addLoreLine("§eLeft click for previous page");
            }
            return iconBuilder.toItemStack();
        }

    }
}
