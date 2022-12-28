package particleSimulator;

/**
 * 
 * @author farzin.nasiri
 * 
 *         Every instance of this class is a vector in two dimensional space
 *         this class enables us to calculate the velocity of the particles after and before the collision
 *         in addition the force vector for shooting the particles is going to be of kind Vector
 *         also every particles' position is going to be defined by a Vector
 *
 */

public class Vector {
	private float x;
	private float y;

	public Vector() {
		this(0, 0);
	}

	public Vector(float x, float y) {
		this.setX(x);
		this.setY(y);
	}
	
	public Vector(Point p1, Point p2) {
		this.setX(p2.getX()-p1.getX());
		this.setY(p2.getY()-p1.getY());
	}
	
	public Vector(Point p) {
		this(p.getX(),p.getY());
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getX() {
		return x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getY() {
		return y;
	}

	public void set(float x, float y) {
		this.setX(x);
		this.setY(y);
	}

	// for calculating the velocities after the collision we need to define the
	// vector dot product
	public float dot(Vector v) {
		float result = 0.0f;
		result = this.getX() * v.getX() + this.getY() * v.getY();
		return result;
	}

	// this method returns the vectors length
	public float getMagnitude() {
		return (float) Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
	}

	public float getDistance(Vector v) {
		return (float) Math.sqrt(Math.pow((v.getX() - this.getX()), 2) + (Math.pow((v.getY() - this.getY()), 2)));
	}
	
	//this method adds another vector to the current instance 
	public Vector add(Vector v) {
		Vector v2 = new Vector();
		v2.setX(this.getX() + v.getX());
		v2.setY(this.getY() + v.getY());
		return v2;
	}
	//this method subtracts another vector to the current instance 
	public Vector subtract(Vector v) {
		Vector v2 = new Vector();
		v2.setX(this.getX() - v.getX());
		v2.setY(this.getY() - v.getY());
		return v2;
	}
	//a vector can also be multiplied by a number,then its size might increase or decrease
	public Vector multiply(float number) {
		Vector v2 = new Vector();
		v2.setX(this.getX() * number);
		v2.setY(this.getY() * number);
		return v2;
	}
	
	//in the physics of collision, we need to use the normalize of the vector(a vector with the same direction but with magnitude 1)
	public Vector normalize() {
		Vector v2 = new Vector();
		float l = getMagnitude();
		if (l != 0.0f) {
			v2.setX(this.getX() / l);
			v2.setY(this.getY() / l);
		} else {
			v2.setX(0.0f);
			v2.setY(0.0f);
		}

		return v2;
	}

	public String toString() {
		return "X: " + getX() + " Y: " + getY();
	}

}
