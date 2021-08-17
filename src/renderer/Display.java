package renderer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import mainGame.MainGame;
import renderer.builder.Block;
import renderer.builder.BlockType;
import renderer.builder.BlocksBuilder;
import renderer.entity.Player;
import renderer.input.Keyboard;
import renderer.input.Mouse;
import renderer.point.MyPoint;
import renderer.shapes.MyPolygon;
import renderer.shapes.MySquare;
import renderer.world.Camera;
import renderer.world.Chunk;
import renderer.world.ChunkManager;


public class Display extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	private Thread thread;

	private JFrame frame;
	private static String title = "GameEngine";
	public static final int WIDTH = 1200;
	public static final int HEIGHT = 800;
	private static boolean running = false;
	private int FPS = 80;
	private double ns = 100000000/60;
	
	
	private MainGame mainGame;
	
	
	
	// game
	
	
	public Display() {
		this.frame = new JFrame(title);
		Dimension size = new Dimension(WIDTH, HEIGHT);
		this.setPreferredSize(size);
	}
	
	public static void main(String[] argr) {
		Display display = new Display();
		display.frame.setTitle(title);
		display.frame.add(display);
		display.frame.pack();
		display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.frame.setLocationRelativeTo(null);
		display.frame.setResizable(false);
		display.frame.setVisible(true);
		
		display.start();
	}
	
	public synchronized void start() {
		running = true;
		this.thread = new Thread(this, "Display");
		this.thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			this.thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	@Override
	public void run() {	
		
		mainGame = new MainGame(this);
		
		mainGame.run();
		
		//mainGame.run();
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		this.setNs(1000000000.0/getFPS());
		double delta = 0;
		int frames = 0;
		
		long t1 = System.nanoTime();
		long t2 = System.nanoTime();
		
		while(running) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / getNs();
			lastTime = now;
			while(delta >= 1) {
				t1 = System.nanoTime();
				update((double)(t1-t2)/1000000000);
				delta--;
				
				t2 = System.nanoTime();
				
				render();
				frames++;
			}
			
			
			if(System.currentTimeMillis()-timer>1000) {
				timer += 1000;
				this.frame.setTitle(title + " | "+ frames + " fps");
				frames = 0;
			}
		}
		stop();
		
	}
		
	
	private void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs== null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D)g;
		
		this.mainGame.render(g, g2d);
		
		
		g.dispose();
		bs.show();
	}
	
	@SuppressWarnings("null")
	private void update(double deltaT) {
		
		this.mainGame.update(deltaT);	
	}

	public double getNs() {
		return ns;
	}

	public void setNs(double ns) {
		this.ns = ns;
	}

	public int getFPS() {
		return FPS;
	}

	public void setFPS(int fPS) {
		FPS = fPS;
	}
	
}
