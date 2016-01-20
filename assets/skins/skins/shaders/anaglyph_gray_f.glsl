//#version 100
precision stdp float;

// Gray anaglyph

varying vec2 vTexCoord;
uniform sampler2D LeftEye;
uniform sampler2D RightEye;

void main()
{
	vec4 incolorL = texture2D( LeftEye,  vTexCoord );
	vec4 incolorR = texture2D( RightEye, vTexCoord );
   
	// See http://3dtv.at/Knowhow/AnaglyphComparison_en.aspx for discussion.

	vec4 outcolor;
/*   
	outcolor.r = dot( vec3( 0.299, 0.587, 0.114 ), incolorL.rgb );
	outcolor.g = dot( vec3( 0.299, 0.587, 0.114 ), incolorR.rgb );
	outcolor.b = dot( vec3( 0.299, 0.587, 0.114 ), incolorR.rgb );
	outcolor.a = 1.0;
*/

	// convert to grayscale
	incolorL = vec4( dot( vec4( 0.3, 0.59, 0.11, 0.0 ), incolorL ) );
	incolorR = vec4( dot( vec4( 0.3, 0.59, 0.11, 0.0 ), incolorR ) );

	// mask colors
   vec4 red  = vec4(1.0, 0.0, 0.0, 1.0);  
   vec4 cyan = vec4(0.0, 1.0, 1.0, 1.0);  

	float r = dot( vec3( 0.0, 0.5, 0.5 ), incolorL.rgb );
	float g = 1.0 * incolorR.g;
	float b = 1.0 * incolorR.b;

	outcolor = vec4( r, g, b, 1.0 );

//	outcolor = incolorL * red + incolorR * cyan;
	outcolor.a = 1.0;

	gl_FragColor = outcolor; 
}
