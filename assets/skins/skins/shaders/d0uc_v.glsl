//#version 100
precision highp float;

uniform mat4 MVP;
uniform mat4 ModelView;
uniform mat4 ModelViewIT;
uniform vec3 LightDirViewer0;
uniform float Ambient;
attribute vec3 Position;
attribute vec3 Normal;
varying vec4 vLighting;

void main()
{
    gl_Position = MVP * vec4(Position, 1.0);
    
    // Perform view-space lighting calculation
    vec3 n = Normal;
    vec3 ToLight = -LightDirViewer0;
    float diffuse = max(dot(ToLight, n), 0.0);
    float DiffusePlusAmbient = mix(Ambient, 1.0, diffuse);
    vLighting = vec4(0.0, 0.0, 0.0, DiffusePlusAmbient);
}
