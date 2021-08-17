package renderer.world;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import renderer.builder.Block;
import renderer.builder.utils;
import renderer.entity.Drop;
import renderer.entity.DropManager;
import renderer.shapes.BlockSquare;
import renderer.shapes.MySquare;
import renderer.texture.TextureManager;
import renderer.builder.BlocksBuilder;
import renderer.builder.Items;

public class ChunkManager {

	public int blockSize;
	public int renderDistanceChunk = 2;
	public List<Integer> listOfChunkID = new ArrayList<Integer>();
	public Map<Integer, Integer> mapOfChunk = new HashMap<Integer, Integer>();
	public BlocksBuilder blocksBuilder;
	public static TextureManager textureManager;
	public int chunkWidth;
	public int chunkHeight;
	public int width = 1200;
	public int height = 800;
	public int mouseDetectionDistance;
	public Drop projectileClass;
	public DropManager dropManager;
	public final static String imagesSource = new File("").getAbsolutePath() + "\\assets\\";
	//public static final String imagesSource = "D:\\Programmation\\EclipseWorkspace\\Terra" + "\\src\\renderer\\texture\\images\\";
	public MySquare renderVision;
	public int maxLuminosity = 255;
	public static Camera camera;
	public BlockSquare target = new BlockSquare(0,0);
	public MySquare chunkLimitSurface = new MySquare(0, 0, 1, 0);
	

	public TextureManager getTextureManager() {
		return textureManager;
	}

	private List<Chunk> chunkList = new ArrayList<Chunk>();
	public ChunkManager(BlocksBuilder blocksBuilder, int blockSize, int chunkWidth, int chunkHeight, int mouseDetectionDistance) {
		this.blockSize = blockSize;
		this.chunkWidth = chunkWidth;
		this.chunkHeight = chunkHeight;
		ChunkManager.textureManager = new TextureManager(blockSize);
		this.blocksBuilder = blocksBuilder;
		this.mouseDetectionDistance = mouseDetectionDistance;
		this.renderVision = new MySquare(0, 0, renderDistanceChunk*blockSize*chunkWidth, chunkHeight);
		this.chunkLimitSurface.setH(chunkHeight*blockSize);
		TextureManager.error = TextureManager.getImage(imagesSource+"error.png");
		textureManager.add(TextureManager.getImage(imagesSource + "dirt.png"),Items.Dirt);
		textureManager.add(TextureManager.getImage(imagesSource + "sky.png"),Items.Sky);
		textureManager.add(TextureManager.getImage(imagesSource + "air_sprite.png"),Items.Air);
		textureManager.add(TextureManager.getImage(imagesSource + "stone.png"),Items.Rock);
		textureManager.add(TextureManager.getImage(imagesSource + "water.png"),Items.Water);
		textureManager.add(TextureManager.getImage(imagesSource + "iron_sprite.png"),Items.Iron);
		textureManager.add(TextureManager.getImage(imagesSource + "coal_sprite.png"),Items.Coal);
		textureManager.add(TextureManager.getImage(imagesSource + "diamond_sprite.png"),Items.Diamond);
		textureManager.add(TextureManager.getImage(imagesSource + "grass_sprite.png"),Items.Grass);
		textureManager.add(TextureManager.getImage(imagesSource + "ruby_sprite.png"),Items.Gold);
		textureManager.add(TextureManager.getImage(imagesSource + "stone_background.png"),Items.Rock_);
		textureManager.add(TextureManager.getImage(imagesSource + "bedrock_sprite.png"),Items.BedRock);
		textureManager.add(TextureManager.getImage(imagesSource + "ruby_sprite.png"),Items.Ruby);
		textureManager.add(TextureManager.getImage(imagesSource + "wood.png"),Items.Wood);
		textureManager.add(TextureManager.getImage(imagesSource + "leaf_sprite.png"),Items.Leaf);
		textureManager.add(TextureManager.getImage(imagesSource + "dirt_background.png"),Items.Dirt_);
		textureManager.add(TextureManager.getImage(imagesSource + "organic_drop.png"),Items.Dirt_drop);
		textureManager.add(TextureManager.getImage(imagesSource + "torch.png"),Items.Torch);
		textureManager.add(TextureManager.getImage(imagesSource + "wooden_pickaxe.png"),Items.Wooden_Pickaxe);
		textureManager.add(TextureManager.getImage(imagesSource + "chest.png"),Items.Chest);
		textureManager.add(TextureManager.getImage(imagesSource + "fire_flower.png"),Items.Fire_Flower);
		textureManager.add(TextureManager.getImage(imagesSource + "water.png"),Items.Water);
		textureManager.add(TextureManager.getImage(imagesSource + "grass_plant.png"),Items.Grass_Plant);
		textureManager.add(TextureManager.getImage(imagesSource + "poppy.png"),Items.Poppy);
		textureManager.add(TextureManager.getImage(imagesSource + "cornflower.png"),Items.Cornflower);
	}
	
	
	public int[] posToWorldPos(int camX, int camY, int mouseX, int mouseY) {
		return new int[] {camX+mouseX, camY+mouseY};
	}
	
