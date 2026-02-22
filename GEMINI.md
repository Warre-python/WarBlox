# WarBlox

This is a game built using LWJGL, following the [learnopengl.com](https://learnopengl.com) tutorials.

## Project Structure

The source code is located in `src/main/java/be/warrox/warblox/`.

### Game Logic (`/game`)
Contains the core game loop and world management.
- `Game.java`: Main game loop.
- `World.java`: World management.
- `/objects`: Contains game object definitions.

### Render Engine (`/renderEngine`)
Handles the low-level rendering, input, and scene management.

- **Window & Input**:
  - `Window.java`: Manages the application window.
  - `KeyListener.java`: Handles keyboard input.
  - `MouseListener.java`: Handles mouse input.

- **Rendering**:
  - `RenderBatch.java`: Handles batch rendering.
  - `Shader.java`: Manages OpenGL shaders.
  - `Texture.java`: Handles texture loading and binding.
  - `Camera.java`: Manages the view.

- **Scene Graph & Components**:
  - `GameObject.java`: Base class for objects in the scene.
  - `Transform.java`: Handles position, rotation, and scale.

- **Geometry**:
  - `Mesh.java`
  - `Model.java`
  - `Vertex.java`
  - `Primitives.java`

## How to Run

To run the game, use the Gradle wrapper:

```bash
./gradlew run
```
