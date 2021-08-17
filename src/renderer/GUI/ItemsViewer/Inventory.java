package renderer.GUI.ItemsViewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;

import renderer.GUI.Crafter.PortableCrafter;
import renderer.builder.Items;
import renderer.builder.utils;
import renderer.entity.Drop;
import renderer.shapes.MySquare;
import renderer.texture.TextureManager;
import renderer.world.ChunkManager;

public class Inventory {
	public Slot[][] slots;
	public int itemBarIndex;
	public int stackSize = 20;
	public boolean showInventory = true;
	public boolean step = false;
	public int seletedSlot;
	public int width, height, margin;
	public boolean transfert = false;
	public int[] from;
	public int[] to;
	public Slot[] slotsList;
	public int tempMouse[] = {0, 0};
	public MySquare surface;
	public int startX, startY;
	public int selectedID = 0;
	public MySquare itemBarSurface;
	public MySquare closeBoxSurface;
	public Image closeBoxImage;


	public Inventory(int xSlots, int ySlots, int width, int height, int blockSize, int margin) {
		this.showInventory = false;
		this.xSlots = xSlots;
		this.ySlots = ySlots;
		this.slots = new Slot[ySlots][xSlots];
		this.width = width;
		this.height = height;
		this.margin = margin;
		this.startX = (width - (blockSize+margin) * xSlots) / 2;
		this.startY = height - ySlots*50;
		this.closeBoxImage = TextureManager.getImage(ChunkManager.imagesSource+"close_box.png", false, 0);
		
		this.surface = new MySquare(startX-5, startY-25, 50*xSlots, 50*ySlots+20);
		this.itemBarSurface = new MySquare(startX-5, (ySlots-1)*50+startY, 50*xSlots, 50*ySlots);
		this.closeBoxSurface = new MySquare(startX-25+50*xSlots, startY-25, 20, 20);
		
		slotsList = new Slot[xSlots*ySlots];
		
		int c =0;
		for(int y = 0; y < ySlots; y++) {
			for(int x = 0; x < xSlots; x++) {
				this.slots[y][x] = new Slot(Items.Unknown, 0);
				this.slots[y][x].surface.setX(startX+x*50);
				this.slots[y][x].surface.setY(y*50+startY);
				this.slotsList[c] = this.slots[y][x];
				c++;
			}
		}
		
		slotsList[16].setType(Items.Wooden_Pickaxe);
		slotsList[16].setAmount(1);
		slotsList[17].setType(Items.Torch);
		slotsList[17].setAmount(5);

	}
	
	public boolean isInRow(Items type, int rowIndex) {
		for(int i = rowIndex*xSlots; i < (rowIndex+1)*xSlots; i++) {
			if(slotsList[i].getType() == type && slotsList[i].getAmount() != 0){
				return true;
			}
		}
		return false;
	}
	
	public Slot getSelectedSlot() {
		return slots[ySlots-1][selectedID];
	}
	
	public void setSelector(Items type) {
		int x = 0;
		for(int i = (ySlots-1)*xSlots; i < (ySlots)*xSlots; i++) {
			
			if(slotsList[i].getType() == type && slotsList[i].getAmount() > 0){
				selectedID = x;
			}
			if(x == 9) {x=0;}
		}
		
	}
	
	public void toogleInventoryVisibility() {
		if(showInventory) {
			showInventory = false;
		}else {
			showInventory = true;
		}
	}
	
	
	public void render(Graphics2D g2) {
		g2.setColor(new Color(150,150,150));
		
		if(showInventory) {
			surface.directRender(g2);
		}
		
		for(int y = 0; y < ySlots; y++) {
			if(y == ySlots-1 || showInventory) {
				g2.setColor(new Color(150,150,150));
				g2.fillRect(startX-5, startY+y*50-5, 50*(xSlots), 50);
				for(int x = 0; x < xSlots; x++) {
					
					g2.setColor(new Color(100,100,100));
					g2.fillRect(startX+x*50-2, y*50+startY-2, 40+4, 40+4);
					if(this.slots[y][x].getAmount() != 0 && this.slots[y][x].getType() != Items.Unknown) {
						this.slots[y][x].render(g2, startX+x*50, y*50+startY);
					}
					
					if(y == ySlots-1 && x == selectedID) {
						g2.setColor(Color.RED);
						g2.setStroke(new BasicStroke(1.5f));
						g2.drawRect(startX+x*50-3, y*50+startY-3, 40+6, 40+6);
					}
				}
			}
			
		}
		
		if(showInventory) {
			g2.setColor(Color.BLACK);
			g2.drawImage(closeBoxImage, closeBoxSurface.getX(), closeBoxSurface.getY(), null);
		}
	}
	
	
	public Slot getSlotByPos(int mouseX, int mouseY) {
		for(Slot slot :slotsList) {
			if(utils.isPointOnRectCollision(mouseX, mouseY, slot.surface)) {
				return slot;
			}
		}
		return new Slot(Items.Unknown, -444);
	}
	
