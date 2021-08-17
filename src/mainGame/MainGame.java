package mainGame;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import renderer.Display;
import renderer.GUI.Chest.Chest;
import renderer.GUI.Crafter.PortableCrafter;
import renderer.GUI.GlobalSlotManager.SlotManager;

import renderer.GUI.ItemsViewer.Slot;
import renderer.builder.Block;
import renderer.builder.BlocksBuilder;
import renderer.entity.DropManager;
import renderer.entity.Player;
import renderer.input.Keyboard;
import renderer.input.Mouse;
import renderer.shapes.BlockSquare;
import renderer.shapes.MyPolygon;
import renderer.shapes.MySquare;
import renderer.world.Camera;
import renderer.world.Chunk;
import renderer.world.ChunkManager;
import renderer.builder.Items;

public class MainGame {

	private static Camera camera;
	private static Keyboard keyboard;
	private static Mouse mouse;
	private static Player player;
	private static Display display;

	private int FPS = 60;
	private double ns = 100000000 / 60;

	private static BlocksBuilder blocksBuilder;
	private ChunkManager chunkManager;
	private DropManager dropManager;

	private boolean cooldown = false;
	private double cooldownTime = 0;

	public final int blockSize = 40;
	public final int chunkWidth = 50;
	public final int chunkHeight = 170;
	
	public int backgroundIndice = 100;

	private PortableCrafter portableCrafter;
	private SlotManager slotManager;
	
	

	public MainGame(Display display) {
		this.display = display;
	}

	public void run() {

		init();
	}

	public void init() {
		
		this.camera = new Camera(0, 100, this.display.WIDTH, this.display.HEIGHT);
		MyPolygon.camera = this.camera;
		MySquare.camera = this.camera;
		BlockSquare.camera = this.camera;
		Chunk.camera = camera;
		
		this.keyboard = new Keyboard();
		this.mouse = new Mouse();

		this.display.addKeyListener(keyboard);
		this.display.addMouseListener(mouse);
		this.display.addMouseMotionListener(mouse);
		this.display.addMouseWheelListener(mouse);
		
		this.blocksBuilder = new BlocksBuilder(blockSize, chunkWidth, chunkHeight, this.display.WIDTH, this.display.HEIGHT);
		Chunk.blockBuilder = this.blocksBuilder;
		Chunk.blockClass = blocksBuilder.blockClass;
		this.chunkManager = new ChunkManager(blocksBuilder, blockSize, chunkWidth, chunkHeight, (int)(8*blockSize));
		this.dropManager = new DropManager(chunkManager);
		this.chunkManager.dropManager = dropManager;
		Chunk.chunkManager = chunkManager;
		Slot.textureManager = chunkManager.getTextureManager();
		chunkManager.addChunk(new Chunk(0, 0, blockSize, blocksBuilder.generateBlocks(0), true));

		this.portableCrafter = new PortableCrafter(display.WIDTH, display.HEIGHT);
		Block.chunkManager = chunkManager;
		ChunkManager.camera = camera;
		this.slotManager = new SlotManager(display.WIDTH, display.HEIGHT, 8, 3, blockSize);
		this.slotManager.textureManager = chunkManager.getTextureManager();;
		this.player = new Player(0, 2000, camera, keyboard, mouse, chunkManager, slotManager);
		player.dropManager = dropManager;
		Chest.width = display.WIDTH;
		Chest.height = display.HEIGHT;
		
		this.display.requestFocusInWindow();

	}