	public int[] playerToWorldPos(int playerX, int playerY, int mouseX, int mouseY) {
		int newX = playerX+mouseX-width/2;
		if(newX < 0) {
			newX -= blockSize;
		}
		return new int[] {newX, playerY+mouseY-height/2};
	}
	
	public int[] worldPosToRelativePos(int[] worldPos) {
		return new int[] {worldPos[0]/blockSize, worldPos[1]/blockSize};
	}
	
	public int[] RelativePosToChunkPos(int[] relative) {
		
		//System.out.println("input : xRelative="+relative[0]+", yRelative="+relative[1]);
		
		int chunkX;
		if((float)relative[0]/chunkWidth < 0) {
			chunkX = relative[0]/chunkWidth -1;
		}else {
			chunkX =  relative[0]/chunkWidth;
		}

		int newX;
		if(relative[0] < 0) {
			if(relative[0]%chunkWidth == 0) {
				newX = 0;
				chunkX =  relative[0]/chunkWidth;
			}else {
				newX =  -(relative[0]/chunkWidth -1)*chunkWidth + relative[0];
			}
		}else {
			newX = relative[0]%chunkWidth;
		}
		
		//System.out.println("output : chunk="+chunkX*chunkWidth+", x="+newX+", y="+relative[1]);
		
		return new int[] {chunkX*chunkWidth, newX, relative[1]};
	}
	
	public Chunk ChunkPosToChunk(int[] chunkPos) {
		return chunkList.get(mapOfChunk.get(chunkPos[0]));
	}
	
	public Block ChunkPosToBlock(int[] chunkPos) {
		return chunkList.get(mapOfChunk.get(chunkPos[0])).getBlocks()[chunkPos[2]][chunkPos[1]];
	}
	
	public void addChunk(Chunk chunk) {

		getChunkList().add(chunk);
		listOfChunkID.add(chunk.getxRelative());
		mapOfChunk.put(chunk.getxRelative(), chunkList.size()-1);
		
	}

	public List<Chunk> getChunksList() {
		return this.getChunkList();
	}

	public int c;

	public void render(Graphics g, MySquare RectA, int mouseY, int mouseX) {
		for (int i = 0; i < this.getChunkList().size(); i++) {
			if (utils.isCossilison(RectA, this.getChunkList().get(i).getSurface())) {
				this.getChunkList().get(i).render(g, mouseY, mouseX);
				//g.setColor(Color.RED);
				//chunkLimitSurface.setX(this.getChunkList().get(i).getX());
				//chunkLimitSurface.setY(this.getChunkList().get(i).getY());
				//chunkLimitSurface.render(g);
				//g.drawString("ChunkRelativePos="+this.getChunkList().get(i).getxRelative(), this.getChunkList().get(i).getX()-camera.getX()+10, 10);
				
			}
		}
		
	}
	
	public int getChunkID(int playerX, int playerY, int mouseX, int mouseY) {
		return RelativePosToChunkPos(worldPosToRelativePos(playerToWorldPos(playerX, playerY, mouseX, mouseY)))[0];
	}
	
	public Block getBlock(int playerX, int playerY, int mouseX, int mouseY) {
		return ChunkPosToBlock(RelativePosToChunkPos(worldPosToRelativePos(playerToWorldPos(playerX, playerY, mouseX, mouseY))));
	}
	
