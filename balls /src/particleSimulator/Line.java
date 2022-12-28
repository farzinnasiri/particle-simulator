package particleSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Line {
	private Point p1;
    private Point p2;
 
    public Line() {
    	
    	
    }
 
    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
 
    public Point getP1() {
        return p1;
    }
 
    public Point getP2() {
        return p2;
    }
 
    public void setP1(Point p1) {
        this.p1 = p1;
    }
 
    public void setP2(Point p2) {
        this.p2 = p2;
    }
    //drawing lines
    public void draw(Graphics2D g2) {
    	g2.setColor(Color.WHITE);
    	int x1 = (int) getP1().getX();
    	int x2 = (int) getP2().getX();
    	int y1 = (int) getP1().getY();
    	int y2 = (int) getP2().getY();
    	g2.drawLine(x1, y1, x2, y2);
    }

}
