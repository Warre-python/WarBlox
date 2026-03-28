package be.warrox.engine.util;

import be.warrox.engine.gfx.Texture;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    static Map<String, Texture> textureMap = new HashMap<String, Texture>();

    public AssetManager() {

    }

    public static Texture getTexture(String shortName) {
        // Als de naam null is, geef niets terug
        if (shortName == null) return null;

        // Kijk of we de texture al eens geladen hebben
        if (textureMap.containsKey(shortName)) {
            return textureMap.get(shortName);
        }


        String fullPath = "assets/textures/block/" + shortName + ".png";

        // Maak de nieuwe texture aan en sla hem op in de map (cache)
        Texture texture = new Texture(fullPath);
        textureMap.put(shortName, texture);

        return texture;
    }
}
