package com.ru.tgra.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

import com.ru.tgra.graphics.*;
import com.ru.tgra.graphics.motion.BSplineMotion;
import com.ru.tgra.graphics.motion.BezierMotion;
import com.ru.tgra.graphics.motion.LinearMotion;
import com.ru.tgra.graphics.motion.Tentacle;
import com.ru.tgra.graphics.particles.ParticleEffect;
import com.ru.tgra.graphics.shapes.*;
import com.ru.tgra.graphics.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.graphics.shapes.g3djmodel.MeshModel;

public class LabMeshTexGame extends ApplicationAdapter implements InputProcessor {

	Shader shader;

	private float angle;

	private Camera cam;
	private Camera topCam;
	
	private float fov = 90.0f;
	
	float currentTime;
	boolean firstFrame = true;

	MeshModel model;
	MeshModel model2;
	MeshModel model3;
	MeshModel model4;
	MeshModel model5;
	
	ParticleEffect particleEffect1;
	ParticleEffect particleEffect2;
	
	//BezierMotion motion;
	BSplineMotion motion;
	Point3D modelPosition;

	private Texture tex;
	private Texture tex2;
	private Texture tex3;
	private Texture tex4;
	private Texture tex5;
	private Texture tex6;
	private Texture alphaTex;
	private Texture particleTex;
	
	private Octopus Otto;
	private Tentacle tent;
	private Point3D target;
	private float targetAngle;
	
	Random rand = new Random();
	
	BezierPatch patch;

