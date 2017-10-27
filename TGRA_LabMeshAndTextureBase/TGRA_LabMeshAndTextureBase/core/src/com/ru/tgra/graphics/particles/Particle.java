package com.ru.tgra.graphics.particles;

import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.graphics.ModelMatrix;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Shader;
import com.ru.tgra.graphics.Vector3D;
import com.ru.tgra.graphics.shapes.SpriteGraphic;

public class Particle {
	
	Texture emissionTexture;
	Texture alphaTexture;
	Point3D position;
	Vector3D speed;
	Vector3D originalSpeed;
	
	float size;
	float maxSize;
	float timeToLive;
	float lifeTime;
	float fadeInTime;
	float fadeOutTime;
	float maxAlpha;
	float alpha;
	
	public Particle(Point3D position, Vector3D speed, float size, float timeToLive, float fadeInTime, float fadeOutTime, float maxAlpha, Texture emissionTexture, Texture alphaTexture)
	{
		this.position = position;
		this.speed = speed;
		originalSpeed = new Vector3D(speed.x, speed.y, speed.z);
		this.emissionTexture = emissionTexture;
		this.alphaTexture = alphaTexture;
		
		this.lifeTime = 0;	
		this.timeToLive = timeToLive;
		this.fadeInTime = fadeInTime;
		this.fadeOutTime = fadeOutTime;
		this.alpha = 0;
		this.maxAlpha = maxAlpha;
		
		this.maxSize = size;
	}
	
	public void update(float deltaTime)
	{
		position.add(Vector3D.scale(speed, deltaTime));
		
		if(lifeTime < fadeInTime)
		{
			alpha = (lifeTime / fadeInTime) * maxAlpha;
			size = (lifeTime / fadeInTime) * maxSize;
		}
		else if(lifeTime > (timeToLive - fadeOutTime))
		{
			alpha = ((timeToLive- lifeTime) / (fadeOutTime)) * maxAlpha;
		}
		else
		{
			alpha = maxAlpha; //between fade in and fade out
		}
		//this should not be in here, comment out for smoke. We should set this somewhere else and call it speedchange, speed gravity or speed gradient
		//speed.x -= originalSpeed.x * 1.3 * deltaTime;
		//speed.z -= originalSpeed.z * 1.3 * deltaTime;
		
		lifeTime += deltaTime;
	}
	
	public void draw(Shader shader)
	{
		shader.setMaterialDiffuse(0, 0, 0, alpha);
		
		ModelMatrix.main.pushMatrix();
		
		ModelMatrix.main.addTranslation(position.x, position.y, position.z);
		//ModelMatrix.main.addRotationY(rotationAngle);
		
		ModelMatrix.main.addScale(size, size, size);

		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SpriteGraphic.drawSprite(shader, emissionTexture, alphaTexture);
		ModelMatrix.main.addRotationY(90.0f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SpriteGraphic.drawSprite(shader, emissionTexture, alphaTexture);
		ModelMatrix.main.addRotationX(90.0f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SpriteGraphic.drawSprite(shader, emissionTexture, alphaTexture);
		
		ModelMatrix.main.popMatrix();
	}
}
