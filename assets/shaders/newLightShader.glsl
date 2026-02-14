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

in vec3 Normal;
in vec3 FragPos;
in vec2 TexCoord; // Moet exact matchen met de Vertex Shader 'out'

struct Material {
    sampler2D diffuse;
    sampler2D specular;
    float shininess;
};

struct DirLight {
    vec3 direction;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct PointLight {
    vec3 position;
    float constant;
    float linear;
    float quadratic;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

#define NR_POINT_LIGHTS 4
uniform vec3 viewPos;
uniform DirLight dirLight;
uniform PointLight pointLights[NR_POINT_LIGHTS];
uniform Material material;
uniform bool useTexture;
uniform vec4 objectColor; // Voor als er geen texture is
uniform bool isLightSource;

vec3 CalcDirLight(DirLight light, vec3 normal, vec3 viewDir, vec3 baseColor)
{
    vec3 lightDir = normalize(-light.direction);
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

    vec3 ambient  = light.ambient  * baseColor;
    vec3 diffuse  = light.diffuse  * diff * baseColor;
    vec3 specular = light.specular * spec * baseColor;
    return (ambient + diffuse + specular);
}

vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir, vec3 baseColor)
{
    vec3 lightDir = normalize(light.position - fragPos);
    float diff = max(dot(normal, lightDir), 0.0);
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

    float distance    = length(light.position - fragPos);
    float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));

    // In CalcPointLight
    vec3 ambient  = light.ambient * baseColor * 0.1; // Heel laag ambient
    vec3 diffuse  = light.diffuse * diff * baseColor;
    vec3 specular = light.specular * spec * baseColor; // Specular zorgt voor die "glans"


    return (ambient + diffuse + specular) * attenuation;
}

void main()
{

    if(isLightSource) {
        // Als het een lamp is, teken hem direct met zijn kleur zonder lichtberekening
        FragColor = objectColor;
        return;
    }

    vec3 norm = normalize(Normal);
    vec3 viewDir = normalize(viewPos - FragPos);

    // Bepaal de basiskleur (Texture of Kleur)
    vec3 baseColor;
    if(useTexture) {
        baseColor = vec3(texture(material.diffuse, TexCoord));
    } else {
        baseColor = vec3(objectColor);
    }

    // Phase 1: Directional lighting
    vec3 result = CalcDirLight(dirLight, norm, viewDir, baseColor);

    // Phase 2: Point lights
    for(int i = 0; i < NR_POINT_LIGHTS; i++) {
        result += CalcPointLight(pointLights[i], norm, FragPos, viewDir, baseColor);
    }

    FragColor = vec4(result, 1.0);
}

