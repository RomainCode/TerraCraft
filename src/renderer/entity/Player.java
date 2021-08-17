package renderer.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;

import renderer.GUI.Chest.Chest;
import renderer.GUI.GlobalSlotManager.SlotManager;
import renderer.GUI.ItemsViewer.Slot;
import renderer.builder.Block;
import renderer.builder.Items;
import renderer.builder.utils;
import renderer.input.ClickType;
import renderer.input.Keyboard;
import renderer.input.Mouse;
import renderer.shapes.MySquare;
import renderer.world.Camera;
import renderer.world.Chunk;
import renderer.world.ChunkManager;
import renderer.world.MiningMaster;


public class Player {
	
	private String name = "";
	private double x, y;
	private double velocity = 6;
	private double verticalVelocity = 0;
	private double horizontalVelocity = 0;
	private final int speedFactor = 30;
	private boolean isGrounded = false;
	private MySquare surface = new MySquare((int)getX(), (int)getY(), 22, 70);
	private double gravity = 1.9f;
	private double deltaT = 0;
	private Camera camera;
	private Keyboard keyboard;
	ChunkManager chunkManager;
	private Mouse mouse;
	private int gameMode = 1; // 0 = survivor, 1 = creative
	public DropManager dropManager;
	public Block blockClass;
	private MySquare bottomSurface;
	private Image playerImage;
	private Image[] playerImages;
	private Image[] breakingImages = new Image[4];
	private Image currentBreaking;
	private SlotManager slotManager;
	float health;
	private int maxHealth;
	private MiningMaster miningMaster;
	private float care = 0;
	
	
	public Player(int x, int y, Camera camera, Keyboard keyboard, Mouse mouse, ChunkManager chunkManager, SlotManager slotManager) {
		this.health = 100;
		this.maxHealth = 100;
		this.setX(x);
		this.setY(y);
		this.camera = camera;
		this.keyboard = keyboard;
		this.chunkManager = chunkManager;
		this.mouse = mouse;
		this.slotManager = slotManager;
		this.blockClass = new Block();
		Block.loadGroups();
		this.miningMaster = new MiningMaster();
		this.bottomSurface = new MySquare(0,0,surface.getW(),20);
		this.playerImages = new Image[2];
		this.playerImages[0] = ChunkManager.textureManager.getImage(chunkManager.imagesSource + "player.png", true, 1.2f);
		this.playerImages[1] = ChunkManager.textureManager.getImage(chunkManager.imagesSource + "player_1.png", true, 1.2f);
		this.playerImage = playerImages[1];
		breakingImages[0] = ChunkManager.textureManager.getImage(chunkManager.imagesSource +"breaking_0.png", true, 1.33f);
		breakingImages[1] = ChunkManager.textureManager.getImage(chunkManager.imagesSource +"breaking_1.png", true, 1.33f);
		breakingImages[2] = ChunkManager.textureManager.getImage(chunkManager.imagesSource +"breaking_2.png", true, 1.33f);
		breakingImages[3] = ChunkManager.textureManager.getImage(chunkManager.imagesSource +"breaking_blank.png", true, 1.33f);
		currentBreaking = breakingImages[0];
		Entity.camera = this.camera;
		Entity.player = this;
	}
	
	public void render(Graphics2D g) {
		//this.getSurface().render(g);
		g.drawImage(currentBreaking, (int)(miningMaster.worldPos[0]-camera.getX()), (int)(miningMaster.worldPos[1]-camera.getY()), null);
		g.drawImage(playerImage, (int)(this.x-camera.getX()), (int)(this.y-camera.getY()), null);
		g.setColor(Color.WHITE);
		g.drawString((int)health+"/"+maxHealth, chunkManager.width-250, 40);
		g.fillRoundRect(chunkManager.width-250, 50, 200, 20, 10, 10);
		g.setColor(Color.RED);
		g.fillRoundRect(chunkManager.width-250, 50, (int)((float)health/(float)maxHealth * 200), 20, 10, 10);
		//g.drawRect((int)this.x-camera.getX(), (int)this.y-camera.getY(), surface.getW(), surface.getH());
	}
	
