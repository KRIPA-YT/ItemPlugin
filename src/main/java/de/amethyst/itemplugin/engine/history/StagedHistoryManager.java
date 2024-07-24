package de.amethyst.itemplugin.engine.history;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class StagedHistoryManager<K, E> extends OrderedHistoryManager<K, E> {
    private final Map<K, E> stage = new HashMap<>();

    @Override
    public void clearHistory(K key) {
        super.clearHistory(key);
        this.clearStaged(key);
    }

    public void editStaged(K key, E element) {
        this.stage.put(key, element);
    }

    public E getStaged(K key) {
        if (!this.stage.containsKey(key)) {
            this.initStage(key);
        }
        return this.stage.get(key);
    }

    @SuppressWarnings("unchecked")
    public void initStage(K key) {
        if (!this.stage.containsKey(key)) {
            E element = this.getCurrentEntry(key);
            try {
                Method clone = element.getClass().getMethod("clone");
                clone.setAccessible(true);
                Object cloneResult = clone.invoke(element);
                element = (E) cloneResult;
            } catch (ReflectiveOperationException | ClassCastException ignored) {

            } finally {
                this.stage.put(key, element);
            }
        }
    }

    public void push(K key) {
        if (!this.stage.containsKey(key)) {
            throw new IllegalArgumentException("%s does not have staged changes!".formatted(key.toString()));
        }
        if (this.getStaged(key).equals(this.getCurrentEntry(key))) {
            this.clearStaged(key);
            return;
        }
        this.addEntry(key, this.getStaged(key));
        this.clearStaged(key);
    }

    public void clearStaged(K key) {
        this.stage.remove(key);
    }


}
