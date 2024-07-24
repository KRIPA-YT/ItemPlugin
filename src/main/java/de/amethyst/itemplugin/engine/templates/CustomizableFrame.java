package de.amethyst.itemplugin.engine.templates;

import de.amethyst.itemplugin.engine.GUIElement;
import de.amethyst.itemplugin.engine.GUIFrame;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.stream.IntStream;

@Setter(AccessLevel.PROTECTED)
@Getter
public class CustomizableFrame implements GUIFrame {
    @NonNull
    protected String title;
    protected GUIElement[] content;

    public CustomizableFrame(@NonNull String title) {
        this.title = title;
        this.content = IntStream.range(0, this.getSize())
                .mapToObj(i -> new AirElement())
                .toArray(GUIElement[]::new);
    }

    protected void setElement(int x, int y, GUIElement element) {
        if (x < 0 || x >= this.getXSize()) {
            throw new IndexOutOfBoundsException("x must be between 0 and %d!".formatted(this.getXSize()));
        }
        if (y < 0 || y >= this.getYSize()) {
            throw new IndexOutOfBoundsException("y must be between 0 and %d!".formatted(this.getYSize()));
        }
        int index = x + 9 * y;
        this.setElement(index, element);
    }

    protected void setElement(int index, GUIElement element) {
        if (index < 0 || index >= this.getSize()) {
            throw new IndexOutOfBoundsException("index must be between 0 and %d!".formatted(this.getSize()));
        }
        this.content[index] = element;
    }

    @Override
    public GUIElement getElement(int index) {
        if (index < 0 || index >= this.getSize()) {
            throw new IndexOutOfBoundsException("index must be between 0 and %d!".formatted(this.getSize()));
        }
        return content[index];
    }
}
