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
	//Segment child;
	Segment parent;
	Segment child;
	
	float length;
	
	// Going to use spherical coordinate system
	float polarAngle;
	float azimuthAngle;
	
	
	public Segment(Point3D pos, float length, Segment parent)
	{
		polarAngle = (float) (Math.PI/4);
		azimuthAngle = (float) (Math.PI/4);
		this.length = length;
		
		this.pointA = pos;
		this.parent = parent;
		this.child = null;
		setParentage();
		calculateB();
		
	}
	
	private void setParentage()
	{
		if(parent != null)
		{
			this.parent.child = this;
		}
	}
	
	private void calculateB()
	{
		float dx = (float) (length * Math.sin(polarAngle) * Math.cos(azimuthAngle));
		float dy = (float) (length * Math.cos(polarAngle));
		float dz = (float) (length * Math.sin(polarAngle) * Math.sin(azimuthAngle));
		
		pointB = new Point3D(pointA.x + dx, pointA.y + dy, pointA.z + dz);
	}
	
	public void follow(Point3D target)
	{
		Vector3D dir = pointA.to(target);	
		polarAngle = (float) (Math.acos(dir.y/dir.length()));
		if (dir.x < 0) 
		{
			azimuthAngle = (float) (Math.atan(dir.z/dir.x) + Math.PI);
		}
		else
		{
			azimuthAngle = (float) (Math.atan(dir.z/dir.x));
		}
		
		float dx = (float) (Math.sin(polarAngle) * Math.cos(azimuthAngle));
		float dy = (float) (Math.cos(polarAngle));
		float dz = (float) (Math.sin(polarAngle) * Math.sin(azimuthAngle));
		
		Vector3D movementA = new Vector3D(dx,dy,dz);
		movementA.scale(this.length);
		pointA.set(target.x - movementA.x, target.y - movementA.y, target.z - movementA.z);
		pointB.set(target.x, target.y, target.z);
		
		if(this.parent != null)
		{
			this.parent.follow(pointA);
		}
	}

	public void anchor(Point3D anchor)
	{
		pointA.set(anchor.x, anchor.y, anchor.z);
		calculateB();
		
		if(this.child != null)
		{
			this.child.anchor(this.pointB);
		}
	}
	
	public void drawSegment(Shader shader)
	{
		
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation((pointB.x+pointA.x)/2, (pointB.y+pointA.y)/2, (pointB.z+pointA.z)/2);
		
		Vector3D temp = pointA.to(pointB);
		temp.normalize();
		Vector3D rotationVector = temp.cross(new Vector3D(0,-1,0));
		float angle = (float) Math.acos(temp.y);
		angle = (float) (angle * 180.0f / Math.PI);
		ModelMatrix.main.addRotation(angle, rotationVector);
		ModelMatrix.main.addScale(length, length, length);
		
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		CapsuleGraphic.drawSolidCapsule(shader, null, null);
		
		ModelMatrix.main.popMatrix();
	}	
	
	public void drawSegmentBone(Shader shader)
	{

		
		shader.setMaterialDiffuse(0.7f, 0.7f, 0.7f, 1.0f);
		shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
		shader.setShininess(150.0f);
		shader.setMaterialEmission(0, 0, 0, 1);
		
		ModelMatrix.main.pushMatrix();
		
		ModelMatrix.main.addTranslation(pointA.x, pointA.y, pointA.z);
		ModelMatrix.main.addScale(0.2f*this.length, 0.2f*this.length, 0.2f*this.length);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere(shader, null, null);
		
		ModelMatrix.main.popMatrix();
		
		
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation((pointB.x+pointA.x)/2, (pointB.y+pointA.y)/2, (pointB.z+pointA.z)/2);
		
		Vector3D temp = pointA.to(pointB);
		temp.normalize();
		Vector3D rotationVector = temp.cross(new Vector3D(0,-1,0));
		float angle = (float) Math.acos(temp.y);
		angle = (float) (angle * 180.0f / Math.PI);
		ModelMatrix.main.addRotation(angle, rotationVector);

		
		ModelMatrix.main.addScale(0.2f*this.length, this.length, 0.2f*this.length);
		
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		BoxGraphic.drawSolidCube(shader, null, null);
		
		ModelMatrix.main.popMatrix();
	}
	
}
