package com.ru.tgra.graphics.shapes;

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;
import com.ru.tgra.graphics.Shader;

public class TorusGraphic {

	private static FloatBuffer vertexBuffer;
	private static FloatBuffer normalBuffer;
	private static FloatBuffer uvBuffer;

	private static int stacks = 12;
	private static int slices = 12;
	private static int vertexCount;
	
	public static void create(float gap) {
		//VERTEX ARRAY IS FILLED HERE
		
		vertexCount = 0;
		float[] array = new float[(stacks)*(slices+1)*6];
		float[] normalArray = new float[(stacks)*(slices+1)*6];
		float[] uvArray = new float[(stacks)*(slices+1)*4]; //  2/3 of the size
		
		float stackInterval = 2.0f * (float)Math.PI / (float)stacks; //multiply with 2
		float sliceInterval = 2.0f * (float)Math.PI / (float)slices;
		float stackAngle, sliceAngle;
		for(int stackCount = 0; stackCount < stacks; stackCount++)
		{
			stackAngle = stackCount * stackInterval; //longetude lines of globe
			for(int sliceCount = 0; sliceCount < slices+1; sliceCount++)
			{
				sliceAngle = sliceCount * sliceInterval; //latetude lines of globe
				array[vertexCount*3] = 	  ((float)Math.sin(stackAngle) * (float)Math.cos(sliceAngle) + gap * (float)Math.cos(sliceAngle))/gap;
				array[vertexCount*3 + 1] = (float)Math.cos(stackAngle)/gap;
				array[vertexCount*3 + 2] = ((float)Math.sin(stackAngle) * (float)Math.sin(sliceAngle) + gap * (float)Math.sin(sliceAngle))/gap;
				
				normalArray[vertexCount*3] = 	 (float)Math.sin(stackAngle) * (float)Math.cos(sliceAngle);
				normalArray[vertexCount*3 + 1] = (float)Math.cos(stackAngle);
				normalArray[vertexCount*3 + 2] = (float)Math.sin(stackAngle) * (float)Math.sin(sliceAngle);

				uvArray[vertexCount*2] = (float)sliceCount / (float)(slices);
				uvArray[vertexCount*2 + 1] = (float)stackCount / (float)(stacks);
				
				array[vertexCount*3 + 3] = ((float)Math.sin(stackAngle + stackInterval) * (float)Math.cos(sliceAngle) + gap * (float)Math.cos(sliceAngle))/gap;
				array[vertexCount*3 + 4] = (float)Math.cos(stackAngle + stackInterval)/gap;
				array[vertexCount*3 + 5] = ((float)Math.sin(stackAngle + stackInterval) * (float)Math.sin(sliceAngle) + gap * (float)Math.sin(sliceAngle))/gap;
				
				normalArray[vertexCount*3 + 3] = (float)Math.sin(stackAngle + stackInterval) * (float)Math.cos(sliceAngle);
				normalArray[vertexCount*3 + 4] = (float)Math.cos(stackAngle + stackInterval);
				normalArray[vertexCount*3 + 5] = (float)Math.sin(stackAngle + stackInterval) * (float)Math.sin(sliceAngle);
				
				
				uvArray[vertexCount*2 + 2] = (float)(sliceCount) / (float)(slices);
				uvArray[vertexCount*2 + 3] = (float)(stackCount + 1) / (float)(stacks);
										
				vertexCount += 2;
			}
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

	public static void drawSolidTorus(Shader shader, Texture diffuseTexture, Texture alphaTexture/*, Texture specularTexture*/) {

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

}
