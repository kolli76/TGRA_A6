package com.ru.tgra.graphics.shapes.g3djmodel;

import java.nio.FloatBuffer;
import java.util.Vector;

import com.badlogic.gdx.utils.BufferUtils;
import com.ru.tgra.graphics.Vector3D;

public class Mesh {
	//public Vector<String> attributes;
	public FloatBuffer vertices;
	public FloatBuffer normals;
	public FloatBuffer uvBuffer;
	public boolean usesTexture;

	public Mesh()
	{
		vertices = null;
		normals = null;
		uvBuffer = null;
		usesTexture = false;
	}
	
	public void buildSpericalUVMap()
	{
		int vertexCount = vertices.capacity() / 3;
		uvBuffer = BufferUtils.newFloatBuffer(vertexCount * 2);
		//go through each vertex, figure out uv coords based on this object
		//make a vector for each vertex
		for(int vertexNum = 0; vertexNum < vertexCount; vertexNum++)
		{
			//the relation of the vertex relative to the center 	
			//where on the surface of a sphere this vector would be pointing to
			//point on spherical that we wanna map to this vertex, we imagine we take a big sphere and shrinkwrap our irregular object into that sphere
			Vector3D v = new Vector3D(vertices.get(vertexNum*3), vertices.get(vertexNum*3 + 1), vertices.get(vertexNum*3 + 2));
			if(v.length() == 0.0f)
			{
				v.y = 1.0f;
			}
			
			//angle in the latetude and longetude
			//start with latetude, along the equator
			//horizontal v, drop y coords and only look at v coords
			Vector3D vH = new Vector3D(v.x, 0, v.z);
			if(vH.length() == 0.0f)
			{
				vH.y = 1.0f;
			}
			
			//x axis
			Vector3D xA = new Vector3D(1,0,0);
			
			vH.normalize();
			v.normalize();
			
			float latitude;
			if(vH.z <= 0.0f)
			{
				latitude = (float)Math.acos(vH.dot(xA)); //radians
			}
			else
			{
				latitude = (2.0f * (float)Math.PI) - (float)Math.acos(vH.dot(xA));
			}
			uvBuffer.put(vertexNum * 2, latitude / (2.0f * (float)Math.PI)); //convert to degrees?
			
			float longtitude;
			if(v.y == 0.0f)
			{
				longtitude = ((float)Math.PI / 2.0f);
			}
			else if(v.y > 0.0f)	
			{
				longtitude = (float)Math.acos(v.dot(vH)) + ((float)Math.PI / 2.0f); //radians
			}
			else
			{
				longtitude = ((float)Math.PI / 2.0f) - (float)Math.acos(v.dot(vH)); //radians
			}
			uvBuffer.put(vertexNum * 2 + 1, longtitude / (float)Math.PI); //convert to degrees?
		}
		uvBuffer.rewind();
		usesTexture = true;
	}
}
