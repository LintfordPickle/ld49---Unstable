#version 150 core

precision highp float;

#define PIXELATE

uniform sampler2D backgroundSampler;

uniform vec2 screenSize = vec2(800, 600);
#define speed 6.0

uniform vec2 cameraPosition = vec2(0., 0.);
uniform vec2 center = vec2(0.5, 0.5);

#define xDistMag 0.005
#define yDistMag 0.008

#define xSineCycles 60.28
#define ySineCycles 50.28

uniform float time;

in vec2 passTexCoord;

out vec4 outColor;


void main() {
	float t = time * speed;
	
	vec2 uv = passTexCoord;
	uv.x += cameraPosition.x;
	uv.y = 1. - uv.y;
	uv -= vec2(.5);
	uv *= .8;
	uv += vec2(.5);
	
    float xAngle = t + uv.y * ySineCycles;
    float yAngle = t + uv.x * xSineCycles;

	vec2 distortOffset = 
        vec2(sin(xAngle), sin(yAngle)) * // amount of shearing
        vec2(xDistMag,yDistMag); // magnitude adjustment
	
	uv += distortOffset;
	
	vec4 texColor = texture(backgroundSampler,  uv);
		
	outColor = texColor;
	//outColor = vec4(1.);
}
