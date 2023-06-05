package tetris;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TetrisCanvas extends JPanel implements Runnable, KeyListener {
	protected Thread worker;
	protected Color colors[];
	protected int w = 25;
	protected static TetrisData data;
	protected int margin = 20;
	protected boolean stop, makeNew;
	protected Piece current;
	protected static int interval = 2000;
	protected static int level = 2;
	protected static int block_stack[] = {8,8,8};
	protected static int now_block = 8;
	public TetrisCanvas() {
		setSize(getPreferredSize());
		data = new TetrisData();
		addKeyListener(this);
		colors = new Color[8];
		colors[0] = new Color(80, 80, 80);
		colors[1] = new Color(255, 0, 0);
		colors[2] = new Color(0, 255, 0);
		colors[3] = new Color(0, 200, 255);
		colors[4] = new Color(255, 255, 0);
		colors[5] = new Color(255, 150, 0);
		colors[6] = new Color(210, 0, 240);
		colors[7] = new Color(40, 0, 240); 
		}
	public void start() { 
		data.clear();
		worker = new Thread(this);
		worker.start();
		makeNew = true;
		stop = false; 
		requestFocus(); 
		repaint(); 
		}
	public void stop() { 
		stop = true; current = null; 
	}
	public void paint(Graphics g) {
		super.paint(g);
		for(int i = 0; i < TetrisData.ROW; i++) {
			for(int k = 0; k < TetrisData.COL; k++) {
				if(data.getAt(i, k) == 0) { g.setColor(colors[0]); g.draw3DRect(margin/2 + w * k, margin/2 + w * i, w, w, true); } else {
					g.setColor(colors[0]); g.fill3DRect(margin/2 + w * k, margin/2 + w * i, w, w, true); 
					} 
				} 
			}
		if(current != null)
			for(int i = 0; i < 4; i++) {
				g.setColor(colors[current.getType()]);
				g.fill3DRect(margin/2 + w * (current.getX()+current.c[i]), margin/2 + w * (current.getY()+current.r[i]), w, w, true); 
				} 
		} 
	public Dimension getPreferredSize() {
		int tw = w * TetrisData.COL + margin;
		int th = w * TetrisData.ROW + margin;
		return new Dimension(tw, th); 
	}
	public void run() { 
		while(!stop) {
			int now_score = data.getLine() * 175 * level; TetrisView.refresh_now_score(now_score); TetrisView.refresh_delete_line(data.getLine());
			try { 
				if(makeNew) { 
					if(block_stack[0] == 8 && block_stack[1] == 8 && block_stack[2] == 8) {
						block_stack[0] = (int)(Math.random() * Integer.MAX_VALUE) % 7;
						block_stack[1] = (int)(Math.random() * Integer.MAX_VALUE) % 7;
						block_stack[2] = (int)(Math.random() * Integer.MAX_VALUE) % 7; 
					}
					TetrisPreview.input_next_blocks(block_stack[1], block_stack[2]);
					now_block = block_stack[0];
					switch(block_stack[0]){
					case 0: current = new Bar(data); break;
					case 1: current = new Tee(data); break;
					case 2: current = new El(data); break;
					case 3: current = new Z(data); break;
					case 4: current = new O(data); break;
					case 5: current = new S(data); break;
					case 6: current = new J(data); break;
					default: if(block_stack[1] % 2 == 0)
						current = new Tee(data);
					else current = new El(data); }
					int tmp[] = {block_stack[0], block_stack[1], block_stack[2]};
					for(int i = 0; i < 3; i++) {
						block_stack[0] = tmp[1]; block_stack[1] = tmp[2]; block_stack[2] = (int)(Math.random() * Integer.MAX_VALUE) % 7; 
					} 
					makeNew = false; } 
				else { 
					if(current.moveDown()){
						makeNew = true;
						if(current.copy()) { 
							stop();
							int score = data.getLine() * 175 * level; 
							JOptionPane.showMessageDialog(this, "게임 끝\n점수 : " + score); 
						}
						current = null; 
					}		
					data.removeLines();
				} 
				repaint(); 
				Thread.currentThread();
				Thread.sleep(interval/level); 
			} catch(Exception e){ } 
		} 	
	}

	public void keyPressed(KeyEvent e) {
		if(current == null) return;
		switch(e.getKeyCode()) {
		case 37:current.moveLeft(); repaint(); break;
		case 39:current.moveRight(); repaint();
		break;
		case 38:current.rotate(); repaint(); break;
		case 32:current.moveFullDown();
		boolean temp = current.moveDown();
		if(temp) { 
			makeNew = true;
			if(current.copy()) {
				stop();
				int score = data.getLine() * 175 * level;
				JOptionPane.showMessageDialog(this, "게임 끝\n점수 : " + score); 
			} 
		}
		data.removeLines(); repaint(); break;
		case 107: Level_UP(); repaint(); break;
		case 109:Level_DOWN(); repaint(); break;
		case 40:
		boolean temp2 = current.moveDown();
		if(temp2) { 
			makeNew = true;
			if(current.copy()) { 
				stop(); int score = data.getLine() * 175 * level;
				JOptionPane.showMessageDialog(this, "게임 끝\n점수 : " + score); 
			} 
		}
		data.removeLines(); repaint(); 
		} 
	}
	public static void Level_UP() { 
		if(level <= 50) { 
			level++; TetrisView.refresh_now_level(level); 
			} 
		}
	public static void Level_DOWN() {
		if(level > 1) { 
			level--;
			TetrisView.refresh_now_level(level); } }
	public void keyReleased(KeyEvent e) { }
	public void keyTyped(KeyEvent e) { } }
