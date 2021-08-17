package renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;
import renderer.builder.Items;
import renderer.world.Camera;
import renderer.world.ChunkManager;

public class BlockSquare {

	private int x, y;
	public static Camera camera = new Camera(0,0, 0,0);
	
	public BlockSquare(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void render(Graphics g, Color color) {
		g.setColor(color);
		g.fillRect(getX()-camera.getX(), getY()-camera.getY(), 40, 40);	
	}
	
	public void render(Graphics g, Items type) {
		g.drawImage(ChunkManager.textureManager.getImage(type), getX()-camera.getX(), getY()-camera.getY(), null);
	}
	
	public void render(Graphics g) {
		//g.setColor(Color.PINK);
		g.fillRect(getX()-camera.getX(), getY()-camera.getY(), 40, 40);
		
	}
	
	public int getW() {
		return 40;
	}
	
	public int getH() {
		return 40;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