	public void render(Graphics g, Graphics2D g2d) {
		g.setColor(new Color(backgroundIndice, backgroundIndice, backgroundIndice));
		g.fillRect(0, 0, this.display.WIDTH, this.display.HEIGHT);

		chunkManager.render(g, camera.getViewSurface(), camera.getY(), camera.getX());
		
		chunkManager.renderBlockHover(g2d);
		dropManager.renderProjectiles(g, camera.getX(), camera.getY());
		player.render(g2d);
		
		g2d.setColor(new Color(20,20,20,10));
		g.fillRect(0, 0, this.display.WIDTH, this.display.HEIGHT);
	
		//g.setFont(new Font("Arial", Font.PLAIN, 20));
		slotManager.render(g2d, mouse.getX(), mouse.getY());
		//portableCrafter.render(g2d);
		//inventory.render(g2d);
		

		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.WHITE);
		g2d.drawString("Relative pos : " + (int) (player.getX() / blockSize) + ", " + (int) (player.getY() / blockSize),20, 20);
		
	}

	public void update(double deltaT) {

		if (cooldown) {
			cooldownTime -= deltaT;

			if (cooldownTime < 0) {
				cooldown = false;
			}
		}

		if (keyboard.getKey(KeyEvent.VK_UP)) {
			this.chunkManager.maxLuminosity += 1;
			System.out.println("this.chunkManager.maxLuminosity : " + this.chunkManager.maxLuminosity);
		}
		if (keyboard.getKey(KeyEvent.VK_DOWN)) {
			this.chunkManager.maxLuminosity -= 1;
			System.out.println("this.chunkManager.maxLuminosity : " + this.chunkManager.maxLuminosity);
		}
		if (keyboard.getKey(KeyEvent.VK_Q) || keyboard.getKey(KeyEvent.VK_LEFT)) {
			player.moveLeft();
		}

		if (keyboard.getKey(KeyEvent.VK_D) || keyboard.getKey(KeyEvent.VK_RIGHT)) {
			player.moveRight();
		}

		if (keyboard.getKey(KeyEvent.VK_S) && keyboard.getKey(KeyEvent.VK_CONTROL)) {
			// save chunkManager
			if (!cooldown) {
				saveGame();
				cooldown = true;
				cooldownTime = 1;
			}
		}
		

		if (keyboard.getKey(KeyEvent.VK_L) && keyboard.getKey(KeyEvent.VK_CONTROL)) {
			// load game
			if (!cooldown) {
				loadGame();
				cooldown = true;
				cooldownTime = 1;
			}
		}
		
		if(keyboard.getKey(KeyEvent.VK_ESCAPE)) {
			slotManager.inventory.showInventory = false;
			slotManager.portableCrafter.showPortableCrafter = false;
			slotManager.chestManager.showChest = false;
		}
		
		
		if(keyboard.getKey(KeyEvent.VK_E)) {
			//slotManager.inventory.toogleInventoryVisibility();
			slotManager.portableCrafter.toogleInventoryVisibility();
			if(slotManager.portableCrafter.showPortableCrafter) {
				slotManager.chestManager.showChest = false;
			}
			keyboard.resetKey(KeyEvent.VK_E);
		}
		
		
		if(keyboard.getKey(KeyEvent.VK_L)) {
			if(backgroundIndice < 255) {
				System.out.println(backgroundIndice);
				backgroundIndice++;
			}
		}
		
		if(keyboard.getKey(KeyEvent.VK_K)) {
			if(backgroundIndice > 0) {
				System.out.println(backgroundIndice);
				backgroundIndice--;
			}
		}
		
		if(keyboard.getKey(KeyEvent.VK_Y)) {
			chunkManager.getChunkList().get(0).updateShadder();
			System.out.println("shadder updated");
		}

		if (mouse.isSrcollingDown()) {
			slotManager.inventory.selectedID += 1;
			if(slotManager.inventory.selectedID > slotManager.inventory.xSlots-1) {
				slotManager.inventory.selectedID = slotManager.inventory.xSlots-1;
			}
			mouse.resetScroll();
		}

		if (mouse.isScrollingUp()) {
			slotManager.inventory.selectedID -= 1;
			if(slotManager.inventory.selectedID < 0) {
				slotManager.inventory.selectedID = 0;
				}
			mouse.resetScroll();
		}
		
		
		chunkManager.updateBlockHover(mouse.getX(), mouse.getY(), (int) player.getX(), (int) player.getY());
		player.update(deltaT);
		camera.setX((int) player.getX() - this.display.WIDTH / 2);
		camera.setY((int) player.getY() - this.display.HEIGHT / 2);
		chunkManager.updateChunk((int) player.getX(), (int) player.getY());
		dropManager.updateProjectiles(deltaT);

	}

	public void saveGame() {
		String data = "";
		data += "0\n"; // seed
		data += "" + chunkManager.getChunksList().size() + "\n"; // Chunk Number

		List<Chunk> chunkList = chunkManager.getChunksList();

		for (int i = 0; i < chunkList.size(); i++) {
			data += "" + chunkList.get(i).getxRelative() + ", " + chunkList.get(i).getyRelative() + ", "
					+ chunkList.get(i).getBlocks()[0].length + ", " + chunkList.get(i).getBlocks().length + "\n";
			for (int j = 0; j < chunkList.get(i).getBlocks().length; j++) {
				for (int k = 0; k < chunkList.get(i).getBlocks()[0].length; k++) {
					data += "" + chunkList.get(i).getBlocks()[j][k].toString() + "," + chunkList.get(i).getBackgrounds()[j][k].ordinal() + "\n";
				}
			}
		}

		// add the extra-data
		data += (int) player.getX() + "," + (int) player.getY();

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(new File("").getAbsolutePath() + "\\save.sav"));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Game saved !");
	}

	public void loadGame() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(new File("").getAbsolutePath() + "\\save.sav"));
			String line = reader.readLine();
			chunkManager.removeChunks();

			String[] infoOnChunk;
			// read next line
			int seed = Integer.parseInt(line);
			line = reader.readLine();
			int numberOfChunks = Integer.parseInt(line);
			for (int i = 0; i < numberOfChunks; i++) {
				line = reader.readLine();
				System.out.println(line);
				infoOnChunk = line.split(", ");
				Block[][] blocks = new Block[Integer.parseInt(infoOnChunk[3])][Integer.parseInt(infoOnChunk[2])];
				Items[][] backgrounds = new Items[Integer.parseInt(infoOnChunk[3])][Integer.parseInt(infoOnChunk[2])];

				for (int k = 0; k < Integer.parseInt(infoOnChunk[3]); k++) {
					for (int j = 0; j < Integer.parseInt(infoOnChunk[2]); j++) {
						blocks[k][j] = new Block();
						blocks[k][j].setSurface(new BlockSquare(j * blockSize, k * blockSize));
						line = reader.readLine();
						int[] blockData = new int[2];
						blockData[0] = Integer.parseInt(line.split(",")[0]);
						blockData[1] = Integer.parseInt(line.split(",")[1]);
						blocks[k][j].setType(Items.values()[blockData[0]]);
						backgrounds[k][j] = Items.values()[blockData[1]];
						
					}
				}
				System.out.println(Integer.parseInt(infoOnChunk[0]) + ", " + Integer.parseInt(infoOnChunk[1]));
				System.out.println(infoOnChunk);
				Chunk tempChunk = new Chunk(Integer.parseInt(infoOnChunk[0]), Integer.parseInt(infoOnChunk[1]),blockSize, blocks, false);
				tempChunk.setBackgrounds(backgrounds);
				chunkManager.addChunk(tempChunk);
			}
			line = reader.readLine();
			player.setX(Integer.parseInt(line.split(",")[0]));
			player.setY(Integer.parseInt(line.split(",")[1]));

			reader.close();
			System.out.println("Game loaded !");

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}
}
