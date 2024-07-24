package de.amethyst.itemplugin.itemgui;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString(callSuper = true)
public class Scroller<T> extends FilledSelector<T> {
    private int lineLength;

    public Scroller(T filler) {
        this(new ArrayList<>(), 4*7, 0, filler, 7);
    }

    public Scroller(List<T> entities, int resultsPerSelection, int page, T filler) {
        this(entities, resultsPerSelection, page, filler, 7);
    }

    public Scroller(List<T> entities, int resultsPerSelection, int page, T filler, int lineLength) {
        super(entities, resultsPerSelection, page, filler);
        this.setLineLength(lineLength);
    }

    public void setLineLength(int lineLength) {
        if (this.resultsPerSelection % lineLength != 0) {
            throw new IllegalArgumentException("resultsPerSelection has to be divisible by lineLength!");
        }
        this.lineLength = lineLength;
        this.recalculateResults();
    }

    @Override
    public int getMaxIndex() {
        return Math.max(0, (int) Math.ceil((double) this.entities.size() / this.lineLength - ((double) this.resultsPerSelection / this.lineLength)) + 1);
    }

    protected void recalculateResults() {
        int rows = (int) ((double) this.resultsPerSelection / this.lineLength);
        int lower = this.index * this.lineLength;
        int upper = (this.index + rows) * this.lineLength;
        int pollUpper = Math.min(this.entities.size(), upper);
        this.results = new ArrayList<>(this.entities.subList(lower, pollUpper));
        if (this.filler != null) {
            this.results.addAll(Collections.nCopies(upper - pollUpper, this.filler));
        }
    }
}
