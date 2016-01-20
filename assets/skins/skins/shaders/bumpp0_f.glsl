//#version 100
precision mediump float; // manual-setup

varying vec3 vToLight;
varying vec3 vToViewer;
varying vec2 vTexCoord;
uniform sampler2D Tex0; // Diffuse
uniform sampler2D Tex1; // Bump
uniform float Ambient;
uniform float SpecularPower;
uniform vec4 LightDiffuseColor;
uniform vec4 LightSpecularColor;
uniform vec3 BumpScale;
uniform float LightIntensity;

void main()
{
	vec4 BumpSpec = texture2D(Tex1, vTexCoord);
	vec4 Decal = texture2D(Tex0, vTexCoord);
//	vec4 Decal = vec4(1.0, 1.0, 1.0, 1.0);

	vec3 L = normalize(vToLight);
	vec3 V = normalize(vToViewer);
	vec3 H = normalize(V + L);

	vec3 N = 2.0 * BumpSpec.xyz - 1.0;
	N = normalize(BumpScale * N);

	float DiffMin = min( LightIntensity * dot(N, L), 1.0 );
	float DiffMax = max( DiffMin, 0.0 );
	
	float DiffuseWithAmbient = mix(Ambient, 1.0, DiffMax);

//	float s = max(dot(N, H), 0.0);
//	float Specular = pow(s, SpecularPower) * BumpSpec.w;

//	gl_FragColor = 
//		LightDiffuseColor * (Decal * DiffuseWithAmbient) +
//		LightSpecularColor * Specular;
	vec4 C = LightDiffuseColor * (Decal * DiffuseWithAmbient);
	gl_FragColor = vec4(C.xyz, Decal.w);
}

