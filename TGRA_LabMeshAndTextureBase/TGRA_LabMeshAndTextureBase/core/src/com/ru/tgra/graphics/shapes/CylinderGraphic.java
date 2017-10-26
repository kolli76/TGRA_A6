package com.ru.tgra.graphics.shapes;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;
import com.ru.tgra.graphics.Shader;

public class CylinderGraphic {

	private static FloatBuffer vertexBuffer;
	private static FloatBuffer normalBuffer;
	private static FloatBuffer uvBuffer;

	private static int slices = 12;
	private static int vertexCount;
	
	public static void create() {
		//VERTEX ARRAY IS FILLED HERE

		vertexCount = 0;
		float[] array = new float[3*(slices+1)*6];
		float[] normalArray = new float[3*(slices+1)*6];
		float[] uvArray = new float[3*(slices+1)*4]; //  2/3 of the size
		
		float sliceInterval = 2.0f*(float)Math.PI / (float)slices;
		float sliceAngle;
		
		for(int sliceCount = 0; sliceCount < slices+1; sliceCount++)
		{
			sliceAngle = sliceCount * sliceInterval; //latetude lines of globe
			array[vertexCount*3] = 	 0.0f;
			array[vertexCount*3 + 1] = 0.5f;
			array[vertexCount*3 + 2] = 0.0f;
			
			normalArray[vertexCount*3] = 	 0.0f;
			normalArray[vertexCount*3 + 1] = 1.0f;
			normalArray[vertexCount*3 + 2] = 0.0f;

			uvArray[vertexCount*2] = (float)sliceCount / (float)(slices);
			uvArray[vertexCount*2 + 1] = (float)(0.0f) / (float)(1);
			
			array[vertexCount*3 + 3] = 	 0.5f*(float)Math.cos(sliceAngle);
			array[vertexCount*3 + 4] = 0.5f;
			array[vertexCount*3 + 5] = 0.5f*(float)Math.sin(sliceAngle);
			
			normalArray[vertexCount*3 + 3] = 0.0f;
			normalArray[vertexCount*3 + 4] = 1.0f;
			normalArray[vertexCount*3 + 5] = 0.0f;

			uvArray[vertexCount*2 + 2] = (float)sliceCount / (float)(slices);
			uvArray[vertexCount*2 + 3] = (float)(0.25f) / (float)(1);
									
			vertexCount += 2;
		}
		
		
			for(int sliceCount = 0; sliceCount < slices+1; sliceCount++)
			{
				sliceAngle = sliceCount * sliceInterval; //latetude lines of globe
				array[vertexCount*3] = 	 0.5f*(float)Math.cos(sliceAngle);
				array[vertexCount*3 + 1] = 0.5f;
				array[vertexCount*3 + 2] = 0.5f*(float)Math.sin(sliceAngle);
				
				normalArray[vertexCount*3] = 	 (float)Math.cos(sliceAngle);
				normalArray[vertexCount*3 + 1] = 0.0f;
				normalArray[vertexCount*3 + 2] = (float)Math.sin(sliceAngle);

				uvArray[vertexCount*2] = (float)sliceCount / (float)(slices);
				uvArray[vertexCount*2 + 1] = (float)(0.25f) / (float)(1);
				
				array[vertexCount*3 + 3] = 0.5f*(float)Math.cos(sliceAngle);
				array[vertexCount*3 + 4] = -0.5f;
				array[vertexCount*3 + 5] = 0.5f*(float)Math.sin(sliceAngle);
				
				normalArray[vertexCount*3 + 3] = (float)Math.cos(sliceAngle);
				normalArray[vertexCount*3 + 4] = 0.0f;
				normalArray[vertexCount*3 + 5] = (float)Math.sin(sliceAngle);
					
				uvArray[vertexCount*2 + 2] = (float)(sliceCount) / (float)(slices);
				uvArray[vertexCount*2 + 3] = (float)(1.0f) / (float)(1);
										
				vertexCount += 2;
			}
			
		for(int sliceCount = 0; sliceCount < slices+1; sliceCount++)
		{
			/*
			sliceAngle = sliceCount * sliceInterval; //latetude lines of globe
			array[vertexCount*3] = 	 (float)Math.cos(sliceAngle);
			array[vertexCount*3 + 1] = -0.5f;
			array[vertexCount*3 + 2] = (float)Math.sin(sliceAngle);
			
			normalArray[vertexCount*3] = 	 0.0f;
			normalArray[vertexCount*3 + 1] = -1.0f;
			normalArray[vertexCount*3 + 2] = 0.0f;

			uvArray[vertexCount*2] = (float)sliceCount / (float)(slices);
			uvArray[vertexCount*2 + 1] = (float)(0.75f) / (float)(1);
			
			array[vertexCount*3 + 3] = (float)Math.cos(sliceAngle);
			array[vertexCount*3 + 4] = -0.5f;
			array[vertexCount*3 + 5] = (float)Math.sin(sliceAngle);
			
			normalArray[vertexCount*3] = 	 0.0f;
			normalArray[vertexCount*3 + 1] = -1.0f;
			normalArray[vertexCount*3 + 2] = 0.0f;
				
			uvArray[vertexCount*2 + 2] = (float)(sliceCount) / (float)(slices);
			uvArray[vertexCount*2 + 3] = (float)(1.0f) / (float)(1);
									
			vertexCount += 2;
			*/
			sliceAngle = sliceCount * sliceInterval; //latetude lines of globe
			array[vertexCount*3] = 	 0.0f;
			array[vertexCount*3 + 1] = -0.5f;
			array[vertexCount*3 + 2] = 0.0f;
			
			normalArray[vertexCount*3] = 	 0.0f;
			normalArray[vertexCount*3 + 1] = -1.0f;
			normalArray[vertexCount*3 + 2] = 0.0f;

			uvArray[vertexCount*2] = (float)sliceCount / (float)(slices);
			uvArray[vertexCount*2 + 1] = (float)(0.0f) / (float)(1);
			
			array[vertexCount*3 + 3] = 	 0.5f*(float)Math.cos(sliceAngle);
			array[vertexCount*3 + 4] = -0.5f;
			array[vertexCount*3 + 5] = 0.5f*(float)Math.sin(sliceAngle);
			
			normalArray[vertexCount*3 + 3] = 0.0f;
			normalArray[vertexCount*3 + 4] = -1.0f;
			normalArray[vertexCount*3 + 5] = 0.0f;

			uvArray[vertexCount*2 + 2] = (float)sliceCount / (float)(slices);
			uvArray[vertexCount*2 + 3] = (float)(0.25f) / (float)(1);
									
			vertexCount += 2;
		}	
		
		vertexBuffer = BufferUtils.newFloatBuffer(vertexCount*3);
		vertexBuffer.put(array);
		vertexBuffer.rewind();
		normalBuffer = BufferUtils.newFloatBuffer(vertexCount*3);
		normalBuffer.put(normalArray);
		normalBuffer.rewind();
		uvBuffer = BufferUtils.newFloatBuffer(vertexCount*2);
		uvBuffer.put(uvArray);
		uvBuffer.rewind();
	}

