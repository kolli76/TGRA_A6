#version 330

#ifdef GL_ES
precision mediump float;
#endif

const int lightNr = 1;

uniform sampler2D u_diffuseTexture;
uniform sampler2D u_alphaTexture;
uniform sampler2D u_emissionTexture;

uniform float u_usesDiffuseTexture;
uniform float u_usesAlphaTexture;
uniform float u_usesEmissionTexture;

uniform vec4 u_globalAmbient;

uniform vec4 u_lightColor;

uniform vec4 u_spotDirection;
uniform float u_spotExponent;

uniform float u_constantAttenuation;
uniform float u_linearAttenuation;
uniform float u_quadraticAttenuation;

uniform vec4 u_materialDiffuse;
uniform vec4 u_materialSpecular;
uniform float u_materialShininess;

uniform vec4 u_materialEmission;

//FOG stuff

uniform float u_fogStart;
uniform float u_fogEnd;
uniform vec4 u_fogColor;

varying float v_distance;

varying vec2 v_uv;
varying vec4 v_normal;
varying vec4 v_s[lightNr]; //vector to the light
varying vec4 v_h; //halfway vector


void main()
{
	vec4 materialDiffuse;
	
	if(u_usesDiffuseTexture == 1.0)
	{						//sample texture material
		materialDiffuse = texture2D(u_diffuseTexture, v_uv);  //also * u_materialDiffuse ??? up to you.
	}
	else
	{
		materialDiffuse = u_materialDiffuse;
	}
	
	vec4 materialEmission;
	
	if(u_usesEmissionTexture == 1.0)
	{						//sample texture material
		materialEmission = texture2D(u_emissionTexture, v_uv);
	}
	else
	{
		materialEmission = u_materialEmission;
	}
	
	if(u_usesAlphaTexture == 1.0)
	{						//sample texture material
		materialDiffuse.a *= texture2D(u_alphaTexture, v_uv).r;  
	}
	
/*	if(materialDiffuse.a < 0.1)
	{
		discard; //throw away, color wont be in the frame buffer and depth value wont be put in the depth buffer
	}
*/
	vec4 materialSpecular = u_materialSpecular;
	vec4 light1CalcColor;
	//Lighting
	for(int i = 0; i < lightNr; i++)
	{
		float length_s = length(v_s[i]);
	
		float lambert = max(0.0, dot(v_normal, v_s[i]) / (length(v_normal) * length_s));
		float phong = max(0.0, dot(v_normal, v_h) / (length(v_normal) * length(v_h)));

		vec4 diffuseColor = lambert * u_lightColor * materialDiffuse;

		vec4 specularColor = pow(phong, u_materialShininess) * u_lightColor * materialSpecular;

		float attenuation = 1.0;
		if(u_spotExponent != 0.0)
		{
			float spotAttenuation = max(0.0, dot(-v_s[i], u_spotDirection) / (length_s * length(u_spotDirection)));
			spotAttenuation = pow(spotAttenuation, u_spotExponent);
			attenuation *= spotAttenuation;
		}
		attenuation *= 1.0 / (u_constantAttenuation + length_s * u_linearAttenuation + pow(length_s, 2.0) * u_quadraticAttenuation);
	
		light1CalcColor = attenuation * (diffuseColor + specularColor);
	}
	// end for each light
	
	vec4 finalObjectColor = u_globalAmbient * materialDiffuse + materialEmission + light1CalcColor;

	//FOG stuff
	if(v_distance < u_fogStart)
	{
		gl_FragColor = finalObjectColor;
	}
	else if(v_distance > u_fogEnd)
	{
		gl_FragColor = u_fogColor;
	}
	else
	{
		float fogRatio = (v_distance - u_fogStart) / (u_fogEnd - u_fogStart);
		gl_FragColor = (1 - fogRatio) * finalObjectColor + fogRatio * u_fogColor;
	}
	gl_FragColor.a = materialDiffuse.a; //more transparency, transparent objects need to be drawn last
	//in order to draw realistic transparency, we need to order the polygons so that that are drawn from farthest to nearest
}