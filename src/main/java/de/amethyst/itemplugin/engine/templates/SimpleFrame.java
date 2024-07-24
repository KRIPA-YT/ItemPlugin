package de.amethyst.itemplugin.engine.templates;

import de.amethyst.itemplugin.engine.GUIElement;
import lombok.NonNull;

public class SimpleFrame extends CustomizableFrame {
    public SimpleFrame(@NonNull String title) {
        super(title);
    }

    @Override
    public void setTitle(@NonNull String title) {
        super.setTitle(title);
    }

    @Override
    public void setContent(@NonNull GUIElement[] content) {
        if (content.length != this.getSize()) {
            throw new IllegalArgumentException("content must be of length %d!".formatted(this.getSize()));
        }
        super.setContent(content);
    }

    @Override
    public void setElement(int x, int y, GUIElement element) {
        super.setElement(x, y, element);
    }

    @Override
    public void setElement(int index, GUIElement element) {
        super.setElement(index, element);
    }
}
