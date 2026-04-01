package be.warrox.engine.gfx;

import java.util.HashMap;
import java.util.Map;

public class MeshData {
    public final Map<String, Vertex[]> vertices = new HashMap<>();
    public final Map<String, int[]> indices = new HashMap<>();
}
