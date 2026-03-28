package be.warrox.engine.gfx;

import be.warrox.engine.core.Window;
import be.warrox.engine.scene.Camera;
import be.warrox.engine.scene.Transform;
import be.warrox.engine.util.AssetManager;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int vaoId;
    private int vboId;
    private int eboId; // Index Buffer
    private int vertexCount;

    private Vector4f meshColor;
    private Transform transform;
    private String path;

    // Constructor for Color-based Mesh
    public Mesh(Vertex[] vertices, int[] indices, Vector4f color, Transform transform) {
        this(vertices, indices, (String) null, transform); // Call the main setup logic
        this.meshColor = color;
    }

    // Constructor for Texture-based Mesh
    public Mesh(Vertex[] vertices, int[] indices, String path, Transform transform) {
        this.vertexCount = indices.length;
        this.meshColor = new Vector4f(1, 1, 1, 1); // Default to white (no tint)
        this.transform = transform;
        this.path = path;

        setupMesh(vertices, indices);
    }

    private void setupMesh(Vertex[] vertices, int[] indices) {
        this.vertexCount = indices.length;
        FloatBuffer vertexBuffer = null;
        IntBuffer indexBuffer = null;




        try {
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // 1. Pack Vertex objects into a flat FloatBuffer
            vertexBuffer = MemoryUtil.memAllocFloat(vertices.length * Vertex.VERTEX_SIZE_FLOATS);
            for (Vertex v : vertices) {
                vertexBuffer.put(v.position.x).put(v.position.y).put(v.position.z);
                vertexBuffer.put(v.normal.x).put(v.normal.y).put(v.normal.z);
                vertexBuffer.put(v.texCoords.x).put(v.texCoords.y);
                vertexBuffer.put(v.color.x).put(v.color.y).put(v.color.z).put(v.color.w);
            }
            vertexBuffer.flip();

            // 2. VBO (Data)
            vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

            // 3. EBO (Indices)
            indexBuffer = MemoryUtil.memAllocInt(indices.length);
            indexBuffer.put(indices).flip();
            eboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

            // 4. Link Attributes (Using your Vertex constants!)
            int stride = Vertex.VERTEX_SIZE_BYTES;

            // Position
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, Vertex.POS_SIZE, GL_FLOAT, false, stride, Vertex.POS_OFFSET);

            // Normals
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, Vertex.NORMAL_SIZE, GL_FLOAT, false, stride, Vertex.NORMAL_OFFSET);

            // Texture Coords
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, Vertex.TEXCOORDS_SIZE, GL_FLOAT, false, stride, Vertex.TEXCOORDS_OFFSET);

            // Color
            glEnableVertexAttribArray(3);
            glVertexAttribPointer(3, Vertex.COLOR_SIZE, GL_FLOAT, false, stride, Vertex.COLOR_OFFSET);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

        } finally {
            if (vertexBuffer != null) MemoryUtil.memFree(vertexBuffer);
            if (indexBuffer != null) MemoryUtil.memFree(indexBuffer);
        }
    }




    public void draw(Shader shader, Camera camera) {
        // 1. Bind VAO
        glBindVertexArray(vaoId);

        // 2. Texture logica: Alleen ophalen en binden als het pad NIET null is
        if (path != null) {
            Texture texture = AssetManager.getTexture(path);
            if (texture != null) {
                texture.bind();
                shader.uploadBool("useTexture", true);
            }
        } else {
            // Als er geen texture is, vertel de shader dat hij de Vertex Kleur moet gebruiken
            shader.uploadBool("useTexture", false);
        }

        // 3. Matrices uploaden (Gebruik de matrices van de camera en transform)
        shader.uploadMat4f("uModel", this.transform.getModelMatrix());
        shader.uploadMat4f("uView", camera.getViewMatrix());

        // Zorg dat Window.width/height en camera.getFov() gebruikt worden voor de aspect ratio
        shader.uploadMat4f("uProjection", Transform.getProjectionMatrix(Window.width, Window.height, camera));

        // 4. Tekenen
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        // 5. Cleanup
        if (path != null) {
            // We halen de texture nogmaals op om te unbinden, of je bewaart de referentie even
            Texture texture = AssetManager.getTexture(path);
            if (texture != null) texture.unbind();
        }

        glBindVertexArray(0);
    }

    public void cleanup() {
        glDeleteVertexArrays(vaoId);
        glDeleteBuffers(vboId);
        glDeleteBuffers(eboId);

    }


}