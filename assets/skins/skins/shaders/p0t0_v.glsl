//#version 100
precision highp float;

uniform mat4 MVP;
uniform mat4 ModelView;
uniform mat4 ModelViewIT;
uniform vec3 LightPosViewer0;
uniform float Ambient;
uniform vec2 uTexShift;
attribute vec3 Position;
attribute vec2 TexCoord;
attribute vec3 Normal;
varying vec2 vTexCoord;
varying vec4 vLighting;

void main()
{
	gl_Position = MVP * vec4(Position, 1.0);
	vTexCoord = TexCoord + uTexShift;

	// Perform view-space lighting calculation
	vec3 n = (ModelViewIT * vec4(Normal, 0.0)).xyz;
	vec3 p = (ModelView * vec4(Position, 1.0)).xyz;
	vec3 ToLight = normalize(LightPosViewer0 - p);
	float diffuse = max(dot(ToLight, n), 0.0);
	float DiffusePlusAmbient = mix(Ambient, 1.0, diffuse);
	vLighting = vec4(0.0, 0.0, 0.0, DiffusePlusAmbient);
//	vLighting = vec4(Normal, DiffusePlusAmbient);
}
