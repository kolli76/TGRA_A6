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
	MeshModel model6;
	
	ParticleEffect smallBubbles;
	ParticleEffect mediumBubbles;
	ParticleEffect groundRustle;
	
	//BezierMotion motion;
	BSplineMotion motion;
	Point3D modelPosition;

	private Texture tex;
	private Texture tex2;
	private Texture tex3;
	private Texture tex4;
	private Texture tex5;
	private Texture tex6;
	private Texture tex7;
	private Texture bubbleTex01;
	private Texture bubbleTex02;
	private Texture ground01;
	private Texture ground02;
	
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

		tex = new Texture(Gdx.files.internal("textures/seamless_ground2048.png")); 
		tex2 = new Texture(Gdx.files.internal("textures/granite02.png")); 
		tex3 = new Texture(Gdx.files.internal("textures/marble01.png")); 
		tex4 = new Texture(Gdx.files.internal("textures/pinkMarble01.png")); 
		tex5 = new Texture(Gdx.files.internal("textures/slate01.png"));
		tex6 = new Texture(Gdx.files.internal("textures/phobos2k.png"));
		tex7 = new Texture(Gdx.files.internal("textures/seaweed.png"));
		bubbleTex01 = new Texture(Gdx.files.internal("textures/bubble01.png"));    //bubble 02 eða 01 eða 01 eða 01
		bubbleTex02 = new Texture(Gdx.files.internal("textures/bubble02.png")); //bubble 01 eða 02 eða 03 eða 04
		ground01 = new Texture(Gdx.files.internal("textures/groundRustle06.png"));
		ground02 = new Texture(Gdx.files.internal("textures/groundRustle06.png"));
		
		model = G3DJModelLoader.loadG3DJFromFile("testBlob.g3dj", true);
		model2 = G3DJModelLoader.loadG3DJFromFile("testBlob.g3dj", true);
		model3 = G3DJModelLoader.loadG3DJFromFile("testBlob.g3dj", true);
		model4 = G3DJModelLoader.loadG3DJFromFile("testBlob.g3dj", true);
		model5 = G3DJModelLoader.loadG3DJFromFile("testBlob.g3dj", true);
		model6 = G3DJModelLoader.loadG3DJFromFile("Dracena.g3dj", true);
		//bubbles
		float smallBubble = 0.1f;
		float mediumBubble = 0.2f;
		smallBubbles = new ParticleEffect(new Point3D(5,0,5), 
				15.0f, 1.3f, mediumBubble, 0.1f, 0.2f, 0.4f, 
				bubbleTex02, bubbleTex01, false);
		mediumBubbles = new ParticleEffect(new Point3D(5,0,5), 
				15.0f, 1.3f, smallBubble, 0.1f, 0.2f, 0.4f, 
				bubbleTex02, bubbleTex01, false);
		
		groundRustle = new ParticleEffect(new Point3D(5,0,1), 
				20.0f, 5.0f, 1.8f, 2.0f, 2.2f, 0.3f, 
				ground01, ground02, true);
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
		
		targetAngle = 0;
		target = new Point3D(0,0,0);
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
		smallBubbles.update(deltaTime);
		mediumBubbles.update(deltaTime);
		groundRustle.update(deltaTime);
	}
	
	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		//Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

		Gdx.gl.glEnable(GL20.GL_BLEND); //switch on blending, everytime something has gone through the open gl pipeline, it leaves some color
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
				cam.perspectiveProjection(fov, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 0.2f, 100.0f);
				shader.setViewMatrix(cam.getViewMatrix());
				shader.setProjectionMatrix(cam.getProjectionMatrix());
				shader.setEyePosition(cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);
				
				//shader.setFogStart(0.0f);
				//shader.setFogEnd(20.0f);
				//shader.setFogColor(1.0f, 1.0f, 1.0f, 1.0f);
				Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
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

			float s = (float)Math.sin((angle / 2.0) * Math.PI / 180.0);
			float c = (float)Math.cos((angle / 2.0) * Math.PI / 180.0);
			
			shader.setLightPosition(0, 3, 4, 3, 1.0f);
			//shader.setLightPosition(0, cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);


			float s2 = Math.abs((float)Math.sin((angle / 1.312) * Math.PI / 180.0));
			float c2 = Math.abs((float)Math.cos((angle / 1.312) * Math.PI / 180.0));

			shader.setSpotDirection(0.0f, -1.0f, 0.0f, 0.0f);
			//shader.setSpotDirection(s2, -0.3f, c2, 0.0f);
			shader.setSpotExponent(0.0f);
			shader.setConstantAttenuation(1.0f);
			shader.setLinearAttenuation(0.00f);
			shader.setQuadraticAttenuation(0.00f);
			shader.setLightColor(1.0f, 1.0f, 1.0f, 1.0f);
			
			shader.setGlobalAmbient(0.3f, 0.3f, 0.3f, 1);
			//draw stones
			drawStones();
			//draw fishtank
			drawFishTank();
			
			tent.drawTentacle(shader);
	
			
		}
	}

	@Override
	public void render () {
		
		input();
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

	}

	private void drawFishTank()
	{
		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		/*shader.setMaterialDiffuse(0.0f, 0.0f, 1.0f, 0.1f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 0.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 0);
		shader.setShininess(1.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(3.0f, 1.4f, 3.0f);
		ModelMatrix.main.addScale(6.0f, 3.0f, 6.0f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		
		ModelMatrix.main.popMatrix();*
		

		/*shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.5f, 0.5f, 0.5f, 1);
		shader.setShininess(5.0f);*/

	/*	ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(modelPosition.x, modelPosition.y, modelPosition.z);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		
		
		ModelMatrix.main.popMatrix();*/
		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.1f, 0.1f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(10.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(3.0f, 1.4f, 6.0f);
		ModelMatrix.main.addScale(6.0f, 3.0f, 0.1f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		
		ModelMatrix.main.popMatrix();
		
		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.1f, 0.1f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(10.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(6.0f, 1.4f, 3.0f);
		ModelMatrix.main.addScale(0.1f, 3.0f, 6.0f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		
		ModelMatrix.main.popMatrix();
		
		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.1f, 0.1f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(10.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(0.0f, 1.4f, 3.0f);
		ModelMatrix.main.addScale(0.1f, 3.0f, 6.0f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		
		ModelMatrix.main.popMatrix();
		
		smallBubbles.draw(shader);
		mediumBubbles.draw(shader);
		groundRustle.draw(shader);
		
		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.1f, 0.1f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(10.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(3.0f, 1.4f, 0.0f);
		ModelMatrix.main.addScale(6.0f, 3.0f, 0.1f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		
		ModelMatrix.main.popMatrix();
		
	}
	
	private void drawStones()
	{
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(5.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(4.0f, 0.0f, 4.0f);
		ModelMatrix.main.addScale(0.3f, 0.1f, 0.2f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		model.draw(shader, tex6);

		
		ModelMatrix.main.popMatrix();
		
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(4.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(1.0f, 0.0f, 1.0f);
		ModelMatrix.main.addScale(0.2f, 0.1f, 0.5f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		model2.draw(shader, tex2);
		
		ModelMatrix.main.popMatrix();
		
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(3.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(3.0f, 0.0f, 4.0f);
		ModelMatrix.main.addScale(0.5f, 0.5f, 0.5f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		model3.draw(shader, tex3);
		
		ModelMatrix.main.popMatrix();
		
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(2.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(5.0f, 0.0f, 3.0f);
		ModelMatrix.main.addScale(0.2f, 0.1f, 0.3f);

		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		model4.draw(shader, tex4);
		
		ModelMatrix.main.popMatrix();
		
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(1.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(1.0f, 0.0f, 5.0f);
		ModelMatrix.main.addScale(0.1f, 0.2f, 0.3f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		model5.draw(shader, tex5);

		ModelMatrix.main.popMatrix();
		
		//plant
		shader.setMaterialDiffuse(0.0f, 1.0f, 0.f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(1.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(1.0f, 0.0f, 5.0f);
		ModelMatrix.main.addScale(1.0f, 1.0f, 1.0f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		model6.draw(shader, tex7);

		ModelMatrix.main.popMatrix();
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