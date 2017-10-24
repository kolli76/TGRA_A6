package com.ru.tgra.graphics;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;
import com.ru.tgra.graphics.shapes.SphereGraphic;

public class BezierPatch {
	
	ArrayList<Point3D> P;
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer normalBuffer;
	private FloatBuffer uvBuffer;
	private ShortBuffer indexBuffer;
	
	private int resolution = 100; //for 100 by 100 map
	
	public BezierPatch(ArrayList<Point3D> controlPoints)
	{
		this.P = controlPoints;
		buildVertexData();
	}
	
	private void buildVertexData() //build triangles between each of the vertices
	{
	
		int vertexCount = (resolution + 1) * (resolution + 1);
		vertexBuffer = BufferUtils.newFloatBuffer(vertexCount * 3);
		vertexBuffer.rewind();

		normalBuffer = BufferUtils.newFloatBuffer(vertexCount * 3);
		normalBuffer.rewind();

		float increment = 1.0f / (float)resolution;
		
		Point3D P1 = new Point3D();
		Point3D P2 = new Point3D();
		Point3D P3 = new Point3D();
		Point3D P4 = new Point3D();
		
		Point3D point = new Point3D();
		
		Vector3D v1a = new Vector3D(0,1,0);
		Vector3D v2a = new Vector3D(0,1,0);
		Vector3D v3a = new Vector3D(0,1,0);

		Vector3D v1 = new Vector3D(0,1,0);
		Vector3D v2 = new Vector3D(0,1,0);
		Vector3D v3 = new Vector3D(0,1,0);
		Vector3D v4 = new Vector3D(0,1,0);
		
		for(float u = 0.0f; u <= 1.0f; u += increment)
		{
			//4 points along 4 bezier curves
			Bezier.getPosition(P.get(0), P.get(1), P.get(2), P.get(3), u, P1);
			Bezier.getPosition(P.get(4), P.get(5), P.get(6), P.get(7), u, P2);
			Bezier.getPosition(P.get(8), P.get(9), P.get(10), P.get(11), u, P3);
			Bezier.getPosition(P.get(12), P.get(13), P.get(14), P.get(15), u, P4);
			
			//control vectors, direction vectors for each of the curves above
			v1a = Vector3D.difference(P.get(0), P.get(1));
			v2a = Vector3D.difference(P.get(1), P.get(2));
			v3a = Vector3D.difference(P.get(2), P.get(3));
													//v1 is the derived vector for the 1st curve
			Bezier.getDirection(v1a, v2a, v3a, u, v1); // direction on that curve at this point, sent into v1
			
			v1a = Vector3D.difference(P.get(4), P.get(5));
			v2a = Vector3D.difference(P.get(5), P.get(6));
			v3a = Vector3D.difference(P.get(6), P.get(7));
			
			Bezier.getDirection(v1a, v2a, v3a, u, v2); //v2 direction along the curve
			
			v1a = Vector3D.difference(P.get(8), P.get(9));
			v2a = Vector3D.difference(P.get(9), P.get(10));
			v3a = Vector3D.difference(P.get(10), P.get(11));
			
			Bezier.getDirection(v1a, v2a, v3a, u, v3); 
			
			v1a = Vector3D.difference(P.get(12), P.get(13));
			v2a = Vector3D.difference(P.get(13), P.get(14));
			v3a = Vector3D.difference(P.get(14), P.get(15));
			
			Bezier.getDirection(v1a, v2a, v3a, u, v4); //so we have a point and a direction along each of the 4 curves
			
			for(float v = 0.0f; v <= 1.0f; v+=increment)
			{
				//use them to interpolate a single point, point along the inner curve
				Bezier.getPosition(P1, P2, P3, P4, v, point);
				vertexBuffer.put(point.x);
				vertexBuffer.put(point.y);
				vertexBuffer.put(point.z);
				
				Vector3D dir_u = new Vector3D(0.0f, 1.0f, 0.0f);
				Bezier.getDirection(v1, v2, v3, v4, v, dir_u);
				
				//build vectors between points and interpolate between them to get our vector in one direction
				v1a = Vector3D.difference(P1, P2);
				v2a = Vector3D.difference(P2, P3);
				v3a = Vector3D.difference(P3, P4);
				Vector3D dir_v = new Vector3D(0.0f, 1.0f, 0.0f); 
				Bezier.getDirection(v1a, v2a, v3a, v, dir_v);
				
				Vector3D normal = dir_v.cross(dir_u); //velocity in u direction and v dir
				
				//find the direction that each bezier is going
				normalBuffer.put(normal.x);
				normalBuffer.put(normal.y);
				normalBuffer.put(normal.z);
			}
		}
		vertexBuffer.rewind();
		normalBuffer.rewind();

		indexBuffer = BufferUtils.newShortBuffer(resolution * resolution * 6);
		indexBuffer.rewind();
		
		for(int i = 0; i < resolution; i++)
		{
			for(int j = 0; j < resolution; j++)
			{
				indexBuffer.put((short)(i * (resolution + 1) + j));
				indexBuffer.put((short)((i + 1) * (resolution + 1) + j));
				indexBuffer.put((short)(i * (resolution + 1) + j + 1));

				indexBuffer.put((short)(i * (resolution + 1) + j + 1));
				indexBuffer.put((short)((i + 1) * (resolution + 1) + j));
				indexBuffer.put((short)((i + 1) * (resolution + 1) + j + 1));
			}
		}
		indexBuffer.rewind();

	}
	
	public void draw(Shader shader, Texture tex) {


		shader.setMaterialDiffuse(0.5f, 0.5f, 0.5f, 1.0f);
		shader.setMaterialSpecular(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setMaterialEmission(0.0f, 0.0f, 0.0f, 1.0f);
		shader.setShininess(30.0f);
		
		shader.setDiffuseTexture(null);
		shader.setAlphaTexture(null);
		shader.setEmissionTexture(null);
		
		for(Point3D point: P)
		{
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(point.x, point.y, point.z);
			ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
			
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			
			//SphereGraphic.drawSolidSphere(shader, null, null);
			
			ModelMatrix.main.popMatrix();
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
		}
		shader.setDiffuseTexture(tex);
		shader.setAlphaTexture(null);
		shader.setEmissionTexture(null);

		Gdx.gl.glVertexAttribPointer(shader.getVertexPointer(), 3, GL20.GL_FLOAT, false, 0, vertexBuffer);
		Gdx.gl.glVertexAttribPointer(shader.getNormalPointer(), 3, GL20.GL_FLOAT, false, 0, normalBuffer);
		Gdx.gl.glVertexAttribPointer(shader.getUVPointer(), 2, GL20.GL_FLOAT, false, 0, normalBuffer);

		Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, 36, GL20.GL_UNSIGNED_SHORT, indexBuffer);
	}
}
