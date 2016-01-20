//#version 100
precision highp float;

//uniform mat4 MVP;
uniform mat4 ModelView;
uniform mat4 Projection;
uniform vec2 PixelSizeNDC;
attribute vec3 Position;
attribute vec2 TexCoord;
attribute vec4 Extrude;
attribute vec3 Normal;
varying vec2 vTexCoord;
varying vec4 vSpecular;
varying vec4 vDiffuse;

float spec(vec3 n, vec3 l, float p)
{
	return pow(max(dot(n, l), 0.0), p);
}

void main()
{
    vec3 n = (ModelView * vec4(Normal, 0.0)).xyz;
    n = normalize(n);

	vec4 PosH = vec4(Position, 1.0);
	vec3 TangentVS = (ModelView * vec4(Extrude.xyz, 0.0)).xyz;
	vec3 PosVS = (ModelView * PosH).xyz;
	vec4 P0 = Projection * vec4(PosVS, 1.0);
	P0 /= P0.w;
	vec3 ExtVS = PosVS + normalize(cross(TangentVS, PosVS)) * 8.0;
	vec4 PE = Projection * vec4(ExtVS, 1.0);
	PE /= PE.w;
	// Take normal sign to utilize culling.
	float ExtrudeScale = sign(n.z) * Extrude.w;
	P0.xy += ExtrudeScale * PixelSizeNDC * normalize(PE.xy - P0.xy);
	gl_Position = P0;

	vec3 L1 = normalize(vec3(0.08, 0.12, 1.0));
	vec3 L2 = normalize(vec3(1.0, 1.0, 1.0));
    float s = 0.8 * spec(n, L1, 20.0) + 0.75 * spec(n, L2, 200.0);
    vSpecular = vec4(s, s, s, 0.0);

    float d = mix(0.6, 1.0, max(dot(n, L1), 0.0));
    vDiffuse = vec4(d, d, d, 1.0);

	vTexCoord = TexCoord;
}
