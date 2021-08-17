package renderer.GUI.Crafter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import renderer.GUI.ItemsViewer.Inventory;
import renderer.GUI.ItemsViewer.Slot;
import renderer.builder.Items;
import renderer.builder.utils;
import renderer.shapes.MySquare;
import renderer.texture.TextureManager;
import renderer.world.ChunkManager;



public class PortableCrafter {
	
	// Recipes
	
	
	public List<Recipe> recipes = new ArrayList<Recipe>();
	public Slot[][] inputs;
	public Slot[] inputsList;
	public Slot output;
	public int xStart = 200;
	public int yStart = 200;
	public int displayWidth;
	public int displayHeight;
	public int width, height;
	public MySquare[][] inputSurfaces;
	public boolean showPortableCrafter = false;
	public MySquare surface;
	public Recipe currentRecipe;
	public MySquare closeBoxSurface;
	public static Image closeBoxImage;
	
	public PortableCrafter(int width, int height) {
		recipes.add(new Recipe(4, new int[]{2,1,0,0}, new Items[]{Items.Wood, Items.Coal, Items.Unknown, Items.Unknown}, Items.Torch, 4, true));
		recipes.add(new Recipe(4, new int[]{1,1,1,1}, new Items[]{Items.Wood, Items.Wood, Items.Wood, Items.Wood}, Items.Chest, 1, true));
		recipes.add(new Recipe(4, new int[]{1,1,0,0}, new Items[]{Items.Wood, Items.Rock, Items.Unknown, Items.Unknown}, Items.Wooden_Pickaxe, 1, true));
		
		this.displayWidth = width;
		this.displayHeight = height;
		this.width = 350;
		this.height = 220;
		this.xStart = displayWidth/2 - this.width/2;
		this.yStart = displayHeight/2 - this.height/2;
		PortableCrafter.closeBoxImage = TextureManager.getImage(ChunkManager.imagesSource+"close_box.png", false, 0);
		surface = new MySquare(xStart, yStart, this.width, this.height);
		this.closeBoxSurface = new MySquare(xStart+this.width-20, yStart, 20, 20);
		
		inputs = new Slot[2][2];
		inputsList = new Slot[2*2];
		
		int c =0;
		for(int y = 0; y < inputs.length; y++) {
			for(int x = 0; x < inputs[0].length; x++) {
				inputs[y][x] = new Slot(Items.Unknown, 0);
				inputsList[c] = inputs[y][x];
				System.out.println(inputsList[c].getAmount());
				c++;
			}
		}
		output = new Slot(Items.Unknown, 0);
		
		inputs[0][0].setAmount(0);
		inputs[1][0].setAmount(0);
		inputs[0][1].setAmount(0);
		inputs[1][1].setAmount(0);
		
		inputs[0][0].surface = new MySquare(displayWidth/2-2*40, displayHeight/2-50, 40, 40);
		inputs[1][0].surface = new MySquare(displayWidth/2-1*40+10, displayHeight/2-50, 40, 40);
		inputs[0][1].surface = new MySquare(displayWidth/2-2*40, displayHeight/2, 40, 40);
		inputs[1][1].surface = new MySquare(displayWidth/2-1*40+10, displayHeight/2, 40, 40);
		
		output.surface = new MySquare(xStart+4*50+50, displayHeight/2-25, 50, 50);
	}
	
	public void toogleInventoryVisibility() {
		if(showPortableCrafter) {
			showPortableCrafter = false;
		}else {
			showPortableCrafter = true;
		}
	}
	
	public void clear() {
		for(int y = 0; y < inputs.length; y++) {
			for(int x = 0; x < inputs[0].length; x++) {
				inputs[y][x] = new Slot(Items.Unknown, 0);
			}
		}
	}
	
	public boolean craft() {
		if(currentRecipe != null) {
			if(currentRecipe.applay(inputsList, output)) {
				System.out.println("Item crafted");
				update();
				return true;
			}

			return false;
		}
		return false;
	}
	
	public void checkCollisionForFrom(int mouseX, int mouseY) {
		
	}
	
	public void update() {
		
		boolean isCorrect = false;
		
		for(int i = 0; i < recipes.size(); i++) {
			if(recipes.get(i).isCorrect(inputsList)) {
				currentRecipe = recipes.get(i);
				output.setType(recipes.get(i).outputItem);
				output.setAmount(recipes.get(i).outputAmount);
				System.err.println(output.getType());
				isCorrect = true;
			}
		}
		
		if(!isCorrect) {
			output.setType(Items.Unknown);
		}
	}
	
	public Slot getSlotByPos(int mouseX, int mouseY) {
		for(Slot slot :inputsList) {
			if(utils.isPointOnRectCollision(mouseX, mouseY, slot.surface)) {
				return slot;
			}
		}
		return new Slot(Items.Unknown, -444);
	}
	
	public int[] getInput(int mouseX, int mouseY) {
		
		for(int y = 0; y < 2; y++) {
			for(int x = 0; x < 2; x++) {
				if (utils.isPointOnRectCollision(mouseX, mouseY, inputSurfaces[x][y])) {
					return new int[] {x+100, y+100};
				}
			}
		}
		if(utils.isPointOnRectCollision(mouseX, mouseY, output.surface)) {
			return new int[] {150, 150};
		}
		
		return new int[] {-100, -100};
	}
	
	public void render(Graphics2D g2) {
		
		
		if(showPortableCrafter) {
			g2.setColor(new Color(150,150,150));
			surface.directRender(g2);
			//g2.fillRect(xStart, yStart, 300, 200);
			
			for(int y = 0; y < 2; y++) {
				for(int x = 0; x < 2; x++) {
					if(inputs[x][y].getType() != Items.Unknown && inputs[x][y].getAmount() > 0) {
						inputs[x][y].render(g2, inputs[x][y].surface.getX(), inputs[x][y].surface.getY());
					}else {
						g2.setColor(new Color(100,100,100));
						g2.fillRect(inputs[x][y].surface.getX(), inputs[x][y].surface.getY(), 40, 40);
					}				
					//inputs[x][y].render(g2, inputs[x][y].surface.getX(), inputs[x][y].surface.getY());
				}	
			}
			g2.setColor(new Color(100,100,100));
			g2.fillRect(output.surface.getX(), output.surface.getY(), 40, 40);
			if(output.getType() != Items.Unknown && output.getAmount() > 0) {
				output.render(g2, output.surface.getX(), output.surface.getY());
			}
			
			g2.drawImage(closeBoxImage, closeBoxSurface.getX(), closeBoxSurface.getY(), null);
		}	
	}

}
