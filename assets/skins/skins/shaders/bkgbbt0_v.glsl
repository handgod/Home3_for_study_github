//#version 100
precision highp float;

attribute vec2 TexCoord;
attribute vec4 ParticleSize;
varying vec2 vTexCoord;
uniform mat4 uSpecialProj;
uniform vec2 uX0Y0;
uniform vec2 uX1Y1;
uniform vec3 uOffset;

void main()
{
	vec2 p = uX0Y0 * ParticleSize.xy + uX1Y1 * ParticleSize.zw;
	vec4 Pos4 = vec4(p + uOffset.xy, uOffset.z, 1.0);
	gl_Position = uSpecialProj * Pos4;
	
	vTexCoord = TexCoord;
}
