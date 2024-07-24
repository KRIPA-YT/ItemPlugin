package de.amethyst.itemplugin.engine.history;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;

@Getter
@RequiredArgsConstructor
public class History<T> extends LinkedList<T> {
    public T setLast(T element) {
        return this.set(this.size() - 1, element);
    }
}
