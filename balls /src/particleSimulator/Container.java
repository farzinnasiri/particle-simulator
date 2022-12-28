package particleSimulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Container {
	private ArrayList<Point> points;
	
	
	public Container() {
		this.points = new ArrayList<Point>();
		
	}
	public int Size() {
		return this.points.size();
	}
	public ArrayList getPoints() {
		return this.points;
	}
	
	public void addPoint(Point p) {
		this.points.add(p);
	}
	
	public Point getPoint(int i) {
		return this.points.get(i);
	}
	public void clear() {
		this.points.clear();
	}
	public boolean isEmpty() {
		return points.isEmpty();
	}
	
	public void draw(Graphics2D g2) {
		for(int i = 0; i < this.points.size()-1; i++) {
	    	g2.setColor(Color.WHITE);
	    	Point p1 = this.points.get(i);
	    	Point p2 = this.points.get(i+1);
	    	int x1 = p1.getX();
	    	int x2 = p2.getX();
	    	int y1 = p1.getY();
	    	int y2 = p2.getY();
			
			g2.drawLine(x1, y1, x2, y2);
			
		}
		if(this.points.size() > 2) {
			Point pi = this.points.get(0);
			Point pf = this.points.get(this.points.size()-1);
	    	int xi = pi.getX();
	    	int xf = pf.getX();
	    	int yi = pi.getY();
	    	int yf = pf.getY();
	    	g2.drawLine(xi,yi,xf,yf);
			
		}
	}

}
