package renderer.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import renderer.GUI.ItemsViewer.Slot;
import renderer.builder.Block;
import renderer.builder.Items;

public class Drop {
	private float x, y;
	private Image image;
	private int amount;
	private float[] velocity = new float[2];
	private Items type;
	private Slot slot;
	public float[] getVelocity() {
		return velocity;
	}

	public void setVelocity(float[] velocity) {
		this.velocity = velocity;
	}

	public Drop(int x, int y, Image image, float velocityX, float velocityY, int amount, Items type) {
		this.slot = new Slot(type, amount);
		this.setX(x);
		this.setY(y);
		this.image = image;
		this.velocity[0] = velocityX;
		this.velocity[1] = velocityY;
		this.setAmount(amount);
		this.type = type;
	}
	
	public Slot getSlot() {
		return slot;
	}

	public void setSlot(Slot slot) {
		this.slot = slot;
	}

	public Items getType() {
		return type;
	}

	public void setType(Items type) {
		this.type = type;
	}

	public void update(double deltaT, Block posBlock) {
		if(!posBlock.isSolid()) {
			if(velocity[0] > 0) {velocity[0] += 0.1;}
			if(velocity[0] < 0) {velocity[0] -= 0.1;}
			if(velocity[1] > 0) {velocity[1] += 0.1;}
			if(velocity[1] < 0) {velocity[1] -= 0.1;}

			this.setX(this.getX() + this.velocity[0] * (float)deltaT * 40);
			this.setY(this.getY() + this.velocity[1] * (float)deltaT * 40);
		}
	}
	
	
	public void render(Graphics g, int cameraX, int cameraY, int xOffset, int yOffset) {
		g.drawImage(image, (int)getX()-cameraX+xOffset, (int)getY()-cameraY+yOffset, null);
		//g.setColor(Color.RED);
		//g.fillRect((int)getX()-cameraX, (int)getY()-cameraY, 10, 10);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	

}
