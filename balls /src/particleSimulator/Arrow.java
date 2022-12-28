package particleSimulator;

import java.awt.Graphics;
import java.awt.Graphics2D;

//this class is for drawing the force arrow for shooting the particles
public class Arrow {

	private int y1;
	private int x1;
	private int x2;
	private int y2;

	public Arrow(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

	}

	public void draw(Graphics g) {
		g = (Graphics2D) g;

		if ((this.getX1() != this.getX2()) && (this.getY1() != this.getY2())) {
			float arrowWidth = 10.0f;
			// x,yPoints are for the triangles at the end
			int[] xPoints = new int[3];
			int[] yPoints = new int[3];

			float[] vecLine = new float[2];
			float[] vecLeft = new float[2];
			float fLength; // force magnitude
			float th;
			float ta;
			float baseX, baseY;

			xPoints[0] = this.getX2();
			yPoints[0] = this.getY2();

			// build the line vector
			vecLine[0] = (float) xPoints[0] - this.getX1();
			vecLine[1] = (float) yPoints[0] - this.getY1();

			// build the arrow(triangle) base vector - normal to the line(قرینه معکوس)
			vecLeft[0] = -vecLine[1];
			vecLeft[1] = vecLine[0];

			// setup length parameters
			fLength = (float) Math.sqrt(Math.pow(vecLine[0], 2) + Math.pow(vecLine[1], 2));
			// to make the triangle at end of the arrow!numbers don't represent anything
			// here. They are set to best fit the purpose!
			th = arrowWidth / (1.5f * fLength); // the height

			ta = arrowWidth / (0.5f * ((float) Math.tan(1.0f)) * fLength);// the size of the base

			// find the base of the arrow
			baseX = ((float) xPoints[0] - ta * vecLine[0]);
			baseY = ((float) yPoints[0] - ta * vecLine[1]);

			// build the points on the sides of the arrow
			xPoints[1] = (int) (baseX + th * vecLeft[0]);
			yPoints[1] = (int) (baseY + th * vecLeft[1]);
			xPoints[2] = (int) (baseX - th * vecLeft[0]);
			yPoints[2] = (int) (baseY - th * vecLeft[1]);

			g.drawLine(getX1(), getY1(), (int) baseX, (int) baseY);
			g.fillPolygon(xPoints, yPoints, 3);

		}
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public int getY2() {
		return y2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getX2() {
		return x2;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX1() {
		return x1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getY1() {
		return y1;
	}

	public float getLength() {

		return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

}

