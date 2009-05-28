package core;

import java.util.ConcurrentModificationException;
import java.util.Vector;

import javax.swing.JFrame;

public class Main {

	/**
	 * @param args
	 */
	public static final double K = 900000;
	public static final double EQ_CONDITION = .001;
	public static final int X_BOUND = 900;
	public static final int Y_BOUND = 500;
	public static final double colK = .0000000010;
	public static final double wallK = 10;
	public static Vector<Link> links;
	public static Vector<Node> nodes;

	public static boolean running;
	public static JFrame home;
	public static JFrame stats;
	public static GraphComponent kE;
	public static JFrame stats2;
	public static GraphComponent potE;
	public static double dt = .001;
	public static double u = 0;
	public static double t;
	
	public static void main(String[] args) throws InterruptedException {
		links = new Vector<Link>();
		nodes = new Vector<Node>();
		running = false;
		t =0;
		randInit(10, 0, 0);
		//init();
		home = new JFrame("Tree");
		TreeComponent tree = new TreeComponent();
		tree.addKeyListener(tree);
		tree.addMouseListener(tree);
		tree.addMouseMotionListener(tree);
		home.add(tree);
		stats = new JFrame("Kinetics");
		stats.setSize(X_BOUND,250);
		stats.add(kE = new GraphComponent());

		stats2 = new JFrame("Potentials");
		stats2.setSize(X_BOUND,250);
		stats2.add(potE = new GraphComponent());
		kE.setMax(50000000.0);
		potE.setMax(50000000.0);
		home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		home.setSize(X_BOUND+50,Y_BOUND+50);
		home.setVisible(true);
		running = true;
		stats.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		stats.setVisible(true);
		stats2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		stats2.setVisible(true);
		while(true){
			try{
				runSimulation();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public static void randInit(int nodeN, int fixedN, int linkN){
		for(int i=0; i<nodeN; i++){
			nodes.add(new Node(Math.random()*X_BOUND, Math.random()*Y_BOUND, 300*(.5)/*Math.random()*/ + 5, 10*(Math.random()*0)));
		}
		for(int i=0; i<fixedN; i++){
			nodes.add(new FixedNode(Math.random()*X_BOUND, Math.random()*Y_BOUND, 300*Math.random() + 5, 10*(Math.random())));
		}
		for(int i=0; i<linkN; i++){
			int x;
			links.add(new Link(nodes.get(x = (int)(Math.random()*nodes.size())), 
					nodes.get((int)(Math.random()*nodes.size())), Math.random()*25, Math.random()*100));
		}
		for(int i=0; i<links.size(); i++){
			if(links.get(i).getLeft() == links.get(i).getRight()){
				links.remove(i);
			}
		}
	}

	public static void init(){
		//nodes.add(new FixedNode(225, 225, 250, 5));
		//nodes.add(new Node(265, 265, 250, 5));
		//nodes.add(new Node(225, 265, 250, -5));
		//nodes.add(new Node(265, 225, 250, 5));
		/*nodes.add(new Node(325, 325, 250, 5));
		nodes.add(new Node(365, 365, 250, 5));
		nodes.add(new Node(365, 325, 250, 5));
		nodes.add(new Node(420, 200, 250, 5));

		links.add(new Link(nodes.get(1), nodes.get(7), 5, 50));
		links.add(new Link(nodes.get(1), nodes.get(2), 5, 50));
		links.add(new Link(nodes.get(2), nodes.get(0), 5, 50));
		links.add(new Link(nodes.get(5), nodes.get(4), 5, 50));
		links.add(new Link(nodes.get(3), nodes.get(5), 5, 50));
		links.add(new Link(nodes.get(2), nodes.get(6), 5, 50));
		links.add(new Link(nodes.get(5), nodes.get(6), 5, 50));
		 */
	}

	public static void applyElectrostaticForces(){
		for(int i=0; i<nodes.size(); i++){
			for(int j=0; j<nodes.size(); j++){
				if(i != j){
					double magnitude = K * 
					(nodes.get(i).getCharge() * nodes.get(j).getCharge()) /
					Math.pow(Node.distanceBetween(nodes.get(i).getX(), nodes.get(i).getY(), 
							nodes.get(j).getX(), nodes.get(j).getY()), 2);
					double y = nodes.get(i).getY() - nodes.get(j).getY();
					double x = nodes.get(i).getX() - nodes.get(j).getX();
					double angle = Node.getForceDirection(x, y);
					nodes.get(i).applyForce(magnitude * Math.cos(angle), magnitude * Math.sin(angle));
					//nodes.get(j).applyForce(-magnitude * Math.cos(angle), -magnitude * Math.sin(angle));
					//System.out.println("F " +i +" " +j +" = M " +magnitude +" A " +angle);
				}
			}
			//System.out.println("Fn " +i +" = " +nodes.get(i).getNetForceX() +" " +nodes.get(i).getNetForceY());
		}
	}

	public static void applySpringForces(){	
		for(Link l : links){
			l.applyForces();
		}
	}
	
	public static double calcSystemPE(){
		double pE = 0;
		for(Link l: links){
			pE += .5 * l.getK()*Math.pow(l.getPosition()-l.getRestPosition(),2);
		}
		for(Node n: nodes){
			double sumQ=0;
			for(int i=0;i<nodes.size(); i++){
				if(!n.equals(nodes.get(i))){
					sumQ+=nodes.get(i).getCharge()/Node.distanceBetween(n, nodes.get(i));
				}
			}
			sumQ*=(K*n.getCharge())/2;
			pE+=sumQ;
		}
		return pE;
	}
	
	public static void applyCollisionForces(){
		for(int i=0; i<nodes.size(); i++){
			for(int j=0; j<nodes.size(); j++){
				if(i!=j && Node.distanceBetween(nodes.get(i), nodes.get(j)) <= Node.SIZE){
					Node n1 = nodes.get(i);
					Node n2 = nodes.get(j);
					
					double Vx1 = n1.getVelocityX();
					double Vy1 = n1.getVelocityY();
					double Vx2 = n2.getVelocityX();
					double Vy2 = n2.getVelocityY();
					
					double x1 = n1.getX();
					double y1 = n1.getY();
					double x2 = n2.getX();
					double y2 = n2.getY();
					
					double m1 = n1.getMass();
					double m2 = n2.getMass();
					
					double mratio = m2/m1;
					double xdiff = x2 - x1;
					double ydiff = y2 - y1;
					double Vxdiff = Vx2 - Vx1;
					double Vydiff = Vy2 - Vy1;
					
					if(!((Vxdiff*xdiff + Vydiff*ydiff) >= 0)) {
					
					int sign;
					if (Math.abs(xdiff) <ydiff) {
						if (xdiff < 0) {
							sign = -1;
						} else {
							sign = 1;
						}
						xdiff = ydiff*sign;
					}
					
					double a = ydiff/xdiff;
					double dvx2 = -2*(Vxdiff + a*Vydiff)/((1+a*a)*(1+mratio));
					double Vx2p = Vx2 + dvx2;
					double Vy2p = Vy2 + a*dvx2;
					double Vx1p = Vx1 - mratio*dvx2;
					double Vy1p = Vy1 - a*mratio*dvx2;
					
					double Fx2 = m2*(Vx2p - Vx2)/dt;
					double Fy2 = m2*(Vy2p - Vy2)/dt;
					double Fx1 = m1*(Vx1p - Vx1)/dt;
					double Fy1 = m1*(Vy1p - Vy1)/dt;
					
					n2.applyForce(Fx2/2, Fy2/2);
					n1.applyForce(-Fx2/2, -Fy2/2);
//					n2.setVelocityX(Vx2p);
//					n2.setVelocityY(Vy2p);
//					n1.setVelocityX(Vx1p);
//					n1.setVelocityY(Vy1p);
					}
				}
			}
		}
	}

	public static void applyBoundaryForces(){
		for(Node n: nodes){
			if(n.getX() <= Node.SIZE/2|| n.getX() >= X_BOUND){
				n.applyForce(n.getMass()*-2*(1-u)*(n.getVelocityX())/dt, 0);
				n.setX((n.getX()<=Node.SIZE/2? Node.SIZE/2: X_BOUND));
			}
			if(n.getY() <= Node.SIZE/2 || n.getY() >= Y_BOUND){
				n.setY((n.getY()<=Node.SIZE/2? Node.SIZE/2: Y_BOUND));
				n.applyForce(0, n.getMass()*-2*(1-u)*(n.getVelocityY())/dt);
			}
		}
	}

	public static void applyFriction(){
		for(Node n: nodes){
			n.applyForce(9.8 * u * n.getMass() * -1 * (n.getVelocityX() == 0? 0 : (n.getVelocityX()/Math.abs(n.getVelocityX()))),
					9.8 * u * n.getMass() * -1 * (n.getVelocityY() == 0? 0 : (n.getVelocityY()/Math.abs(n.getVelocityY()))));
		}
	}

	public static void runSimulation(){
		double systemKE = 1;
		double lastKE = 0;
		while(systemKE > EQ_CONDITION){
			if(running){
				systemKE = 0;
				for(Node n: nodes){
					n.setNetForceX(0);
					n.setNetForceY(0);
				}
				//Apply forces
				applyElectrostaticForces();
				applySpringForces();
				//Collisions
				applyCollisionForces();
				//Boundary forces
				applyBoundaryForces();
				//Friction
				applyFriction();

				//Step to velocity
				for(Node n : nodes){
					n.setVelocityX(n.getVelocityX() + dt*n.getNetForceX()/n.getMass());
					n.setVelocityY(n.getVelocityY() + dt*n.getNetForceY()/n.getMass());
				}
				//Change position
				for(Node n : nodes){
					n.setX(n.getX() + dt*n.getVelocityX());
					n.setY(n.getY() + dt*n.getVelocityY());
				}
				//Find KE
				for(Node n : nodes){
					systemKE += n.getKE();
				}
				double systemPE;
				System.out.println(t +" : " +systemKE +" : " 
							+(systemPE = calcSystemPE()) +" : " +(systemKE + systemPE));
				if((Math.round(t*1000))%10==0){
					kE.addPoint(t*100, systemKE);
					potE.addPoint(t*100, systemPE);
				}
				try {
					Thread.sleep(Math.round(dt*1000));
					t+=dt;

					lastKE = systemKE;
					stats.repaint();
					stats2.repaint();
					home.repaint();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else
				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

		}


	}



}
