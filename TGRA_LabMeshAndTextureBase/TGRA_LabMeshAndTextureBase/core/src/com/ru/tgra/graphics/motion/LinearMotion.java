package com.ru.tgra.graphics.motion;

import com.ru.tgra.graphics.Point3D;

public class LinearMotion {
	Point3D P1;
	Point3D P2;
	float startTime;
	float endTime;
	
	public LinearMotion(Point3D P1, Point3D P2, float startTime, float endTime)
	{
		this.P1 = P1;
		this.P2 = P2;
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
			out_position.x = P2.x;
			out_position.y = P2.y;
			out_position.z = P2.z;
		}
		else
		{
			float t = (currentTime - startTime) / (endTime - startTime);
			out_position.x = (1.0f - t) * P1.x + t * P2.x; //lerp
			out_position.y = (1.0f - t) * P1.y + t * P2.y; //lerp
			out_position.z = (1.0f - t) * P1.z + t * P2.z; //lerp
		}
	}
}
