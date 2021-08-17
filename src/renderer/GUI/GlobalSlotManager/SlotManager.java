package renderer.GUI.GlobalSlotManager;

import java.awt.Color;
import java.awt.Graphics2D;

import renderer.GUI.Chest.Chest;
import renderer.GUI.Chest.ChestManager;
import renderer.GUI.Crafter.PortableCrafter;
import renderer.GUI.ItemsViewer.Inventory;
import renderer.GUI.ItemsViewer.Slot;
import renderer.builder.Items;
import renderer.builder.utils;
import renderer.texture.TextureManager;

public class SlotManager {
	// coordonne les sytèmes utilisant des slots :
	// - inventaire
	// - crafters
	// - coffres
	
	public Inventory inventory;
	public PortableCrafter portableCrafter;
	public ChestManager chestManager;
	public Chest chest;
	public int width, height, xSlotsInventory, ySlotsInventory, blockSize;
	public int stackSize = 5;
	public Slot fromTransfert, toTransfert;
	public boolean processingTransfer = false;
	public static TextureManager textureManager;
	
	
	public SlotManager(int width, int height, int xSlotsInventory, int ySlotsInventory, int blockSize){
		this.width = width; 
		this.height = height;
		this.xSlotsInventory = xSlotsInventory;
		this.ySlotsInventory = ySlotsInventory;
		this.blockSize = blockSize;
		this.inventory = new Inventory(xSlotsInventory, ySlotsInventory, width, height, blockSize, 5);
		this.portableCrafter = new PortableCrafter(width, height);
		this.chestManager = new ChestManager();
		
	}
	
	public void render(Graphics2D g2, int mouseX, int mouseY) {
		inventory.render(g2);
		portableCrafter.render(g2);
		
		if(chestManager.showChest) {
			chestManager.currentChest.render(g2);
		}
		
		if(processingTransfer) {
			if(fromTransfert != null) {
				if(fromTransfert.getType() != Items.Unknown) {
					g2.setColor(new Color(100,100,100,180));
					g2.fillRect(fromTransfert.surface.getX(), fromTransfert.surface.getY(), blockSize, blockSize);
					fromTransfert.render(g2, mouseX, mouseY);
					//g2.drawImage(textureManager.getImage(fromTransfert.getType()), mouseX, mouseY, null);
				}
				
			}
		}
		
	}
	
	public boolean checkCollision(int mouseX, int mouseY) {
		if(utils.isPointOnRectCollision(mouseX, mouseY, inventory.surface) && inventory.showInventory) {
			return true;
		}
		if(utils.isPointOnRectCollision(mouseX, mouseY, portableCrafter.surface) && portableCrafter.showPortableCrafter) {
			return true;
		}
		
		if(utils.isPointOnRectCollision(mouseX, mouseY, chestManager.currentChest.surface) && chestManager.showChest) {
			return true;
		}
		
		return false;
		
	}
	

	
	public void middleClick(Items temp) {
		
		for(int x=0; x<inventory.xSlots; x++) {
			if(inventory.slots[inventory.ySlots-1][x].getType() == temp) {
				inventory.selectedID = x;
				return;
			}
		}
		for(int y = 0; y < inventory.ySlots; y++) {
			for(int x=0; x<inventory.xSlots; x++) {
				if(inventory.slots[y][x].getType() == temp) {
					flipSlots(inventory.slots[y][x], inventory.slots[inventory.ySlots-1][inventory.selectedID]);
					return;
				}
			}
		}
	}
	
	public boolean setFrom(int mouseX, int mouseY) {
		Slot temp = inventory.getSlotByPos(mouseX, mouseY);
		if(temp.getAmount() > 0 && temp.getType() != Items.Unknown && inventory.showInventory) {
			fromTransfert = temp;
			return true;
		}
		
		temp = portableCrafter.getSlotByPos(mouseX, mouseY);
		if(temp.getAmount() > 0 && temp.getType() != Items.Unknown && portableCrafter.showPortableCrafter) {
			fromTransfert = temp;
			return true;
		}
		
		if(chestManager.currentChest != null) {
			temp = chestManager.currentChest.getSlotByPos(mouseX, mouseY);
			if(temp.getAmount() > 0 && temp.getType() != Items.Unknown && chestManager.showChest) {
				fromTransfert = temp;
				return true;
			}
		}
		
		return false;
	}
	
	public boolean setTo(int mouseX, int mouseY) {
		Slot temp = inventory.getSlotByPos(mouseX, mouseY);
		if(temp.getAmount() != -444 && inventory.showInventory) {
			toTransfert = temp;
			return true;
		}
		
		temp = portableCrafter.getSlotByPos(mouseX, mouseY);
		if(temp.getAmount() != -444 && portableCrafter.showPortableCrafter) {
			toTransfert = temp;
			return true;
		}
		
		if(chestManager.currentChest != null) {
			temp = chestManager.currentChest.getSlotByPos(mouseX, mouseY);
			if(temp.getAmount() != -444 && chestManager.showChest) {
				toTransfert = temp;
				return true;
			}
		}
		
		toTransfert = new Slot(Items.Unknown, -444);
		return false;
	}
	
	public Slot getActualSlot() {
		return inventory.slots[ySlotsInventory-1][inventory.selectedID];
	}
	
public void flipSlots(Slot fromSlot, Slot toSlot) {
	
	if(fromSlot == null || toSlot == null) {
		System.out.println("can't transfer nul slot");
		return;
	}
	
	System.out.println("Trying to transfert "+fromSlot.getType()+", to "+toSlot.getType());
	
	if(fromSlot == toSlot) {
		return;
	}
	
	if(toSlot.getAmount() == -444) {
		System.err.println("error with the amount toSlot");
		return;
	}
	
	
		int aAmount = fromSlot.getAmount();
		Items aType = fromSlot.getType();
		
		int bAmount = toSlot.getAmount();
		Items bType = toSlot.getType();
		
		if(fromSlot == portableCrafter.output) {
			
		}
		
		if(aType == bType && fromSlot.durability == -444 && toSlot.durability == -444) {
			if(aAmount + bAmount <= stackSize) {
				fromSlot.setAmount(0);
				fromSlot.setType(Items.Unknown);
				toSlot.setAmount(aAmount+bAmount);
				toSlot.setType(bType);
			}
			else {
				fromSlot.setAmount(aAmount - (stackSize-bAmount));
				fromSlot.setType(aType);
				toSlot.setAmount(stackSize);
				toSlot.setType(bType);
			}
		}
		else {
			fromSlot.setAmount(bAmount);
			fromSlot.setType(bType);
			toSlot.setAmount(aAmount);
			toSlot.setType(aType);
		}
		
		portableCrafter.update();
	}

public boolean tryFoCraft(int x, int y) {
	if(utils.isPointOnRectCollision(x, y, portableCrafter.surface) && portableCrafter.showPortableCrafter) {
		if(portableCrafter.craft()) {
			return true;
		}
	}
	
	
	return false;
}
}
