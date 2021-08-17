package renderer.GUI.Chest;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.VoiceStatus;

import renderer.builder.Items;
import renderer.entity.DropManager;
import renderer.shapes.MySquare;

public class ChestManager {
	public Map<int[],Chest> chestMap = new HashMap<int[],Chest>();
	public Chest currentChest = new Chest(5, 3, 0, 0);
	public boolean showChest = false;
	//public MySquare surface;
	
	
	public ChestManager() {
		//surface = new MySquare((1200-5*50)/2-5, (800-3*50)/2-5-20, 5*50, 3*50+20);
	}
	
	public void add(Chest chest, int xRelative, int yRelative) {
		chestMap.put(new int[]{xRelative, yRelative}, chest);
	}
	
	public void delete(int[] position, DropManager dropManager, int blockSize) {
		for(int[] key : chestMap.keySet()) {
			if(key[0] == position[0] && key[1] == key[1]) {
				Chest temp = chestMap.get(key);
				for(int i = 0; i < temp.slotsList.length; i++) {
					if(temp.slotsList[i].getType() != Items.Unknown && temp.slotsList[i].getAmount() > 0) {
						dropManager.addProjectile(position[0]*blockSize, position[1]*blockSize, temp.slotsList[i].getType(), temp.slotsList[i].getAmount());
					}
				}
				chestMap.remove(key);
			}
		}
	}
	
	public boolean isOnMap(int[] position) {
		
		for(int[] key : chestMap.keySet()) {
			if(key[0] == position[0] && key[1] == key[1]) {
				return true;
			}
		}
		return false;
	}
	
	public Chest get(int[] position) {
		for(int[] key : chestMap.keySet()) {
			if(key[0] == position[0] && key[1] == key[1]) {
				return chestMap.get(key);
			}
		}
		
		return currentChest;
	}
	
	
	
}
