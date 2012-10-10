package com.mcd_graph.auth;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import com.event.auth.QuitListener;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

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
		mnFichier.setMnemonic('F');
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
		mntmQuitter.addActionListener(new QuitListener(this));
		mntmQuitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
		mnFichier.add(mntmQuitter);
		
		JMenu mnEdition = new JMenu("Edition");
		mnEdition.setMnemonic('E');
		menuBar.add(mnEdition);
		
		JMenuItem mntmCouper = new JMenuItem("Couper");
		mntmCouper.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
		mnEdition.add(mntmCouper);
		
		JMenuItem mntmCopier = new JMenuItem("Copier");
		mntmCopier.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
		mnEdition.add(mntmCopier);
		
		JMenuItem mntmColler = new JMenuItem("Coller");
		mntmColler.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
		mnEdition.add(mntmColler);
		
		JMenu mnAide = new JMenu("Aide");
		mnAide.setMnemonic('A');
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
		
		JButton button_1 = new JButton("");
		button_1.setMargin(new Insets(0, 0, 0, 0));
		button_1.setMinimumSize(new Dimension(32, 32));
		button_1.setMaximumSize(new Dimension(32, 32));
		button_1.setPreferredSize(new Dimension(32, 32));
		button_1.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/relation.png")));
		toolBar.add(button_1);
		
	}

	public void quitter() {
		frame.dispose();
	}

}
