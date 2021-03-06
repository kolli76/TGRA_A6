package com.ru.tgra.game;

import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.graphics.ModelMatrix;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Shader;
import com.ru.tgra.graphics.Vector3D;
import com.ru.tgra.graphics.shapes.SphereGraphic;
import com.ru.tgra.graphics.shapes.TorusGraphic;

public class Octopus {

	private Point3D location;
	
	public Octopus(Point3D location)
	{
		this.location = location;
	}
	
	public float getX()
	{
		return this.location.x;
	}
	
	public float getY()
	{
		return this.location.y;
	}
	
	public float getZ()
	{
		return this.location.z;
	}
	
	public void draw(Shader shader, Texture diffuseTexture, Texture alphaTexture)
	{
		ModelMatrix.main.pushMatrix();
		
		shader.setMaterialDiffuse(1.0f, 0.3f, 0.3f, 1.0f);
		shader.setMaterialSpecular(0, 0, 0, 1.0f);
		
		ModelMatrix.main.addTranslation(location.x, location.y, location.z);
		ModelMatrix.main.addScale(0.5f, 0.5f, 0.5f);
		ModelMatrix.main.addRotation(220f, new Vector3D(1,0,0));
		ModelMatrix.main.addRotation(180f, new Vector3D(0,0,1));
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere(shader, diffuseTexture, alphaTexture);
		
		{// Eye
			float radius = 0.93f;
			float azimuth = (float) (50.0f/180*Math.PI);
			float polar = (float) (70.0f/180.0f*Math.PI);
			float dx = radius*(float)Math.sin(polar) * (float)Math.cos(azimuth);
			float dy = radius*(float)Math.cos(polar);
			float dz = radius*(float)Math.sin(polar) * (float)Math.sin(azimuth);
			
			ModelMatrix.main.pushMatrix();
			
			shader.setMaterialDiffuse(0.2f, 0.0f, 0.0f, 1.0f);
			shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setShininess(20.0f);
			
			ModelMatrix.main.addTranslation(dx, dy, dz);
			ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
			
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			SphereGraphic.drawSolidSphere(shader, diffuseTexture, alphaTexture);
			
			ModelMatrix.main.popMatrix();
		}
		
		{// Eye
			float radius = 0.93f;
			float azimuth = (float) (130.0f/180*Math.PI);
			float polar = (float) (70.0f/180.0f*Math.PI);
			float dx = radius*(float)Math.sin(polar) * (float)Math.cos(azimuth);
			float dy = radius*(float)Math.cos(polar);
			float dz = radius*(float)Math.sin(polar) * (float)Math.sin(azimuth);
			
			ModelMatrix.main.pushMatrix();
			
			shader.setMaterialDiffuse(0.2f, 0.0f, 0.0f, 1.0f);
			shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setShininess(20.0f);
			
			ModelMatrix.main.addTranslation(dx, dy, dz);
			ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
			
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			SphereGraphic.drawSolidSphere(shader, diffuseTexture, alphaTexture);
			
			ModelMatrix.main.popMatrix();
		}
		
		{// Mouth
			
			float azimuth = (float) (Math.PI/2);
			float polar = (float) (120.0f/180.0f*Math.PI);
			float radius = 1.0f;
			float dx = radius*(float)Math.sin(polar) * (float)Math.cos(azimuth);
			float dy = radius*(float)Math.cos(polar);
			float dz = radius*(float)Math.sin(polar) * (float)Math.sin(azimuth);
			
			ModelMatrix.main.pushMatrix();
			
			shader.setMaterialDiffuse(1.0f, 0.1f, 0.1f, 1.0f);
			shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
			shader.setShininess(1.0f);
			
			ModelMatrix.main.addTranslation(dx, dy, dz);
			ModelMatrix.main.addRotationX((float)(polar*180/Math.PI));
			ModelMatrix.main.addScale(0.18f, 0.18f, 0.18f);
			
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			TorusGraphic.drawSolidTorus(shader, diffuseTexture, alphaTexture);
			
			ModelMatrix.main.popMatrix();
		}
		
		
		
		ModelMatrix.main.popMatrix();
	}
}
