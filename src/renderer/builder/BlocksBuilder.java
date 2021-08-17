package renderer.builder;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import renderer.shapes.BlockSquare;


public class BlocksBuilder {

	public float noiseFactor = 10f;
	public int groundOffsetBlocs = 55;
	public int caveOffsetBloccsY = 53;
	public final float minaralGeneratiobLuckFactor = 4;
	public final int cavesSmoothling = 10;
	public float heightFactor = 9;
	private int blockSize;
	private int WIDTH;
	private int HEIGHT;
	private int blocksByX;
	private int blocksByY;
	public Block blockClass = new Block();
	public PerlinNoise perlinGenerator;

	public int getBlocksByX() {
		return blocksByX;
	}

	public void setBlocksByX(int blocksByX) {
		this.blocksByX = blocksByX;
	}

	public int getBlocksByY() {
		return blocksByY;
	}

	public void setBlocksByY(int blocksByY) {
		this.blocksByY = blocksByY;
	}

	private Block[][] blocks;

	public Block[][] getBlocks() {
		return blocks;
	}

	public void setBlocks(Block[][] blocks) {
		this.blocks = blocks;
	}

	public BlocksBuilder(int blockSize, int blocksByX, int blocksByY, int WIDTH, int HEIGHT) {
		this.blockClass.loadGroups();
		this.blockSize = blockSize;
		this.blocksByX = blocksByX;
		this.blocksByY = blocksByY;
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		this.blocks = new Block[this.blocksByY][this.blocksByX];
		perlinGenerator = new PerlinNoise(1200, 2);
	}
	
	public Items[][] generateBackground(Block[][] blocks) {
		Items[][] items = new Items[blocksByY][blocksByX];
		
		for(int y = 0; y < blocksByY; y++) {
			for(int x = 0; x < blocksByX; x++) {
				if(blockClass.mineralsItems.contains(blocks[y][x].getType()) || blocks[y][x].getType() == Items.Rock_) {
					items[y][x] = Items.Rock_;
				}else if(blocks[y][x].getType() == Items.Dirt || blocks[y][x].getType() == Items.Grass) {
					items[y][x] = Items.Dirt_;
				}else {
					items[y][x] = Items.Air;
				}
			}
		}
		
		return items;
	}

	public Block[][] generateBlocks(int xOrigin) {

		Block[][] blocks;
		blocks = new Block[this.blocksByY][this.blocksByX];

		

		int groundHeight = 0;


		for (int i = 0; i < this.blocksByY; i++) { // by Y
			for (int j = 0; j < this.blocksByX; j++) { // by X
				if (blocks[i][j] != null) {
					continue;
				}
				blocks[i][j] = new Block();
				groundHeight = (int) (perlinGenerator.noise((float) (j + xOrigin * noiseFactor) * 0.1f, 1f)
						* heightFactor) + groundOffsetBlocs;
				if (i < groundHeight) {
					blocks[i][j].setType(Items.Air);
				} else if (i == groundHeight || i > groundHeight && i < groundHeight + 3) {
					if (i == groundHeight) {
						blocks[i][j].setType(Items.Grass);
					} else {
						blocks[i][j].setType(Items.Dirt);
					}

				} else if (i > groundHeight) {

					blocks[i][j].setType(Items.Rock);

					if (i == this.blocksByY - 1) {
						blocks[i][j].setType(Items.BedRock);
					}
				} else {
					blocks[i][j].setType(Items.Air);
				}

				blocks[i][j].setSurface(
						new BlockSquare(j * this.blockSize, i * this.blockSize));
			}
		}
		
		

		return blocks;
	}
	
	public Block[][] generateToGrassBlocks(Block[][] blocks){
		for(int y = 0; y < blocksByY; y++) {
			for(int x = 0; x < blocksByX; x++) {
				if(blocks[y][x].getType() == Items.Grass) {
					if(utils.booleanByProba(0.21)) {
						if(Block.isBackground(blocks[y-1][x].getType())) {
							if(utils.booleanByProba(0.2)){
								blocks[y-1][x].setType(Items.Poppy);
								continue;
							}
							if(utils.booleanByProba(0.2)){
								blocks[y-1][x].setType(Items.Cornflower);
								continue;
							}
							if(utils.booleanByProba(0.1)){
								blocks[y-1][x].setType(Items.Fire_Flower);
								continue;
							}
							blocks[y-1][x].setType(Items.Grass_Plant);
						}
					}
				}
			}
		}
		
		
		return blocks;
	}
	
