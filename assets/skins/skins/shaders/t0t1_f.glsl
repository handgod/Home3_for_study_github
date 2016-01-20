//#version 100
precision stdp float;

varying vec2 vTexCoord;
varying vec2 vTexCoord1;
uniform sampler2D Tex0;
uniform sampler2D Tex1;
uniform vec4 uColor;

void main()
{
	gl_FragColor = 
		texture2D(Tex0, vTexCoord) * 
		texture2D(Tex1, vTexCoord1) * uColor;
//	gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
}
