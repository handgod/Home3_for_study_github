//#version 100
precision highp float;

uniform mat4 MVP;
attribute vec3 Position;

void main()
{
	gl_Position = MVP * vec4(Position, 1.0);
}
