package com.ru.tgra.graphics.motion;

import java.util.ArrayList;
import com.ru.tgra.graphics.Point3D;
import com.ru.tgra.graphics.Vector3D;

public class BSplineMotion {

	ArrayList<BezierMotion> motions;
	Point3D pStart;
	Point3D pEnd;
	float startTime;
	float endTime;
	//use control points to build separate bezier motions
	public BSplineMotion(ArrayList<Point3D> controlPoints, float startTime, float endTime)
	{
		this.startTime = startTime;
		this.endTime = endTime;
		
		int motionCount = (controlPoints.size() / 2 ) - 1; // 2 and 2 together
		float timePerMotion = (endTime - startTime) /(float)motionCount;
		
		
		motions = new ArrayList<BezierMotion>();
		
		if(controlPoints.size() < 4)
		{
			motions = null;
			return;
		}
		Point3D P1 = controlPoints.get(0);
		Point3D P2 = controlPoints.get(1);
		Point3D P3 = controlPoints.get(2);
		Point3D P4 = controlPoints.get(3);
		//1st 4 control points, 1st motion in the BSpline curve
		BezierMotion motion = 
				new BezierMotion(P1, P2, P3, P4,
								startTime, startTime + timePerMotion);
		
		motions.add(motion);
		
		pStart = P1;
		//we need to decide start and end time for each motion
		//take 2 points out of the control point list
		for(int i = 1; i < motionCount; i++)
		{
			//build points for next curve
			P1 = P4;
			P2 = P1;
			P2.add(Vector3D.difference(P4, P3));
			
			P3 = controlPoints.get((i + 1) * 2);
			P4 = controlPoints.get((i + 1) * 2 + 1);
			
			motion = new BezierMotion(P1, P2, P3, P4,
					startTime + timePerMotion * i, startTime + timePerMotion * (i+1));
			motions.add(motion); //add motion to list of motions
		}
		
		pEnd = P4;
	}
	
	public void getCurrentPosition(float currentTime, Point3D out_position)
	{
		if(currentTime < startTime)
		{
			out_position.x = pStart.x;
			out_position.y = pStart.y;
			out_position.z = pStart.z;
		}
		else if(currentTime > endTime)
		{
			out_position.x = pEnd.x;
			out_position.y = pEnd.y;
			out_position.z = pEnd.z;
		}
		else
		{
			for(BezierMotion motion: motions)
			{
				if(motion.startTime < currentTime && currentTime < motion.endTime) //this is the one we should use right now
				{
					motion.getCurrentPosition(currentTime, out_position);
					break;
				}
			}
		}
	}
}
