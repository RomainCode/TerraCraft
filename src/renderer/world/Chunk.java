package renderer.world;

import java.awt.Color;
import java.awt.Graphics;


import renderer.builder.Block;

import renderer.builder.BlocksBuilder;
import renderer.builder.Items;
import renderer.shapes.MySquare;

class MyColor{
	private short r, g, b, a;
	public MyColor(short r, short g, short b, short a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public void setA(short a) {
		this.a = a;
	}
}

public class Chunk{
	public static Camera camera;
	public static BlocksBuilder blockBuilder;
	public static Block blockClass;
	private Block[][] blocks;
	private Items[][] backgrounds;
	private int[][] shadingValues;
	private int x, y, xRelative, yRelative;
	private MySquare surface;
	public static ChunkManager chunkManager;
	public static int blockSize;
	public static float blockSizeFactor, chunkWidthFactor;
	
	public Chunk(int xRelative, int yRelative, int blockSize, Block[][] blocks, boolean generate){
		
		shadingValues = new int[blocks.length][blocks[0].length];
		
		if(generate) {
			int[][] mapIntCave = blockBuilder.generateCave(blockBuilder.getBlocksByX(), blockBuilder.getBlocksByY() - blockBuilder.caveOffsetBloccsY);
			blocks = blockBuilder.generateWater(blocks);
			blocks = blockBuilder.applayCaves(mapIntCave, blocks);
			blocks = blockBuilder.applayMinerals(blocks);
			blocks = blockBuilder.applayBedRock(blocks);
			blocks = blockBuilder.applayTrees(blocks);
			backgrounds = blockBuilder.generateBackground(blocks);
			blocks = blockBuilder.generateToGrassBlocks(blocks);
			
		}
		
		this.xRelative = xRelative;
		this.yRelative = yRelative;
		this.x = xRelative*blockSize;
		this.y = yRelative*blockSize;
		this.blockSize = blockSize;
		this.blockSizeFactor = (float)1/blockSize;
		this.chunkWidthFactor = (float)1/50;
		this.setBlocks(blocks);
		this.setSurface(new MySquare(xRelative*blockSize, yRelative*blockSize, blocks[0].length*blockSize, blocks.length*blockSize));
		
		// Initiate blocks
		for(int i = 0; i < this.getBlocks().length; i++) { // by Y
			for(int j = 0 ; j < this.getBlocks()[0].length; j++) { // by X
				this.getBlocks()[i][j].getSurface().setX(this.getBlocks()[i][j].getSurface().getX()+this.x);
				this.getBlocks()[i][j].getSurface().setY(this.getBlocks()[i][j].getSurface().getY()+this.y);
			}
		}
		
		
		// Initiate shading
		for(int y = 0; y < blocks.length; y++) {
			for(int x = 0; x < blocks[0].length; x++) {
				shadingValues[y][x] = 0;
			}
		}
		
		updateShadder();
		System.out.println("light succesfully difused");

	
	}
	
	private int getDiffusionAmount(int newX, int newY) {
		if(Block.isPartialyTransparent(blocks[newY][newX].getType())) {
			return 0;
		}
		if (Block.isBackground(blocks[newY][newX].getType())) {
			return -20;
		}
		
		return -25;
	}
	
	public void diffuseLight(int xV, int yV, int lightValue) {
			
			// diffuse light
		if(lightValue > shadingValues[yV][xV]) {
			shadingValues[yV][xV] = lightValue;
			

				if(xV > 0) {
					diffuseLight(xV-1, yV, lightValue + getDiffusionAmount(xV-1, yV));
				}else {
				}
				if(xV < shadingValues[0].length-1) {
					diffuseLight(xV+1, yV, lightValue + getDiffusionAmount(xV+1, yV));
				}else {			
					}
				if(yV > 0) {
					diffuseLight(xV, yV-1, lightValue + getDiffusionAmount(xV, yV-1));
				}
				if(yV < shadingValues.length-1) {
					diffuseLight(xV, yV+1, lightValue + getDiffusionAmount(xV, yV+1));
				}
		}
	}
	