	@Override
	public void create () {

		Gdx.input.setInputProcessor(this);

		DisplayMode disp = Gdx.graphics.getDesktopDisplayMode();
		//Gdx.graphics.setDisplayMode(disp.width, disp.height, true);

		shader = new Shader();

		tex = new Texture(Gdx.files.internal("textures/sand5.png")); 
		tex2 = new Texture(Gdx.files.internal("textures/granite02.png")); 
		tex3 = new Texture(Gdx.files.internal("textures/marble01.png")); 
		tex4 = new Texture(Gdx.files.internal("textures/pinkMarble01.png")); 
		tex5 = new Texture(Gdx.files.internal("textures/slate01.png"));
		tex6 = new Texture(Gdx.files.internal("textures/phobos2k.png"));
		alphaTex = new Texture(Gdx.files.internal("textures/bubble01.png"));    //bubble 02 eða 01 eða 01 eða 01
		particleTex = new Texture(Gdx.files.internal("textures/bubble02.png")); //bubble 01 eða 02 eða 03 eða 04

		model = G3DJModelLoader.loadG3DJFromFile("testBlob.g3dj", true);
		model2 = G3DJModelLoader.loadG3DJFromFile("testBlob.g3dj", true);
		model3 = G3DJModelLoader.loadG3DJFromFile("testBlob.g3dj", true);
		model4 = G3DJModelLoader.loadG3DJFromFile("testBlob.g3dj", true);
		model5 = G3DJModelLoader.loadG3DJFromFile("testBlob.g3dj", true);
		//bubbles
		float smallBubble = 0.1f;
		float mediumBubble = 0.2f;
		particleEffect1 = new ParticleEffect(new Point3D(5,0,5), 
				15.0f, 1.3f, mediumBubble, 0.1f, 0.2f, 0.4f, 
				particleTex, alphaTex);
		particleEffect2 = new ParticleEffect(new Point3D(5,0,5), 
				15.0f, 1.3f, smallBubble, 0.1f, 0.2f, 0.4f, 
				particleTex, alphaTex);
		
		//fire
		/*particleEffect = new ParticleEffect(new Point3D(-1,4,-1), 
										120.0f, 1.0f, 0.4f, 0.1f, 0.2f, 0.3f, 
										particleTex, alphaTex);*/
		//smoke
	/*	particleEffect = new ParticleEffect(new Point3D(-1,4,-1), 
				20.0f, 10.0f, 2.8f, 2.0f, 2.2f, 0.3f, 
				tex, particleTex);
		
		particleEffect.fadeInTime = 5.0f;
		particleEffect.fadeOutTime = 5.0f;
		particleEffect.setParticleLifeTime(15.0f);*/

		BoxGraphic.create();
		SphereGraphic.create();
		SpriteGraphic.create();
		TorusGraphic.create(1.0f);
		CapsuleGraphic.create(1.0f);
		CylinderGraphic.create();
		

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		cam = new Camera();
		cam.look(new Point3D(0f, 4f, -3f), new Point3D(0,4,0), new Vector3D(0,1,0));

		topCam = new Camera();
		//orthoCam.orthographicProjection(-5, 5, -5, 5, 3.0f, 100);
		topCam.perspectiveProjection(30.0f, 1, 3, 100);

		//TODO: try this way to create a texture image
		/*Pixmap pm = new Pixmap(128, 128, Format.RGBA8888);
		for(int i = 0; i < pm.getWidth(); i++)
		{
			for(int j = 0; j < pm.getWidth(); j++)
			{
				pm.drawPixel(i, j, rand.nextInt());
			}
		}
		tex = new Texture(pm);*/

		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	
		
		ArrayList<Point3D> controlPoints = new ArrayList<Point3D>();
		/*
		controlPoints.add(new Point3D(0.4f, 2.0f, 1.0f));
		controlPoints.add(new Point3D(2.7f, 5.0f, 0.2f));
		controlPoints.add(new Point3D(3.5f, 2.0f, 0.9f));
		controlPoints.add(new Point3D(6.8f, 7.0f, 0.0f));
		controlPoints.add(new Point3D(0.0f, 6.0f, 2.0f));
		controlPoints.add(new Point3D(2.3f, 5.0f, 2.0f));
		controlPoints.add(new Point3D(4.0f, -2.0f, 2.5f));
		controlPoints.add(new Point3D(6.9f, -5.0f, 2.0f));
		controlPoints.add(new Point3D(0.0f, 6.0f, 4.4f));
		controlPoints.add(new Point3D(2.2f, 2.0f, 4.0f));
		controlPoints.add(new Point3D(4.0f, 4.0f, 4.0f));
		controlPoints.add(new Point3D(6.0f, 7.0f, 4.8f));
		controlPoints.add(new Point3D(0.5f, 4.0f, 6.0f));
		controlPoints.add(new Point3D(2.0f, -2.0f, 6.0f));
		controlPoints.add(new Point3D(4.0f, 1.0f, 6.0f));
		controlPoints.add(new Point3D(6.0f, 3.0f, 6.0f));
		*/
		controlPoints.add(new Point3D(0.0f, 0.0f, 0.0f));
		controlPoints.add(new Point3D(2.0f, 0.0f, 0.0f));
		controlPoints.add(new Point3D(4.0f, 0.0f, 0.0f));
		controlPoints.add(new Point3D(6.0f, 0.0f, 0.0f));
		controlPoints.add(new Point3D(0.0f, 0.0f, 2.0f));
		controlPoints.add(new Point3D(2.0f, 0.0f, 2.0f));
		controlPoints.add(new Point3D(4.0f, 0.0f, 2.0f));
		controlPoints.add(new Point3D(6.0f, 0.0f, 2.0f));
		controlPoints.add(new Point3D(0.0f, 0.0f, 4.0f));
		controlPoints.add(new Point3D(2.0f, 0.0f, 4.0f));
		controlPoints.add(new Point3D(4.0f, 0.0f, 4.0f));
		controlPoints.add(new Point3D(6.0f, 0.0f, 4.0f));
		controlPoints.add(new Point3D(0.0f, 0.0f, 6.0f));
		controlPoints.add(new Point3D(2.0f, 0.0f, 6.0f));
		controlPoints.add(new Point3D(4.0f, 0.0f, 6.0f));
		controlPoints.add(new Point3D(6.0f, 0.0f, 6.0f));
		patch = new BezierPatch(controlPoints);
	/*	
		motion = new BezierMotion(new Point3D(-1,4,-1), new Point3D(1,6,1), 
									new Point3D(7,6,-4), new Point3D(1,3,1),
									3.0f, 10.0f);*/
		
		motion = new BSplineMotion(controlPoints, 1.0f, 20.0f);
		modelPosition = new Point3D();
		
		tent = new Tentacle(20, 0.1f, new Point3D(-1,0,-1));
		Otto = new Octopus(new Point3D(-5,0,-5));
		target = new Point3D (-1,-1,-1);
		targetAngle = 0;
	}

	private void input()
	{
	}
	
	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime(); //gives time between frames
		
		targetAngle += 0.3f * deltaTime;
		
		float dx = (float) Math.cos(targetAngle);
		float dy = (float) Math.sin(targetAngle);
		float dz = (float) Math.sin(targetAngle);
		target.set(dx, dy, dz);
		
		//tent.follow(target);
		tent.reach(target);
		
		if(firstFrame)
		{
			currentTime = 0.0f;
			firstFrame = false;
		}
		else
		{
			currentTime += Gdx.graphics.getRawDeltaTime(); //actual true time that this frame took
		}

