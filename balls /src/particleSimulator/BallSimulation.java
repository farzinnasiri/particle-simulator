package particleSimulator;
/**
 * @author farzin.nasiri
 * Basic Programming project number 2
 * Fall-Winter 97 semester
 */

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;

import javax.swing.JFrame;

public class BallSimulation {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Physics Particle Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		try {
			frame.setIconImage(ImageIO.read(new File("res/logo2.png")));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.X_AXIS));


		MainPanel ballCanvas = new MainPanel();
		Menu controlPanel = new Menu(ballCanvas);

		frame.getContentPane().add(ballCanvas);
		frame.getContentPane().add(controlPanel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

		ballCanvas.start();

	}

}
