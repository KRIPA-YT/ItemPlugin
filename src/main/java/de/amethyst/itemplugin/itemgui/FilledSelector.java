package de.amethyst.itemplugin.itemgui;

import com.google.common.primitives.Ints;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
public abstract class FilledSelector<T> implements Selector<T> {
    protected List<T> entities;
    protected int resultsPerSelection;
    protected int index;
    protected T filler;
    protected List<T> results;

    public FilledSelector(List<T> entities, int resultsPerSelection, int page, T filler) {
        this.setEntities(entities);
        this.setResultsPerSelection(resultsPerSelection);
        this.setIndex(page);
        this.setFiller(filler);
    }

    @Override
    public void setResultsPerSelection(int resultsPerSelection) {
        if (resultsPerSelection < 0) {
            throw new IndexOutOfBoundsException("resultsPerSelection must not be negative!");
        }
        this.resultsPerSelection = resultsPerSelection;
        this.ensureIndexBelowMaxIndex();
        this.recalculateResults();
    }

    @Override
    public void setIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("index must not be negative!");
        }
        if (index > this.getMaxIndex()) {
            throw new IndexOutOfBoundsException("index can't be bigger than max index!");
        }
        this.index = index;
        this.recalculateResults();
    }

    @Override
    public void setEntities(List<T> entities) {
        this.entities = entities;
        this.index = 0;
        this.ensureIndexBelowMaxIndex();
        this.recalculateResults();
    }

    public void setFiller(T filler) {
        this.filler = filler;
        this.recalculateResults();
    }

    private void ensureIndexBelowMaxIndex() {
        this.index = Ints.constrainToRange(this.getIndex(), 0, this.getMaxIndex());
    }

    protected abstract void recalculateResults();
}
