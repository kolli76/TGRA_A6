package com.ru.tgra.graphics.motion;

import com.ru.tgra.graphics.ModelMatrix;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Shader;
import com.ru.tgra.graphics.Vector3D;
import com.ru.tgra.graphics.shapes.BoxGraphic;
import com.ru.tgra.graphics.shapes.CapsuleGraphic;
import com.ru.tgra.graphics.shapes.SphereGraphic;

public class Segment {
	
	Point3D pointA;
	Point3D pointB;
	Segment next;
	
	float length;
	
	// Going to use spherical coordinate system
	float polarAngle;
	float azimuthAngle;
	float maxAngle;
	
	public Segment(Point3D pos, float length, Segment next)
	{
		polarAngle = (float) (Math.PI/4);
		azimuthAngle = (float) (Math.PI/4);
		maxAngle = 0.1f;
		this.length = length;
		
		this.pointA = pos;
		this.next = next;
		calculateB();
		System.out.println(pointA.x + " " +pointA.y + " " +pointA.z + " ");
		System.out.println(pointB.x + " " +pointB.y + " " +pointB.z + " ");
	}
	
	private void calculateB()
	{
		float dx = (float) (length * Math.sin(polarAngle) * Math.cos(azimuthAngle));
		float dy = (float) (length * Math.cos(polarAngle));
		float dz = (float) (length * Math.sin(polarAngle) * Math.sin(azimuthAngle));
		
		pointB = new Point3D(pointA.x + dx, pointA.y + dy, pointA.z + dz);
	}
	
	public void updateSegment()
	{
		calculateB();
	}
	
	public void follow(Point3D point)
	{
		//Vector3D target = new Vector3D(point.x, point.y, point.z);
		Vector3D dir = pointA.to(point);
		
		polarAngle = (float) (Math.acos(dir.y/dir.length()));
		
		if (dir.x < 0) 
		{
			azimuthAngle = (float) (Math.atan(dir.z/dir.x) + Math.PI);
		}
		else
		{
			azimuthAngle = (float) (Math.atan(dir.z/dir.x));
		}
		System.out.println( "angle "+ azimuthAngle);
		calculateB();
		//System.out.println(pointA.x + " " +pointA.y + " " +pointA.z + " ");
		//System.out.println(pointB.x + " " +pointB.y + " " +pointB.z + " ");
	}

	public void drawSegment(Shader shader)
	{

		
		shader.setMaterialDiffuse(0.5f, 0.3f, 1.0f, 0.8f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setShininess(150.0f);
		shader.setMaterialEmission(0, 0, 0, 1);
		
		ModelMatrix.main.pushMatrix();
		
		ModelMatrix.main.addTranslation(pointB.x, pointB.y, pointB.z);
		ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere(shader, null, null);
		
		ModelMatrix.main.popMatrix();
		
		
		ModelMatrix.main.pushMatrix();
		
		ModelMatrix.main.addTranslation(pointA.x, pointA.y, pointA.z);
		ModelMatrix.main.addScale(0.2f, 0.2f, 0.2f);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere(shader, null, null);
		
		ModelMatrix.main.popMatrix();
		
		
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation((pointB.x-pointA.x)/2, (pointB.y-pointA.y)/2, (pointB.z-pointA.z)/2);
		
		Vector3D temp = pointA.to(pointB);
		temp.normalize();
		Vector3D rotationVector = temp.cross(new Vector3D(0,-1,0));
		float angle = (float) Math.acos(temp.y);
		angle = (float) (angle * 180.0f / Math.PI);
		ModelMatrix.main.addRotation(angle, rotationVector);

		
		ModelMatrix.main.addScale(0.2f, 1, 0.2f);
		
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		//CapsuleGraphic.drawSolidCapsule(shader, null, null);
		BoxGraphic.drawSolidCube(shader, null, null);
		
		ModelMatrix.main.popMatrix();
	}
	
}
