package be.warrox.game.util;

import be.warrox.engine.core.Window;
import be.warrox.engine.gameInterfaces.IHUD;
import be.warrox.engine.gfx.Renderer;
import be.warrox.engine.gui.Element;

import java.util.ArrayList;
import java.util.List;

public class MyHUD implements IHUD {
    private List<Element> elements = new ArrayList<>();

    public MyHUD() {
        init();
    }

    public void init() {
        int screenWidth = Window.width;
        int screenHeight = Window.height;

        // Afmetingen van de hotbar (Minecraft schaal x3)
        int hotbarWidth = 182 * 3;
        int hotbarHeight = 22 * 3;

        // Bereken de posities
        int x = (screenWidth / 2) - (hotbarWidth / 2);
        int y = screenHeight - hotbarHeight - 10; // 10 pixels marge van de onderkant

        addElement(new Element(x, y, hotbarWidth, hotbarHeight, "hotbar"));

        int crosshairSize = 15 * 3;


        // Bereken de posities
        x = (screenWidth / 2) - (crosshairSize / 2);
        y = (screenHeight / 2) - (crosshairSize / 2);
        addElement(new Element(x, y, crosshairSize, crosshairSize, "crosshair"));
    }


    public void addElement(Element element) {
        elements.add(element);
    }

    public void draw(Renderer renderer) {
        for (Element element : elements) {
            element.draw(renderer);
        }
    }
}
