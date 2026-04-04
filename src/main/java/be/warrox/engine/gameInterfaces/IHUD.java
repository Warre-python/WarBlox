package be.warrox.engine.gameInterfaces;

import be.warrox.engine.gfx.Renderer;
import be.warrox.engine.gui.Element;

public interface IHUD {
    void init();
    void addElement(Element element);
    void draw(Renderer renderer);
}
