package de.amethyst.itemplugin.itemgui.edit;

import de.amethyst.itemplugin.engine.history.StagedHistoryManager;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemEditHistoryManager extends StagedHistoryManager<UUID, ItemStack> {
    @Getter
    private static final ItemEditHistoryManager singleton;

    static {
        singleton = new ItemEditHistoryManager();
    }
}