package be.warrox.warblox.renderEngine;

import be.warrox.warblox.game.World;
import be.warrox.warblox.game.objects.LightSource;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sin;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public abstract class GameObject {
    public Vector4f color;
    public Texture texture;
    public Transform transform;

    protected int elementCount;
    private float[] vertices;
    private int[] indices;
    private int vao;

    /**
     * A protected constructor for GameObjects that handle their own rendering (e.g., Models).
     * This constructor bypasses the default mesh setup.
     */
    protected GameObject(Transform transform) {
        this.transform = transform;
        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.elementCount = 0;
        this.vao = -1; // Indicates no VAO for this GameObject itself
    }


    private List<Vector3f> pointLightPositions = new ArrayList<>();


    public GameObject(Transform transform, Vector4f color, float[] vertices, int[] indices) {
        this.transform = transform;
        this.color = color;
        this.elementCount = indices.length;
        this.vertices = vertices;
        this.indices = indices;
        init();
    }

    public GameObject(Transform transform, String path, float[] vertices, int[] indices) {
        this.transform = transform;
        // Updated to pass a default type "texture_diffuse"
        this.texture = new Texture(path, "texture_diffuse");
        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.elementCount = indices.length;
        this.vertices = vertices;
        this.indices = indices;
        init();
    }

    public void init() {
        vao = RenderBatch.setupMesh(vertices, indices);

        pointLightPositions.add(new Vector3f(0.7f,  0.2f,  2.0f));
        pointLightPositions.add(new Vector3f(2.3f, -3.3f, -4.0f));
        pointLightPositions.add(new Vector3f(-4.0f,  2.0f, -12.0f));
        pointLightPositions.add(new Vector3f(0.0f,  0.0f, -3.0f));
    }

    public void render(Shader shader, Camera camera, World world) {
        Matrix4f model = this.transform.getModelMatrix();

        shader.uploadMat4f("view", camera.getViewMatrix());
        shader.uploadMat4f("model", model);
        shader.uploadMat4f("projection", Transform.getProjectionMatrix(Window.width, Window.height, camera));

        shader.uploadFloat("pointLights[0].constant", 1.0f);

        shader.uploadVec3f("viewPos", camera.position);

        shader.uploadVec3f("viewPos", camera.position);
        shader.uploadVec4f("objectColor", this.color); // Upload de kleur van het object

        // Directional Light (Zon) - Belangrijk: anders zie je niks zonder pointlights
        shader.uploadVec3f("dirLight.direction", new Vector3f(-0.2f, -1.0f, -0.3f));
        shader.uploadVec3f("dirLight.ambient", new Vector3f(0.1f, 0.1f, 0.1f));
        shader.uploadVec3f("dirLight.diffuse", new Vector3f(0.4f, 0.4f, 0.4f));
        shader.uploadVec3f("dirLight.specular", new Vector3f(0.5f, 0.5f, 0.5f));

        // Point Lights upload loop (je bestaande code is goed)
        for (int i = 0; i < pointLightPositions.size(); i++) {
            String base = "pointLights[" + i + "]";
            shader.uploadVec3f(base + ".position", pointLightPositions.get(i));

            // Standaard lichtsterkte waarden
            shader.uploadVec3f(base + ".ambient", new Vector3f(0.05f, 0.05f, 0.05f));
            shader.uploadVec3f(base + ".diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
            shader.uploadVec3f(base + ".specular", new Vector3f(1.0f, 1.0f, 1.0f));

            // Attenuation (bereik)
            shader.uploadFloat(base + ".constant", 1.0f);
            shader.uploadFloat(base + ".linear", 0.045f);    // Lager = licht reikt verder
            shader.uploadFloat(base + ".quadratic", 0.0075f); // Lager = zachtere afloop
        }

        shader.uploadFloat("material.shininess", 32.0f);

        // Check of dit object een LightSource is
        if (this instanceof LightSource) {
            shader.uploadBool("isLightSource", true);
        } else {
            shader.uploadBool("isLightSource", false);
        }

        if (texture != null) {
            shader.uploadBool("useTexture", true);
        } else {
            shader.uploadBool("useTexture", false);
        }


        glBindVertexArray(vao);
        if (this.texture != null) this.texture.bind();

        // Use the specific element count for this object
        glDrawElements(GL_TRIANGLES, elementCount, GL_UNSIGNED_INT, 0);

        if (this.texture != null) this.texture.unbind();
        glBindVertexArray(0);
    }

    public void update() {

    }

    public Transform getTransform() {
        return this.transform;
    }
}
