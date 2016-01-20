//#version 100
precision stdp float;

// Half-color anaglyph

varying vec2 vTexCoord;
uniform sampler2D LeftEye;
uniform sampler2D RightEye;

void main()
{
	vec4 incolorR = texture2D( RightEye, vTexCoord );
	vec4 incolorL = texture2D( LeftEye,  vTexCoord );

	float Pixel = mod( gl_FragCoord.x, 2.0 );

	gl_FragColor = ( Pixel > 0.5 ) ? incolorR : incolorL;
}
