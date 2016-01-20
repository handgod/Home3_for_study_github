//#version 100
precision highp float;

uniform mat4 MVP;
uniform vec2 uTexShift;
attribute vec3 Position;
attribute vec2 TexCoord;
attribute vec2 TexCoord1;
varying vec2 vTexCoord;
varying vec2 vTexCoord1;

void main()
{
	gl_Position = MVP * vec4(Position, 1.0);
	vTexCoord = TexCoord + uTexShift;
	vTexCoord1 = TexCoord1;
}
