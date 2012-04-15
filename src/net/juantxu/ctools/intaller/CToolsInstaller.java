package net.juantxu.ctools.intaller;


import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CToolsInstaller {
	private String pentahoSolutionsDir, tomcatDir;
	private JLabel lblPthSol, lblTomcat, lblUpdate;
	private JTextArea txtExplicacion;
	private JButton btnPthSol, btnTomcat, btnUpdate;
	private JCheckBox CDF, CDA, CDE, CGG, saikuStable, saikuDev;
	private JFrame frame;
	private JPanel botones;

	private void CreateComponents() {
		pentahoSolutionsDir = System.getProperty("user.dir");
		tomcatDir = System.getProperty("user.dir");
		txtExplicacion = new JTextArea(
				"Graphic Tools For CTOOLS Installer.\n"

						+ "Set the pentaho Solutions Dir and the Tomcat dir if you want to intall CGG. \n"
						+ "Check the components you want to install/update\n"
						+ "And click update...\n"
						+"\n To run this installer YOU MUST STOP PENTAHO BI SERVER! \n"			
						+"\n MAKE A BACKUP OF YOUR BI SERVER BEFORE No warranty is given.\n"	
		);
		txtExplicacion.setEditable(false);

		// Pentaho Solutions Dir
		lblPthSol = new JLabel(" Input the pentaho-solutions path: ");
		btnPthSol = new JButton("Find Pentaho Solutions:");
		btnPthSol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					pentahoSolutionsDir = buscarArchivo(pentahoSolutionsDir);
					System.out.println("Pentaho Solutions Path:"
							+ pentahoSolutionsDir);
					if (! pentahoSolutionsDir.contains("pentaho-solutions")) {
						JOptionPane.showMessageDialog(null,
								"You must point to pentaho-solutions dir",
								"CToolsInstaller", 1);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		// Tomcat Dir
		lblTomcat = new JLabel(" Input the pentaho tomcat path: ");
		btnTomcat = new JButton("Find Tomcat:");
		btnTomcat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					tomcatDir = buscarArchivo(tomcatDir);
					System.out.println("Pentaho Tomcat Path:" + tomcatDir);
					if (! tomcatDir.contains("tomcat")) {
  						JOptionPane.showMessageDialog(null,
								"You must point to pentaho TONCAT dir",
								"CToolsInstaller", 1);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		// COMPONENTS

		CDF = new JCheckBox("Update CDF", null, true);
		CDA = new JCheckBox("Update CDA", null, true);
		CDE = new JCheckBox("Update CDE", null, true);
		CGG = new JCheckBox("Update CGG", null, true);
		saikuStable = new JCheckBox("Update Saiku to 2.3", null, true);
		saikuStable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (saikuStable.isSelected()) {
					saikuDev.setSelected(false);
				}
			}
		});
		saikuDev = new JCheckBox("Update Saiku to last developement", null,
				false);
		saikuDev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (saikuDev.isSelected()) {
					saikuStable.setSelected(false);

				}
			}
		});

		// Update button
		lblUpdate = new JLabel("Update CTOOLS ");
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			// Update ---------------------------------------------------
			public void actionPerformed(ActionEvent e) {
				if (pentahoSolutionsDir.contains("pentaho-solutions")) {
					System.out.println("pentaho solutions dir"
							+ pentahoSolutionsDir);
					File tmp = new File( pentahoSolutionsDir + "/tmp/");

       
				        
					
					System.out.println("Deleting tmp folder under pertaho-solutions");
					new DeleteDir().deleteDirectory(tmp);
			
					tmp.mkdir();

					if (CGG.isSelected()) {

						if (tomcatDir.contains("tomcat")) {
							Descargador d = new Descargador();
							if ( d.update(pentahoSolutionsDir, 
									tomcatDir, 
									CDF.isSelected(),
									CDA.isSelected(),
									CDE.isSelected(),
									CGG.isSelected(),
									saikuDev.isSelected(),
									saikuStable.isSelected())
								){							
							JOptionPane.showMessageDialog(null,
									"Update Done. \nThere is a backup of the original files under pentaho-solutions/tmp \nTHIS FOLDER WILL BE DELETED NEXT TIME THIS PROGRAM WILL BE EXECUTED",
									"CToolsInstaller", 1);
							}else{
								JOptionPane.showMessageDialog(null,
										"Unable to update.\nPENTAHO TOMCAT MUST BE DOWN TO UPDATE \nStop pentaho tomcat and try again",
										"CToolsInstaller", 1);
								
							}
						} else {
							JOptionPane.showMessageDialog(null,
									"You must point to Tomcat dir if you want to install also CGG\n Point  to Tomcat dir or uncheck CGG",
									"CToolsInstaller", 1);
						}
					} else {
						Descargador d = new Descargador();
						if ( d.update(pentahoSolutionsDir, 
								tomcatDir, 
								CDF.isSelected(),
								CDA.isSelected(),
								CDE.isSelected(),
								CGG.isSelected(),
								saikuDev.isSelected(),
								saikuStable.isSelected())
					      	){							
							JOptionPane.showMessageDialog(null,
									"Update Done. \nThere is a backup of the original files under pentaho-solutions/tmp \nTHIS FOLDER WILL BE DELETED NEXT TIME THIS PROGRAM WILL BE EXECUTED",
									"CToolsInstaller", 1);
							}else{
								JOptionPane.showMessageDialog(null,
										"Unable to update.\nPENTAHO TOMCAT MUST BE DOWN TO UPDATE \nStop pentaho tomcat and try again",
										"CToolsInstaller", 1);	
							}
						
						
					}

				} else {
					JOptionPane.showMessageDialog(null,
							"You must point to pentaho-solutions dir",
							"CToolsInstaller", 1);
				}

			}
		});

	}

	private void BuildWindow() {

		botones = new JPanel();
		botones.setLayout(new GridLayout(10, 2));
		// PTH Solutions
		botones.add(lblPthSol);
		botones.add(btnPthSol);
		// Tomcat Dir
		botones.add(lblTomcat);
		botones.add(btnTomcat);
		// Compoents
		botones.add(new JLabel());
		botones.add(CDF);
		botones.add(new JLabel());
		botones.add(CDA);
		botones.add(new JLabel());
		botones.add(CDE);
		botones.add(new JLabel());
		botones.add(CGG);
		botones.add(new JLabel());
		botones.add(saikuStable);
		botones.add(new JLabel());
		botones.add(saikuDev);

		// Update Button
		botones.add(lblUpdate);
		botones.add(btnUpdate);

		// Frame
		frame = new JFrame("Graphic CTOOLS Installer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout((LayoutManager) new BoxLayout(frame.getContentPane(),
				BoxLayout.Y_AXIS));

		frame.add(txtExplicacion);
		frame.add(botones);
		frame.pack();
		frame.setVisible(true);
	}

	public CToolsInstaller() {
		CreateComponents();
		BuildWindow();

	}

	public static void main(String g[]) {
		new CToolsInstaller();
	}

	private String buscarArchivo(String Path) throws IOException {
		File file = new File(Path);
		JFileChooser fileopen = new JFileChooser();
		fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int ret = fileopen.showDialog(null, "Open file");

		if (ret == JFileChooser.APPROVE_OPTION) {
			file = fileopen.getSelectedFile();
		}
		return file.getAbsolutePath();
	}

}
