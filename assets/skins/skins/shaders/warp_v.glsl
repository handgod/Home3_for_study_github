//#version 100
precision highp float;

uniform mat4 MVP;
uniform vec2 uTexShift;
//uniform vec2 uSkew;
attribute vec3 Position;
attribute vec2 TexCoord;
varying vec3 vTexCoord;
varying vec4 vPosition;

void main()
{
//    Position.x += 2.0 * (TexCoord.x - 0.5) * TexCoord.y * uSkew.x;
    gl_Position = vPosition = MVP * vec4(Position, 1.0);
    vTexCoord = vec3(TexCoord + uTexShift, 1.0);
}
