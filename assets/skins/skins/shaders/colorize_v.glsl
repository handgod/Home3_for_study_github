//#version 100
precision highp float;

attribute vec3 Position;
varying vec4 vClipPos;
uniform mat4 MVP;

void main()
{
	gl_Position = vClipPos = MVP * vec4(Position, 1.0);
}
