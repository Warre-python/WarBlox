#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 2) in vec2 aTexCoords;
layout (location = 3) in vec4 aColor;

out vec4 ourColor;
out vec2 TexCoords;

uniform mat4 uModel;
uniform mat4 uProjection;

void main()
{
    gl_Position = uProjection * uModel * vec4(aPos, 1.0);
    ourColor = aColor;
    TexCoords = aTexCoords;
}

#type fragment
#version 330 core
out vec4 FragColor;
in vec4 ourColor;
in vec2 TexCoords;

uniform bool useTexture;
uniform sampler2D ourTexture;

void main()
{
    if (useTexture) {
        FragColor = texture(ourTexture, TexCoords);
    } else {
        FragColor = ourColor;
    }
}