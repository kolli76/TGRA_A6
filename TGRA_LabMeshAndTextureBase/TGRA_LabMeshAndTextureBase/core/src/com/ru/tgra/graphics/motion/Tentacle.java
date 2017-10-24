package com.ru.tgra.graphics.motion;

import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Shader;

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
			segments[i] = new Segment(new Point3D(startPoint.x, startPoint.y + i*segmentLen, startPoint.z), segmentLen, segments[i-1]);
		}
	}
	
	public void follow(Point3D target)
	{
		segments[segmentNum-1].follow(target);
	}
	
	public void drawTentacle(Shader shader)
	{
		for (int i = 0; i < segmentNum; i++)
		{
			segments[i].drawSegment(shader);
		}
	}
}
