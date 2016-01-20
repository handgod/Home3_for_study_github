//#version 100
precision highp float; // manual-setup

/*
 * THIS NOTICE MAY NOT BE REMOVED FROM THE PROGRAM BY ANY USER THEREOF
 *
 * Author: Roger Dass
 * Roger.Dass@Spatialview.com
 *
 * Copyright (c) 2004-2011, Spatial View Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are not permitted without the written consent of Spatial View Inc.
 *
 * Redistributions of source code are not permitted.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this 
 * list of conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
**/

varying vec2 v_TexCoord;

uniform sampler2D uLeft;
uniform sampler2D uRight;
uniform float uPitch;
uniform float uTan;
uniform float uShift;
uniform vec2 uMag;
uniform vec2 uScreen;
uniform vec2 uScreenCenter;

void main()
{
    float temp = (gl_FragCoord.x - uScreenCenter.x) + (gl_FragCoord.y - uScreenCenter.y)*uTan + uShift;
    float modulus = mod(temp, uPitch);

    float tOffset = modulus - uPitch*0.75;
    float tc_x = v_TexCoord.s + tOffset*uMag.x/uScreen.x;
    float tc_y = v_TexCoord.t + tOffset*uMag.y/uScreen.y;

    vec2 tc = vec2(tc_x, tc_y);

    vec4 left = texture2D(uLeft, tc);
    vec4 right = texture2D(uRight, tc);

    gl_FragColor = (modulus / uPitch) < 0.5 ? left:right;
}
