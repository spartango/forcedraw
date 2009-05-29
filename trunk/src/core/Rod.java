package core;

import java.awt.*;

public class Rod{
	private int distance;
	private double dfromcenter1, dfromcenter2;
	private double DnodeX, DnodeY;
	private Point RestPosition;
	private double ForceToCenterL,ForceToCenterR;
	private Node left;
	private Node right;
	
	public Rod(Node left, Node right, int d) {
		this.left = left;
		this.right = right;
		d = distance;
		DnodeX = Math.abs(left.getX() - right.getX());
		DnodeY = Math.abs(left.getY() - right.getY());
		ForceToCenterL = Math.pow(left.getNetVelocity(), 2) * left.getMass() / getD1();
		ForceToCenterR = Math.pow(right.getNetVelocity(), 2) * right.getMass() / getD2();	
		// TODO Auto-generated constructor stub
	}
	public double getD2(){
		double force1 = (left.getForceMagnitude(left.getNetForceX(), left.getNetForceY()));
		double force2 = (right.getForceMagnitude(right.getNetForceX(), right.getNetForceY()));
		dfromcenter2 = distance * (left.getMass() / (left.getMass() + right.getMass()));
		return dfromcenter2;
	}
	public double getD1(){
		dfromcenter1 = (double) distance - this.getD2();
		return dfromcenter1;
	}
	public Point getRestPos(){
		double PointX, PointY;
		double ratio = getD2() / distance;
		PointX = ratio * DnodeX;
		PointY = ratio * DnodeY;
		RestPosition.setLocation(DnodeX - PointX, DnodeY - PointY);
		return RestPosition;
	}

	

}
