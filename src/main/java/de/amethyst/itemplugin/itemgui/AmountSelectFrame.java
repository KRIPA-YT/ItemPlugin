package de.amethyst.itemplugin.itemgui;

import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import de.amethyst.itemplugin.engine.templates.CloseButton;
import de.amethyst.itemplugin.engine.templates.EmptyFrame;
import de.amethyst.itemplugin.engine.templates.PreviousButton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Setter
@Getter
public class AmountSelectFrame extends EmptyFrame {
    private ItemStack item;
    public AmountSelectFrame(ItemStack item) {
        super("Select Amount");

        this.setItem(item);
        this.setElement(2, 1, new GiveItemButton(1));
        this.setElement(3, 1, new GiveItemButton(5));
        this.setElement(4, 1, new GiveItemButton(10));
        this.setElement(5, 1, new GiveItemButton(32));
        this.setElement(6, 1, new GiveItemButton(64));
        this.setElement(4, 2, new PreviousButton(new CloseButton()));
    }

    @Getter
    @RequiredArgsConstructor
    private class GiveItemButton implements GUIElement {
        private final int amount;
        @Override
        public ItemStack getIcon(Player player) {
            return this.getAmountedItem();
        }

        @Override
        public void onClick(GUIElementClickEvent event) {
            Player player = event.getPlayer();
            event.setRefresh(false);
            if (!player.getInventory().addItem(this.getAmountedItem()).isEmpty()) {
                this.playFail(player);
                return;
            }
            this.playDing(player);
        }

        private ItemStack getAmountedItem() {
            ItemStack item = AmountSelectFrame.this.item.clone();
            item.setAmount(this.amount);
            return item;
        }
    }

    @Override
    public int getYSize() {
        return 3;
    }
}
