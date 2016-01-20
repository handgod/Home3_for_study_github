//#version 100
precision highp float;

attribute vec3 Position;
attribute vec2 TexCoord;
varying vec2 vTexCoord;
uniform mat4 MVP;

void main()
{
	gl_Position = MVP * vec4(Position, 1.0);
	vTexCoord = TexCoord;
}
