package be.warrox.engine.util;

import be.warrox.engine.core.Window;
import be.warrox.engine.scene.Camera;
import be.warrox.engine.scene.Transform;
import be.warrox.game.world.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Raycast {
    private final Vector3f origin;
    private final Vector3f direction;
    private final float maxDistance;

    public Raycast(Vector3f origin, Vector3f direction, float maxDistance) {
        this.origin = new Vector3f(origin);
        this.direction = new Vector3f(direction).normalize();
        this.maxDistance = maxDistance;
    }

    /**
     * Creates a ray starting from the camera position and pointing in the camera's front direction.
     * This is typically used when the crosshair is fixed in the center of the screen.
     */
    public static Raycast fromCamera(Camera camera, float maxDistance) {
        return new Raycast(camera.position, camera.front, maxDistance);
    }

    /**
     * Creates a ray from the center of the screen, accounting for potential projection offsets.
     * In most first-person games where the crosshair is centered, this is equivalent to fromCamera.
     */
    public static Raycast fromScreenCenter(Camera camera, float maxDistance) {
        // Since the crosshair in MyHUD is placed at (screenWidth/2, screenHeight/2),
        // and the camera.front vector represents the look direction at the center of the screen,
        // we can use fromCamera.
        return fromCamera(camera, maxDistance);
    }

    public static class RaycastResult {
        public Vector3f blockPos;
        public Vector3f faceNormal;
        public float distance;

        public RaycastResult(Vector3f blockPos, Vector3f faceNormal, float distance) {
            this.blockPos = blockPos;
            this.faceNormal = faceNormal;
            this.distance = distance;
        }
    }

    /**
     * Fast Voxel Traversal Algorithm for Ray Tracing
     */
    public RaycastResult cast(World world) {
        float x = origin.x;
        float y = origin.y;
        float z = origin.z;

        // Current voxel coordinates
        int ix = (int) Math.floor(x);
        int iy = (int) Math.floor(y);
        int iz = (int) Math.floor(z);

        float dx = direction.x;
        float dy = direction.y;
        float dz = direction.z;

        // Direction of movement
        int stepX = (dx > 0) ? 1 : -1;
        int stepY = (dy > 0) ? 1 : -1;
        int stepZ = (dz > 0) ? 1 : -1;

        // How far we move along the ray for each unit of X, Y, or Z
        float deltaX = (dx == 0) ? Float.MAX_VALUE : Math.abs(1 / dx);
        float deltaY = (dy == 0) ? Float.MAX_VALUE : Math.abs(1 / dy);
        float deltaZ = (dz == 0) ? Float.MAX_VALUE : Math.abs(1 / dz);

        // Distance to the next voxel boundary
        float maxX = (dx > 0) ? (ix + 1 - x) * deltaX : (x - ix) * deltaX;
        float maxY = (dy > 0) ? (iy + 1 - y) * deltaY : (y - iy) * deltaY;
        float maxZ = (dz > 0) ? (iz + 1 - z) * deltaZ : (z - iz) * deltaZ;

        float distance = 0;
        Vector3f faceNormal = new Vector3f(0, 0, 0);

        while (distance <= maxDistance) {
            // Check if the current block is solid (not AIR)
            byte blockId = world.getBlock(ix, iy, iz);
            if (be.warrox.game.world.BlockType.fromId(blockId).isSolid()) {
                return new RaycastResult(new Vector3f(ix, iy, iz), faceNormal, distance);
            }

            // Move to the next voxel boundary
            if (maxX < maxY) {
                if (maxX < maxZ) {
                    distance = maxX;
                    maxX += deltaX;
                    ix += stepX;
                    faceNormal.set(-stepX, 0, 0); // Hit X face
                } else {
                    distance = maxZ;
                    maxZ += deltaZ;
                    iz += stepZ;
                    faceNormal.set(0, 0, -stepZ); // Hit Z face
                }
            } else {
                if (maxY < maxZ) {
                    distance = maxY;
                    maxY += deltaY;
                    iy += stepY;
                    faceNormal.set(0, -stepY, 0); // Hit Y face
                } else {
                    distance = maxZ;
                    maxZ += deltaZ;
                    iz += stepZ;
                    faceNormal.set(0, 0, -stepZ); // Hit Z face
                }
            }
        }

        return null; // No hit within maxDistance
    }
}
