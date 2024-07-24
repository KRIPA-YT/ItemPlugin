package de.amethyst.itemplugin.engine.templates;

import de.amethyst.itemplugin.engine.GUIElement;
import lombok.NonNull;

import java.util.stream.IntStream;

public class EmptyFrame extends CustomizableFrame {
    public EmptyFrame(@NonNull String title) {
        super(title);
        content = IntStream.range(0, this.getSize())
                .mapToObj(i -> new EmptyElement())
                .toArray(GUIElement[]::new);
    }

    protected void setColor(String color) {
        for (GUIElement element : content) {
            if (element instanceof EmptyElement empty) {
                empty.setColor(color);
                continue;
            }
            if (element instanceof HasAlternate alternate && alternate.getAlternate() instanceof EmptyElement empty) {
                empty.setColor(color);
                continue;
            }
        }
    }
}
