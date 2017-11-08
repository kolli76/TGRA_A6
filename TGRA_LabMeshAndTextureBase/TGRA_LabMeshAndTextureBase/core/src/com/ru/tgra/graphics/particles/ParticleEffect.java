package com.ru.tgra.graphics.particles;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Shader;
import com.ru.tgra.graphics.Vector3D;

public class ParticleEffect {

	private Point3D position;
	private Queue<Particle> particles;
	private Texture emissionTexture;
	private Texture alphaTexture;
	private float leftOverTime;
	private float particleInterval;
	private int maxParticleCount;
	private Random rand;
	private boolean tex;
	
	public float particleSize;
	private float rate;
	private float particleLifeTime;
	public float fadeInTime;
	public float fadeOutTime;
	public float maxAlpha;
	
	//rate = more particles
	public ParticleEffect(Point3D position, float rate, float particleLifeTime, float size, float fadeInTime, float fadeOutTime, float maxAlpha, Texture emissionTexture, Texture alphaTexture, boolean tex)
	{
		this.tex = tex;
		this.position = position;
		this.rate = rate;
		particles = new LinkedList<Particle>();
		this.particleInterval = 1.0f / rate;
		this.maxParticleCount = (int)(particleLifeTime * rate);
		leftOverTime = 0.0f;
		this.emissionTexture = emissionTexture;
		this.alphaTexture = alphaTexture;

		this.particleLifeTime = particleLifeTime;
		this.fadeInTime = fadeInTime;
		this.fadeOutTime = fadeOutTime;
		this.maxAlpha = maxAlpha;
		
		this.particleSize = size;
		
		rand = new Random();
	}
	
	public void setParticleLifeTime(float particleLifeTime)
	{
		this.maxParticleCount = (int)(particleLifeTime * rate);
		this.particleLifeTime = particleLifeTime;
	}
	
	public void setRate(float rate)
	{
		this.maxParticleCount = (int)(particleLifeTime * rate);
		this.rate = rate;
	}
	
	public void update(float deltaTime)
	{
		for(Particle particle: particles)
		{
			particle.update(deltaTime);
		}
		
		deltaTime += leftOverTime;
		while(deltaTime >= particleInterval)
		{		
			if(this.tex == true)
			{
																	
				Vector3D particleSpeed = new Vector3D(rand.nextFloat() - 0.5f, rand.nextFloat() -0.5f, rand.nextFloat() - 0.5f);
				particleSpeed.scale(0.02f); 
			
				Particle particle = new Particle(new Point3D(position.x + rand.nextFloat() - 0.5f, position.y + rand.nextFloat() - 0.5f, position.z + rand.nextFloat() - 0.5f), 
						particleSpeed, particleSize, particleLifeTime, fadeInTime, fadeOutTime, maxAlpha,
						emissionTexture, alphaTexture);
				
				particle.update(deltaTime);
				particles.add(particle);
			}
			else
			{
				Vector3D particleSpeed = new Vector3D(rand.nextFloat() - 0.5f, rand.nextFloat() + 1.5f, rand.nextFloat() - 0.5f);
				particleSpeed.scale(0.6f); 

				Particle particle = new Particle(new Point3D(position.x , position.y, position.z), 
						particleSpeed, particleSize, particleLifeTime, fadeInTime, fadeOutTime, maxAlpha,
						emissionTexture, alphaTexture);
				
				particle.update(deltaTime);
				particles.add(particle);
			}
			
			deltaTime -= particleInterval;
		}
		leftOverTime = deltaTime;
		
		for(int particleCount = particles.size(); particleCount > maxParticleCount; particleCount--)
		{
			particles.remove();
		}
	}
	
	public void draw(Shader shader)
	{
		
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glEnable(GL20.GL_BLEND); 

		if(this.tex == true)
		{
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);//smoke with graygradient
		}
		else
		{
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE); //source color, destination color, traditional transparency
		}
		for(Particle particle: particles)
		{
			particle.draw(shader);
		}
		Gdx.gl.glDisable(GL20.GL_BLEND);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
	}
}
