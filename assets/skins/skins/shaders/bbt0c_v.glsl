//#version 100
precision highp float;

attribute vec3 Position;
attribute vec2 TexCoord;
attribute vec2 ParticleSize;
attribute vec4 Color;
varying vec2 vTexCoord;
varying vec4 vColor;
uniform mat4 ModelView;
uniform mat4 ModelViewI;
uniform mat4 Projection;

// Spherical billboard
void main()
{
	vec4 PosVS = ModelView * vec4(Position, 1.0);
	vec2 SizeScale;
	SizeScale.x = length(ModelViewI[0].xyz);
	SizeScale.y = length(ModelViewI[1].xyz);
	PosVS.xy += ParticleSize / SizeScale;
	gl_Position = Projection * PosVS;
	
	vTexCoord = TexCoord;

	vColor = Color;
}
