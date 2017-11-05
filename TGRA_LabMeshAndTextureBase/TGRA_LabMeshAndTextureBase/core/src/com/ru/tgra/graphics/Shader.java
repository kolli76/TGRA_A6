package com.ru.tgra.graphics;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class Shader {

	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;
	private int normalLoc;
	private int uvLoc;
	
	private int modelMatrixLoc;
	private int viewMatrixLoc;
	private int projectionMatrixLoc;

	private boolean usesDiffuseTexture = false;
	private int usesDiffuseTexLoc;
	private int diffuseTextureLoc;
	
	private boolean usesAlphaTexture = false;
	private int usesAlphaTexLoc;
	private int alphaTextureLoc;
	
	private boolean usesEmissionTexture = false;
	private int usesEmissionTexLoc;
	private int emissionTextureLoc;

	private int eyePosLoc;

	private int globalAmbLoc;
	//private int colorLoc;

	
	//private int lightPosLoc;
	private int numberOfLights = 1;
	private int lightPosLoc[] = new int[numberOfLights];
	private int spotDirLoc[] = new int[numberOfLights];
	private int spotExpLoc[] = new int[numberOfLights];
	private int constantAttLoc[] = new int[numberOfLights];
	private int linearAttLoc[] = new int[numberOfLights];
	private int quadraticAttLoc[] = new int[numberOfLights];
	private int lightColorLoc[] = new int[numberOfLights];
	
	private int matDifLoc;
	private int matAmbLoc;
	private int matSpecLoc;
	private int matShineLoc;
	private int matEmissionLoc;
	
	private int fogStartLoc;
	private int fogEndLoc;
	private int fogColorLoc;

	public Shader()
	{
		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/fragmentLighting3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/fragmentLighting3D.frag").readString();

		vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
		fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
		Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);

		Gdx.gl.glCompileShader(vertexShaderID);
		Gdx.gl.glCompileShader(fragmentShaderID);

		System.out.println("Vertex shader compile messages:");
		System.out.println(Gdx.gl.glGetShaderInfoLog(vertexShaderID));
		System.out.println("Fragment shader compile messages:");
		System.out.println(Gdx.gl.glGetShaderInfoLog(fragmentShaderID));

		renderingProgramID = Gdx.gl.glCreateProgram();

		Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
		Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);
	
		Gdx.gl.glLinkProgram(renderingProgramID);

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(normalLoc);

		uvLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_uv");
		Gdx.gl.glEnableVertexAttribArray(uvLoc);

		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
		projectionMatrixLoc	= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

		usesDiffuseTexLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_usesDiffuseTexture");
		diffuseTextureLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_diffuseTexture");
		
		usesAlphaTexLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_usesAlphaTexture");
		alphaTextureLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_alphaTexture");
		
		usesEmissionTexLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_usesEmissionTexture");
		emissionTextureLoc		= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_emissionTexture");

		eyePosLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_eyePosition");

		globalAmbLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_globalAmbient");

		for(int i = 0; i < numberOfLights; i++)
		{
			lightPosLoc[i]		= Gdx.gl.glGetUniformLocation(renderingProgramID, "lightsV["+i+"].lightPosition");
			spotDirLoc[i]		= Gdx.gl.glGetUniformLocation(renderingProgramID, "lightsF["+i+"].spotDirection");
			spotExpLoc[i]		= Gdx.gl.glGetUniformLocation(renderingProgramID, "lightsF["+i+"].spotExponent");
			lightColorLoc[i]	= Gdx.gl.glGetUniformLocation(renderingProgramID, "lightsF["+i+"].lightColor");
			constantAttLoc[i]	= Gdx.gl.glGetUniformLocation(renderingProgramID, "lightsF["+i+"].constantAttenuation");
			linearAttLoc[i]		= Gdx.gl.glGetUniformLocation(renderingProgramID, "lightsF["+i+"].linearAttenuation");
			quadraticAttLoc[i]	= Gdx.gl.glGetUniformLocation(renderingProgramID, "lightsF["+i+"].quadraticAttenuation");
		}
		matDifLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
		matAmbLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialAmbiance");
		matSpecLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");
		matShineLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialShininess");
		
		fogStartLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_fogStart");
		fogEndLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_fogEnd");
		fogColorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_fogColor");
		
		matEmissionLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialEmission");

		Gdx.gl.glUseProgram(renderingProgramID);
	}

	public void setDiffuseTexture(Texture tex)
	{
		if(tex == null)
		{
			Gdx.gl.glUniform1f(usesDiffuseTexLoc, 0.0f);
			usesDiffuseTexture = false;
		}
		else
		{
			tex.bind(0); //binding texture, number sent in is texture unit, we can send many units in
			Gdx.gl.glUniform1i(diffuseTextureLoc, 0); //use 0 for its diffuse texture location
			Gdx.gl.glUniform1f(usesDiffuseTexLoc, 1.0f);
			usesDiffuseTexture = true;

			Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT); //repeat the texture
			Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
		}
	}
	
	public void setAlphaTexture(Texture tex)
	{
		if(tex == null)
		{
			Gdx.gl.glUniform1f(usesAlphaTexLoc, 0.0f);
			usesAlphaTexture = false;
		}
		else
		{
			tex.bind(1); //binding texture, number sent in is texture unit, we can send many units in
			Gdx.gl.glUniform1i(alphaTextureLoc, 1); //use 0 for its diffuse texture location
			Gdx.gl.glUniform1f(usesAlphaTexLoc, 1.0f);
			usesAlphaTexture = true;
		}
	}
	
	public void setEmissionTexture(Texture tex)
	{
		if(tex == null)
		{
			Gdx.gl.glUniform1f(usesEmissionTexLoc, 0.0f);
			usesEmissionTexture = false;
		}
		else
		{
			tex.bind(2); //binding texture, number sent in is texture unit, we can send many units in
			Gdx.gl.glUniform1i(emissionTextureLoc, 2); //use 0 for its diffuse texture location
			Gdx.gl.glUniform1f(usesEmissionTexLoc, 1.0f);
			usesEmissionTexture = true;

			Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT); //repeat the texture
			Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
		}
	}

	public boolean usesTextures()
	{
		return (usesDiffuseTexture || usesAlphaTexture || usesEmissionTexture/* || usesSpecularTexture ... etc.*/);
	}


	public void setEyePosition(float x, float y, float z, float w)
	{
		Gdx.gl.glUniform4f(eyePosLoc, x, y, z, w);
	}
	public void setGlobalAmbient(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(globalAmbLoc, r, g, b, a);
	}
	public void setLightPosition(int i, float x, float y, float z, float w)
	{
		//Gdx.gl.glUniform4f(lightPosLoc, x, y, z, w);	
		if (i >= numberOfLights){return;}
			Gdx.gl.glUniform4f(lightPosLoc[i], x, y, z, w);
	}

	public void setSpotDirection(int i, float x, float y, float z, float w)
	{
		if (i >= numberOfLights){return;}
			Gdx.gl.glUniform4f(spotDirLoc[i], x, y, z, w);
	}
	public void setSpotExponent(int i, float exp)
	{
		if (i >= numberOfLights){return;}
			Gdx.gl.glUniform1f(spotExpLoc[i], exp);
	}
	public void setConstantAttenuation(int i, float att)
	{
		if (i >= numberOfLights){return;}
			Gdx.gl.glUniform1f(constantAttLoc[i], att);
	}
	public void setLinearAttenuation(int i, float att)
	{
		if (i >= numberOfLights){return;}
			Gdx.gl.glUniform1f(linearAttLoc[i], att);
	}
	public void setQuadraticAttenuation(int i, float att)
	{
		if (i >= numberOfLights){return;}
			Gdx.gl.glUniform1f(quadraticAttLoc[i], att);
	}

	public void setLightColor(int i, float r, float g, float b, float a)
	{
		if (i >= numberOfLights){return;}
			Gdx.gl.glUniform4f(lightColorLoc[i], r, g, b, a);
	}
	public void setMaterialDiffuse(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(matDifLoc, r, g, b, a);
	}
	public void setMaterialAmbiance(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(matAmbLoc, r, g, b, a);
	}
	public void setMaterialSpecular(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(matSpecLoc, r, g, b, a);
	}
	public void setShininess(float shine)
	{
		Gdx.gl.glUniform1f(matShineLoc, shine);
	}
	public void setMaterialEmission(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(matEmissionLoc, r, g, b, a);
	}


	public int getVertexPointer()
	{
		return positionLoc;
	}
	public int getNormalPointer()
	{
		return normalLoc;
	}
	public int getUVPointer()
	{
		return uvLoc;
	}

	public void setModelMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, matrix);
	}
	public void setViewMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrix);
	}
	public void setProjectionMatrix(FloatBuffer matrix)
	{
		Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrix);
	}
	
	public void setFogStart(float att)
	{
		Gdx.gl.glUniform1f(fogStartLoc, att);
	}
	
	public void setFogEnd(float att)
	{
		Gdx.gl.glUniform1f(fogEndLoc, att);
	}
	
	public void setFogColor(float r, float g, float b, float a)
	{
		Gdx.gl.glUniform4f(fogColorLoc, r, g, b, a);
	}
}
