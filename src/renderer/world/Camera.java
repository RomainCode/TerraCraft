package renderer.world;

import renderer.shapes.MySquare;

public class Camera {

	private int x;
	private int y;
	private MySquare viewSurface;
	private int WIDTH, HEIGHT;
	

	public Camera(int x, int y, int WIDTH, int HEIGHT) {
		this.x = x;
		this.y = y;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.viewSurface = new MySquare(x, y, WIDTH, HEIGHT);
	}
	
	
	public MySquare getViewSurface() {
		return viewSurface;
	}


	public void setViewSurface(MySquare viewSurface) {
		this.viewSurface = viewSurface;
	}


	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setX(int x) {
		this.x = x;
		viewSurface.setX(this.x);
		
	}
	
	public void setY(int y) {
		this.y = y;
		viewSurface.setX(this.x);
	}
	
	public void translate(int x, int y) {
		this.x += x;
		this.y += y;
	}
}
