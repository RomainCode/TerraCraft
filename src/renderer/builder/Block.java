package renderer.builder;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import renderer.shapes.BlockSquare;
import renderer.world.ChunkManager;

public class Block {

	private BlockSquare surface;
	private Items type = Items.Unknown;
	public Items getType() {
		return this.type;
	}

	public void setType(Items type) {
		this.type = type;
		
	}

	public BlockSquare getSurface() {
		return this.surface;
	}

	public void setSurface(BlockSquare surface) {
		this.surface = surface;
	}
	
	
	public static Items[] minerals = {Items.Rock, Items.BedRock, Items.Coal, Items.Gold, Items.Diamond, Items.Ruby, Items.Iron};
	public static Items[] tools = {Items.Pickaxe, Items.Axe, Items.Sword};
	public static Items[] backgrounds = {Items.Air, Items.Unknown, Items.Sky, Items.Cloud, Items.Rock_, Items.Dirt_, Items.Dirt_drop};
	public static Items[] emmetingLight = {Items.Air, Items.Torch};
	public static Items[] isNotPlaceable = {Items.Wooden_Pickaxe, Items.Air, Items.Unknown, Items.Sky, Items.Cloud, Items.Rock_, Items.Dirt_, Items.Dirt_drop};
	public static Items[] partialyTransparent = {Items.Torch, Items.Fire_Flower, Items.Water, Items.Grass_Plant, Items.Poppy, Items.Cornflower};
	public static Items[] breakablWithHand = {Items.Chest, Items.Wood, Items.Grass, Items.Dirt, Items.Torch, Items.Leaf, Items.Fire_Flower, Items.Grass_Plant, Items.Poppy, Items.Cornflower};
	public static Items[] breakablWithWoodenPickaxe = {Items.Rock, Items.Coal};
	public static Items[] soilVegetables = {Items.Fire_Flower, Items.Poppy, Items.Grass_Plant};
	public static Items[] destroyInstantanitlyWithHand = {Items.Fire_Flower, Items.Poppy, Items.Grass_Plant, Items.Cornflower};
	public static List<Items> mineralsItems = new ArrayList<Items>();
	public static List<Items> blocksItems = new ArrayList<Items>();
	public static List<Items> toolsItems = new ArrayList<Items>();
	public static List<Items> backgroundsItems = new ArrayList<Items>();
	public static List<Items> emmetingLightItems = new ArrayList<Items>();
	public static List<Items> partialyTransparentItems = new ArrayList<Items>();
	public static List<Items> isNotPlaceableItems = new ArrayList<Items>();
	public static List<Items> breakablWithHandItems = new ArrayList<Items>();
	public static List<Items> breakablWithWoodenPickaxeItems = new ArrayList<Items>();
	public static List<Items> destroyInstantanitlyWithHandItems = new ArrayList<Items>();
	public static ChunkManager chunkManager;
	
	public static void loadGroups() {
		mineralsItems.clear();
		for(Items type : minerals) {
			mineralsItems.add(type);
		}
		
		toolsItems.clear();
		for(Items type : tools) {
			toolsItems.add(type);
		}
		
		emmetingLightItems.clear();
		for(Items type : emmetingLight) {
			emmetingLightItems.add(type);
		}
		
		backgroundsItems.clear();
		for(Items type : backgrounds) {
			backgroundsItems.add(type);
		}
		
		partialyTransparentItems.clear();
		for(Items type : partialyTransparent) {
			partialyTransparentItems.add(type);
		}
		
		isNotPlaceableItems.clear();
		for(Items type : isNotPlaceable) {
			isNotPlaceableItems.add(type);
		}
		
		breakablWithHandItems.clear();
		for(Items type : breakablWithHand) {
			breakablWithHandItems.add(type);
		}
		
		breakablWithWoodenPickaxeItems.clear();
		for(Items type : breakablWithWoodenPickaxe) {
			breakablWithWoodenPickaxeItems.add(type);
		}
		
		destroyInstantanitlyWithHandItems.clear();
		for(Items type : destroyInstantanitlyWithHand) {
			destroyInstantanitlyWithHandItems.add(type);
		}
		
		System.out.println("Block properties loaded");
		
	}
	
	
	public int getxRelative() {
		return (int)(surface.getX()/chunkManager.blockSize * chunkManager.blockSize);
	}
	
	public int getyRelative() {
		return (int)(surface.getY()/chunkManager.blockSize * chunkManager.blockSize);
	}
	
	public static boolean isPlaceable(Items type) {
		return !isNotPlaceableItems.contains(type);
	}
	
	public static boolean isPartialyTransparent(Items type) {
		return partialyTransparentItems.contains(type);
	}
	
	public boolean isEmettingLisght(Items type) {
		return emmetingLightItems.contains(type);
	}
	
	public boolean isDropAble(Items type) {
		return !backgroundsItems.contains(type);
	}
	
	public static boolean isMineral(Items type) {
		return mineralsItems.contains(type);
	}
	
	public static boolean isBackground(Items type) {
		return backgroundsItems.contains(type);
	}

	public boolean isSolid() {
		switch (this.type) {
		case Rock:
			return true;
		case Dirt:
			return true;
		case Wood:
			return false;
		case BedRock:
			return true;
		case Grass:
			return true;
		case Coal:
			return true;
		case Iron:
			return true;
		case Diamond:
			return true;
		case Gold:
			return true;

		default:
			return false;
		}
	}
	

	public String toString() {
		return "" + type.ordinal();
	}
	

	public void render(Graphics g) {

		/**Color color = null;

		switch (this.type) {
		case Air:
			color = new Color(250, 250, 250);
			break;
		case Dirt:
			color = new Color(120, 80, 10);
			break;
		case Wood:
			color = new Color(120, 80, 10);
			break;
		case Water:
			color = new Color(50, 50, 250);
		case Sky:
			color = new Color(170, 210, 240);
			break;
		case Rock:
			color = new Color(100, 100, 100);
			break;

		}

		if (color == null) {
			color = Color.PINK;
		}*/
		this.surface.render(g, type);
	}

}
