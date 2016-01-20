//#version 100
precision stdp float; // manual-setup

varying vec2 vTexCoord;
uniform sampler2D Tex0;
uniform sampler2D Hue;
uniform float HueShift;
uniform vec4 uColor;

void main()
{
	vec4 Encoded = texture2D(Tex0, vTexCoord);

	// Encoding:
	//  R = C (== 0 for monochrome)
	//  G = m
	//  B = H / 360
	//  A = texture alpha

	float h = Encoded.b + HueShift;
	vec3 BaseColor = texture2D(Hue, vec2(h, 0.5)).rgb;
	vec3 SatAndLitColor = BaseColor * Encoded.r + Encoded.g;
	
	float alpha = uColor.w * Encoded.a;
	vec4  color = vec4(SatAndLitColor, alpha);

#ifdef PREMULTIPLIED_ALPHA
	color.rgb *= color.a;
#endif

	gl_FragColor = color;
}
