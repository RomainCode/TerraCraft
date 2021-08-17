package renderer.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener  {

	private boolean[] keys = new boolean[66568];
	
	
	public boolean getKey(int k) {
		/// input KeyEvent.VK_A
		return this.keys[k];
	}
	
	public void resetKey(int k) {
		this.keys[k] = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
	}
	
}
