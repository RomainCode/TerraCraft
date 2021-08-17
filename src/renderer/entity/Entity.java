package renderer.entity;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import renderer.builder.Items;
import renderer.builder.utils;
import renderer.shapes.MySquare;
import renderer.texture.TextureManager;
import renderer.world.Camera;
import renderer.world.Chunk;
import renderer.world.ChunkManager;

public class Entity {

	public static Camera camera;
	public static Player player;
	public float x, y;
	public MySquare surface;
	public Image image;
	public Items type;
	public EntityType entityType;
	public int xVec = 30;
	public int yVec = 0;
	public float lastAction = 0;
	public List<Entity> entityGroup = new ArrayList<Entity>();
	public List<Entity> deleteList = new ArrayList<Entity>();
	
	public Entity(int x, int y, int w, int h, String image_name, EntityType entityType, Items type) {
		this.x = x;
		this.y = y;
		this.entityType = entityType;
		this.type = type;
		this.surface = new MySquare(x, y, w, h);
		this.image = TextureManager.getImage(ChunkManager.imagesSource+image_name, false, 0);
	}
	
	public void render(Graphics2D g2) {
		g2.drawImage(image, (int)x-camera.getX(), (int)y-camera.getY(), null);
		surface.lineRender(g2);
		
		if(entityType == EntityType.Static_Projectil_Launcher) {
			for(int i = 0; i < entityGroup.size(); i++) {
				entityGroup.get(i).render(g2);
			}
		}
	}
	
	public void update(double deltaT) {
		
		lastAction += deltaT;
		
		if(entityType == EntityType.Following_Vector) {
			this.x += xVec * deltaT;
			this.y += yVec * deltaT;
			this.surface.setX((int)x);
			this.surface.setY((int)y);
		}
		
		if(entityType == EntityType.Static_Projectil_Launcher) {
			if(getDistance((int)player.getX(), (int)player.getY(), (int)x, (int)y) < 10*player.chunkManager.blockSize) {
				for(int i = 0; i < deleteList.size(); i++) {
					entityGroup.remove(deleteList.get(i));
				}
				deleteList.clear();
				
				for(int i = 0; i < entityGroup.size(); i++) {
					entityGroup.get(i).update(deltaT);
					if(utils.isCossilison(player.getSurface(), entityGroup.get(i).surface)) {
						// the entity touched the player
						player.health -= 5;
						deleteList.add(entityGroup.get(i));
					}
					
					// check if the moving entity is collided in a solid block
					if(isCollided(entityGroup.get(i).surface, (int)entityGroup.get(i).x, (int)entityGroup.get(i).y)) {
						deleteList.add(entityGroup.get(i));
					}
					
				}
				
				if(lastAction > 5) {
					if(entityGroup.size() < 100) {
						entityGroup.add(new Entity((int)x, (int)y, 10, 10, "fire_projectile.png", EntityType.Following_Vector, Items.Air));
					}
					lastAction = 0;
				}
			}
		}
	}
	
	
	
	
	private int getDistance(int xA, int yA, int xB, int yB) {
		return (int) Math.sqrt(Math.pow(xA-xB, 2) + Math.pow(yA-yB, 2));
	}
	
	
	private boolean isCollided(MySquare surface, int xP, int yP) {
		int[] chunkPos = player.chunkManager.RelativePosToChunkPos(player.chunkManager.worldPosToRelativePos(new int[] {xP, yP}));
		int startX = 0;
		int endX = player.chunkManager.chunkWidth;
		if(chunkPos[1] - 3 >= 0) {startX = chunkPos[1] - 3;}
		if(chunkPos[1] + 3 < endX) {endX = chunkPos[1] + 3;}
		
		int yStart = 0;
		int yEnd = player.chunkManager.chunkHeight;
		if(chunkPos[2] - 3 >= 0) {yStart = chunkPos[2] - 3;}
		if(chunkPos[2] + 3 < yEnd) {yEnd = chunkPos[2] +3;}

		
		int c = 0;
		for(Chunk chunk : player.chunkManager.getChunksList()) {
			if(utils.isCossilison(player.getSurface(), chunk.getSurface(), 0, 0)) {
				
				if(chunk.getxRelative() != chunkPos[0]) {
					if(xP > chunk.getX()) {
						startX = 45;
						endX = 50;
					}
					if(xP < chunk.getX()) {
						startX = 0;
						endX = 5;
					}
				}
				
				c++;
				for(int y = 0; y < player.chunkManager.chunkHeight; y++) {
					for(int x = startX; x < endX; x++) {
						if(utils.isCossilisonSquareBlock(surface, chunk.getBlocks()[y][x].getSurface(), 0, 0)) {
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
	
	
	
}
