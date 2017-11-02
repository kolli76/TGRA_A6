	#version 330

#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_uv;

uniform mat4 u_modelMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

uniform vec4 u_eyePosition;

const int lightNumber = 1;

struct lightVertex
{
	vec4 lightPosition;
};
uniform lightVertex lightsV[lightNumber];

//uniform vec4 u_lightPosition;

varying vec2 v_uv;
varying vec4 v_normal;
varying vec4 v_s[lightNumber];
varying vec4 v_h;
varying float v_distance;

void main()
{
	vec4 position = vec4(a_position.x, a_position.y, a_position.z, 1.0);
	position = u_modelMatrix * position;

	vec4 normal = vec4(a_normal.x, a_normal.y, a_normal.z, 0.0);
	normal = u_modelMatrix * normal;
	
	//global coordinates

	//preparation for lighting
	
	v_normal = normal;
	vec4 v = u_eyePosition - position; //direction to the camera
	v_distance = length(v);
	
	
	for(int i = 0; i < lightNumber; i++)
	{
		if(lightsV[i].lightPosition.w == 0.0f) //for directional light
		{
			v_s[i] = lightsV[i].lightPosition;
		}
		else
		{
			v_s[i] = lightsV[i].lightPosition - position; //for position light, direction to the light
		}

		v_h = v + v_s[i]; //highest intensity if h is parallel to the normal n (or m)
	}


	position = u_viewMatrix * position;
	//eye coordinates
	
	//v_distance = -position.z;

	v_uv = a_uv;
	gl_Position = u_projectionMatrix * position;
	//clip coordinates
}