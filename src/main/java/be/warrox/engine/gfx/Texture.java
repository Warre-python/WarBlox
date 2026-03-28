package be.warrox.engine.gfx;

import org.lwjgl.BufferUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filepath;
    private int texID;

    public Texture(String filepath) {
        this.filepath = filepath;

        // 1. Genereer texture op GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // 2. Stel parameters in (Perfect voor Minecraft-stijl: NEAREST = pixelachtig)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_NEAREST); // Gebruik mipmaps voor afstand
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // 3. Voorbereiden buffers voor STB
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);

        // 4. Probeer de afbeelding te laden
        ByteBuffer image = stbi_load(this.filepath, width, height, channels, 4);

        if (image != null) {
            // Succes! Upload naar GPU
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                    0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);

            // Geheugen op de CPU direct weer vrijgeven
            stbi_image_free(image);
        } else {
            // Fout! Print het volledige pad zodat je kunt zien waar het misgaat
            String absolutePath = new java.io.File(this.filepath).getAbsolutePath();
            System.err.println("!!! ERROR: Texture niet gevonden !!!");
            System.err.println("Gezocht op pad: " + filepath);
            System.err.println("Volledig systeem-pad: " + absolutePath);

            // Optioneel: gooi een runtime exception zodat de game direct stopt met een duidelijke reden
            // throw new RuntimeException("Kon texture niet laden: " + filepath);
        }
    }

    public int getTexID() {
        return texID;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }


}
