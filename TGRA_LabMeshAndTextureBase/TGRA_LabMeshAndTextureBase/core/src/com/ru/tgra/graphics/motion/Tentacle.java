package com.ru.tgra.graphics.motion;

import com.ru.tgra.graphics.ModelMatrix;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Shader;
import com.ru.tgra.graphics.shapes.SphereGraphic;

public class Tentacle {

	private Segment segments[];
	private int segmentNum;
	private float segmentLen;
	private Point3D startPoint;
	
	public Tentacle(int segmentNum, float segmentLen, Point3D startPoint)
	{
		this.segmentNum = segmentNum;
		this.segmentLen = segmentLen;
		this.startPoint = startPoint;
		
		segments = new Segment[segmentNum];
		segments[0] = new Segment(new Point3D(startPoint.x, startPoint.y, startPoint.z), segmentLen, null);
		for (int i = 1; i < segmentNum; i++)
		{
			segments[i] = new Segment(new Point3D(segments[i-1].pointB.x, segments[i-1].pointB.y, segments[i-1].pointB.z), segmentLen, segments[i-1]);
		}
	}
	
	public void reach(Point3D target)
	{
		follow(target);
		anchor();
	}
	
	public void follow(Point3D target)
	{
		segments[segmentNum-1].follow(target);
	}
	
	public void anchor()
	{
		segments[0].anchor(this.startPoint);
	}
	
	public void drawTentacle(Shader shader)
	{
		for (int i = 0; i < segmentNum; i++)
		{
			segments[i].drawSegment(shader);
		}
	}
	
	public void drawTentacleSkeleton(Shader shader)
	{
		for (int i = 0; i < segmentNum; i++)
		{
			segments[i].drawSegmentBone(shader);
		}
	}
	
	public void drawTentacleHollow(Shader shader)
	{
		for (int i = 0; i < segmentNum; i++)
		{
			segments[i].drawCylinderSegment(shader);
		}
		
		ModelMatrix.main.pushMatrix();
		
		ModelMatrix.main.addTranslation(segments[segmentNum-1].pointB.x, segments[segmentNum-1].pointB.y, segments[segmentNum-1].pointB.z);
		ModelMatrix.main.addScale(0.5f*segmentLen, 0.5f*segmentLen, 0.5f*segmentLen);
		shader.setModelMatrix(ModelMatrix.main.getMatrix());
		SphereGraphic.drawSolidSphere(shader, null, null);
		
		ModelMatrix.main.popMatrix();
	}
	
	public int getSegmentNum()
	{
		return this.segmentNum;
	}
	
	public float getSegmentLen()
	{
		return this.segmentLen;
	}
}
