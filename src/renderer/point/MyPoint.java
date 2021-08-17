package renderer.point;

import java.io.Serializable;

public class MyPoint implements Serializable {

	private float x, y;
	private float xOffset, yOffset;
	
	public MyPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
}
