package de.amethyst.itemplugin.engine.templates;

import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class SimpleElement implements GUIElement {
    @NonNull
    private ItemStack icon;
    private Consumer<GUIElementClickEvent> clickHandler = null;

    public ItemStack getIcon(Player player) {
        return this.icon;
    }

    @Override
    public void onClick(GUIElementClickEvent event) {
        if (clickHandler == null) {
            return;
        }
        clickHandler.accept(event);
    }
}
