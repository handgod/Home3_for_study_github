//#version 100
precision stdp float;

varying vec2 vTexCoord;
varying vec2 vTexCoord1;
uniform sampler2D Tex0;
uniform sampler2D Tex1;
uniform vec4 uColor;

void main()
{
    // Workaround for Motorola Droid 3: it fails to compile:
    //  vec4(1.0, 1.0, 1.0, texture2D(Tex0, vTexCoord).w)
    vec4 c = texture2D(Tex0, vTexCoord) * (vec4(1.0, 1.0, 1.0, 0.0) + texture2D(Tex1, vTexCoord1)) * uColor;
#ifdef PREMULTIPLIED_ALPHA
    c.xyz *= c.w;
#endif
	gl_FragColor = c;
//  gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
}
