//#version 100
precision stdp float;

// Half-color anaglyph

varying vec2 vTexCoord;
uniform sampler2D LeftEye;
uniform sampler2D RightEye;

void main()
{
	vec4 incolorR = texture2D( RightEye, vec2( 1.0, 2.0 ) * vTexCoord-vec2(0.0, 1.0) );
	vec4 incolorL = texture2D( LeftEye,  vec2( 1.0, 2.0 ) * vTexCoord );
//	vec4 incolorR = vec4( 1.0, 0.0, 0.0, 0.0 );

	vec4 Color = incolorL * step( vTexCoord.y, 0.5 ) + incolorR * step( 0.5, vTexCoord.y );

	//if ( vTexCoord.x > 0.5 ) Color = inColorR;
   
	gl_FragColor = Color; 
}
