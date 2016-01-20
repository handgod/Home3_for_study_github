//#version 100
precision stdp float;

// True anaglyph

varying vec2 vTexCoord;
uniform sampler2D LeftEye;
uniform sampler2D RightEye;

void main()
{
	vec4 incolorL = texture2D( LeftEye,  vTexCoord );
	vec4 incolorR = texture2D( RightEye, vTexCoord );
   
	// See http://3dtv.at/Knowhow/AnaglyphComparison_en.aspx for discussion.

	vec4 outcolor;
   
	outcolor.r = dot( vec3( 0.299, 0.587, 0.114 ), incolorL.rgb );
	outcolor.g = 0.0;
	outcolor.b = dot( vec3( 0.299, 0.587, 0.114 ), incolorR.rgb );
	outcolor.a = 1.0;

	gl_FragColor = outcolor; 
}
