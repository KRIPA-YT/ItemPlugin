package de.amethyst.itemplugin.itemgui;

import de.amethyst.itemplugin.ItemBuilder;
import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIElementClickEvent;
import de.amethyst.itemplugin.engine.GUIManager;
import de.amethyst.itemplugin.engine.templates.EmptyElement;
import de.amethyst.itemplugin.engine.templates.EmptyFrame;
import de.amethyst.itemplugin.engine.templates.HasAlternate;
import de.amethyst.itemplugin.engine.templates.RotatingSelectButton;
import de.amethyst.itemplugin.itemgui.edit.ItemEditFrame;
import de.rapha149.signgui.SignGUI;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class SelectionFrame extends EmptyFrame {
    @Getter(AccessLevel.PROTECTED)
    private String searchString;
    protected final FilledSelector<ItemStack> selector;
    @Getter(AccessLevel.PROTECTED)
    private SortMode sortMode = SortMode.SORT_ITEM_ID;
    @Getter(AccessLevel.PROTECTED)
    protected List<ItemStack> validEntities;

    private List<List<ItemStack>> validEntitiesList = new ArrayList<>();

    @Setter(AccessLevel.PROTECTED) @Getter(AccessLevel.PROTECTED)
    private Comparator<ItemStack> sorter = (first, second) -> String.CASE_INSENSITIVE_ORDER.compare(first.getType().getKey().getKey(), second.getType().getKey().getKey());

    public SelectionFrame(String searchString, FilledSelector<ItemStack> selector) {
        super("");
        this.selector = selector;
        this.setSearchString(searchString);
    }

    protected void setSortMode(SortMode sortMode) {
        this.sortMode = sortMode;
        this.order();
    }

    protected void setSearchString(String searchString) {
        this.searchString = searchString;
        this.updateValidEntities();
    }

    protected void setValidEntities(List<ItemStack> validEntities) {
        this.validEntities = validEntities;
        this.order();
    }

    protected void updateValidEntities() {
        this.setValidEntities(this.search());
    }

    protected void order() {
        List<ItemStack> results = new ArrayList<>(this.validEntities);

        this.selector.setEntities(switch (this.sortMode) {
            case SORT_ITEM_ID -> results;
            case SORT_ALPHABET -> {
                results.sort(this.sorter);
                yield results;
            }
            case SORT_REV_ALPHABET -> {
                results.sort(this.sorter);
                yield results.reversed();
            }
        });
    }

    protected abstract List<ItemStack> search();

    @Getter
    @AllArgsConstructor
    protected enum SortMode {
        SORT_ITEM_ID(0), SORT_ALPHABET(1), SORT_REV_ALPHABET(2);
        private final int value;
        public static SortMode valueOf(int value) {
            for (SortMode mode : values()) {
                if (mode.value == value) {
                    return mode;
                }
            }
            return SortMode.SORT_ITEM_ID;
        }
    }

    public class SearchButton implements GUIElement {
        @Override
        public ItemStack getIcon(Player player) {
            return new ItemBuilder(Material.OAK_SIGN)
                    .setName("§aSearch:")
                    .addLoreLine(getSearchString().isEmpty() ? "§8§oempty" : "§8" + getSearchString())
                    .addLoreLine("")
                    .addLoreLine("§bRight click to reset")
                    .addLoreLine("§eLeft click to search")
                    .toItemStack();
        }

        @Override
        public void onClick(GUIElementClickEvent event) {
            Player player = event.getPlayer();
            this.playDing(player);
            if (event.getClickType().isRightClick()) {
                setSearchString("");
                return;
            }
            SignGUI signGUI = SignGUI.builder()
                    .setLines(getSearchString(),
                            "^^^^^^^^^^^^^^^",
                            "Enter your",
                            "search here")
                    .setType(Material.OAK_SIGN)
                    .setHandler((signGUIPlayer, signGUIResult) -> {
                        setSearchString(signGUIResult.getLineWithoutColor(0).replace(' ', '_'));
                        GUIManager.getSingleton().reopenCurrentGUI(signGUIPlayer);
                        return Collections.emptyList();
                    })
                    .build();
            GUIManager.getSingleton().openExternalInventory(player);
            signGUI.open(player);
        }
    }

    protected class SortModeSelectButton extends RotatingSelectButton {
        public SortModeSelectButton() {
            super(new ItemBuilder(Material.HOPPER).setName("§aSort by:").toItemStack(),
                    "Item ID",
                    "Alphabet",
                    "Reverse Alphabet");
        }

        @Override
        public void onClick(GUIElementClickEvent event) {
            super.onClick(event);
            setSortMode(SortMode.valueOf(this.getSelectedOption()));
        }
    }

    @AllArgsConstructor
    protected class ItemButton implements GUIElement {
        public final int index;
        @Override
        public ItemStack getIcon(Player player) {
            return selector.getResults().get(this.index);
        }

        @Override
        public void onClick(GUIElementClickEvent event) {
            Player player = event.getPlayer();
            this.playDing(player);
            GUIManager.getSingleton().nextGUI(player, new ItemEditFrame(player, selector.getResults().get(this.index)));
        }
    }

    @AllArgsConstructor
    protected abstract class IndexButton implements GUIElement, HasAlternate {
        public boolean increment;
        @Setter @Getter
        private GUIElement alternate;

        public IndexButton(boolean increment) {
            this(increment, new EmptyElement());
        }

        @Override
        public ItemStack getIcon(Player player) {
            int targetPage = selector.getIndex() + (increment ? 2 : 0);
            if (targetPage <= 0 || targetPage > selector.getMaxIndex()) {
                return this.alternate.getIcon(player);
            }
            return this.getButtonIcon(player);
        }

        public abstract ItemStack getButtonIcon(Player player);

        @Override
        public void onClick(GUIElementClickEvent event) {
            Player player = event.getPlayer();
            int targetPage = selector.getIndex() + (increment ? 2 : 0);
            if (targetPage <= 0 || targetPage > selector.getMaxIndex()) {
                this.alternate.onClick(event);
                return;
            }
            this.playDing(player);
            if (!event.getClickType().isLeftClick()) {
                selector.setIndex(increment ? selector.getMaxIndex() - 1 : 0);
                return;
            }

            if (increment) {
                selector.increment();
            } else {
                selector.decrement();
            }
        }
    }
}
