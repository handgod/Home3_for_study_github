//#version 100
precision stdp float;

varying vec4 vLighting;
uniform vec4 uColor;

void main()
{
	gl_FragColor = vec4(uColor.xyz * vLighting.w, uColor.w);
//	gl_FragColor = vLighting;
//	gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
}
