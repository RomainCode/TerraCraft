package renderer.texture;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import renderer.shapes.MySquare;
import renderer.builder.BlockType;
import renderer.builder.Items;

public class TextureManager{

	public Image defaultImage;
	
	public Map<Items, Image> textureMap = new HashMap<>();
	
	public static int blockSize = 60;
	public static Image error;
	
	public TextureManager(int blockSize) {
		TextureManager.blockSize = blockSize;
	}
	
	public static Image getImage(String filePath) {
		//BufferedImage image = ImageIO.read(new File(path, "image.png"));
		Image image;
		try {
			image = ImageIO.read(new File(filePath));
			image = image.getScaledInstance(blockSize, blockSize, Image.SCALE_DEFAULT);
			if(image != null) {
				System.out.println(Paths.get(filePath).getFileName().toString() + " loaded !");
			}else {
				System.err.println("can't load : " + filePath);
			}
		} catch (IOException e) {
			System.err.println("can't load : " + filePath + "          " + error);
			return error;
		}
		return image;
	}
	
	public static Image getImage(String filePath, boolean resize, float scaleFactor) {
		//BufferedImage image = ImageIO.read(new File(path, "image.png"));
		Image image;
		try {
			image = ImageIO.read(new File(filePath));
			if(resize) {
				image = image.getScaledInstance((int)(image.getWidth(null)*scaleFactor), (int)(image.getHeight(null)*scaleFactor), Image.SCALE_DEFAULT);
			}
			if(image != null) {
				System.out.println(Paths.get(filePath).getFileName().toString() + " loaded !");
			}else {
				System.err.println("can't load : " + filePath);
			}
		} catch (IOException e) {
			System.err.println("can't load : " + filePath + "          " + error);
			return error;
		}
		return image;
	}
	
	public void add(Image image, Items type) {
		this.textureMap.put(type, image);
		System.out.println(type + " linked !");
	}
	
	public Image getImage(Items type) {
		if(!textureMap.containsKey(type)) {
			return this.error;
		}
		return this.textureMap.get(type);
	}
}
