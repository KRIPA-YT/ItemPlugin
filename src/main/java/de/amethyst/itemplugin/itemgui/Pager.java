package de.amethyst.itemplugin.itemgui;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode(callSuper = false)
@Getter
public class Pager<T> extends FilledSelector<T> {

    public Pager() {
        this(new ArrayList<>(), 4*7, 0, null);
    }

    public Pager(T filler) {
        this(new ArrayList<>(), 4*7, 0, filler);
    }

    public Pager(List<T> entities) {
        this(entities, 4*7, 0, null);
    }

    public Pager(List<T> entities, int page) {
        this(entities, 4*7, page, null);
    }

    public Pager(List<T> entities, int page, T filler) {
        this(entities, 4*7, page, null);
    }

    public Pager(int resultsPerSelection) {
        this(new ArrayList<>(), resultsPerSelection, 0, null);
    }

    public Pager(int resultsPerSelection, int page) {
        this(new ArrayList<>(), resultsPerSelection, page, null);
    }

    public Pager(int resultsPerSelection, int page, T filler) {
        this(new ArrayList<>(), resultsPerSelection, page, filler);
    }

    public Pager(List<T> entities, int resultsPerSelection, int page, T filler) {
        super(entities, resultsPerSelection, page, filler);
    }

    @Override
    public int getMaxIndex() {
        return (int) Math.ceil(entities.size() / (double) this.resultsPerSelection);
    }

    protected void recalculateResults() {
        int lower = this.index * resultsPerSelection;
        int upper = (this.index + 1) * resultsPerSelection;
        int pollUpper = Math.min(this.entities.size(), upper);
        this.results = new ArrayList<>(this.entities.subList(lower, pollUpper));
        if (this.filler != null) {
            this.results.addAll(Collections.nCopies(upper - pollUpper, this.filler));
        }
    }
}
