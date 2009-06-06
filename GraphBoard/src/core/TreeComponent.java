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

    public static final int DRAG = 0;
    public static final int CREATEPOS = 1;
    public static final int LINK = 2;
    public static final int CREATENEG = 3;
    public static final int CREATENEU = 4;
    public int state = DRAG;
	private Node focusNode;
	private FixedNode dragPoint;
	private Link dragLine;
	private int lastX;
	private int lastY;
	public void paintComponent(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Main.X_BOUND+50, Main.Y_BOUND+50);
        try{
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
        }catch(NullPointerException e){
            
        }
	}

	public void mouseClicked(MouseEvent arg0) {
		if(state == CREATEPOS || state == CREATENEG || state == CREATENEU){
			Main.nodes.add(new Node(arg0.getX(), arg0.getY(), 100, (state == CREATEPOS? 5 :
                                                                    (state == CREATENEG? -5 : 0))));
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		if(state == DRAG || state == LINK){
            for(Node n : Main.nodes){
                try{
                    if(Math.abs(n.getX()-arg0.getX()) < Node.SIZE/2
                        	&& Math.abs(n.getY()-arg0.getY()) < Node.SIZE/2){
                        focusNode = n;
                    	break;
                    }
                }catch(NullPointerException e){}
            }
        }
        if(state == DRAG){
            try{
                Main.nodes.add(dragPoint = new FixedNode(arg0.getX(), arg0.getY(), 1, 0));
                Main.links.add(dragLine = new Link(Main.nodes.lastElement(), focusNode, 220, 1));
		
            }catch(NullPointerException e){}
        }
	}

	public void mouseReleased(MouseEvent arg0) {	
		if(state == DRAG || state == LINK){
            if(dragLine!=null){
                Main.links.remove(dragLine);
                dragLine = null;
            }
            if(dragPoint != null){
                Main.nodes.remove(dragPoint);
                dragPoint = null;
            }
            focusNode = null;
            
            
        }
	}

	public void mouseDragged(MouseEvent e) {
		lastX = e.getX();
		lastY = e.getY();
		if(state == LINK){
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
		}else if(dragPoint != null && state == DRAG){
			dragPoint.replant(lastX, lastY);
		}

	}

	public void mouseMoved(MouseEvent e) {
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