	private boolean isGroundedX(double xOffset) {
		int[] chunkPos = chunkManager.RelativePosToChunkPos(chunkManager.worldPosToRelativePos(new int[] {(int)this.x, (int)this.y}));
		int startX = 0;
		int endX = chunkManager.chunkWidth;
		if(chunkPos[1] - 3 >= 0) {startX = chunkPos[1] - 3;}
		if(chunkPos[1] + 3 < endX) {endX = chunkPos[1] + 3;}
		
		int yStart = 0;
		int yEnd = chunkManager.chunkHeight;
		if(chunkPos[2] - 3 >= 0) {yStart = chunkPos[2] - 3;}
		if(chunkPos[2] + 3 < yEnd) {yEnd = chunkPos[2] +3;}

		
		int c = 0;
		for(Chunk chunk : this.chunkManager.getChunksList()) {
			if(utils.isCossilison(this.getSurface(), chunk.getSurface(), xOffset, 0)) {
				
				if(chunk.getxRelative() != chunkPos[0]) {
					if(this.x > chunk.getX()) {
						startX = 45;
						endX = 50;
					}
					if(this.x < chunk.getX()) {
						startX = 0;
						endX = 5;
					}
				}
				
				c++;
				for(int y = 0; y < chunkManager.chunkHeight; y++) {
					for(int x = startX; x < endX; x++) {
						if(utils.isCossilisonSquareBlock(this.getSurface(), chunk.getBlocks()[y][x].getSurface(), xOffset, 0)) {
							if(chunk.getBlocks()[y][x].isSolid()) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean isGroundedY(double yOffset) {
		bottomSurface.setX((int)this.getX());
		bottomSurface.setY((int)(this.getY()+surface.getH()-20+yOffset));
		
		int[] chunkPos = chunkManager.RelativePosToChunkPos(chunkManager.worldPosToRelativePos(new int[] {(int)this.x, (int)this.y}));
	
		
		int startX = 0;
		int endX = chunkManager.chunkWidth;
		if(chunkPos[1] - 3 >= 0) {startX = chunkPos[1] - 3;}
		if(chunkPos[1] + 3 < endX) {endX = chunkPos[1] + 3;}
		
		int yStart = 0;
		int yEnd = chunkManager.chunkHeight;
		if(chunkPos[2] - 3 >= 0) {yStart = chunkPos[2] - 3;}
		if(chunkPos[2] + 3 < yEnd) {yEnd = chunkPos[2] +3;}
		
		for(Chunk chunk : this.chunkManager.getChunksList()) {
			if(utils.isCossilison(this.getSurface(), chunk.getSurface(), 0, yOffset)) {
		
				if(chunk.getxRelative() != chunkPos[0]) {
					if(this.x > chunk.getX()) {
						startX = 45;
						endX = 50;
					}
					if(this.x < chunk.getX()) {
						startX = 0;
						endX = 5;
					}
				}
				
				for(int y = yStart; y < yEnd; y++) {
					for(int x = startX; x < endX; x++) {	
						if(utils.isCossilisonSquareBlock(this.getSurface(), chunk.getBlocks()[y][x].getSurface(), (double)0, (double)-yOffset) || utils.isCossilisonSquareBlock(this.getSurface(), chunk.getBlocks()[y][x].getSurface(), (double)0, (double)yOffset)) {
							if(chunk.getBlocks()[y][x].isSolid()) {
								if(utils.isCossilison(bottomSurface, chunk.getBlocks()[y][x].getSurface())) {
									this.setY(chunk.getBlocks()[y][x].getSurface().getY()-surface.getH());
									if(verticalVelocity > 30) {
										System.err.println("dammage="+verticalVelocity/2);
										health -= verticalVelocity/2;
									}
									this.verticalVelocity = 0;
									this.isGrounded = true;
									return true;
								}
								else {
									this.verticalVelocity = this.gravity * speedFactor * deltaT;
									//this.horizontalVelocity = 0;
								}
							}else {
								if(utils.isCossilison(bottomSurface, chunk.getBlocks()[y][x].getSurface())) {
									if(chunk.getBlocks()[y][x].getType() == Items.Water) {
										this.verticalVelocity -= gravity*0.3 * speedFactor*deltaT;
									}
								}
							}
						}
					}
				}
			}
		}
		this.isGrounded = false;
		return false;
	}

	
	private void handleDestroy() {
		Block tempBlock = chunkManager.getBlock((int)this.x, (int)this.y, mouse.getX(), mouse.getY());
		int[] relative = chunkManager.worldPosToRelativePos(chunkManager.playerToWorldPos((int)this.x, (int)this.y, mouse.getX(), mouse.getY()));
		
		miningMaster.worldPos = new int[] {relative[0]*chunkManager.blockSize, relative[1]*chunkManager.blockSize};
		
		if(miningMaster.handleClickBlock(tempBlock, slotManager.getActualSlot(), chunkManager.RelativePosToChunkPos(chunkManager.worldPosToRelativePos(chunkManager.playerToWorldPos((int)this.x, (int)this.y, mouse.getX(), mouse.getY()))), deltaT)) {
			// the block need to be destroyed and the actual tool need to be damaged
			
			//check if the block to destroy is a chest -> delete the chest and drop his content
			if(tempBlock.getType() == Items.Chest) {
				slotManager.chestManager.delete(relative, dropManager, chunkManager.blockSize);
			}
			
			dropManager.addProjectile(tempBlock.getSurface().getX(), tempBlock.getSurface().getY(), tempBlock.getType(), 1);
			chunkManager.setBlock((int)this.x, (int)this.y, mouse.getX(), mouse.getY(), null, true, false, true, surface);
			
			if(slotManager.getActualSlot().durability != -444) {
				slotManager.getActualSlot().durability -= 1;
				
				//check if we need to destroy the tool
				if(slotManager.getActualSlot().durability <= 0) {
					// destroy the slot content
					slotManager.getActualSlot().setType(Items.Unknown);
					slotManager.getActualSlot().setAmount(0);
				}
			}
			mouse.resetButton();
		}
		currentBreaking = breakingImages[miningMaster.getBreakingIndex()];
	}
	
	
	public void update(double deltaT) {
		
		this.deltaT = deltaT;
		
		if(health <= 0) {
			care = maxHealth;
		}
		
		if(care != 0) {
			health += deltaT*10;
			care -= deltaT*10;
			
			if(care <= 0) {
				care = 0;
			}
		}
		
		// Claim drop
		for(int i = 0; i < dropManager.projectileList.size(); i++) {
			Drop temp = dropManager.projectileList.get(i);
			if(utils.isPointOnRectCollision((int)temp.getX()+10, (int)temp.getY()-10, this.getSurface())) {
				if(slotManager.inventory.fillWithSlot(temp.getType(), temp.getAmount(), temp)) {
					// put the items in the inventory
					// delete the drop
					dropManager.deleteList.add(temp);
					}
			}
		}
		
		// Mouse Picking
		if(mouse.getButton() == ClickType.LeftClick) {
			
			if(slotManager.checkCollision(mouse.getX(), mouse.getY())) {
				
				if(!slotManager.processingTransfer) {
					if(slotManager.setFrom(mouse.getX(), mouse.getY())) {
						slotManager.processingTransfer = true;
					}
					mouse.resetButton();
				}
				else if(slotManager.processingTransfer) {
					slotManager.setTo(mouse.getX(), mouse.getY());
					slotManager.flipSlots(slotManager.fromTransfert, slotManager.toTransfert);
					slotManager.processingTransfer = false;
					slotManager.portableCrafter.update();
					mouse.resetButton();
				}
				
				if(slotManager.portableCrafter.showPortableCrafter) {
					if(utils.isPointOnRectCollision(mouse.getX(), mouse.getY(), slotManager.portableCrafter.output.surface)) {
						if(slotManager.tryFoCraft(mouse.getX(), mouse.getY())){
							dropManager.addProjectile((int)this.x, (int)this.y, slotManager.portableCrafter.currentRecipe.outputItem, slotManager.portableCrafter.currentRecipe.outputAmount);
						}
					}
				}
			}
			
			if(utils.isPointOnRectCollision(mouse.getX(), mouse.getY(), slotManager.inventory.itemBarSurface)) {
				slotManager.inventory.showInventory = true;
			}
			
			if(utils.isPointOnRectCollision(mouse.getX(), mouse.getY(), slotManager.inventory.closeBoxSurface)) {
				slotManager.inventory.showInventory = false;
				mouse.resetButton();
			}
			
			if(utils.isPointOnRectCollision(mouse.getX(), mouse.getY(), slotManager.portableCrafter.closeBoxSurface)) {
				slotManager.portableCrafter.showPortableCrafter = false;
				mouse.resetButton();
			}
			
			if(utils.isPointOnRectCollision(mouse.getX(), mouse.getY(), slotManager.chestManager.currentChest.closeBoxSurface)) {
				slotManager.chestManager.showChest = false;
				mouse.resetButton();
			}
			
			if(chunkManager.checkMouseDistance(mouse.getX(), mouse.getY(), chunkManager.mouseDetectionDistance)) {
				handleDestroy();
			}
		}


		if(mouse.getButton() == ClickType.RightClick) {
			mouse.resetButton();
			if(chunkManager.checkMouseDistance(mouse.getX(), mouse.getY(), chunkManager.mouseDetectionDistance)) {
				Slot tempSlot = slotManager.inventory.slots[slotManager.inventory.ySlots-1][slotManager.inventory.selectedID];
				
				Block tempBlock = chunkManager.getBlock((int)this.x, (int)this.y, mouse.getX(), mouse.getY());
				// check if the destination block is a chest
				if(tempBlock.getType() == Items.Chest) {
					int[] chestPos = chunkManager.worldPosToRelativePos(chunkManager.playerToWorldPos((int)this.x, (int)this.y, mouse.getX(), mouse.getY()));
					if(slotManager.chestManager.isOnMap(chestPos)) {
						slotManager.chestManager.currentChest = slotManager.chestManager.get(chestPos);
						slotManager.chestManager.showChest = true;
						System.out.println("Chest x="+chestPos[0]+", y="+chestPos[1]+" opened");
					}else {
						System.out.println("chest not found in the database, creation of a new one");
						// add a blank chest to the map
						slotManager.chestManager.add(new Chest(8, 3, chestPos[0],chestPos[1]), chestPos[0], chestPos[1]);
						// try to open-it
						slotManager.chestManager.currentChest = slotManager.chestManager.get(chestPos);
						slotManager.chestManager.showChest = true;
						System.out.println("Chest x="+chestPos[0]+", y="+chestPos[1]+" opened");
					}
						
				}
				
				
				if(tempSlot.getAmount() != 0 && tempSlot.getType() != Items.Unknown && Block.isPlaceable(tempSlot.getType()) &&Block.isBackground(chunkManager.getBlock((int)this.x, (int)this.y, mouse.getX(), mouse.getY()).getType())) {
					if(chunkManager.setBlock((int)this.x, (int)this.y, mouse.getX(), mouse.getY(), slotManager.inventory.slots[slotManager.inventory.ySlots-1][slotManager.inventory.selectedID].getType(), false, true, true, surface)) {
						if(tempSlot.getType() == Items.Chest) {
							int[] chestPos = chunkManager.worldPosToRelativePos(chunkManager.playerToWorldPos((int)this.x, (int)this.y, mouse.getX(), mouse.getY()));
							Chest tempChest = new Chest(8, 3, chestPos[0], chestPos[1]);
							slotManager.chestManager.add(tempChest, chestPos[0], chestPos[1]);
							System.out.println("Chest succesfully added !");
						}
						tempSlot.setAmount(tempSlot.getAmount()-1);
					}
				}
			}

			
		}
		
		if(mouse.getButton() == ClickType.MiddleClick) {
			mouse.resetButton();
			Items temp = chunkManager.getBlock((int)this.x, (int)this.y, mouse.getX(), mouse.getY()).getType();
			System.out.println("middle clicked : " + temp);
			slotManager.middleClick(temp);
		}
		
		
		// Y Axis
		this.verticalVelocity += this.gravity * speedFactor * deltaT;
			
		if(this.isGroundedY(1)) {
			if(!this.isGrounded) {return;}
		}
		 
		 if(this.keyboard.getKey(KeyEvent.VK_SPACE) && this.isGrounded) {
			 this.verticalVelocity = -this.gravity*7;
			 this.isGrounded = false;
		 }

		 // X Axis
		 if(horizontalVelocity != 0) {
			 if(!this.isGroundedX(this.horizontalVelocity)) {
				 this.isGrounded = false;
				 this.setX(this.getX() + this.horizontalVelocity * deltaT * speedFactor);	
				 if(this.horizontalVelocity < 0) {
					 this.playerImage = playerImages[0];
				 }
				 if(this.horizontalVelocity > 0) {
					 this.playerImage = playerImages[1];
				 }
			 }
			 this.horizontalVelocity = 0;
		 }

		
		// Max speed of player by frame
		// # to avoid player to pass through a block by falling with very high speed,
		// # we need to limit the falling speed < blockSize
		if(this.verticalVelocity * speedFactor * deltaT > chunkManager.blockSize) {
			this.setY(this.getY() + 45);
		}else {
			this.setY(this.getY() + this.verticalVelocity * speedFactor * deltaT);
		}
		
		this.surface.setX((int)getX());
		this.surface.setY((int)getY());
		
		
	}

	public void moveLeft() {this.horizontalVelocity -= this.velocity;}

	public void moveRight() {this.horizontalVelocity += this.velocity;}
	
	public double getX() {return this.x;}
	
	public double getY() {return this.y;}

	public MySquare getSurface() {return surface;}

	public void setSurface(MySquare surface) {this.surface = surface;}

	public void setX(double x) {this.x = x;}

	public void setY(double y) {this.y = y;}

	public int getGameMode() {return gameMode;}

	public void setGameMode(int gameMode) {this.gameMode = gameMode;}
	
}
