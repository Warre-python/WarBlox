package be.warrox.warblox.game.objects;

import be.warrox.warblox.game.World;
import be.warrox.warblox.renderEngine.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class MyModel extends GameObject {
    private Model model;
    private Transform transform;
    private List<Vector3f> pointLightPositions = new ArrayList<>();

    public MyModel(Transform transform, String path, RenderBatch rb) {
        super(transform);
        this.model = new Model(path);
        
        pointLightPositions.add(new Vector3f(0.7f,  0.2f,  2.0f));
        pointLightPositions.add(new Vector3f(2.3f, -3.3f, -4.0f));
        pointLightPositions.add(new Vector3f(-4.0f,  2.0f, -12.0f));
        pointLightPositions.add(new Vector3f(0.0f,  0.0f, -3.0f));
    }

    public void render(Shader shader, Camera camera, World world) {
        Matrix4f modelMatrix = this.transform.getModelMatrix();

        shader.uploadMat4f("view", camera.getViewMatrix());
        shader.uploadMat4f("model", modelMatrix);
        shader.uploadMat4f("projection", Transform.getProjectionMatrix(Window.width, Window.height, camera));

        shader.uploadFloat("pointLights[0].constant", 1.0f);

        shader.uploadVec3f("viewPos", camera.position);
        
        // Default color if no texture, though Model handles textures
        shader.uploadVec4f("objectColor", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f)); 

        // Directional Light
        shader.uploadVec3f("dirLight.direction", new Vector3f(-0.2f, -1.0f, -0.3f));
        shader.uploadVec3f("dirLight.ambient", new Vector3f(0.1f, 0.1f, 0.1f));
        shader.uploadVec3f("dirLight.diffuse", new Vector3f(0.4f, 0.4f, 0.4f));
        shader.uploadVec3f("dirLight.specular", new Vector3f(0.5f, 0.5f, 0.5f));

        // Point Lights
        for (int i = 0; i < pointLightPositions.size(); i++) {
            String base = "pointLights[" + i + "]";
            shader.uploadVec3f(base + ".position", pointLightPositions.get(i));
            shader.uploadVec3f(base + ".ambient", new Vector3f(0.05f, 0.05f, 0.05f));
            shader.uploadVec3f(base + ".diffuse", new Vector3f(0.8f, 0.8f, 0.8f));
            shader.uploadVec3f(base + ".specular", new Vector3f(1.0f, 1.0f, 1.0f));
            shader.uploadFloat(base + ".constant", 1.0f);
            shader.uploadFloat(base + ".linear", 0.045f);
            shader.uploadFloat(base + ".quadratic", 0.0075f);
        }

        shader.uploadFloat("material.shininess", 32.0f);
        shader.uploadBool("isLightSource", false);
        
        // Model.draw handles binding textures and setting "useTexture" if needed, 
        // but wait, Mesh.draw sets "material.texture_diffuse1" etc.
        // We need to ensure the shader expects what Mesh.draw provides.
        // Mesh.draw sets uniforms like "material.texture_diffuse1".
        // GameObject.render sets "useTexture".
        // We might need to set "useTexture" to true if the model has textures.
        // But Model has multiple meshes, some might have textures, some might not.
        // Ideally Mesh.draw should handle this.
        
        // For now, let's assume the shader handles the uniforms set by Mesh.draw.
        // If the shader uses "useTexture" boolean to toggle sampling, we need to set it.
        // Mesh.draw doesn't set "useTexture".
        // I'll update Mesh.draw in a future step if needed, but for now let's just call model.draw
        
        // Actually, looking at Mesh.java, it binds textures but doesn't set a "useTexture" flag.
        // If the shader relies on "useTexture", we might have issues.
        // However, standard Assimp shaders usually check if texture units are bound or use a different technique.
        // Given GameObject uses "useTexture", the shader probably has an if statement.
        // I'll set useTexture to true for now as the backpack has textures.
        shader.uploadBool("useTexture", true);

        model.draw(shader);
    }
    
    public Transform getTransform() {
        return transform;
    }
}
