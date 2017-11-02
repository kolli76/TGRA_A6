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

	private Camera playerCam;
	private Camera railCam;
	
	private BSplineMotion cameraRail;
	private Point3D camOnRail;
	
	private BSplineMotion targetRail;
	private Point3D targetOnRail;
	
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
	ParticleEffect groundRustle1;
	ParticleEffect groundRustle2;
	ParticleEffect groundRustle3;
	ParticleEffect groundRustle4;
	
	//BezierMotion motion;

	BSplineMotion tentacleMotion[];
	Point3D modelPosition[];
	
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
	private Tentacle[] tentacles;
	
	Random rand = new Random();
	
	BezierPatch patch0;	

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
		smallBubbles = new ParticleEffect(new Point3D(3.0f,0.0f,2.4f), 
				15.0f, 1.3f, mediumBubble, 0.1f, 0.2f, 0.4f, 
				bubbleTex02, bubbleTex01, false);
		mediumBubbles = new ParticleEffect(new Point3D(3.0f,0.0f,2.4f), 
				15.0f, 1.3f, smallBubble, 0.1f, 0.2f, 0.4f, 
				bubbleTex02, bubbleTex01, false);
		
		groundRustle1 = new ParticleEffect(new Point3D(2,-0.5f,2), 
				20.0f, 5.0f, 0.8f, 2.0f, 2.2f, 0.1f, 
				ground01, ground02, true);
		
		groundRustle2 = new ParticleEffect(new Point3D(4,-0.5f,4), 
				20.0f, 5.0f, 0.8f, 2.0f, 2.2f, 0.1f, 
				ground01, ground02, true);
		
		groundRustle3 = new ParticleEffect(new Point3D(4,-0.5f,2), 
				20.0f, 5.0f, 0.8f, 2.0f, 2.2f, 0.1f, 
				ground01, ground02, true);
		
		groundRustle4 = new ParticleEffect(new Point3D(2,-0.5f,4), 
				20.0f, 5.0f, 0.8f, 2.0f, 2.2f, 0.1f, 
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

		playerCam = new Camera();
		playerCam.look(new Point3D(-2f, 3f, -2f), new Point3D(0,2,0), new Vector3D(0,1,0));

		railCam = new Camera();
		railCam.perspectiveProjection(30.0f, 1, 3, 100);
		
		Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		Otto = new Octopus(new Point3D(3.0f, 0.0f, 3.0f));
		tentacles = new Tentacle[8];
		
		for(int i = 0; i < 4; i++)
		{
			tentacles[i] = new Tentacle(30, 0.1f, new Point3D(3.0f+1.0f, -0.1f, 2.0f + i * 0.8f));
			tentacles[4+i] = new Tentacle(30, 0.1f, new Point3D(3.0f-1.0f, -0.1f, 2.0f + i * 0.8f));
		}
		
		ArrayList<Point3D> controlPoints = new ArrayList<Point3D>();
		ArrayList<Point3D> tent1pts = new ArrayList<Point3D>();
		ArrayList<Point3D> tent2pts = new ArrayList<Point3D>();
		ArrayList<Point3D> tent3pts = new ArrayList<Point3D>();
		ArrayList<Point3D> tent4pts = new ArrayList<Point3D>();
		ArrayList<Point3D> tent5pts = new ArrayList<Point3D>();
		ArrayList<Point3D> tent6pts = new ArrayList<Point3D>();
		ArrayList<Point3D> tent7pts = new ArrayList<Point3D>();
		ArrayList<Point3D> tent8pts = new ArrayList<Point3D>();
		
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

		for (int i = 0; i < 8; i++)
		{
			tent1pts.add(new Point3D(rand.nextFloat() +1.0f, rand.nextFloat(), rand.nextFloat() +1.0f));
			tent2pts.add(new Point3D(rand.nextFloat() + 1.0f, rand.nextFloat() - 0.5f, rand.nextFloat() + 1.0f));
			tent3pts.add(new Point3D(rand.nextFloat() + 4.0f, rand.nextFloat() - 0.5f, rand.nextFloat() + 4.0f));
			tent4pts.add(new Point3D(rand.nextFloat() + 4.0f, rand.nextFloat(), rand.nextFloat() + 4.0f));
			tent5pts.add(new Point3D(rand.nextFloat() + 1.0f, rand.nextFloat() - 0.5f, rand.nextFloat() + 4.0f));
			tent6pts.add(new Point3D(rand.nextFloat() + 1.0f, rand.nextFloat(), rand.nextFloat() + 4.0f));
			tent7pts.add(new Point3D(rand.nextFloat() + 4.0f, rand.nextFloat() - 0.5f, rand.nextFloat() + 1.0f));
			tent8pts.add(new Point3D(rand.nextFloat() + 4.0f, rand.nextFloat(), rand.nextFloat() + 1.0f));
		}
		patch0 = new BezierPatch(controlPoints);
		
		tentacleMotion = new BSplineMotion[8];
		tentacleMotion[0] = new BSplineMotion(tent1pts, 2.0f, 20.0f);
		tentacleMotion[1] = new BSplineMotion(tent2pts, 2.0f, 20.0f);
		tentacleMotion[2] = new BSplineMotion(tent3pts, 2.0f, 20.0f);
		tentacleMotion[3] = new BSplineMotion(tent4pts, 2.0f, 20.0f);
		tentacleMotion[4] = new BSplineMotion(tent5pts, 2.0f, 20.0f);
		tentacleMotion[5] = new BSplineMotion(tent6pts, 2.0f, 20.0f);
		tentacleMotion[6] = new BSplineMotion(tent7pts, 2.0f, 20.0f);
		tentacleMotion[7] = new BSplineMotion(tent8pts, 2.0f, 20.0f);

		modelPosition = new Point3D[8];
		for(int i = 0; i < 8; i++)
		{
			modelPosition[i] = new Point3D();
		}
		
		camOnRail = new Point3D(10,10,5);
		targetOnRail = new Point3D(0,0,0);
		
		ArrayList<Point3D> cameraRailPoints = new ArrayList<Point3D>();
		
	}

	private void input()
	{
	}
	
	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime(); //gives time between frames

		
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
			playerCam.slide(-3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			playerCam.slide(3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			playerCam.slide(0, 0, -3.0f * deltaTime);
			//cam.walkForward(3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			playerCam.slide(0, 0, 3.0f * deltaTime);
			//cam.walkForward(-3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {
			playerCam.slide(0, 3.0f * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			playerCam.slide(0, -3.0f * deltaTime, 0);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			playerCam.yaw(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			playerCam.yaw(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			playerCam.pitch(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			playerCam.pitch(-90.0f * deltaTime);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
			playerCam.roll(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			playerCam.roll(90.0f * deltaTime);
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
		for (int i = 0; i < 8; i++)
		{
			tentacleMotion[i].getCurrentPosition(currentTime, modelPosition[i]);
		}
		
		for (int i = 0; i < 8; i++)
		{
			tentacles[i].reach(modelPosition[i]);
		}
		
		smallBubbles.update(deltaTime);
		mediumBubbles.update(deltaTime);
		groundRustle1.update(deltaTime);
		groundRustle2.update(deltaTime);
		groundRustle3.update(deltaTime);
		groundRustle4.update(deltaTime);
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
				playerCam.perspectiveProjection(fov, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 0.2f, 100.0f);
				shader.setViewMatrix(playerCam.getViewMatrix());
				shader.setProjectionMatrix(playerCam.getProjectionMatrix());
				shader.setEyePosition(playerCam.eye.x, playerCam.eye.y, playerCam.eye.z, 1.0f);
				
				//shader.setFogStart(0.0f);
				//shader.setFogEnd(20.0f);
				//shader.setFogColor(0.0f, 0.0f, 0.0f, 1.0f);
				Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			}
			else
			{
				Gdx.gl.glViewport(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
				Gdx.gl.glScissor(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
				
				railCam.look(camOnRail, targetOnRail, new Vector3D(0,1,0));
				
				railCam.perspectiveProjection(fov, (float)Gdx.graphics.getWidth() / (float)(2*Gdx.graphics.getHeight()), 0.2f, 100.0f);
				shader.setViewMatrix(railCam.getViewMatrix());
				shader.setProjectionMatrix(railCam.getProjectionMatrix());
				shader.setEyePosition(railCam.eye.x, railCam.eye.y, railCam.eye.z, 1.0f);
				
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

/*
			Otto.draw(shader, null, null);

			shader.setMaterialDiffuse(1.0f, 0.0f, 0.0f, 1.0f); //looks good, red
			tentacles[0].drawTentacle(shader);

			shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f); //white
			tentacles[1].drawTentacle(shader);

			shader.setMaterialDiffuse(0.5f, 0.5f, 0.5f, 1.0f); //gray
			tentacles[2].drawTentacle(shader);

			shader.setMaterialDiffuse(0.0f, 1.0f, 0.0f, 1.0f);
			tentacles[3].drawTentacle(shader);

			shader.setMaterialDiffuse(0.0f, 0.0f, 1.0f, 1.0f); //blue looks good
			tentacles[4].drawTentacle(shader);

			shader.setMaterialDiffuse(0.0f, 1.0f, 1.0f, 1.0f); //cyan looks good
			tentacles[5].drawTentacle(shader);

			shader.setMaterialDiffuse(1.0f, 1.0f, 0.0f, 1.0f); //yellow, looks good
			tentacles[6].drawTentacle(shader);

			shader.setMaterialDiffuse(1.0f, 0.0f, 1.0f, 1.0f); //purple looks good
			tentacles[7].drawTentacle(shader);
*/
			
			//float s = (float)Math.sin((angle / 2.0) * Math.PI / 180.0);
			//float c = (float)Math.cos((angle / 2.0) * Math.PI / 180.0);
			
			//shader.setLightPosition(3, 3, 3, 1.0f);
			shader.setLightPosition(0, playerCam.eye.x, playerCam.eye.y, playerCam.eye.z, 1.0f);


			float s2 = Math.abs((float)Math.sin((angle / 1.312) * Math.PI / 180.0));
			float c2 = Math.abs((float)Math.cos((angle / 1.312) * Math.PI / 180.0));

			//shader.setSpotDirection(0.0f, -1.0f, 0.0f, 0.0f);
			shader.setSpotDirection(0,-playerCam.n.x, -playerCam.n.y, -playerCam.n.z, 0.0f);

			//shader.setSpotDirection(s2, -0.3f, c2, 0.0f);
			shader.setSpotExponent(0,2.0f);
			shader.setConstantAttenuation(0,1.0f);
			shader.setLinearAttenuation(0,0.00f);
			shader.setQuadraticAttenuation(0,0.00f);
			shader.setLightColor(0, 1.0f, 1.0f, 1.0f, 1.0f);
			
			/*shader.setLightPosition(1, 4, 4, 4, 1.0f);

			shader.setSpotDirection(1, 0, -0.3f, 0, 0.0f);
			
			shader.setSpotExponent(1,10.0f);
			shader.setConstantAttenuation(1,1.0f);
			shader.setLinearAttenuation(1,0.00f);
			shader.setQuadraticAttenuation(1,0.00f);
			shader.setLightColor(1, 1.0f, 0.0f, 0.0f, 1.0f);*/
			
			shader.setGlobalAmbient(0.1f, 0.1f, 0.1f, 1);
			

			/*ModelMatrix.main.pushMatrix();
			shader.setMaterialDiffuse(1.0f, 0.0f, 0.0f, 1.0f);
			shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
			shader.setShininess(10.0f);

			ModelMatrix.main.addTranslation(0.0f, 0.0f, 0.0f);
			ModelMatrix.main.addScale(10.0f, 0.1f, 10.0f);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			BoxGraphic.drawSolidCube(shader, null, null);
			
			ModelMatrix.main.popMatrix();*/
			//draw stones
			
			
			drawStones();
			//draw fishtank
			drawFishTank();
			
			
			
			//tent.drawTentacle(shader);
	
			
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
		

		patch0.draw(shader, tex);

		shader.setMaterialDiffuse(0.0f, 0.0f, 1.0f, 0.1f);

		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(10.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(6.0f, 1.0f, 3.0f);
		ModelMatrix.main.addScale(0.1f, 2.5f, 6.0f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		
		ModelMatrix.main.popMatrix();
		
		
		shader.setMaterialDiffuse(0.5f, 0.5f, 0.5f, 1.0f); //gray
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(1.0f);
		tentacles[2].drawTentacle(shader);

		shader.setMaterialDiffuse(0.0f, 1.0f, 0.0f, 1.0f); //green
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(1.0f);
		tentacles[3].drawTentacle(shader);

		shader.setMaterialDiffuse(0.0f, 0.0f, 1.0f, 1.0f); //blue looks good
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(1.0f);
		tentacles[4].drawTentacle(shader);

		shader.setMaterialDiffuse(0.0f, 1.0f, 1.0f, 1.0f); //cyan looks good
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(1.0f);
		tentacles[5].drawTentacle(shader);
		
		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		groundRustle2.draw(shader);

		groundRustle4.draw(shader);
		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//Gdx.gl.glDisable(GL20.GL_BLEND); 
		
		shader.setMaterialDiffuse(1.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(1.0f);
		Otto.draw(shader, null, null);
		

		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		smallBubbles.draw(shader);
		mediumBubbles.draw(shader);

		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shader.setMaterialDiffuse(1.0f, 0.0f, 0.0f, 1.0f); //looks good, red
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(1.0f);
		tentacles[0].drawTentacle(shader);

		shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f); //white
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(1.0f);
		tentacles[1].drawTentacle(shader);

	

		shader.setMaterialDiffuse(1.0f, 1.0f, 0.0f, 1.0f); //yellow, looks good
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(1.0f);
		tentacles[6].drawTentacle(shader);

		shader.setMaterialDiffuse(1.0f, 0.0f, 1.0f, 1.0f); //purple looks good
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(1.0f);
		tentacles[7].drawTentacle(shader);
		
		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		groundRustle1.draw(shader);
		
		groundRustle3.draw(shader);
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
		
		ModelMatrix.main.popMatrix();*/
		

/*		shader.setMaterialDiffuse(0.0f, 1.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.5f, 0.5f, 0.5f, 1);
		shader.setShininess(5.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(modelPosition0.x, modelPosition0.y, modelPosition0.z);
		ModelMatrix.main.addScale(0.5f, 0.5f, 0.5f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		SphereGraphic.drawSolidSphere(shader, null, null);
		
		ModelMatrix.main.popMatrix(); */

		


		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shader.setMaterialDiffuse(0.0f, 0.0f, 1.0f, 0.1f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(10.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(3.0f, 1.0f, 6.0f);
		ModelMatrix.main.addScale(6.0f, 2.5f, 0.1f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		
		ModelMatrix.main.popMatrix();
		
		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shader.setMaterialDiffuse(0.0f, 0.0f, 1.0f, 0.1f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(10.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(0.0f, 1.0f, 3.0f);
		ModelMatrix.main.addScale(0.1f, 2.5f, 6.0f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		
		ModelMatrix.main.popMatrix();
		
		Gdx.gl.glEnable(GL20.GL_BLEND); 
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		

		shader.setMaterialDiffuse(0.0f, 0.0f, 1.0f, 0.1f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(10.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(3.0f, 1.0f, 0.0f);
		ModelMatrix.main.addScale(6.0f, 2.5f, 0.1f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		
		ModelMatrix.main.popMatrix();
		
	}
	
	private void drawStones()
	{
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(0.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(4.0f, 0.0f, 4.0f);
		ModelMatrix.main.addScale(0.3f, 0.1f, 0.2f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		model.draw(shader, tex6);

		
		ModelMatrix.main.popMatrix();
		
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(0.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(5.0f, 0.0f, 1.0f);
		ModelMatrix.main.addScale(0.2f, 0.1f, 0.5f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		
		model2.draw(shader, tex2);
		
		ModelMatrix.main.popMatrix();
		
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(0.0f);

		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(3.0f, 0.0f, 4.0f);
		ModelMatrix.main.addScale(0.5f, 0.5f, 0.5f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		model3.draw(shader, tex3);
		
		ModelMatrix.main.popMatrix();
		
		shader.setMaterialDiffuse(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1);
		shader.setShininess(0.0f);

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
		shader.setMaterialSpecular(0.2f, 0.2f, 0.2f, 1.0f);
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