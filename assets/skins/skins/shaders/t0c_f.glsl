//#version 100
precision stdp float;

varying vec2 vTexCoord;
varying vec4 vColor;
uniform sampler2D Tex0;
uniform vec4 uColor;

void main()
{
	gl_FragColor = texture2D(Tex0, vTexCoord) * vColor * uColor;
//	gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
}