	public void updateShadder() {
		// Initiate shading
		for(int y = 0; y < blocks.length; y++) {
			for(int x = 0; x < blocks[0].length; x++) {
				shadingValues[y][x] = 0;
			}
		}
				
		for(int y = 0; y < blocks.length; y++) {
			for(int x = 0; x < blocks[0].length; x++) {
				if(blockClass.isEmettingLisght(blocks[y][x].getType())) {
					if(blocks[y][x].getType() == Items.Torch) {
						diffuseLight(x, y, 255);
					}else {
						diffuseLight(x, y, 255);
					}
				}else {
					if(x == 0) {
						if(chunkManager.mapOfChunk.containsKey(xRelative-chunkManager.chunkWidth)){
							Chunk leftChunk = chunkManager.getChunkList().get(chunkManager.mapOfChunk.get(xRelative-chunkManager.chunkWidth));
							if(leftChunk.shadingValues[y][chunkManager.chunkWidth-1] > 20) {
								diffuseLight(x, y, leftChunk.shadingValues[y][chunkManager.chunkWidth-1]);
							}
						}
					}/*else if (x == chunkManager.chunkWidth-1) {
						if(chunkManager.mapOfChunk.containsKey(xRelative+chunkManager.chunkWidth)){
							Chunk right = chunkManager.getChunkList().get(chunkManager.mapOfChunk.get(xRelative+chunkManager.chunkWidth));
							if(right.shadingValues[y][0] > 20) {
								diffuseLight(x, y, right.shadingValues[y][0]);
							}
						}
					}*/
					
				}
			}
		}
	}
	
	
	public int getxRelative() {
		return xRelative;
	}

	public void setxRelative(int xRelative) {
		this.xRelative = xRelative;
	}

	public int getyRelative() {
		return yRelative;
	}

	public void setyRelative(int yRelative) {
		this.yRelative = yRelative;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
	// Frustum culling
	public void render(Graphics g, int camPosY, int camPosX) {
		
		if(camPosY < 0) {
			camPosY = 0;
		}
		
		if(camPosX <= 0) {
			camPosX = 0;
		}
		
		int maxView = (int)((camPosY)*blockSizeFactor) + (int)(800*blockSizeFactor);
		if(maxView+1 > this.getBlocks().length-1) {
			maxView = this.getBlocks().length-1;
		}
		
		
		
		for(int i = (int)((camPosY)*blockSizeFactor); i < maxView+1; i++) { // by Y
			
			for(int j = 0 ; j < this.getBlocks()[0].length; j++) { // by X
				
					
				//blocks[i][j].render(g);
				
					if(shadingValues[i][j] != 255) {
						if(shadingValues[i][j] > 20) {
							if(Block.isPartialyTransparent(blocks[i][j].getType())) {
								blocks[i][j].getSurface().render(g, backgrounds[i][j]);
							}
							blocks[i][j].render(g);
						}
						
						if (shadingValues[i][j] < 20) {
							g.setColor(Color.BLACK);}
						else {
							g.setColor(new Color(0,0,0, 255-shadingValues[i][j]));
							}
							
						g.fillRect(x+j*40-camera.getX(), y+i*40-camera.getY(), 40, 40);
					}else {
						if(Block.isPartialyTransparent(blocks[i][j].getType())) {
							blocks[i][j].getSurface().render(g, backgrounds[i][j]);
						}
						blocks[i][j].render(g);
					}
				
			}
		}
	}

	public MySquare getSurface() {
		return surface;
	}

	public void setSurface(MySquare surface) {
		this.surface = surface;
	}

	public Block[][] getBlocks() {
		return blocks;
	}

	public void setBlocks(Block[][] blocks) {
		this.blocks = blocks;
	}

	public Items[][] getBackgrounds() {
		return backgrounds;
	}

	public void setBackgrounds(Items[][] backgrounds) {
		this.backgrounds = backgrounds;
	}
	
	
}
