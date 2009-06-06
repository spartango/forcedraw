package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

public class Node {

	public static final int SIZE = 25;

	protected double x;
	protected double y;

	//Vector
	private double velocityX;
	private double velocityY;

	private double mass;
	private double charge;

	//Vector
	private double netForceX;
	private double netForceY;

	public Node(double x, double y, double mass, double charge) {
		this.x = x;
		this.y = y;
		this.mass = mass;
		this.charge = charge;
		this.netForceX = this.netForceY = 0;
		this.velocityX = this.velocityY = 0;
	}

	public double getKE(){
		return .5 * mass * (velocityX*velocityX + velocityY*velocityY);
	}

	public static double getNetVelocity(Node left) {
		return Math.sqrt(Math.pow(left.getVelocityX(), 2)
				+ Math.pow(left.getVelocityY(), 2));
	}

	public static double getForceMagnitude(double Fx, double Fy){
		return Math.sqrt((Fx*Fx) + (Fy*Fy));
	}

	public static double getForceDirection(double Fx, double Fy){
		if(Fx == 0 && Fy == 0)
			return -255;
		if(Fx == 0)
			if(Fy > 0)
				return Math.PI/2;
			else return 3*Math.PI/2;
		else if(Fy == 0)
			if(Fx > 0)
				return 0;
			else return Math.PI;

		double a = Math.atan(Fy/Fx);

		if(Fx < 0)
			a += Math.PI;
		else if(Fy < 0){
			a += Math.PI*2;
		}
		return a;
	}

	public void applyForce(double Fx, double Fy){
		netForceX += Fx;
		netForceY += Fy;
	}

	public void draw(Graphics g, boolean showVectors){
		g.setColor(charge>0 ? new Color((int) (20*Math.abs(charge)) +50, 0, 0) : new Color(0,0, (int) (20*Math.abs(charge)) +50));
		if(charge == 0){
			g.setColor(Color.WHITE);
		}
		g.fillOval((int) (x-SIZE/2), (int) (y-SIZE/2), SIZE, SIZE);
		if(showVectors){
			g.setColor(Color.GREEN);
			g.drawLine((int)x, (int)y, (int)(x+.025*netForceX),(int) (y+.025*netForceY));
			g.setColor(Color.WHITE);
			//g.drawString(arg0, arg1, arg2);
			g.setColor(Color.YELLOW);
			g.drawLine((int)x, (int)y, (int)(x+velocityX),(int) (y+velocityY));
			g.setColor(Color.WHITE);
			g.drawString(""+mass, (int)x+SIZE/2, (int)y+SIZE/2);
		}
		//g.setColor(Color.YELLOW);
		//g.drawLine((int)x, (int)y,(int) x,(int) (y+.05*netForceY));
		//g.drawLine((int)x, (int)y, (int)(x+.05*netForceX),(int)y);

		//g.setColor(Color.BLACK);
		//g.fillOval((int) (x), (int) (y), 1, 1);
	}

	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getVelocityX() {
		return velocityX;
	}
	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}
	public double getVelocityY() {
		return velocityY;
	}
	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}
	public double getMass() {
		return mass;
	}
	public void setMass(double mass) {
		this.mass = mass;
	}
	public double getCharge() {
		return charge;
	}
	public void setCharge(double charge) {
		this.charge = charge;
	}
	public double getNetForceX() {
		return netForceX;
	}
	public void setNetForceX(double netForceX) {
		this.netForceX = netForceX;
	}
	public double getNetForceY() {
		return netForceY;
	}
	public void setNetForceY(double netForceY) {
		this.netForceY = netForceY;
	}

	public static double distanceBetween(double x1, double y1, double x2, double y2){
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	
	public static double distanceBetween(Node n1, Node n2){
		return distanceBetween(n1.getX(), n1.getY(), n2.getX(), n2.getY());
	}
}
