package particleSimulator;

import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.naming.directory.InvalidAttributeValueException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class Menu extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainPanel mainPanel;
	private JButton resetButton;
	private JButton generateButton;
	private JButton mixButton;
	private JButton spButton;
	private JButton setMass;
	private JButton recordSimulation;
	private JButton setAngularVel;
	private JLabel labelMessage;
	private JTextField angularVel;
	private JTextField fieldMass;
	private JTextField numParticles;
	private JTextField simTime;
	private ImageIcon pause;
	private ImageIcon start;
	private JComboBox akhs;
	private JComboBox ballSounds;
	private JCheckBox akh;
	private JCheckBox ballSound;
	private JCheckBox rotation;
	

	private JSlider restitutionSlider;
	private JSlider radiusSlider;
	

	private JButton openButton, saveButton, setDirButton;

	private JFileChooser fc;

	private String path;

	public Menu(MainPanel mainPanel) {
		this.setPreferredSize(new Dimension(200, 700));

		this.mainPanel = mainPanel;
		// label to show the result
		labelMessage = new JLabel("", SwingConstants.CENTER);
		this.labelMessage.setPreferredSize(new Dimension(200, 20));

		// instantiate controls
		this.resetButton = new JButton("Reset");
		this.resetButton.setPreferredSize(new Dimension(80, 80));

		// start the simulation
		this.spButton = new JButton();
		this.spButton.setPreferredSize(new Dimension(80, 80));

		this.generateButton = new JButton("Generate particles");
		this.mixButton = new JButton("Mix Paritcles");

		// JSlider for e
		this.restitutionSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
		this.restitutionSlider.setBorder(BorderFactory.createTitledBorder("e: " + restitutionSlider.getValue() + "%"));

		// set text field for number of particles
		this.numParticles = new HintTextField("ex:100");
		this.numParticles.setPreferredSize(new Dimension(50, 20));

		// JSlider for radius
		this.radiusSlider = new JSlider(JSlider.HORIZONTAL, 1, 150, 15);
		this.radiusSlider.setBorder(BorderFactory.createTitledBorder("Radius: " + radiusSlider.getValue()));
		

		// setting the mass
		this.setMass = new JButton("Set Mass");
		this.setMass.setPreferredSize(new Dimension(150, 20));

		// text field to get input
		this.fieldMass = new HintTextField("ex:15");
		this.fieldMass.setPreferredSize(new Dimension(100, 20));
		
		//settings for angular velocity
		this.rotation = new JCheckBox("Rotation");
		this.setAngularVel = new JButton("Set \u03C9");
		this.angularVel = new HintTextField("Enter \u03C9 here");
		this.angularVel.setPreferredSize(new Dimension(70,30));
		

		// setting open and save buttons
		this.openButton = new JButton("Import settings");
		this.openButton.setPreferredSize(new Dimension(180, 30));

		this.saveButton = new JButton("Save settings");
		this.saveButton.setPreferredSize(new Dimension(180, 30));

		this.setDirButton = new JButton("Set directory");
		this.setDirButton.setPreferredSize(new Dimension(180, 30));

		this.recordSimulation = new JButton("Rec");
		this.recordSimulation.setPreferredSize(new Dimension(90, 50));

		this.simTime = new HintTextField("ex:100(s)");
		this.simTime.setPreferredSize(new Dimension(90, 30));
		
		//setting the combo boxes and check boxes
		String[] akhsL = {"AKH1","AKH2"};
		String[] ballSoundsL = {"b1","b2","b3"};
		this.akhs = new JComboBox(akhsL);
		this.akhs.setEnabled(false);
		this.ballSounds = new JComboBox(ballSoundsL);
		this.ballSounds.setEnabled(false);
		
		this.akh = new JCheckBox("AKH!");
		
		this.ballSound = new JCheckBox("Collision Sound");
		

		// file chooser box
		fc = new JFileChooser();

		try {
			// open button image icon
			ImageIcon open = new ImageIcon("res/import.png");
			Image scaledOpen = open.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			open = new ImageIcon(scaledOpen);

			// close button image icon
			ImageIcon save = new ImageIcon("res/save.png");
			Image scaledSave = save.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
			save = new ImageIcon(scaledSave);

			// directory button
			ImageIcon folder = new ImageIcon("res/folder.png");
			Image scaledFolder = folder.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
			folder = new ImageIcon(scaledFolder);

			// start and pause button
			start = new ImageIcon("res/play.png");
			Image scaledSP = start.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			start = new ImageIcon(scaledSP);

			pause = new ImageIcon("res/pause.png");
			scaledSP = pause.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			pause = new ImageIcon(scaledSP);

			ImageIcon rec = new ImageIcon("res/rec.png");
			Image scaledRec = rec.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			rec = new ImageIcon(scaledRec);

			this.openButton.setIcon(open);
			this.saveButton.setIcon(save);
			this.setDirButton.setIcon(folder);
			this.spButton.setIcon(start);
			this.recordSimulation.setIcon(rec);

		} catch (Exception e) {
			System.out.println("Image not found");
		}

		// add controls to panel
		this.add(this.labelMessage);
		this.add(this.restitutionSlider);
		this.add(this.numParticles);
		this.add(this.generateButton);
		this.add(this.mixButton);
		this.add(this.radiusSlider);

		// text field for setting the mass:
		JLabel label = new JLabel("Enter particles' mass: ");
		this.add(label);

		this.add(this.fieldMass);

		// set mass button

		this.add(this.setMass);
		
		this.add(this.rotation);
		this.add(this.setAngularVel);
		this.add(this.angularVel);

		// open and save buttons
		this.add(this.openButton);
		this.add(this.saveButton);
		this.add(this.setDirButton);
		this.add(this.recordSimulation);
		this.add(this.simTime);
		
		//the combo boxes
		this.add(this.akh);
		this.add(this.akhs);
		this.add(this.ballSound);
		this.add(this.ballSounds);

		// reset button and start button
		this.add(this.resetButton);
		this.add(this.spButton);

		// wire up gui events
		ButtonHandler buttonHandler = new ButtonHandler();

		this.resetButton.addActionListener(buttonHandler);

		this.spButton.addActionListener(buttonHandler);

		this.generateButton.addActionListener(buttonHandler);

		this.mixButton.addActionListener(buttonHandler);

		this.setMass.addActionListener(buttonHandler);
		this.setAngularVel.addActionListener(buttonHandler);

		this.openButton.addActionListener(buttonHandler);
		this.saveButton.addActionListener(buttonHandler);
		this.setDirButton.addActionListener(buttonHandler);
		this.recordSimulation.addActionListener(buttonHandler);
		
		this.rotation.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				int status = e.getStateChange();
				if(status == ItemEvent.SELECTED) {
					mainPanel.rotation = true;
				}else if(status == ItemEvent.DESELECTED) {
					mainPanel.rotation = false;
					
				}
				
			}
		});

		this.akhs.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent e) {
	            String sound = (String) akhs.getSelectedItem();
	            mainPanel.akhSound = sound;
	            
	        }
	    });
		this.ballSounds.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent e) {
	            String sound = (String) ballSounds.getSelectedItem();
	            mainPanel.ballSound = sound;
	            
	        }
	    });

		SliderHandler sliderHandler = new SliderHandler();

		this.restitutionSlider.addChangeListener(sliderHandler);
		this.radiusSlider.addChangeListener(sliderHandler);
		
		ItemListen itemListener = new ItemListen();
		
		this.akh.addItemListener(itemListener);
		this.ballSound.addItemListener(itemListener);

	}

	private class ButtonHandler implements ActionListener {
		private int clicks = 0;
		private boolean recording = false;

		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();

			if (source == resetButton) {
				// set everything to default
				mainPanel.clearParticles();
				mainPanel.clearScreen();
				mainPanel.setMass(15);
				mainPanel.setRadius(15);
				fieldMass.setText("");
				numParticles.setText("");
				restitutionSlider.setValue(100);
				radiusSlider.setValue(15);
				mainPanel.rotation =false;
				mainPanel.angularVel = 5;
				akh.setSelected(false);
				ballSound.setSelected(false);
				rotation.setSelected(false);
				mainPanel.running = false;
				MainPanel.time = System.currentTimeMillis();
				labelMessage.setForeground(Color.GREEN);
				labelMessage.setText("Simulator reseted");

			}

			else if (source == generateButton) {
				// get the number of particles
				String numP = numParticles.getText();

				try {
					if (numP.equals("ex:100")) {
						mainPanel.randomParticleGenerator(100);
						labelMessage.setForeground(Color.GREEN);
						labelMessage.setText("Particles Generated");
					} else {
						Integer n = Integer.valueOf(numP);

						if (n < 0) {
							throw new InvalidAttributeValueException();
						}
						labelMessage.setForeground(Color.GREEN);
						labelMessage.setText("Particles Generated");

						mainPanel.randomParticleGenerator(n);
					}

				} catch (Exception e1) {
					labelMessage.setForeground(Color.RED);
					labelMessage.setText("Invalid input");
				}

			} else if (source == mixButton) {
				if (mainPanel.getCount() != 0) {
					mainPanel.mix();
					labelMessage.setForeground(Color.BLUE);
					labelMessage.setText("Particles mixed");
				} else {
					labelMessage.setForeground(Color.RED);
					labelMessage.setText("No particle in the simulator");
				}
			} else if (source == setMass) {
				String m = fieldMass.getText();
				try {
					Float mass = Float.valueOf(m);

					if (mass <= 0) {
						throw new InvalidAttributeValueException();
					}
					mainPanel.setMass(mass);
					labelMessage.setForeground(Color.GREEN);
					labelMessage.setText("mass is set to: " + mainPanel.getMass());
					fieldMass.setText("");

				} catch (Exception e1) {
					labelMessage.setForeground(Color.RED);
					labelMessage.setText("Invalid input");
				}
			} else if (source == openButton) {
				int returnVal = fc.showOpenDialog(Menu.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					try {
						String extension = "";

						int i = file.getAbsolutePath().lastIndexOf('.');
						if (i > 0) {
						    extension = file.getAbsolutePath().substring(i+1);
						}
						
						if(extension.equals("sim")) {
							mainPanel.startSimulation(file);
							
						}else if(extension.equals("init")) {
							mainPanel.setSettings(file);
	
						}else {
							throw new IOException();
						}
						
					} catch (IOException e1) {
						labelMessage.setForeground(Color.red);
						labelMessage.setText("Invalid file format");
					}
					
				
			

				}

			} else if (source == saveButton) {
				try {
					if (path != null && !path.isEmpty()) {

						mainPanel.saveSettings(path);

					} else {
						mainPanel.saveSettings(null);

					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} else if (source == setDirButton) {
				int returnVal = fc.showSaveDialog(Menu.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					path = file.getAbsolutePath();

				}
			} else if (source == spButton) {
				if (clicks == 0) {
					spButton.setIcon(pause);
					mainPanel.running = true;
					clicks++;
				} else {
					spButton.setIcon(start);
					mainPanel.running = false;
					clicks = 0;
				}
			} else if (source == recordSimulation) {
				try {
					long timer = Integer.valueOf(simTime.getText());
					mainPanel.time = System.currentTimeMillis();
					mainPanel.saveSimulation(timer, path);

					
					

				} catch (Exception e9) {
					labelMessage.setForeground(Color.RED);
					labelMessage.setText("Please enter the simulation duration");
				}
			}else if (source == setAngularVel) {
				try {
					float w = Float.valueOf(angularVel.getText());
					mainPanel.setAngularVel(w);
					
				
					
				}catch (Exception e10) {
					labelMessage.setText("Invalid input");
				}
			}

		}

	}

	private class SliderHandler implements ChangeListener {

		public void stateChanged(ChangeEvent e) {

			JSlider source = (JSlider) e.getSource();

			if (source == restitutionSlider) {
				source.setBorder(BorderFactory.createTitledBorder("e: " + source.getValue() + "%"));
				mainPanel.setRestitution(source.getValue());
				labelMessage.setForeground(Color.BLUE);
				labelMessage.setText("e has changed to " + source.getValue());

			} else if (source == radiusSlider) {
				source.setBorder(BorderFactory.createTitledBorder("Radius: " + source.getValue()));
				labelMessage.setForeground(Color.BLUE);
				labelMessage.setText("Radius has changed to " + source.getValue());
				mainPanel.setRadius(source.getValue());

			}

		}
	}
	private class ItemListen implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getItemSelectable();
			if(source == akh && akh.isSelected()) {
				akhs.setEnabled(true);
				mainPanel.soundOn1 = true;
				
			}else if(source == ballSound && ballSound.isSelected()) {
				ballSounds.setEnabled(true);
				mainPanel.soundOn2 = true;
				
			}
			
			else if(source == akh && !akh.isSelected()) {
				
				akhs.setEnabled(false);
				mainPanel.soundOn1 = false;
				
			}
			else if(source == ballSound && !ballSound.isSelected()) {
				
				ballSounds.setEnabled(false);
				mainPanel.soundOn2 = false;
				
			}
				
			
			
			
		}
		
	}
}
