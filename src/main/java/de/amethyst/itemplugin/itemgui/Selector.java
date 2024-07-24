package de.amethyst.itemplugin.itemgui;

import java.util.List;

public interface Selector<T> {

    void setResultsPerSelection(int resultsPerSelection);

    void setIndex(int index);

    void setEntities(List<T> entities);

    default void increment() {
        this.setIndex(this.getIndex() + 1);
    }

    default void decrement() {
        this.setIndex(this.getIndex() - 1);
    }

    int getMaxIndex();

    List<T> getEntities();

    int getResultsPerSelection();

    int getIndex();

    List<T> getResults();
}
