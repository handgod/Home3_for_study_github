//#version 100
precision mediump float; // manual-setup

varying vec2 vTexCoord;
uniform sampler2D Tex0;
uniform vec2 uTargetSizeInv;

void main()
{
	vec4 c0 = texture2D(Tex0, vTexCoord - vec2(0.0,2.0)*uTargetSizeInv);
	vec4 c1 = texture2D(Tex0, vTexCoord - vec2(0.0,1.0)*uTargetSizeInv);
	vec4 c2 = texture2D(Tex0, vTexCoord);
	vec4 c3 = texture2D(Tex0, vTexCoord + vec2(0.0,1.0)*uTargetSizeInv);
	vec4 c4 = texture2D(Tex0, vTexCoord + vec2(0.0,2.0)*uTargetSizeInv);
	gl_FragColor = c0*0.152 + c1*0.222 + c2*0.251 + c3*0.222 + c4*0.152;

//	gl_FragColor = texture2D(Tex0, vTexCoord);
//	gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
}
