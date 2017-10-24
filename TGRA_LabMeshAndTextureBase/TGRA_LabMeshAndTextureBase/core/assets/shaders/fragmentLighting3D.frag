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

struct lightFragment
{
	vec4 lightColor; //we could use uniform vec4 u_lightColor[]; instead of struct
	vec4 spotDirection;
	float spotExponent;
	float constantAttenuation;
	float linearAttenuation;
	float quadraticAttenuation;
};

uniform lightFragment lightsF[lightNr];

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
	float lambert;
	float phong;
	float length_s[lightNr];
	for(int i = 0; i < lightNr; i++)
	{
		length_s[i] = length(v_s[i]);
	}
	//float length_s = length(v_s);
	float length_n = length(v_normal);
	float spotAttenuation = 1.0;
	vec4 totalFragColor = u_materialEmission;
	for(int i = 0; i < lightNr; i++)
	{
		lambert = max(0.0, dot(v_normal, v_s[i]) / (length_n * length_s[i]));
		phong = max(0.0, dot(v_normal, v_h) / (length_n * length(v_h)));
		phong = pow(phong, u_materialShininess);
		
		vec4 diffuseColor = lambert * lightsF[i].lightColor * materialDiffuse;

		vec4 specularColor =  phong * lightsF[i].lightColor * materialSpecular;

		if(lightsF[i].spotExponent != 0.0)
		{
			spotAttenuation = max(0.0, dot(-v_s[i], lightsF[i].spotDirection) / (length_s[i] * length(lightsF[i].spotDirection)));
			spotAttenuation = pow(spotAttenuation, lightsF[i].spotExponent);
		}
		float distanceAttenuation = 1.0 / (lightsF[i].constantAttenuation + length_s[i] * lightsF[i].linearAttenuation + pow(length_s[i], 2.0) * lightsF[i].quadraticAttenuation);
	
		light1CalcColor = distanceAttenuation * spotAttenuation * (diffuseColor + specularColor);
		totalFragColor += light1CalcColor;
	}
	// end for each light
	
	vec4 finalObjectColor = u_globalAmbient * materialDiffuse + totalFragColor;

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