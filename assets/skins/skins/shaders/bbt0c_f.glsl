//#version 100
precision stdp float;

varying vec2 vTexCoord;
varying vec4 vColor;
uniform sampler2D Tex0;
uniform vec4 uColor;

void main()
{
    vec4 c = texture2D(Tex0, vTexCoord) * uColor * vColor;
#ifdef PREMULTIPLIED_ALPHA
    c.xyz *= c.w;
#endif
	gl_FragColor = c;
//	gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);
}
