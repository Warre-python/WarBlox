package be.warrox.warblox.renderEngine;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Color {
    private Vector4f color = new Vector4f();

    public Color(Vector3f color) {
        this.color = new Vector4f(color.x, color.y, color.z, 1);
    }

    public Color(Vector4f color) {
        this.color = color;
    }

    public Color(String nameOfColor) {
        switch (nameOfColor.toLowerCase()) { // .toLowerCase() voor extra veiligheid
            case "red":
                this.color = new Vector4f(1, 0, 0, 1);
                break;
            case "green":
                this.color = new Vector4f(0, 1, 0, 1);
                break;
            case "blue":
                this.color = new Vector4f(0, 0, 1, 1);
                break;
            case "yellow":
                this.color = new Vector4f(1, 1, 0, 1);
                break;
            case "cyan":
                this.color = new Vector4f(0, 1, 1, 1);
                break;
            case "magenta":
                this.color = new Vector4f(1, 0, 1, 1);
                break;
            case "white":
                this.color = new Vector4f(1, 1, 1, 1);
                break;
            case "black":
                this.color = new Vector4f(0, 0, 0, 1);
                break;
            case "gray":
                this.color = new Vector4f(0.5f, 0.5f, 0.5f, 1);
                break;
            case "orange":
                this.color = new Vector4f(1, 0.5f, 0, 1);
                break;
            case "pink":
                this.color = new Vector4f(1, 0.68f, 0.68f, 1);
                break;
            case "purple":
                this.color = new Vector4f(0.5f, 0, 0.5f, 1);
                break;
            case "brown":
                this.color = new Vector4f(0.6f, 0.3f, 0, 1);
                break;
            default:
                // Fallback naar wit als de kleur onbekend is
                this.color = new Vector4f(1, 1, 1, 1);
                System.out.println("Unknown color: " + nameOfColor);
                break;
        }
    }


    public Vector4f getColorV4f() {
        return color;
    }

    public Vector3f getColorV3f() {
        return new Vector3f(color.x, color.y, color.z);
    }

    public void setColorV4f(Vector4f color) {
        this.color = color;
    }

    public void setColorV3f(Vector3f color) {
        this.color = new Vector4f(color.x, color.y, color.z, 1);
    }

    public void setColor(float r, float g, float b, float a) {
        this.color = new Vector4f(r, g, b, a);
    }

    public void setColor(float r, float g, float b) {
        this.color = new Vector4f(r, g, b, 1);
    }

}
