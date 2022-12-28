package particleSimulator;

import java.awt.Canvas;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;



public class MainPanel extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Rendering / Buffer objects
	private BufferStrategy strategy;
	private Graphics2D g2;


	// Particle objects
	private Particle[] Particles = new Particle[2000];
	private Particle currentParticle;
	private int ParticleCount;
	private float radius = 15;
	private float mass = 15;
	public float angularVel = 5;
	public boolean running = false;
	private boolean saving = false;
	private long duration = Integer.MAX_VALUE;
	public String akhSound = "AKH1";
	public String ballSound = "b1";
	public boolean soundOn1 = false;
	public boolean soundOn2 = false;
	public boolean rotation = false;
	

	private File simFile = null;

	public static long time;

	private Container container = new Container(); // container

	public boolean flag = false; // flag for drawing polygon

	public ArrayList<Line> lines = new ArrayList<Line>();

	// force Arrow to throw the particles
	private Arrow forceArrow;
	private float arrowScale = 5.0f;

	// Frames
	private int fps;

	private final int WIDTH = 1200;
	private final int HEIGHT = 700;

	public MainPanel() {
		// timer


		// setting
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setIgnoreRepaint(true);

		// Events Handling
		EventHandler eventHandler = new EventHandler();
		addMouseMotionListener(eventHandler);
		addMouseListener(eventHandler);
		addKeyListener(eventHandler);
	}

	// Start Render and Update Threads
	public void start() {
		mainLoop();
	}

	public long Time() {
		return (System.currentTimeMillis() - time);
	}

	public void mainLoop() {
		long previousTime = System.currentTimeMillis();
		long currentTime = previousTime;
		long elapsedTime;
		long totalElapsedTime = 0;
		int frameCount = 0;

		while (true) {
			currentTime = System.currentTimeMillis();
			elapsedTime = (currentTime - previousTime); // elapsed time in seconds
			totalElapsedTime += elapsedTime;
			// we are checking if more than one second has passed
			if (totalElapsedTime > 1000) {
				this.fps = frameCount;
				frameCount = 0;
				totalElapsedTime = 0;
			}
			if (running) {
				moveParticles(elapsedTime / 1000f);
				if(rotation) {
				rotateParticles(elapsedTime / 1000f);
				}

			}
			checkTime(this.duration);
			render();

			try {

				Thread.sleep(5);
			} catch (Exception e) {
				e.printStackTrace();
			}

			previousTime = currentTime;
			frameCount++;

		}

	}

	public void clearParticles() {
		ParticleCount = 0;
	}

	public void clearScreen() {
		container.clear();
		lines.clear();
	}

	public void mix() {
		Random r = new Random();
		for (int i = 0; i < this.ParticleCount; i++) {
			this.Particles[i].velocity.set((r.nextFloat() * 1000), (r.nextFloat() * 2000));
		}
	}

	public void setRestitution(float percentage) {
		currentParticle.e = percentage / 100.0f;
	}

	public void setRadius(float r) {
		this.radius = r;
	}

	public float getRadius() {
		return this.radius;
	}

	public float getMass() {
		return this.mass;
	}

	public int getCount() {
		return ParticleCount;
	}

	public void setMass(float mass) {
		this.mass = mass;

	}
	public float getAngularVel() {
		return this.angularVel;
	}
	public void setAngularVel(float w) {
		this.angularVel = w;
	}
	

	public void randomParticleGenerator(int numParticles) {
		Random rand = new Random();
		for (int i = 0; i < numParticles; i++) {
			Particle tempParticle = new Particle(rand.nextInt(this.WIDTH), rand.nextInt(this.HEIGHT), getRadius(),
					getMass());

			Particles[ParticleCount] = tempParticle;
			ParticleCount++;
		}
	}

	public void render() {
		// for the first frame strategy is null
		if (strategy == null || strategy.contentsLost()) {
			// Create BufferStrategy for rendering/drawing
			createBufferStrategy(2);
			strategy = getBufferStrategy();

			this.g2 = (Graphics2D) strategy.getDrawGraphics();
		}

		// Turn on anti-aliasing
		this.g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Render Background
		this.g2.setColor(Color.black);
		this.g2.fillRect(0, 0, getWidth(), getHeight());

		// Render Game Objects
		for (int i = 0; i < ParticleCount; i++) {
			Particles[i].draw(this.g2);
		}
		// Render Line
		if (!lines.isEmpty()) {
			for (int i = 0; i < lines.size(); i++) {
				lines.get(i).draw(this.g2);

			}
		}

		// Render Container

		if (!container.getPoints().isEmpty()) {
			container.draw(g2);
		}

		Particle tempParticle = currentParticle;
		if (tempParticle != null)
			tempParticle.draw(this.g2);

		// Render Foreground (text, etc)

		// Draw force Arrow and Speed Text along arrow if we are launching a Particle
		Arrow tempArrow = forceArrow;
		if (tempArrow != null) {
			tempArrow.draw(this.g2);

			// Power Arrow Magnitude Text
			this.g2.setColor(Color.RED);
			this.g2.drawString(String.format("%.2f px/s", tempArrow.getLength() * arrowScale, 2),
					(tempArrow.getX2() + tempArrow.getX1()) / 2, (tempArrow.getY1() + tempArrow.getY2()) / 2);
		}

		// Display introduction Text in center if no Particles
		if (ParticleCount == 0 && currentParticle == null) {
			this.g2.setColor(Color.BLUE);
			this.g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
			// adding my logo!
			Image pic = new ImageIcon("res/logo2.png").getImage();

			this.g2.drawImage(pic, 450, 90, 300, 300, null);

			// title
			String title = "Physics Particle Simulator";

			for (int i = 0; i < title.length(); i++) {
				Random r = new Random();
				int red = r.nextInt(255);
				int green = r.nextInt(255);
				int blue = r.nextInt(255);
				Color color = new Color(red, green, blue);
				this.g2.setColor(color);
				this.g2.drawString(Character.toString(title.charAt(i)), getWidth() / 2 - 370 + 30 * i,
						getHeight() / 2 - 250);
			}

			// creation details!
			this.g2.setFont(new Font(Font.SERIF, Font.ITALIC, 15));
			this.g2.setColor(Color.WHITE);

			String author = "created by Farzin Nasiri";
			this.g2.drawString(author, getWidth() / 2 - (this.g2.getFontMetrics().stringWidth(author) / 2),
					getHeight() / 2 + 30);

			author = "CS student of Sharif University of Thechnology-Iran-Tehran";
			this.g2.drawString(author, getWidth() / 2 - (this.g2.getFontMetrics().stringWidth(author) / 2),
					getHeight() / 2 + 50);
			String det = "BP Fall-Winter of 1397, First semester";
			this.g2.drawString(det, getWidth() / 2 - (this.g2.getFontMetrics().stringWidth(det) / 2),
					getHeight() / 2 + 70);
			// help
			this.g2.setFont(new Font(Font.DIALOG, Font.PLAIN, 25));
			String helpString1 = "Click and drag your mouse to creat new particles";

			this.g2.drawString(helpString1, getWidth() / 2 - (this.g2.getFontMetrics().stringWidth(helpString1) / 2),
					getHeight() / 2 + 150);

			String helpString2 = "Hold SHIFT button and click to draw a wall";
			this.g2.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
			this.g2.drawString(helpString2, getWidth() / 2 - (this.g2.getFontMetrics().stringWidth(helpString2) / 2),
					getHeight() / 2 + 180);
			String helpString3 = "Hold CONTROL button and click to draw a container";
			this.g2.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
			this.g2.drawString(helpString3, getWidth() / 2 - (this.g2.getFontMetrics().stringWidth(helpString3) / 2),
					getHeight() / 2 + 200);
		}

		// Draw our frame rate and Particle count
		this.g2.setColor(Color.WHITE);
		this.g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));

		this.g2.drawString("Number of particles in the simulator: " + ParticleCount, 500, 12);

		if (!strategy.contentsLost()) {

			strategy.show();
		}

	}

	// for moving
	public void moveParticles(float deltaT) {

		// step the position of movable objects based off their velocity
		// elapsedTime
		for (int i = 0; i < ParticleCount; i++) {
			
			Particles[i].velocity.setY(Particles[i].velocity.getY());

			Particles[i].position.setX(Particles[i].position.getX() + (Particles[i].velocity.getX() * (deltaT)));
			Particles[i].position.setY(Particles[i].position.getY() + (Particles[i].velocity.getY() * (deltaT)));
			


			// if the speed is less than an small amount stop!
			if (Math.abs(Particles[i].velocity.getX()) < 0.00001f)
				Particles[i].velocity.setX(0);
			if (Math.abs(Particles[i].velocity.getY()) < 0.00001f)
				Particles[i].velocity.setY(0);

		}
		
		checkCollisions();

	}
	public void rotateParticles(float deltaT) {
		for(int i = 0; i < ParticleCount; i++) {
			Particles[i].rotate(Particles[i].getW()*deltaT);
			Particles[i].moveEndX((Particles[i].velocity.getX() * (deltaT)));
			Particles[i].moveEndY((Particles[i].velocity.getY() * (deltaT)));
			
		}
		
	}

	// sorting the ball array for collision checking using insertion sort
	// source:https://www.geeksforgeeks.org/insertion-sort/
	public void sort(Comparable[] arr) {

		for (int i = 1; i < ParticleCount; ++i) {
			Comparable key = arr[i];
			int j = i - 1;

			/*
			 * Move elements of arr[0..i-1], that are greater than key, to one position
			 * ahead of their current position
			 */
			while (j >= 0 && key.compareTo(arr[j]) < 0) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = key;
		}
	}
	
	public void checkCollisions() {
		sort(Particles);
		checkWallCollison();

	}

