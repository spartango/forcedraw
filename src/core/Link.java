package core;

import java.awt.Color;
import java.awt.Graphics;

public class Link {
	private Node left;
	private Node right;
	private double k;
	private double restPosition;
	private double position;
	
	public Link(Node left, Node right, double k, double restPosition) {
		this.left = left;
		this.right = right;
		this.k = k;
		this.restPosition = restPosition;
		this.position = Node.distanceBetween(left.getX(), left.getY(), 
												right.getX(), right.getY());
	}
	
	public void draw(Graphics g){
		updatePosition();
		if(position < restPosition)
			g.setColor(Color.ORANGE);
		else if(position > restPosition){
			g.setColor(Color.MAGENTA);
		}else
			g.setColor(Color.BLACK);
		g.drawLine((int) left.getX(),(int) left.getY(),(int) right.getX(),(int) right.getY());
	}
	
	public void applyForces(){
		updatePosition();
		//System.out.println("P " +position);
		double magnitude = -k * (position - restPosition);
		double y = right.getY() - left.getY();
		double x = right.getX() - left.getX();
		double angle = Node.getForceDirection(x, y);
		left.applyForce(-magnitude * Math.cos(angle), -magnitude * Math.sin(angle));
		right.applyForce(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
		//System.out.println("F " +this +" = " +magnitude +" " +angle);
	}
	
	public void updatePosition(){
		this.position = Node.distanceBetween(left.getX(), left.getY(), 
				right.getX(), right.getY());
	}
	
	public Node getLeft() {
		return left;
	}
	public void setLeft(Node left) {
		this.left = left;
	}
	public Node getRight() {
		return right;
	}
	public void setRight(Node right) {
		this.right = right;
	}
	public double getK() {
		return k;
	}
	public void setK(double k) {
		this.k = k;
	}
	public double getRestPosition() {
		return restPosition;
	}
	public void setRestPosition(double restPosition) {
		this.restPosition = restPosition;
	}
	public double getPosition() {
		return position;
	}
	
	
}
