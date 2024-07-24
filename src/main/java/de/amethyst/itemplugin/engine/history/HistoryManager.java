package de.amethyst.itemplugin.engine.history;

public interface HistoryManager<K, E> {
    void addEntry(K key, E entry);

    void pop(K key);

    void clearHistory(K key);

    boolean hasHistory(K key);

    E getEntry(K key, int index);

    E getLastEntry(K key);

    int length(K key);
}
