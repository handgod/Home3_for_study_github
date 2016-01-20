//#version 100
precision stdp float;

varying vec4 vTexCoord;
uniform sampler2D Tex0;
uniform sampler2D Tex1;
uniform vec4 uColor;

void main()
{
//	gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
	float m = texture2D(Tex0, vTexCoord.xy).w;
	vec4  g = texture2D(Tex1, vTexCoord.zw);
	gl_FragColor = g * vec4(1.0, 1.0, 1.0, m) * uColor;
}
