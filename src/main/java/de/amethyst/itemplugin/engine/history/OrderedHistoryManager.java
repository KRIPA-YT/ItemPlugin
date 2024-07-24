package de.amethyst.itemplugin.engine.history;

import java.util.HashMap;
import java.util.Map;

public class OrderedHistoryManager<K, E> extends UnorderedHistoryManager<K, E> {
    protected final Map<K, Integer> pointer = new HashMap<>();

    @Override
    public void addEntry(K key, E entry) {
        this.ensureHistoryEntry(key);
        this.ensureModifyLast(key);
        super.addEntry(key, entry);
        this.pointer.put(key, this.pointer.get(key) + 1);
    }

    @Override
    public void pop(K key) {
        this.ensureHistoryEntry(key);
        this.ensureModifyLast(key);
        super.pop(key);
        this.pointer.put(key, this.pointer.get(key) - 1);
    }

    @Override
    public void clearHistory(K key) {
        super.clearHistory(key);
        this.pointer.remove(key);
    }

    @Override
    public boolean hasHistory(K key) {
        return super.hasHistory(key) && this.pointer.containsKey(key);
    }

    public E getCurrentEntry(K key) {
        if (!this.hasHistory(key)) {
            return null;
        }
        return this.history.get(key).get(this.pointer.get(key) - 1);
    }

    public void undo(K key) {
        int pointer = this.pointer.get(key);
        if (this.isStart(key)) {
            throw new IndexOutOfBoundsException("Can't undo, pointer is at first position");
        }
        this.pointer.put(key, pointer - 1);
    }

    public void redo(K key) {
        int pointer = this.pointer.get(key);
        if (this.isEnd(key)) {
            throw new IndexOutOfBoundsException("Can't redo, pointer is at last position");
        }
        this.pointer.put(key, pointer + 1);
    }

    public boolean isStart(K key) {
        return pointer.get(key) <= 1;
    }

    public boolean isEnd(K key) {
        return pointer.get(key) >= this.history.get(key).size();
    }

    protected void ensureModifyLast(K key) {
        this.history.get(key).subList(this.pointer.get(key), this.history.get(key).size()).clear();
    }

    @Override
    protected void ensureHistoryEntry(K key) {
        super.ensureHistoryEntry(key);
        if (this.pointer.containsKey(key)) {
            return;
        }
        this.pointer.put(key, 0);
    }
}