	public Block[][] generateWater(Block[][] blocks){
		for(int y = groundOffsetBlocs+4; y < blocksByY; y++) {
			for(int x = 0; x < blocksByX; x++) {
				if(blocks[y][x].getType() != Items.Rock && blocks[y][x].getType() != Items.Dirt) {
					blocks[y][x].setType(Items.Water);
				}
			}
		}
		return blocks;
	}
	
	public Block[][] applayTrees(Block[][] blocks) {
		
		
		List<int[]> grassPos = getSurfacePosBlocks(blocks);
		for(int i = 0; i < grassPos.size(); i++) {
			if(utils.booleanByProba(0.05)) {
					blocks = generateTree(blocks, grassPos.get(i)[1], grassPos.get(i)[0]-1);
			}
		}
		
		
		return blocks;
	}
	
	private List<int[]> getSurfacePosBlocks(Block[][] blocks){
		List<int[]> grassPos = new ArrayList<int[]>();
		
		for(int x = 0; x < blocks[0].length; x++) {
			for(int y = groundOffsetBlocs - 10; y < groundOffsetBlocs + 10; y++) {
				if(blocks[y][x].getType() == Items.Grass) {
					grassPos.add(new int[]{y, x});
				}
			}
		}
		
		return grassPos;
	}
	
	private Block[][] generateTree(Block[][] blocks, int xLeft, int yBottom) {
		// generate log
		int treeHeight = utils.randomRange(2, 5);
		for(int i = 0; i < treeHeight; i++) {
			blocks[yBottom-i][xLeft].setType(Items.Wood);
		}
		
		//generate leaves
		blocks[yBottom-treeHeight][xLeft].setType(Items.Leaf);
		blocks[yBottom-treeHeight-1][xLeft].setType(Items.Leaf);
		
		if(xLeft-1 >= 0) {
			blocks[yBottom-treeHeight][xLeft-1].setType(Items.Leaf);
			blocks[yBottom-treeHeight-1][xLeft-1].setType(Items.Leaf);
		}
		
		if(xLeft+1 < blocks[0].length) {
			blocks[yBottom-treeHeight][xLeft+1].setType(Items.Leaf);
			blocks[yBottom-treeHeight-1][xLeft+1].setType(Items.Leaf);
		}
		
		blocks[yBottom-treeHeight-2][xLeft].setType(Items.Leaf);
		
		
		return blocks;
	}

	public Block[][] applayMinerals(Block[][] blocks) {

		List<Integer>[] spotList = generateMineralsSpots(blocks, 0, blocksByX, caveOffsetBloccsY, blocksByY);

		for (int i = 0; i < spotList[0].size(); i++) {
			int y = spotList[1].get(i);
			Items type = stoneChanger(minaralGeneratiobLuckFactor, y, blocksByY);
			if (type != Items.Rock) {
				int x = spotList[0].get(i);
				blocks[y][x].setType(type);
				if (y > 0 && utils.booleanByProba(0.4)) {
					blocks[y - 1][x].setType(type);
				}
				if (y < blocksByY - 1 && utils.booleanByProba(0.4)) {
					blocks[y - 1][x].setType(type);
				}
				if (x > 0 && utils.booleanByProba(0.4)) {
					blocks[y][x - 1].setType(type);
				}
				if (x < blocksByX - 1 && utils.booleanByProba(0.4)) {
					blocks[y][x + 1].setType(type);
				}
				if (x > 0 && y > 0 && utils.booleanByProba(0.4)) {
					blocks[y - 1][x - 1].setType(type);
				}
				if (x < blocksByX - 1 && y < blocksByY - 1 && utils.booleanByProba(0.4)) {
					blocks[y + 1][x + 1].setType(type);
				}
				if (x > 0 && y < blocksByY - 1 && utils.booleanByProba(0.4)) {
					blocks[y + 1][x - 1].setType(type);
				}
				if (x < blocksByX - 1 && y > 0 && utils.booleanByProba(0.4)) {
					blocks[y - 1][x + 1].setType(type);
				}
			}
		}

		return blocks;
	}

