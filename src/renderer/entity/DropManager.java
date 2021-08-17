package renderer.entity;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import renderer.builder.Items;
import renderer.texture.TextureManager;
import renderer.world.ChunkManager;

public class DropManager {

	public List<Drop> projectileList = new ArrayList<Drop>();
	public List<Drop> deleteList = new ArrayList<Drop>();
	public ChunkManager chunkManager;
	public double mergeTime = 0;
	public TextureManager textureManager;
	public Map<DropType, Image> dropMapTextures = new HashMap<>();
	public float scaleFactor = 0.7f;
	public int xOffset;
	public int yOffset;

	
	public DropManager(ChunkManager chunkManager) {
		this.chunkManager = chunkManager;
		this.textureManager = chunkManager.getTextureManager();
		//xOffset = (int)(textureManager.getImage(chunkManager.imagesSource + "mineral_drop.png", false, 0f).getWidth(null)*(1-scaleFactor));
		//yOffset = (int)(textureManager.getImage(chunkManager.imagesSource + "mineral_drop.png", false, 0f).getHeight(null)*(scaleFactor+(1-scaleFactor)));
		xOffset = 0;
		yOffset = 25;
		
		dropMapTextures.put(DropType.Mineral_Drop, textureManager.getImage(chunkManager.imagesSource + "mineral_drop.png", true, 0.7f));
		dropMapTextures.put(DropType.Ore_Drop, textureManager.getImage(chunkManager.imagesSource + "ores_mineral_drop.png", true, 0.7f));
		dropMapTextures.put(DropType.Organic_Drop, textureManager.getImage(chunkManager.imagesSource + "organic_drop.png", true, 0.7f));
		dropMapTextures.put(DropType.Living_Organic_Drop, textureManager.getImage(chunkManager.imagesSource + "living_organic_drop.png", true, 0.7f));
		System.out.println(dropMapTextures.get(DropType.Mineral_Drop).getWidth(null));
		

	}
	
	
	public void updateProjectiles(double deltaT) {
		
		for(int i = 0; i < deleteList.size(); i++) {
			projectileList.remove(deleteList.get(i));
		}
		deleteList.clear();
		
		if(mergeTime > 5) {
			mergeProjectiles(90);
			mergeTime = 0;
		}else {
			mergeTime += deltaT;
		}
		
		
		for(int i = 0; i<projectileList.size(); i++) {
			int x = (int)projectileList.get(i).getX();
			int y = (int)projectileList.get(i).getY();
			projectileList.get(i).update(deltaT, chunkManager.getBlockByWorldPos(x, y+40));
		}
	}
	
	public void mergeProjectiles(int distance) {
		for(int i = 0; i<projectileList.size(); i++) {
			for(int j = 0; j<projectileList.size(); j++) {
				if(projectileList.get(i).getAmount() != 0) {
					if(i != j && projectileList.get(i).getType() == projectileList.get(j).getType()) {
						if(distance(projectileList.get(i), projectileList.get(j)) < distance && i != j) {
							projectileList.get(i).setAmount(projectileList.get(i).getAmount()+projectileList.get(j).getAmount());
							projectileList.get(j).setAmount(0);
							deleteList.add(projectileList.get(j));
						}
					}
				}
			}
		}
	}
	
	private int distance(Drop A, Drop B) {
		return (int)Math.sqrt(Math.pow(A.getX()-B.getX(), 2)+Math.pow(A.getY()-B.getY(), 2));
	}
	
	public void renderProjectiles(Graphics g, int cameraX, int cameraY) {
		for(int i = 0; i<projectileList.size(); i++) {
				projectileList.get(i).render(g, cameraX, cameraY, xOffset, yOffset);
		}
	}
	
	public void addProjectile(int x, int y, Items type, int amount) {
		if(chunkManager.blocksBuilder.blockClass.isMineral(type)){
			if(type == Items.Rock) {
				projectileList.add(new Drop(x, y, dropMapTextures.get(DropType.Mineral_Drop), 0f, 3f, amount, type));
			}else {
				projectileList.add(new Drop(x, y, dropMapTextures.get(DropType.Ore_Drop), 0f, 3f, amount, type));
			}	
		}
		else {
			if(type == Items.Grass || type == Items.Dirt) {
				projectileList.add(new Drop(x, y, dropMapTextures.get(DropType.Organic_Drop), 0f, 3f, amount, type));
			}else {
				projectileList.add(new Drop(x, y, dropMapTextures.get(DropType.Living_Organic_Drop), 0f, 3f, amount, type));
			}
			
		}
	}
	
		
}
