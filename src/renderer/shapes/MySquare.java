package renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;
import renderer.builder.Items;
import renderer.world.Camera;
import renderer.world.ChunkManager;

public class MySquare {

	private int x, y;
	private short w, h;
	public static Camera camera = new Camera(0,0, 0,0);
	
	public MySquare(int x, int y, int w, int h) {
		this.setX(x);
		this.setY(y);
		this.setW(w);
		this.setH(h);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void render(Graphics g, Color color) {
		g.setColor(color);
		g.fillRect(getX()-camera.getX(), getY()-camera.getY(), getW(), getH());	
	}
	
	public void render(Graphics g, Items type) {
		g.drawImage(ChunkManager.textureManager.getImage(type), getX()-camera.getX(), getY()-camera.getY(), null);
	}
	
	public void render(Graphics g) {
		//g.setColor(Color.PINK);
		g.fillRect(getX()-camera.getX(), getY()-camera.getY(), getW(), getH());
		
	}
	
	public void directRender(Graphics g) {
		//g.setColor(Color.PINK);
		g.fillRect(getX(), getY(), getW(), getH());
		
	}
	
	public void lineRender(Graphics g) {
		//g.setColor(Color.PINK);
		g.drawRect(getX()-camera.getX(), getY()-camera.getY(), getW(), getH());
		
	}
	
	public void directLineRender(Graphics g) {
		//g.setColor(Color.PINK);
		g.drawRect(getX(), getY(), getW(), getH());
		
	}

	public int getW() {
		return this.w;
	}

	public void setW(int w) {
		this.w = (short)w;
	}

	public int getH() {
		return this.h;
	}

	public void setH(int h) {
		this.h = (short)h;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
