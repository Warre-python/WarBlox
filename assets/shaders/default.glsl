#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;

out vec2 TexCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    gl_Position = projection * view * model * vec4(aPos, 1.0);
    TexCoord = aTexCoord;
}

#type fragment
#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D ourTexture;
uniform vec4 ourColor;
uniform bool useTexture; // Boolean flag to switch logic

void main()
{
    if (useTexture) {
        FragColor = texture(ourTexture, TexCoord);
    } else {
        FragColor = ourColor;
    }
}
