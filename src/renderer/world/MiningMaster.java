package renderer.world;

import renderer.GUI.ItemsViewer.Slot;
import renderer.builder.Block;
import renderer.builder.Items;

public class MiningMaster {
	
	public int[] lastChunkPos = {0, 0, 0};
	public Block lastBlock = new Block();
	public double blockDurability = 10;
	public double blockMaxDurability = 10;
	public Items lastSlotType;
	public int[] worldPos = {0,0};
	
	
	private double getBlockDurability(Items type) {
		if(Block.isMineral(type)) {return 10;}
		if(Block.destroyInstantanitlyWithHandItems.contains(type)) {
			return 0.1;
		}
		
		return 3;
	}
	
	private double getDurabilityDammage(Slot handSlot, double deltaT, Items type) {
		double dammage = 0;
		
		if(handSlot.getType() == Items.Wooden_Pickaxe && Block.breakablWithWoodenPickaxeItems.contains(type)) {
			dammage = 20*deltaT;
		}else {
			if(Block.breakablWithHandItems.contains(type)) {
				dammage = 5*deltaT;
			}
		}
		
		
		return dammage;
	}
	
	public int getBreakingIndex() {
		int index = 3;
		float durability = (float)(blockDurability/blockMaxDurability);
		//System.out.println(durability);
		
		if(durability <= 1) {
			index = 3;
			if(durability < 0.7) {
				index = 0;
				if(durability < 0.5) {
					index = 1;
					if(durability < 0.25) {
						index = 2;
						if(durability <= 0) {
							index = 3;
						}
					}
				}
			}
		}
		
		//System.out.println("index="+index);
		return index;
	}
	
	public boolean handleClickBlock(Block block, Slot handSlot, int[] chunkPos, double deltaT) {
		
		//check if the block id minable
		if(!Block.isBackground(block.getType())) {
			
			// check if the targeted block is the same than the last
			if(lastChunkPos[0] == chunkPos[0] && lastChunkPos[1] == chunkPos[1] && lastChunkPos[2] == chunkPos[2]) {
				// the block is the same than the last

				// check if the last tool is the same the actual
				if(lastSlotType == handSlot.getType()) {
					blockDurability -= getDurabilityDammage(handSlot, deltaT, block.getType());
					
					// check if the block need to be destroyed
					if(blockDurability <= 0) {
						blockDurability = getBlockDurability(block.getType());
						return true;
					}
					
				}else {
					// the user change the tool, we reset the durability
					blockDurability = getBlockDurability(block.getType());
					blockMaxDurability = getBlockDurability(block.getType());
					lastSlotType = handSlot.getType();
				}
				
				
				
			}else {
				// the block isn't the last block
				lastChunkPos = chunkPos;
				lastBlock = block;
				blockDurability = getBlockDurability(block.getType());
				blockMaxDurability = getBlockDurability(block.getType());
			}
		}
		
		return false;
	}

}