	public static void drawSolidCylinder(Shader shader, Texture diffuseTexture, Texture alphaTexture/*, Texture specularTexture*/) {

		shader.setDiffuseTexture(diffuseTexture);
		shader.setAlphaTexture(alphaTexture);

		Gdx.gl.glVertexAttribPointer(shader.getVertexPointer(), 3, GL20.GL_FLOAT, false, 0, vertexBuffer);
		Gdx.gl.glVertexAttribPointer(shader.getNormalPointer(), 3, GL20.GL_FLOAT, false, 0, normalBuffer);
		Gdx.gl.glVertexAttribPointer(shader.getUVPointer(), 2, GL20.GL_FLOAT, false, 0, uvBuffer);

		for(int i = 0; i < vertexCount; i += (slices+1)*2)
		{
			Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_STRIP, i, (slices+1)*2);
		}
	}
	
	public static void drawHollowCylinder(Shader shader, Texture diffuseTexture, Texture alphaTexture/*, Texture specularTexture*/) {

		shader.setDiffuseTexture(diffuseTexture);
		shader.setAlphaTexture(alphaTexture);

		Gdx.gl.glVertexAttribPointer(shader.getVertexPointer(), 3, GL20.GL_FLOAT, false, 0, vertexBuffer);
		Gdx.gl.glVertexAttribPointer(shader.getNormalPointer(), 3, GL20.GL_FLOAT, false, 0, normalBuffer);
		Gdx.gl.glVertexAttribPointer(shader.getUVPointer(), 2, GL20.GL_FLOAT, false, 0, uvBuffer);

		for(int i = (slices+1)*2; i < vertexCount-(slices+1)*2; i += (slices+1)*2)
		{
			Gdx.gl.glDrawArrays(GL20.GL_TRIANGLE_STRIP, i, (slices+1)*2);
		}
	}
	
	

}
