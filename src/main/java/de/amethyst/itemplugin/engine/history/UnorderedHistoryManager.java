package de.amethyst.itemplugin.engine.history;

import java.util.HashMap;
import java.util.Map;

public class UnorderedHistoryManager<K, E> implements HistoryManager<K, E> {
    protected final Map<K, History<E>> history = new HashMap<>();
    @Override
    public void addEntry(K key, E entry) {
        this.ensureHistoryEntry(key);
        History<E> keyHistory = this.history.get(key);
        keyHistory.add(entry);
        this.history.put(key, keyHistory);
    }

    @Override
    public void pop(K key) {
        this.ensureHistoryEntry(key);
        History<E> keyHistory = this.history.get(key);
        keyHistory.removeLast();
        this.history.put(key, keyHistory);
    }

    @Override
    public void clearHistory(K key) {
        this.history.remove(key);
    }

    @Override
    public boolean hasHistory(K key) {
        return this.history.containsKey(key) && !this.history.get(key).isEmpty();
    }

    @Override
    public E getEntry(K key, int index) {
        return this.history.get(key).get(index);
    }

    @Override
    public E getLastEntry(K key) {
        if (!this.hasHistory(key)) {
            return null;
        }
        return this.history.get(key).getLast();
    }

    @Override
    public int length(K key) {
        return this.history.get(key).size();
    }

    protected void ensureHistoryEntry(K key) {
        if (this.history.containsKey(key)) {
            return;
        }
        this.history.put(key, new History<>());
    }
}