	private List<Integer>[] generateMineralsSpots(Block[][] blocks, int xStart, int xStop, int yStart, int yStop) {
		List<Integer> spotListX = new ArrayList<Integer>();
		List<Integer> spotListY = new ArrayList<Integer>();

		for (int x = xStart; x < xStop; x++) {
			for (int y = yStart; y < yStop; y++) {
				if (utils.booleanByProba(0.01)) {
					spotListX.add(x);
					spotListY.add(y);
				}
			}
		}
		List<Integer>[] output = new List[2];
		output[0] = spotListX;
		output[1] = spotListY;
		return output;
	}

	public Block[][] applayBedRock(Block[][] blocks) {

		for (int i = 0; i < blocks[0].length; i++) {
			blocks[blocks.length - 1][i].setType(Items.BedRock);
		}

		return blocks;
	}

	public Block[][] applayCaves(int[][] mapIntCave, Block[][] blocks) {
		for (int x = 0; x < mapIntCave.length; x++) {
			for (int y = 0; y < mapIntCave[0].length; y++) {
				if (mapIntCave[x][y] == 1) {
					if(blocks[y][x].getType() == Items.Rock) {
						blocks[y + caveOffsetBloccsY][x].setType(Items.Rock);
					}
				} else {
					if(blocks[y + caveOffsetBloccsY][x].getType() != Items.Air) {
						blocks[y + caveOffsetBloccsY][x].setType(Items.Rock_);
					}
					
				}
			}
		}
		return blocks;
	}

	private Items stoneChanger(float luckMult, int y, int blocksByY) {

		Items t = Items.Rock;

		if (utils.booleanByProba(0.1 * luckMult) && (float) y / blocksByY > 0.4) {
			t = Items.Coal;
		}
		if (utils.booleanByProba(0.02 * luckMult) && (float) y / blocksByY > 0.5) {
			t = Items.Iron;
		}
		if (utils.booleanByProba(0.01 * luckMult) && (float) y / blocksByY > 0.6) {
			t = Items.Gold;
		}
		if (utils.booleanByProba(0.01 * luckMult) && (float) y / blocksByY > 0.75) {
			t = Items.Ruby;
		}
		if (utils.booleanByProba(0.008 * luckMult) && (float) y / blocksByY > 0.85) {
			t = Items.Diamond;
		}

		return t;
	}

	public int[][] generateCave(int width, int height) {
		int[][] map = new int[width][height];
		map = randomFillMap(map);

		for (int i = 0; i < cavesSmoothling; i++) {
			map = smoothCave(map);
		}
		return map;
	}

	private int[][] smoothCave(int[][] map) {
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				int neighbourWallTiles = getSurroundingWallCount(map, x, y);

				if (neighbourWallTiles > 4) {
					map[x][y] = 1;
				} else if (neighbourWallTiles < 4) {
					map[x][y] = 0;
				}
			}
		}

		return map;
	}

	private int getSurroundingWallCount(int[][] map, int gridX, int gridY) {
		int wallCount = 0;
		for (int neighbourX = gridX - 1; neighbourX <= gridX + 1; neighbourX++) {
			for (int neighbourY = gridY - 1; neighbourY <= gridY + 1; neighbourY++) {
				if (neighbourX >= 0 && neighbourX < map.length && neighbourY >= 0 && neighbourY < map[0].length) {
					if (neighbourX != gridX || neighbourY != gridY) {
						wallCount += map[neighbourX][neighbourY];
					}
				} else {
					wallCount++;
				}
			}
		}
		return wallCount;
	}

	private int[][] randomFillMap(int[][] map) {
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (x == 0 || x == map.length - 1 || y == 0 || y == map[0].length - 1) {
					map[x][y] = 1;
				} else {
					map[x][y] = (utils.booleanByProba(0.53)) ? 1 : 0;
				}
			}
		}
		return map;
	}

	public void render(Graphics g) {
		for (int i = 0; i < this.blocksByY; i++) { // by Y
			for (int j = 0; j < this.blocksByX; j++) { // by X

				this.blocks[i][j].render(g);

			}
		}
	}
}
