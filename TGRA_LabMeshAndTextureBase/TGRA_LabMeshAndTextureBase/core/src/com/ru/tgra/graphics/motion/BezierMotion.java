package com.ru.tgra.graphics.motion;

import com.ru.tgra.graphics.Bezier;
import com.ru.tgra.graphics.Point3D;

public class BezierMotion {
	Point3D P1;
	Point3D P2;
	Point3D P3;
	Point3D P4;
	float startTime;
	float endTime;
	
	public BezierMotion(Point3D P1, Point3D P2, Point3D P3, Point3D P4, float startTime, float endTime)
	{
		this.P1 = P1;
		this.P2 = P2;
		this.P3 = P3;
		this.P4 = P4;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public void getCurrentPosition(float currentTime, Point3D out_position)
	{
		if(currentTime < startTime)
		{
			out_position.x = P1.x;
			out_position.y = P1.y;
			out_position.z = P1.z;
		}
		else if(currentTime > endTime)
		{
			out_position.x = P4.x;
			out_position.y = P4.y;
			out_position.z = P4.z;
		}
		else
		{
			float t = (currentTime - startTime) / (endTime - startTime);
			Bezier.getPosition(P1,P2,P3,P4,t,out_position);
		}
	}
}