//Particle to Particle collision 
	private void checkParticleCollision(int i) {

		for (int j = i + 1; j < ParticleCount; j++) {
			if ((Particles[i].position.getX() + Particles[i].getRadius()) < (Particles[j].position.getX()
					- Particles[j].getRadius())) {
				break;
			}

			if (Particles[i].getRadius() + Particles[j].getRadius() < Math
					.abs(Particles[i].position.getY() - Particles[j].position.getY())) {
				continue;
			}

			Particles[i].collision(Particles[j]);
			if(rotation) {
				Particles[i].rotationResponce(Particles[j]);
			}
			playCollision(this.ballSound);
			if (this.saving) {
				writeCollisonDetails(i, j, Particles[i].getVelocity(), Particles[j].getVelocity(), Time());
				
			}
		}

	}

	// Check for collision with walls
	private void checkWallCollison() {

		for (int i = 0; i < ParticleCount; i++) {
			Particle p = Particles[i];

			if (p.position.getX() - p.getRadius() < 0) {
				playSound(this.akhSound);
				p.position.setX(p.getRadius()); // Place Particle against edge
				p.velocity.setX(-(p.velocity.getX())); // Reversing the velocity

				p.velocity.setY(p.velocity.getY());
			} else if (p.position.getX() + p.getRadius() > getWidth()) // Right Wall
			{
				playSound(this.akhSound);
				p.position.setX(getWidth() - p.getRadius()); // Place Particle against edge
				p.velocity.setX(-(p.velocity.getX())); // Reversing the velocity
				p.velocity.setY((p.velocity.getY()));
			}

			if (p.position.getY() - p.getRadius() < 0) // Top Wall
			{
				playSound(this.akhSound);
				p.position.setY(p.getRadius()); // Place Particle against edge
				p.velocity.setY(-(p.velocity.getY())); // Reversing the velocity
				p.velocity.setX((p.velocity.getX()));// Reversing the velocity
			} else if (p.position.getY() + p.getRadius() > getHeight()) // Bottom Wall
			{
				playSound(this.akhSound);
				p.position.setY(getHeight() - p.getRadius()); // Place Particle against edge
				p.velocity.setY(-(p.velocity.getY()));// Reversing the velocity
				p.velocity.setX((p.velocity.getX()));// Reversing the velocity
			}
			checkParticleCollision(i);
			if (!lines.isEmpty()) {
				Iterator<Line> iter = lines.iterator();
				while (iter.hasNext()) {
					Line l = iter.next();

					checkLineCollision(i, l);
				}
			}
			if (!container.isEmpty()) {
				checkContainerCollision(i);
			}

		}
	}

	// check collision with line
	private void checkLineCollision(int i, Line l) {

		Particle p = Particles[i];
		Vector v1 = new Vector(l.getP1());
		Vector v2 = new Vector(l.getP2());

		Vector c = new Vector(p.position.getX(), p.position.getY());

		// a is the closet of points 1 & 2 to the circles center
		// b is the farthest of points 1 & 2 to the circles center

		float deltaD1 = c.subtract(v1).getMagnitude();
		float deltaD2 = c.subtract(v2).getMagnitude();

		Vector a;
		Vector b;
		if (deltaD1 <= deltaD2) {
			a = v1;
			b = v2;
		} else {
			a = v2;
			b = v1;
		}

		Vector line = b.subtract(a);
		float r = p.getRadius();

		// seg_v is a vector from a to b
		// pt_v is a vector from a to the center of the circle
		Vector seg_v = line.normalize();
		Vector pt_v = c.subtract(a);

		// now we find the closest point on the line to the circle
		float closeSize = pt_v.dot(seg_v);

		Vector closest;
		float distance;
		if (closeSize <= 0) {
			closest = a;
			distance = pt_v.getMagnitude();

		} else {
			closest = seg_v.multiply(closeSize).add(a);
			distance = c.getDistance(closest);
		}
		Vector rePositionVector = (((c.subtract(closest)).normalize()).multiply(r)).add(closest);

		// now the collision checking!

		if (distance <= r) {

			// repositioning the particle if collides with a wall
			p.position.setX(rePositionVector.getX());
			p.position.setY(rePositionVector.getY());

			// changing velocity of the particle after collision

			wallCollision(p, line);

		}

	}

	private void wallCollision(Particle p, Vector line) {

		// when collision happens the normal vector changes but the tangent vector is
		// the same
		Vector v = p.getVelocity();
		Vector image = (line.normalize()).multiply(v.dot(line) / (line.getMagnitude()));

		Vector newVelocity = (image.multiply(2f)).subtract(v);
		p.setVelocity(newVelocity);

	}

	private void checkContainerCollision(int j) {
		for (int i = 0; i < this.container.Size(); i++) {

			if (i + 1 == this.container.Size()) {
				Line l = new Line(this.container.getPoint(i), this.container.getPoint(0));
				checkLineCollision(j, l);
			} else {
				Line l = new Line(this.container.getPoint(i), this.container.getPoint(i + 1));
				checkLineCollision(j, l);
			}
		}

	}

	public void playSound(String name) {
		if(this.soundOn1) {

		try {
			File f = new File(String.format("res/%s.wav", name));
			AudioInputStream ais = AudioSystem.getAudioInputStream(f);
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			clip.setFramePosition(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

	}
	


	public void playCollision(String name) {
		if(this.soundOn2) {

		try {
			File f = new File(String.format("res/%s.wav", name));
			AudioInputStream ais = AudioSystem.getAudioInputStream(f);
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			clip.setFramePosition(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	}


	public void setSettings(File settings) {

		Scanner in = null;
		try {
			in = new Scanner(new FileReader(settings));
			int numOfPar = in.nextInt();

			for (int i = 0; i < numOfPar && in.hasNextLine(); i++) {
				String line = in.next();

				String[] data = line.split(",");
				float x = Float.parseFloat(data[0]);
				float y = Float.parseFloat(data[1]);
				float vx = Float.parseFloat(data[2]);
				float vy = Float.parseFloat(data[3]);
				float m = Float.parseFloat(data[4]);
				float r = Float.parseFloat(data[5]);
				int red = Integer.parseInt(data[6]);
				int green = Integer.parseInt(data[7]);
				int blue = Integer.parseInt(data[8]);
				currentParticle = new Particle(x, y, r, m, new Color(red, green, blue));
				
				currentParticle.velocity.set(vx, vy);
				Particles[ParticleCount] = currentParticle;
				ParticleCount++;
				currentParticle = null;

			}
			int con = in.nextInt();

			String line2 = in.next();
			String[] data = line2.split(",");
			for (int i = 0; i < 2 * con; i += 2) {
				int x = Integer.parseInt(data[i]);
				int y = Integer.parseInt(data[i + 1]);
				Point p = new Point(x,y);
				this.container.addPoint(p);
				
				
				

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveSettings(String path) throws IOException {
		File settings;
		if (path == null) {
			settings = new File("C:\\settings.init");
		} else {
			settings = new File(path);
		}

		// Create the file
		PrintWriter writer = new PrintWriter(settings);
		// position+velocity+mass+radius
		writer.print(ParticleCount);
		writer.print("\n");
		for (int i = 0; i < ParticleCount; i++) {

			writer.print(Particles[i].position.getX());
			writer.print(",");
			writer.print(Particles[i].position.getY());
			writer.print(",");
			writer.print(Particles[i].velocity.getX());
			writer.print(",");
			writer.print(Particles[i].velocity.getY());
			writer.print(",");
			writer.print(Particles[i].getMass());
			writer.print(",");
			writer.print(Particles[i].getRadius());
			writer.print(",");
			writer.print(Particles[i].getColor().getRed() + "," + Particles[i].getColor().getGreen() + ","
					+ Particles[i].getColor().getBlue());
			writer.print("\n");
		}

		writer.print(container.Size());
		writer.print("\n");

		ArrayList<Point> c = container.getPoints();
		for (int j = 0; j < c.size(); j++) {

			writer.print(c.get(j).getX());
			writer.print(",");
			writer.print(c.get(j).getY());
			writer.print(",");

		}
		writer.print("\n");
		writer.print("\n");

		writer.close();
		this.simFile = settings;

	}

	public void saveSimulation(long duration, String path) throws IOException {
		this.saving = true;
		this.duration = duration;
		if (path == null) {
			path = "C:\\simulation.sim";
		}
		saveSettings(path);
		FileWriter fr = new FileWriter(this.simFile, true);
		BufferedWriter br = new BufferedWriter(fr);
		PrintWriter pr = new PrintWriter(br);
		pr.println(duration);
		pr.close();
		br.close();
		fr.close();

	}

	private void writeCollisonDetails(int ball1, int ball2, Vector vector1, Vector vector2, long t) {
		FileWriter fr;
		try {
			fr = new FileWriter(this.simFile, true);
			BufferedWriter br = new BufferedWriter(fr);
			PrintWriter pr = new PrintWriter(br);
			pr.println(ball1 + " " + ball2);
			pr.println(t);
			pr.println(vector1.getX() + " " + vector1.getY());
			pr.println(vector2.getX() + " " + vector2.getY());
			pr.println();
			pr.close();
			br.close();
			fr.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void startSimulation(File file) {

	}

	private void checkTime(long duration) {
		long currentTime = Time();
		if (currentTime >= duration*1000) {
			this.running = false;
		}
		

	}

	// this class is for shooting the particles!
	private class EventHandler extends MouseAdapter implements MouseMotionListener, KeyListener {
		boolean flagL = false;
		boolean flagC = false;
		private int clicks = 0;
		private Point p1;
		private Point p2;
		private Line line;

		public void mousePressed(MouseEvent e) {
			try {
				if (flagL) {
					int x = e.getX();
					int y = e.getY();
					if (clicks == 0) {
						p1 = new Point(x, y);
						line = new Line();
						line.setP1(p1);
						clicks++;

					} else if (clicks == 1) {
						p2 = new Point(x, y);
						line.setP2(p2);
						lines.add(line);
						clicks = 0;

					}

				} else if (flagC) {
					int x = e.getX();
					int y = e.getY();

					container.addPoint(new Point(x, y));

				}

				else {
					if(!rotation) {

					currentParticle = new Particle(e.getX(), e.getY(), getRadius(), getMass());
					}else if(rotation) {
						currentParticle = new Particle(e.getX(), e.getY(), getRadius(), getMass(),getAngularVel());
					}
					forceArrow = new Arrow(e.getX(), e.getY(), e.getX(), e.getY());
				}
			} catch (Exception e3) {
				// TODO: handle exception
			}
		}

		// when the mouse is released we can calculate the velocities
		public void mouseReleased(MouseEvent e) {
			try {
				if (!(flagL && flagC)) {
					// Change in x/y per second
					float xVector = (forceArrow.getX2() - forceArrow.getX1()) * 5;
					float yVector = (forceArrow.getY2() - forceArrow.getY1()) * 5;

					currentParticle.velocity.set(xVector, yVector);

					Particles[ParticleCount] = currentParticle;
					ParticleCount++;
					// reassign the parameters for later use;
					currentParticle = null;
					forceArrow = null;
				}
			} catch (Exception e4) {
				// TODO: handle exception
			}
		}

		// the mouseDragged works until you havn't release the mouse button!it is
		// automatically called if it detects dragging!
		public void mouseDragged(MouseEvent e) {
			try {
				// for dragging first we get the initial position of the mouse, these
				// coordinates are saved in the first instance of force arrow
				int x1 = forceArrow.getX1();
				int y1 = forceArrow.getY1();

				// now we get the final position of the mouse from the event
				int x2 = e.getX();
				int y2 = e.getY();

				// now we calculate the displacement
				int dx = Math.abs(x2 - x1);
				int dy = Math.abs(y2 - y1);

				/**
				 * now we set the final points(ends) of the vector for more elegance and more
				 * realism the arrow would go against the direction of the mouse so it looks
				 * like a bow throwing a ball
				 */
				if ((x2 - x1) < 0) {
					forceArrow.setX2(x1 + dx);
				} else {
					forceArrow.setX2(x1 - dx);
				}

				if ((y2 - y1) < 0) {
					forceArrow.setY2(y1 + dy);
				} else {
					forceArrow.setY2(y1 - dy);
				}
			} catch (Exception e6) {
				// TODO: handle exception
			}
		}

		// for combo keys! L+click => line , C+click => container
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			try {
				int code = e.getKeyCode();
				if (code == KeyEvent.VK_CONTROL) {
					flagC = true;
				} else if (code == KeyEvent.VK_SHIFT) {
					flagL = true;
				}
			} catch (Exception e5) {
				// TODO: handle exception
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_CONTROL) {
				flagC = false;
			} else if (code == KeyEvent.VK_SHIFT) {
				flagL = false;
			}

		}
	}

}