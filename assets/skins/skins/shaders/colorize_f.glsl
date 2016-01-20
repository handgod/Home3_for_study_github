//#version 100
precision madiump float; // manual-setup

#ifdef GL_ES
#define HIGHP highp
#endif

varying HIGHP vec4 vClipPos;
uniform sampler2D Tex0;
uniform vec4 uColor;
uniform float uS;
uniform vec3 uBaseColor; // Instead of hue, hue encoded to RGB

void main()
{
	HIGHP vec2 PosNDC = (vClipPos.xy / vClipPos.w) * 0.5 + 0.5;
	vec4 Encoded = texture2D(Tex0, PosNDC);

	// Encoding:
	//  R,G,B = V
	//  A = 0 if monochrome, 1 otherwise

	float V = Encoded.r;
	float C = V * uS;
	float m = V - C;
	vec3 RGB = uBaseColor * (Encoded.w * C) + m;
	gl_FragColor = vec4(RGB, uColor.w);
//	gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
