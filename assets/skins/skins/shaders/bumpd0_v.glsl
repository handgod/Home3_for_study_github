//#version 100
precision highp float;

attribute vec3 Position;
attribute vec2 TexCoord;
attribute vec3 TangentX;
attribute vec3 TangentY;
attribute vec3 TangentZ;
varying vec3 vToLight;
varying vec3 vToViewer;
varying vec2 vTexCoord;
uniform mat4 MVP;
uniform vec3 ViewerPos; // Object space
uniform vec3 LightPos; // Object space

void main()
{
	gl_Position = MVP * vec4(Position, 1.0);

	// Column major order. Tanglent{X,Y,Z} are columns.
	mat3 TM = mat3(TangentX, TangentY, TangentZ);

//	vec3 ToLight = LightPos - Position;
	vec3 ToLight = -LightPos;
	vToLight = ToLight * TM;

	vec3 ToViewer = ViewerPos - Position;
	vToViewer = ToViewer * TM;

	vTexCoord = vec2(TexCoord.x, 1.0 - TexCoord.y);
}
