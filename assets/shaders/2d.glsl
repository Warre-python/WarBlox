#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoords;

out vec2 TexCoords;

uniform mat4 model;
uniform mat4 projection;
uniform vec4 uColor;

void main()
{
    gl_Position = projection * model * vec4(aPos, 1.0);
    TexCoords = aTexCoords;
}

#type fragment
#version 330 core
out vec4 FragColor;
in vec2 TexCoords;


uniform vec4 uColor;

uniform bool useTexture;
uniform sampler2D ourTexture;

void main()
{
    if(useTexture) {
        FragColor = texture(ourTexture, TexCoords)* uColor;
    } else {
        FragColor = uColor;
    }
}