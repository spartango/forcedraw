package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class TreeComponent extends JComponent implements MouseListener, 
											MouseMotionListener, KeyListener{

	private Node focusNode;
	private FixedNode dragPoint;
	private Link dragLine;

	private int lastX;
	private int lastY;
	public void paintComponent(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Main.X_BOUND+50, Main.Y_BOUND+50);
		g.setColor(Color.RED);
		for(Node n : Main.nodes){
			//focusNode != null && n.equals(focusNode)
			boolean showVectors = 
				(Math.abs(n.getX()-lastX)) < Node.SIZE*2 
						&& Math.abs(n.getY()-lastY) < Node.SIZE*2;
			n.draw(g, showVectors);
		}
		g.setColor(Color.BLACK);
		for(Link l : Main.links){
			l.draw(g);
		}
		if(focusNode!=null){
			g.setColor(Color.YELLOW);
			g.drawLine((int)focusNode.getX(), (int)focusNode.getY(), lastX, lastY);
		}
	}

	public void mouseClicked(MouseEvent arg0) {
		/*if(arg0.getButton() == MouseEvent.BUTTON3){
			Main.nodes.add(new Node(arg0.getX(), arg0.getY(), 250, -10));
		}*/
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		for(Node n : Main.nodes){
			if(Math.abs(n.getX()-arg0.getX()) < Node.SIZE/2 
					&& Math.abs(n.getY()-arg0.getY()) < Node.SIZE/2){
				focusNode = n;
				break;
			}
		}
		if(arg0.getButton() == MouseEvent.BUTTON1){
			Main.nodes.add(dragPoint = new FixedNode(arg0.getX(), arg0.getY(), 1, 0));
			Main.links.add(dragLine = new Link(Main.nodes.lastElement(), focusNode, 170, 1));
		}
	}

	public void mouseReleased(MouseEvent arg0) {	
		Main.links.remove(dragLine);
		Main.nodes.remove(dragPoint);
		focusNode = null;
		dragLine = null;
		dragPoint = null;
	}

	public void mouseDragged(MouseEvent e) {
		lastX = e.getX();
		lastY = e.getY();
		if(e.getButton() == MouseEvent.BUTTON3){
			for(Node n : Main.nodes){
				if(Math.abs(n.getX()-e.getX()) < 
						Node.SIZE/2 && Math.abs(n.getY()-e.getY()) < Node.SIZE/2 
						&& !n.equals(focusNode)){
					Main.links.add(new Link(focusNode, n, 10, 
							Node.distanceBetween(n.getX(), n.getY(), 
									focusNode.getX(), focusNode.getY())/2));
					focusNode = null;
					break;
				}
			}
		}else if(dragPoint != null){
			dragPoint.replant(lastX, lastY);
		}

	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		lastX = e.getX();
		lastY = e.getY();
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			Main.running = !Main.running;
			System.out.println("--- Simulation ---" +(Main.running? " Running": " Suspended"));
		}
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}


}
