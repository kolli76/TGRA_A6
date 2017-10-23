package com.ru.tgra.graphics;

public class Bezier {
	
	public static void getPosition(Point3D P1, Point3D P2, Point3D P3, Point3D P4, float t, Point3D out_position)
	{
		out_position.x = (1.0f - t) * (1.0f - t) * (1.0f - t) * P1.x + 3 * (1.0f - t) * (1.0f - t) * t * P2.x + 3 * (1.0f - t) * t * t * P3.x + t * t * t * P4.x; //lerp
		out_position.y = (1.0f - t) * (1.0f - t) * (1.0f - t) * P1.y + 3 * (1.0f - t) * (1.0f - t) * t * P2.y + 3 * (1.0f - t) * t * t * P3.y + t * t * t * P4.y; //lerp
		out_position.z = (1.0f - t) * (1.0f - t) * (1.0f - t) * P1.z + 3 * (1.0f - t) * (1.0f - t) * t * P2.z + 3 * (1.0f - t) * t * t * P3.z + t * t * t * P4.z; //lerp
	}
	
	public static void getDirection(Vector3D v1, Vector3D v2, Vector3D v3, float t, Vector3D out_direction)
	{
		out_direction.x = (1.0f - t) * (1.0f - t) * v1.x + 2 * (1.0f - t) * t * v2.x + t * t * v3.x;
		out_direction.y = (1.0f - t) * (1.0f - t) * v1.y + 2 * (1.0f - t) * t * v2.y + t * t * v3.y;
		out_direction.z = (1.0f - t) * (1.0f - t) * v1.z + 2 * (1.0f - t) * t * v2.z + t * t * v3.z;
	}
	
	public static void getDirection(Vector3D v1, Vector3D v2, Vector3D v3, Vector3D v4, float t, Vector3D out_direction)
	{
		out_direction.x = (1.0f - t) * (1.0f - t) * (1.0f - t) * v1.x + 3 * (1.0f - t) * (1.0f - t) * t * v2.x + 3 * (1.0f - t) * t * t * v3.x + t * t * t * v4.x; //lerp
		out_direction.y = (1.0f - t) * (1.0f - t) * (1.0f - t) * v1.y + 3 * (1.0f - t) * (1.0f - t) * t * v2.y + 3 * (1.0f - t) * t * t * v3.y + t * t * t * v4.y; //lerp
		out_direction.z = (1.0f - t) * (1.0f - t) * (1.0f - t) * v1.z + 3 * (1.0f - t) * (1.0f - t) * t * v2.z + 3 * (1.0f - t) * t * t * v3.z + t * t * t * v4.z; //lerp
	}
}
