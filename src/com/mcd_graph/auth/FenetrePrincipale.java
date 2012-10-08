package com.mcd_graph.auth;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Insets;

public class FenetrePrincipale {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetrePrincipale window = new FenetrePrincipale();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FenetrePrincipale() {
		initialize();
		frame.getContentPane().add(new McdGraph());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu("Fichier");
		menuBar.add(mnFichier);
		
		JMenuItem menuItem = new JMenuItem("Nouveau");
		mnFichier.add(menuItem);
		
		JMenuItem mntmOuvrir = new JMenuItem("Ouvrir");
		mnFichier.add(mntmOuvrir);
		
		JSeparator separator = new JSeparator();
		mnFichier.add(separator);
		
		JMenuItem mntmExporterEnPng = new JMenuItem("Exporter en PNG");
		mnFichier.add(mntmExporterEnPng);
		
		JSeparator separator_1 = new JSeparator();
		mnFichier.add(separator_1);
		
		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		mnFichier.add(mntmQuitter);
		
		JMenu mnEdition = new JMenu("Edition");
		menuBar.add(mnEdition);
		
		JMenuItem mntmCouper = new JMenuItem("Couper");
		mnEdition.add(mntmCouper);
		
		JMenuItem mntmCopier = new JMenuItem("Copier");
		mnEdition.add(mntmCopier);
		
		JMenuItem mntmColler = new JMenuItem("Coller");
		mnEdition.add(mntmColler);
		
		JMenu mnAide = new JMenu("Aide");
		menuBar.add(mnAide);
		
		JMenuItem mntmAPropos = new JMenuItem("A propos...");
		mnAide.add(mntmAPropos);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton button = new JButton("");
		button.setSize(new Dimension(32, 327));
		button.setPreferredSize(new Dimension(32, 32));
		button.setMinimumSize(new Dimension(32, 32));
		button.setMaximumSize(new Dimension(32, 32));
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/objet.png")));
		toolBar.add(button);
		
	}

	public void quitter() {
		frame.dispose();
	}

}
