package renderer.GUI.Chest;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import renderer.GUI.ItemsViewer.Slot;
import renderer.builder.Items;
import renderer.builder.utils;
import renderer.shapes.MySquare;
import renderer.texture.TextureManager;
import renderer.world.ChunkManager;

public class Chest {

	public int[] position = new int[2];
	public Slot[] slotsList;
	public int xSlots, ySlots;
	public static int width;
	public static int height;
	public int startX, startY;
	public MySquare surface;
	public MySquare closeBoxSurface;
	public static Image closeBoxImage;
	
	public Chest(int xSlots, int ySlots, int xRelative, int yRelative) {
		position[0] = xRelative;
		position[1] = yRelative;
		this.xSlots = xSlots;
		this.ySlots = ySlots;
		startX = (width-xSlots*50)/2;
		startY = (height-ySlots*50)/2;
		Chest.closeBoxImage = TextureManager.getImage(ChunkManager.imagesSource+"close_box.png", false, 0);
		
		
		slotsList = new Slot[xSlots*ySlots];
		
		int c = 0;
		for(int y = 0; y < ySlots; y++) {
			for(int x = 0; x < xSlots; x++) {
				slotsList[c] = new Slot(Items.Unknown, c);
				slotsList[c].surface = new MySquare(startX+x*47, startY+y*47, 40, 40);
				c++;
			}
		}
		
		surface = new MySquare((width-xSlots*50)/2-5, (height-ySlots*50)/2-5-20, xSlots*48, ySlots*48+20);
		closeBoxSurface = new MySquare((width-xSlots*50)/2-5+xSlots*48-20, (height-ySlots*50)/2-5-20, 20, 20);
		
		
	}
	
	public Slot getSlotByPos(int mouseX, int mouseY) {
		for(Slot slot :slotsList) {
			if(utils.isPointOnRectCollision(mouseX, mouseY, slot.surface)) {
				return slot;
			}
		}
		return new Slot(Items.Unknown, -444);
	}
	
	public void render(Graphics2D g2) {
		int c = 0;
		g2.setColor(new Color(150,150,150));
		surface.directRender(g2);
		
		for(int y = 0; y < ySlots; y++) {
			for(int x = 0; x < xSlots; x++) {
				g2.setColor(new Color(100,100,100));
				slotsList[c].surface.directRender(g2);
				if(slotsList[c].getAmount() != 0 && slotsList[c].getType() != Items.Unknown) {
					slotsList[c].render(g2, slotsList[c].surface.getX(), slotsList[c].surface.getY());
				}
				c++;
			}
		}
		
		g2.drawImage(closeBoxImage, closeBoxSurface.getX(), closeBoxSurface.getY(), null);
	}
	
	
}
