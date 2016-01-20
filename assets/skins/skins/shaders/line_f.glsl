//#version 100
precision stdp float;

varying vec2 vTexCoord;
varying vec4 vSpecular;
varying vec4 vDiffuse;
uniform sampler2D Tex0;
uniform vec4 uColor;

void main()
{
//	gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0) + vSpecular;
	vec4 c = vec4(uColor.xyz, uColor.w*texture2D(Tex0, vTexCoord).w);
	c = c * vDiffuse + vSpecular;
#ifdef PREMULTIPLIED_ALPHA
    c.xyz *= c.w;
#endif
    gl_FragColor = c;
//	gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
}
