package renderer.GUI.Crafter;

import renderer.GUI.ItemsViewer.Slot;
import renderer.builder.Items;


public class Recipe{
	public int craftUnit;
	public int[] amounts;
	public Items[] items;
	public Items outputItem;
	public int outputAmount;
	public boolean isOrder;
	
	public Recipe(int craftUnit, int[] amounts, Items[] items, Items outputItem, int outputAmount, boolean isOrder) {
		this.craftUnit = craftUnit;
		this.amounts = amounts;
		this.items = items;
		this.outputItem = outputItem;
		this.outputAmount = outputAmount;
		this.isOrder = isOrder;
		
	}
	
	public boolean isCorrect(Slot[] slots) {
		
		if(this.isOrder) {
			for(int i = 0; i < craftUnit; i++) {
				if(items[i] != slots[i].getType() || slots[i].getAmount() < amounts[i]) {
					return false;
				}
			}
		}
		return true;
	}
		
	public boolean applay(Slot[] slots, Slot output) {
		if(isCorrect(slots)) {
			for(int i = 0; i < craftUnit; i++) {
				slots[i].setAmount(slots[i].getAmount()-amounts[i]);
			}
			output.setAmount(0);
			output.setType(Items.Unknown);
			return true;
		}else {
			return false;
		}
	}
		
	
}