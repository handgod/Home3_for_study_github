//#version 100
precision stdp float;

varying vec4 vColor;
uniform vec4 uColor;

void main()
{
	gl_FragColor = vColor * uColor;
//	gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
