//#version 100
precision highp float;

uniform mat4 MVP;
attribute vec3 Position;
attribute vec4 Color;
attribute float PointSize;
varying vec4 vColor;

void main()
{
	gl_PointSize = PointSize;
	gl_Position = MVP * vec4(Position, 1.0);
	vColor = Color;
}
