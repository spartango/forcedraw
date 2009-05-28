package core;

import java.awt.Color;
import java.awt.Graphics;

public class FixedNode extends Node {
	public static final int SIZE = 15;
	public FixedNode(double x, double y, double mass, double charge) {
		super(x, y, mass, charge);
		// TODO Auto-generated constructor stub
	}
	
	public void setX(double newX){ }
	public void setY(double newY){ }
	public void setVelocityX(double newVx){	}
	public void setVelocityY(double newVy){	}
	public void draw(Graphics g, boolean showVectors){
		super.draw(g, false);
		g.setColor(Color.DARK_GRAY);
		g.fillOval((int) (getX()-SIZE/2), (int) (getY()-SIZE/2), SIZE, SIZE);
	}
	public void replant(double x, double y){
		this.x = x;
		this.y = y;
	}
}
