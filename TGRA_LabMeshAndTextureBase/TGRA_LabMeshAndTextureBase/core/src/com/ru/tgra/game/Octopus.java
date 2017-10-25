package com.ru.tgra.game;

import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.graphics.ModelMatrix;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Shader;
import com.ru.tgra.graphics.shapes.SphereGraphic;
import com.ru.tgra.graphics.shapes.TorusGraphic;

public class Octopus {

	private Point3D location;
	
	public Octopus(Point3D location)
	{
		this.location = location;
	}
	
	public void draw(Shader shader, Texture diffuseTexture, Texture alphaTexture)
	{
		ModelMatrix.main.pushMatrix();
		
		shader.setMaterialDiffuse(1.0f, 0.3f, 0.3f, 1.0f);
		shader.setMaterialSpecular(0, 0, 0, 1.0f);
		
		ModelMatrix.main.addTranslation(location.x, location.y, location.z);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere(shader, diffuseTexture, alphaTexture);
		
		{// Eye
			ModelMatrix.main.pushMatrix();
			
			shader.setMaterialDiffuse(0.2f, 0.0f, 0.0f, 1.0f);
			shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setShininess(20.0f);
			
			ModelMatrix.main.addTranslation(-0.5f, 0, 0.8f);
			ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
			
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			SphereGraphic.drawSolidSphere(shader, diffuseTexture, alphaTexture);
			
			ModelMatrix.main.popMatrix();
		}
		
		{// Eye
			ModelMatrix.main.pushMatrix();
			
			shader.setMaterialDiffuse(0.2f, 0.0f, 0.0f, 1.0f);
			shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setShininess(20.0f);
			
			ModelMatrix.main.addTranslation(0.5f, 0, 0.8f);
			ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
			
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			SphereGraphic.drawSolidSphere(shader, diffuseTexture, alphaTexture);
			
			ModelMatrix.main.popMatrix();
		}
		
		{// Mouth
			ModelMatrix.main.pushMatrix();
			
			shader.setMaterialDiffuse(1.0f, 0.1f, 0.1f, 1.0f);
			shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
			shader.setShininess(1.0f);
			
			ModelMatrix.main.addTranslation(0.0f, -0.5f, 0.8f);
			ModelMatrix.main.addRotationX(135.0f);
			ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
			
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			TorusGraphic.drawSolidTorus(shader, diffuseTexture, alphaTexture);
			
			ModelMatrix.main.popMatrix();
		}
		
		
		
		ModelMatrix.main.popMatrix();
	}
}