	public Block getBlockByWorldPos(int x, int y) {
		return ChunkPosToBlock(RelativePosToChunkPos(worldPosToRelativePos(new int[] {x, y})));
		
		
	}
	
	public boolean setBlock(int playerX, int playerY, int mouseX, int mouseY, Items type, boolean byBackGround, boolean antiFeetBuild, boolean updateChunkShadder, MySquare surface) {
		int[] chunkPos = RelativePosToChunkPos(worldPosToRelativePos(playerToWorldPos(playerX, playerY, mouseX, mouseY)));
		
		if(antiFeetBuild) {
			if(utils.isCossilison(surface, chunkList.get(mapOfChunk.get(chunkPos[0])).getBlocks()[chunkPos[2]][chunkPos[1]].getSurface())) {
				return false;
			}
		}
		
		if(byBackGround) {
			chunkList.get(mapOfChunk.get(chunkPos[0])).getBlocks()[chunkPos[2]][chunkPos[1]].setType(chunkList.get(mapOfChunk.get(chunkPos[0])).getBackgrounds()[chunkPos[2]][chunkPos[1]]);
		}
		else {
			chunkList.get(mapOfChunk.get(chunkPos[0])).getBlocks()[chunkPos[2]][chunkPos[1]].setType(type);
		}
		
		if(updateChunkShadder) {
			chunkList.get(mapOfChunk.get(chunkPos[0])).updateShadder();
				}
		return true;
	}
	
	public Items getBackground(int playerX, int playerY, int mouseX, int mouseY) {
		int[] chunkPos = RelativePosToChunkPos(worldPosToRelativePos(playerToWorldPos(playerX, playerY, mouseX, mouseY)));
		
		return chunkList.get(mapOfChunk.get(chunkPos[0])).getBackgrounds()[chunkPos[2]][chunkPos[1]];
	}

	
	public void updateChunk(int xPlayer, int yPlayer) {
		renderVision.setX(xPlayer-renderDistanceChunk*blockSize*chunkWidth);
		
		// get the current chunk
		int currentChunkPos = (int)((int)xPlayer / (blockSize*chunkWidth));
		
		for(int i = currentChunkPos-renderDistanceChunk; i < currentChunkPos + renderDistanceChunk; i++) {
			
			// check if the chunkManager contains the chunk
			if(listOfChunkID.contains(i*chunkWidth)) {
				// it's already here
			}else {
				// need to create it !
				this.addChunk(new Chunk(i*chunkWidth, 0, blockSize, blocksBuilder.generateBlocks(i*chunkWidth), true));
				System.out.println("New chunk ID="+i*chunkWidth+" width seed="+i*chunkWidth);
			}
		}
	}
	
	public void renderBlockHover(Graphics2D g2) {
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.WHITE);
		g2.drawRect(target.getX()-camera.getX(), target.getY()-camera.getY(), target.getW(), target.getH());
	}
	
	public void updateBlockHover(int mouseX, int mouseY, int playerX, int playerY) {
		
		if(checkMouseDistance(mouseX, mouseY, mouseDetectionDistance)) {
			try {
				target = this.getBlock(playerX, playerY, mouseX, mouseY).getSurface();
			}catch(Exception e) {
				
			}
		}
	}
	
	public boolean checkMouseDistance(int mouseX, int mouseY, int distance) {
		if(Math.sqrt(Math.pow((double)mouseX-(double)width/2, 2) + Math.pow((double)mouseY-(double)height/2, 2)) < distance) {
			return true;
		}
		else {
			return false;
		}
	}
	
	

	public Items selectItemByPos(int mouseX, int mouseY, int cameraX, int cameraY, boolean distanceLimitation) {
		if(checkMouseDistance(mouseX, mouseY, mouseDetectionDistance) || !distanceLimitation) {
			
			Block tempBlock = getBlock(cameraX, cameraY, mouseX, mouseY);
			if(tempBlock.getType() != Items.Unknown) {
				return tempBlock.getType();
				//this.selectedItem = tempBlock.getType();
			}
		}
		
		return Items.Unknown;
	}

	public void removeChunks() {
		chunkList.clear();
		listOfChunkID.clear();
	}

	public List<Chunk> getChunkList() {
		return chunkList;
	}

	public void setChunkList(List<Chunk> chunkList) {
		this.chunkList = chunkList;
	}

}
