package renderer.GUI.ItemsViewer;

import java.awt.Color;
import java.awt.Graphics2D;

import renderer.builder.Items;
import renderer.shapes.MySquare;
import renderer.texture.TextureManager;

public class Slot {
	private int amount;
	private Items type;
	public static TextureManager textureManager;
	public static Color textColor = new Color(250,250,250);
	public MySquare surface;
	public short  durability = -444;
	public short maxDurability = 0;
	
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Items getType() {
		return type;
	}

	public void setType(Items type) {
		this.type = type;
		
		switch (type) {
		case Wooden_Pickaxe:
			durability = 100;
			maxDurability = 100;
			break;

		default:
			durability = -444;
			break;
		}
	}

	public Slot(Items type, int amount) {
		this.amount = amount;
		this.type = type;
		this.surface = new MySquare(0,0,40,40);
		
		switch (type) {
		case Wooden_Pickaxe:
			durability = 100;
			maxDurability = 100;
			break;

		default:
			durability = -444;
			break;
		}
	}
	
	public void render(Graphics2D g2, int x, int y) {
		g2.drawImage(textureManager.getImage(type), x, y, null);
		if(durability >= 0) {
			g2.setColor(Color.GREEN);
			g2.fillRect(x, y+37, (int)((float)durability/(float)maxDurability*40), 3);
		}
		g2.setColor(textColor);
		g2.drawString(Integer.toString(amount), x - (String.valueOf(amount).length()+1)*(9) + textureManager.getImage(type).getWidth(null), y+textureManager.getImage(type).getHeight(null));
		
	}
}