	public boolean fillWithSlot(Items type, int amount, Drop drop) {
			
			System.out.println("You're trying to fill " + amount + " " + type);
			
			
			for(int x = 0; x < xSlots; x++) {
				if(this.slots[ySlots-1][x].getType() == type) {
					if(this.slots[ySlots-1][x].getAmount() + amount <= stackSize && this.slots[ySlots-1][x].durability == -444) {
						this.slots[ySlots-1][x].setAmount(this.slots[ySlots-1][x].getAmount() + amount);
						this.slots[ySlots-1][x].setType(type);
						drop.setAmount(0);
						return true;
					}
					// create else statement
				}
			}
			
			for(int y=ySlots-1; y >= 0; y--) {
				for(int x=0; x < xSlots; x++) {
					if(this.slots[y][x].getType() == type || this.slots[y][x].getAmount() == 0 || this.slots[y][x].getType() == Items.Unknown) {
						if(this.slots[y][x].getAmount() < stackSize && this.slots[y][x].durability == -444) {
							if(this.slots[y][x].getAmount() + amount <= stackSize) {
								this.slots[y][x].setAmount(this.slots[y][x].getAmount() + amount);
								this.slots[y][x].setType(type);
								drop.setAmount(0);
								return true;
							}else {
								int na = this.slots[y][x].getAmount() + amount - stackSize;
								this.slots[y][x].setAmount(stackSize);
								this.slots[y][x].setType(type);
								System.out.println("Refilling " + na + " " + type);
								amount = na;
								drop.setAmount(na);
							}
						}else {
							// create what happen if the stack is more than stackSize
						}
					}
				}
			}
			return false;
		}
	
	
	/*
	
	public int[] getPositionInventory(int mouseX, int mouseY) {
		
		int[] t = portableCrafter.getInput(mouseX, mouseY);
		if(t[0] >= 0) {
			return t;
		}
		
		int x = (int)((mouseX-bars[0].startX)/(bars[0].blockSize+margin));
		//int x = (int)(mouseX-bars[0].startX)/(margin+bars[0].extraMargin+bars[0].blockSize);
		int y = (int)(height-mouseY)/(margin+bars[0].blockSize);
		
		if(x > xSlots-1 || x < 0) {
			return new int[] {-100, -100};
		}
		if(y > ySlots -1 || y < 0) {
			return new int[] {-100, -100};
		}
		
		return new int[] {x, y};
	}
	
	public void render(Graphics2D g2) {
		if(showInventory) {
		for(ItemBar bar : bars) {
			bar.render(g2);
		}
		}
		else {
			bars[itemBarIndex].render(g2);
		}
		
		if(transfert) {
			if(from[0]>=0 && from[0]<100) {
				if(from[0]>=0 && slots[from[1]][from[0]].getType() != Items.Unknown) {
					g2.drawImage(ChunkManager.textureManager.getImage(slots[from[1]][from[0]].getType()), tempMouse[0], tempMouse[1], null);
				}
			}
			else if (from[0] == 150) {
				if(portableCrafter.output.getType() != Items.Unknown) {
					g2.drawImage(ChunkManager.textureManager.getImage(portableCrafter.output.getType()), tempMouse[0], tempMouse[1], null);
				}
			}else if (from[0]>=100) {
				if(portableCrafter.inputs[from[0]-100][from[1]-100].getType() != Items.Unknown){
					g2.drawImage(ChunkManager.textureManager.getImage(portableCrafter.inputs[from[0]-100][from[1]-100].getType()), tempMouse[0], tempMouse[1], null);
				}
				
			}
			
		}
		
		//bars[itemBarIndex].render(g2);
	}
	
	public void update() {
		
	}
	
	public void flipSlots(int aX, int aY, int bX, int bY) {
		
		Slot fromSlot;
		Slot toSlot;
		
		if(aX >= 100 && aX < 150) {
			fromSlot = portableCrafter.inputs[aX-100][aY-100];
			
		}else if (aX == 150) {
			fromSlot = portableCrafter.output;
		}else {
			fromSlot = slots[aY][aX];
		}
		
		if(bX >= 100 && bX < 150) {
			toSlot = portableCrafter.inputs[bX-100][bY-100];
			
		}else if (bX == 150) {
			toSlot = portableCrafter.output;
			return; // can't put an item in the output slot.
		}else {
			toSlot = slots[bY][bX];
		}
		
		
		
		int aAmount = fromSlot.getAmount();
		Items aType = fromSlot.getType();
		
		int bAmount = toSlot.getAmount();
		Items bType = toSlot.getType();
		
		if(fromSlot == portableCrafter.output) {
			
		}
		
		// try to transfer a to b
		if(aType == bType) {
			if(aAmount + bAmount <= stackSize) {
				fromSlot.setAmount(0);
				fromSlot.setType(Items.Unknown);
				toSlot.setAmount(aAmount+bAmount);
				toSlot.setType(bType);
			}
			else {
				fromSlot.setAmount(aAmount - (stackSize-bAmount));
				fromSlot.setType(aType);
				toSlot.setAmount(stackSize);
				toSlot.setType(bType);
			}
		}
		else {
			fromSlot.setAmount(bAmount);
			fromSlot.setType(bType);
			toSlot.setAmount(aAmount);
			toSlot.setType(aType);
		}
		
		portableCrafter.update();
	}
	
	
	
	
	*/
	
	public Slot[][] getSlots() {
		return slots;
	}

	public void setSlots(Slot[][] slots) {
		this.slots = slots;
	}

	public int xSlots, ySlots;
	
	public int getxSlots() {
		return xSlots;
	}

	public void setxSlots(int xSlots) {
		this.xSlots = xSlots;
	}

	public int getySlots() {
		return ySlots;
	}

	public void setySlots(int ySlots) {
		this.ySlots = ySlots;
	}

}

