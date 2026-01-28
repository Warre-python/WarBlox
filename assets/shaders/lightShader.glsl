#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec3 aNormal;

out vec2 TexCoord;
out vec3 FragPos;
out vec3 Normal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    // Pass texture coordinates to fragment shader
    TexCoord = aTexCoord;

    // Transform normal to world space (consider using a Normal Matrix for non-uniform scaling)
    Normal = mat3(transpose(inverse(model))) * aNormal;

    FragPos = vec3(model * vec4(aPos, 1.0));
    gl_Position = projection * view * model * vec4(aPos, 1.0);
}


#type fragment
#version 330 core
out vec4 FragColor;

in vec2 TexCoord;
in vec3 Normal;
in vec3 FragPos;

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

uniform Material material;

struct Light {
    vec3 position;
    vec3 color;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform Light light;

uniform sampler2D ourTexture;
uniform bool useTexture;
uniform vec3 objectColor;
uniform vec3 viewPos;

void main()
{
    // Normalize vectors
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(light.position - FragPos);
    vec3 viewDir  = normalize(viewPos - FragPos);

    // Ambient
    vec3 ambient = vec3(0.1) * (light.ambient * material.ambient * light.color);

    // Diffuse
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = vec3(1.0) * ((diff * light.diffuse * material.diffuse) * light.color);

    // Specular
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = vec3(1.0) * (material.specular * spec * light.color);

    vec3 baseColor = useTexture
    ? texture(ourTexture, TexCoord).rgb
    : objectColor;

    vec3 result = (ambient + diffuse + specular) * baseColor;
    FragColor = vec4(result, 1.0);
}

