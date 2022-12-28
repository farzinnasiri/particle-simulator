package particleSimulator;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.Random;


public class Particle implements Comparable<Particle> {
	public Vector velocity;
	public Vector position;
	private float mass;
	private float radius;
	private Color color;
	public static float e = 1.00f;
	//the angular velocity
	public float w;

	public float dX;
	public float dY;

	private double theta;
	
	public Particle(float x, float y, float radius) {
		this(x, y, radius, 1);

	}

	public Particle(float x, float y, float radius, float mass) {
		this.velocity = new Vector(0, 0);
		this.position = new Vector(x, y);
		this.setMass(mass);
		this.setRadius(radius);
		this.color = setParticleColor();
		this.dX = radius;
		this.dY = 0;

	}
	public Particle(float x, float y, float radius, float mass, Color color) {
		this.velocity = new Vector(0, 0);
		this.position = new Vector(x, y);
		this.setMass(mass);
		this.setRadius(radius);
		this.color = color;

	}


	
	public Particle(int x, int y, float radius, float mass, float angularVel) {
		this(x,y,radius,mass);
		this.w = angularVel;
		
	}

	public Color getColor() {
		return this.color;
	}

	// this method generates a random color for each particle
	public Color setParticleColor() {
		Random r = new Random();

		int red = r.nextInt(255);
		int green = r.nextInt(255);
		int blue = r.nextInt(255);
		Color color = new Color(red, green, blue);
		
		return color;

	}

	public void draw(Graphics2D g2) {
		float x = position.getX();
		float y = position.getY();
		float r = this.getRadius();

		g2.setColor(this.color);
		g2.fillOval((int) (x - r), (int) (y- r),
				(int) (2 * r), (int) (2 *r));
		Line2D line = new Line2D.Double(x,y,x+r * Math.cos(theta),y+ r * Math.sin(theta));
		g2.setStroke(new BasicStroke(2));

	   // Draw the rotated line
	   g2.setColor(Color.white);
	   g2.draw(line);

	}
	
	public void rotate(float theta) {

		this.theta += theta;
		
		
	}
	public void moveEndX(float deltaX) {
		this.dX += deltaX;
		
		
	}
	public void moveEndY(float deltaY) {
		this.dY += deltaY;
		
	}


	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	// this is where the physics of the collision applies
	public void collision(Particle Particle) {
		/**
		 * in order to calculate the final velocities we first need to find the impulse
		 * this is the formula for the impulse: J = (1+e)(m1*m2/(m1+m2)) *(u2-u1).n
		 * where u2,u1 are initial speeds and n is the normalized vector in the diction
		 * of j so the final velocities are: v1 = u1 - J/m2 *n v2 = u2 + J/m1 *n
		 * source:https://en.wikipedia.org/wiki/Inelastic_collision also we need to move
		 * the particles according to SAT(separation axis theorem) the amount we move
		 * the particles is called: Minimum Translation Vector if two particles a and b
		 * are touching each other and we want to move them apart by d(which is the
		 * Minimum Translation Distance) in the direction of v then this equation
		 * applies: (a.x + (v.x * d)) - b.x)2 + ((a.y + (v.y * d)) - b.y)2 = (a.r +
		 * b.r)2 where .x and .y are the positions,v.x and v.y are the components and .r
		 * is the radius solving for d results: d =
		 * (sqrt((v.y²+v.x²)*e-a.x²*v.y²+2*a.x*v.x*a.y*v.y-v.x²*a.y²)-a.y*v.y-a.x*v.x)/(v.y²+v.x²)
		 * source:https://stackoverflow.com/questions/29203036/how-to-calculate-the-minimum-translation-vector-in-a-given-direction
		 * 
		 */

		Vector d = (position.subtract(Particle.position)); // d from the second particle to the first one(this)
		float dis = d.getMagnitude();

		// here the vector form of all equations are going to be used
		Vector minimumTranslationVector;

		// checking the magnitude
		if (dis != 0.0f) {
			minimumTranslationVector = d.multiply(((getRadius() + Particle.getRadius()) - dis) / dis);

		} else // Special case. Particles have completely overlapped

		{
			dis = Particle.getRadius() + getRadius() - 1.0f;
			d = new Vector(Particle.getRadius() + getRadius(), 0.0f);

			minimumTranslationVector = d.multiply(((getRadius() + Particle.getRadius()) - dis) / dis);
		}

		// getting the masses
		float m1 = this.getMass();
		float m2 = Particle.getMass();

		// push-pull them apart
		this.position = this.position.add(minimumTranslationVector.multiply(m2 / (m1 + m2)));
		Particle.position = Particle.position.subtract(minimumTranslationVector.multiply(m1 / (m2 + m1)));
		
		
		
		
		// impact speed
		Vector v = (this.velocity.subtract(Particle.velocity));
		float vn = v.dot(minimumTranslationVector.normalize());

		/**
		 * It is possible that the particles get close enough but not collide hence
		 * abs(v1 - v2) should also be checked
		 */
		if (vn >= 0.0f) {
			return;
		}

		// collision impulse
		float i = ((1.0f + this.e) * vn) * (m1 * m2) / (m1 + m2);
		Vector j = minimumTranslationVector.normalize().multiply(i);// j is the impulse

		// change in momentum and velocities
		this.velocity = this.velocity.subtract(j.multiply(1 / m1));
		Particle.velocity = Particle.velocity.add(j.multiply(1 / m2));


	}
	
	public void rotationResponce(Particle Particle) {
		float m1 = this.getMass();
		float m2 = Particle.getMass();

		float w1f = ((m1-m2)*this.w/(m1+m2) )+(2*m2*Particle.w/(m1+m2));
		float w2f = (2*m1*this.w/(m1+m2))-((m1-m2)*Particle.w/(m1+m2) );
		this.w = w1f;
		Particle.w = w2f;
		

		
	}

	private void setMass(float mass) {
		this.mass = mass;
	}

	public float getMass() {
		return this.mass;
	}
	public float getW() {
		return this.w;
	}
	
	public Vector getVelocity() {
		return this.velocity;
	}
	public void setVelocity(Vector v) {
		this.velocity = v;
	}

	// the compareTo method is used to order the collision queue ,if they are
	public int compareTo(Particle Particle) {
		if (this.position.getX() - this.getRadius() > Particle.position.getX() - Particle.getRadius()) {
			return 1;
		} else if (this.position.getX() - this.getRadius() < Particle.position.getX() - Particle.getRadius()) {
			return -1;
		} else {
			return 0;
		}
	}

}
