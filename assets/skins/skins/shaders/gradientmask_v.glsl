//#version 100
precision highp float;

uniform mat4 MVP;
attribute vec3 Position;
attribute vec3 TexCoord;
varying vec4 vTexCoord;

void main()
{
	gl_Position = MVP * vec4(Position, 1.0);
	vTexCoord = vec4(TexCoord.xy, 0.5, TexCoord.z);
}