		angle += 180.0f * deltaTime;

		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			cam.slide(-3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			cam.slide(3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			cam.slide(0, 0, -3.0f * deltaTime);
			//cam.walkForward(3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			cam.slide(0, 0, 3.0f * deltaTime);
			//cam.walkForward(-3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {
			cam.slide(0, 3.0f * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			cam.slide(0, -3.0f * deltaTime, 0);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			cam.yaw(-90.0f * deltaTime);
			//cam.rotateY(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			cam.yaw(90.0f * deltaTime);
			//cam.rotateY(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cam.pitch(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cam.pitch(-90.0f * deltaTime);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
			cam.roll(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			cam.roll(90.0f * deltaTime);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.T)) {
			fov -= 30.0f * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			fov += 30.0f * deltaTime;
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
		{
			Gdx.graphics.setDisplayMode(500, 500, false);
			Gdx.app.exit();
		}

		//do all updates to the game
		//motion.getCurrentPosition(currentTime, modelPosition);
		particleEffect1.update(deltaTime);
		particleEffect2.update(deltaTime);
	}
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		//Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

		//Gdx.gl.glEnable(GL20.GL_BLEND); //switch on blending, everytime something has gone through the open gl pipeline, it leaves some color
										//when we put a new pixel we mix the new color in a pixel with old value of pixel
		//Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE); //add up colors, the one thats there with the one thats coming in
		//Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); //source color, destination color, traditional transparency
		//Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
		//Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		for(int viewNum = 0; viewNum < 2; viewNum++)
		{
			if(viewNum == 0)
			{
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
				Gdx.gl.glScissor(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
				cam.perspectiveProjection(fov, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 0.2f, 20.0f);
				shader.setViewMatrix(cam.getViewMatrix());
				shader.setProjectionMatrix(cam.getProjectionMatrix());
				shader.setEyePosition(cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);
				
				shader.setFogStart(0.0f);
				shader.setFogEnd(20.0f);
				shader.setFogColor(0.0f, 0.0f, 0.0f, 1.0f);
				Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			}
			else
			{
				Gdx.gl.glViewport(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
				Gdx.gl.glScissor(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
				topCam.look(new Point3D(cam.eye.x, 20.0f, cam.eye.z), cam.eye, new Vector3D(0,0,-1));
				//orthoCam.look(new Point3D(7.0f, 40.0f, -7.0f), new Point3D(7.0f, 0.0f, -7.0f), new Vector3D(0,0,-1));
				topCam.perspectiveProjection(30.0f, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 3, 100);
				shader.setViewMatrix(topCam.getViewMatrix());
				shader.setProjectionMatrix(topCam.getProjectionMatrix());
				shader.setEyePosition(topCam.eye.x, topCam.eye.y, topCam.eye.z, 1.0f);
				
				shader.setFogStart(90.0f);
				shader.setFogEnd(100.0f);
				shader.setFogColor(1.0f, 1.0f, 1.0f, 1.0f);
				Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			}

	
			//BoxGraphic.drawOutlineCube();
			//SphereGraphic.drawSolidSphere();
			//SphereGraphic.drawOutlineSphere();


			ModelMatrix.main.loadIdentityMatrix();
			
			patch.draw(shader, tex);
			

			Otto.draw(shader, null, null);
			tent.drawTentacle(shader);
			//tent.drawTentacleSkeleton(shader);
			
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(target.x, target.y, target.z);
			ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			SphereGraphic.drawSolidSphere(shader, null, null);
			ModelMatrix.main.popMatrix();

			//drawPyramids();

			//ModelMatrix.main.addRotationZ(angle);

			float s = (float)Math.sin((angle / 2.0) * Math.PI / 180.0);
			float c = (float)Math.cos((angle / 2.0) * Math.PI / 180.0);

			//shader.setLightPosition(0.0f + c * 3.0f, 5.0f, 0.0f + s * 3.0f, 1.0f);
			//shader.setLightPosition(3.0f, 4.0f, 0.0f, 1.0f);
			shader.setLightPosition(0, cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);


			float s2 = Math.abs((float)Math.sin((angle / 1.312) * Math.PI / 180.0));
			float c2 = Math.abs((float)Math.cos((angle / 1.312) * Math.PI / 180.0));

			shader.setSpotDirection(s2, -0.3f, c2, 0.0f);
			//shader.setSpotDirection(-cam.n.x, -cam.n.y, -cam.n.z, 0.0f);
			shader.setSpotExponent(0.0f);
			shader.setConstantAttenuation(1.0f);
			shader.setLinearAttenuation(0.00f);
			shader.setQuadraticAttenuation(0.00f);

			//shader.setLightColor(s2, 0.4f, c2, 1.0f);
			shader.setLightColor(1.0f, 1.0f, 1.0f, 1.0f);
			
			shader.setGlobalAmbient(0.3f, 0.3f, 0.3f, 1);

			//shader.setMaterialDiffuse(s, 0.4f, c, 1.0f);
			shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
			//shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
			shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
			shader.setShininess(5.0f);

			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(4.0f, 0.0f, 4.0f);
			//ModelMatrix.main.addTranslation(modelPosition.x, modelPosition.y, modelPosition.z);
			ModelMatrix.main.addScale(0.3f, 0.1f, 0.2f);
			//ModelMatrix.main.addRotation(angle, new Vector3D(1,1,1));
			shader.setModelMatrix(ModelMatrix.main.getMatrix());

			//BoxGraphic.drawSolidCube(shader, null, null);
			//SphereGraphic.drawSolidSphere(shader, tex, null);
			model.draw(shader, tex6);
			//SpriteGraphic.drawSprite(shader, tex, particleTex);
			//particleEffect.draw(shader);
			
			ModelMatrix.main.popMatrix();
			
			shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
			//shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
			shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
			shader.setShininess(4.0f);

			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(1.0f, 0.0f, 1.0f);
			ModelMatrix.main.addScale(0.2f, 0.1f, 0.5f);
			//ModelMatrix.main.addTranslation(modelPosition.x, modelPosition.y, modelPosition.z);
			//ModelMatrix.main.addRotation(180.0f, new Vector3D(1,1,1));
			shader.setModelMatrix(ModelMatrix.main.getMatrix());

			//BoxGraphic.drawSolidCube(shader, null, null);
			//SphereGraphic.drawSolidSphere(shader, tex, null);
			model2.draw(shader, tex2);
			
			ModelMatrix.main.popMatrix();
			
			shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
			//shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
			shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
			shader.setShininess(3.0f);

			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(3.0f, 0.0f, 4.0f);
			ModelMatrix.main.addScale(0.5f, 0.5f, 0.5f);
			//ModelMatrix.main.addTranslation(modelPosition.x, modelPosition.y, modelPosition.z);
			//ModelMatrix.main.addRotation(angle, new Vector3D(1,1,1));
			shader.setModelMatrix(ModelMatrix.main.getMatrix());

			//BoxGraphic.drawSolidCube(shader, null, null);
			//SphereGraphic.drawSolidSphere(shader, tex, null);
			model3.draw(shader, tex3);
			
			ModelMatrix.main.popMatrix();
			
			shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
			//shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
			shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
			shader.setShininess(2.0f);

			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(5.0f, 0.0f, 3.0f);
			ModelMatrix.main.addScale(0.2f, 0.1f, 0.3f);
			//ModelMatrix.main.addTranslation(modelPosition.x, modelPosition.y, modelPosition.z);
			//ModelMatrix.main.addRotation(angle, new Vector3D(1,1,1));
			shader.setModelMatrix(ModelMatrix.main.getMatrix());

			//BoxGraphic.drawSolidCube(shader, null, null);
			//SphereGraphic.drawSolidSphere(shader, tex, null);
			model4.draw(shader, tex4);
			
			ModelMatrix.main.popMatrix();
			
			shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
			//shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
			shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
			shader.setShininess(1.0f);

			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(1.0f, 0.0f, 5.0f);
			ModelMatrix.main.addScale(0.1f, 0.2f, 0.3f);
			//ModelMatrix.main.addTranslation(modelPosition.x, modelPosition.y, modelPosition.z);
			//ModelMatrix.main.addRotation(angle, new Vector3D(1,1,1));
			shader.setModelMatrix(ModelMatrix.main.getMatrix());

			//BoxGraphic.drawSolidCube(shader, null, null);
			//SphereGraphic.drawSolidSphere(shader, tex, null);
			model5.draw(shader, tex5);
			
	
			
			ModelMatrix.main.popMatrix();
			
			Gdx.gl.glEnable(GL20.GL_BLEND); //switch on blending, everytime something has gone through the open gl pipeline, it leaves some color
			//when we put a new pixel we mix the new color in a pixel with old value of pixel
			//Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE); //add up colors, the one thats there with the one thats coming in
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); //source color, destination color, traditional transparency
			//Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
			//Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			shader.setMaterialDiffuse(0.0f, 0.0f, 1.0f, 0.5f);
			//shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 0.0f);
			shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 0);
			shader.setShininess(10.0f);

			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(3.0f, 1.4f, 3.0f);
			ModelMatrix.main.addScale(6.0f, 3.0f, 6.0f);
			//ModelMatrix.main.addTranslation(modelPosition.x, modelPosition.y, modelPosition.z);
			//ModelMatrix.main.addRotation(angle, new Vector3D(1,1,1));
			shader.setModelMatrix(ModelMatrix.main.getMatrix());

			BoxGraphic.drawSolidCube(shader, null, null);
			//SphereGraphic.drawSolidSphere(shader, tex, null);
			
			//particleEffect.draw(shader);
			ModelMatrix.main.popMatrix();
			
			//Gdx.gl.glEnable(GL20.GL_BLEND); //switch on blending, everytime something has gone through the open gl pipeline, it leaves some color
			//when we put a new pixel we mix the new color in a pixel with old value of pixel
			//Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE); //add up colors, the one thats there with the one thats coming in
			//Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); //source color, destination color, traditional transparency
			//Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
			//Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			//shader.setMaterialDiffuse(s, 0.4f, c, 1.0f);
			shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
			//shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
			shader.setMaterialEmission(0.5f, 0.5f, 0.5f, 1);
			shader.setShininess(5.0f);

			ModelMatrix.main.pushMatrix();
			//ModelMatrix.main.addTranslation(4.0f, 6.0f, 4.0f);
			ModelMatrix.main.addTranslation(modelPosition.x, modelPosition.y, modelPosition.z);
			//ModelMatrix.main.addScale(1.3f, 0.1f, 0.2f);
			//ModelMatrix.main.addRotation(angle, new Vector3D(1,1,1));
			shader.setModelMatrix(ModelMatrix.main.getMatrix());

			//BoxGraphic.drawSolidCube(shader, null, null);
			//SphereGraphic.drawSolidSphere(shader, tex, null);
			//model.draw(shader, particleTex);
			//SpriteGraphic.drawSprite(shader, tex, particleTex);
			particleEffect1.draw(shader);
			particleEffect2.draw(shader);
			
			ModelMatrix.main.popMatrix();
			
			
	
			
		}
	}

	@Override
	public void render () {
		
		input();
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

	}

	private void drawPyramids()
	{
		int maxLevel = 9;

		for(int pyramidNr = 0; pyramidNr < 2; pyramidNr++)
		{
			ModelMatrix.main.pushMatrix();
			if(pyramidNr == 0)
			{
				shader.setMaterialDiffuse(0.8f, 0.8f, 0.2f, 0.3f);
				shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
				shader.setShininess(150.0f);
				shader.setMaterialEmission(0, 0, 0, 1);
				ModelMatrix.main.addTranslation(0.0f, 0.0f, -7.0f);
			}
			else
			{
				shader.setMaterialDiffuse(0.5f, 0.3f, 1.0f, 0.8f);
				shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
				shader.setShininess(150.0f);
				shader.setMaterialEmission(0, 0, 0, 1);
				ModelMatrix.main.addTranslation(0.0f, 0.0f, 7.0f);
			}
			ModelMatrix.main.pushMatrix();
			for(int level = 0; level < maxLevel; level++)
			{
	
				ModelMatrix.main.addTranslation(0.55f, 1.0f, -0.55f);
	
				ModelMatrix.main.pushMatrix();
				for(int i = 0; i < maxLevel-level; i++)
				{
					ModelMatrix.main.addTranslation(1.1f, 0, 0);
					ModelMatrix.main.pushMatrix();
					for(int j = 0; j < maxLevel-level; j++)
					{
						ModelMatrix.main.addTranslation(0, 0, -1.1f);
						ModelMatrix.main.pushMatrix();
						if(i % 2 == 0)
						{
							ModelMatrix.main.addScale(0.2f, 1, 1);
						}
						else
						{
							ModelMatrix.main.addScale(1, 1, 0.2f);
						}
						shader.setModelMatrix(ModelMatrix.main.getMatrix());

						BoxGraphic.drawSolidCube(shader, null, null);
						//BoxGraphic.drawSolidCube(shader, tex);
						ModelMatrix.main.popMatrix();
					}
					ModelMatrix.main.popMatrix();
				}
				ModelMatrix.main.popMatrix();
			}
			ModelMatrix.main.popMatrix();
			ModelMatrix.main.popMatrix();
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}


}