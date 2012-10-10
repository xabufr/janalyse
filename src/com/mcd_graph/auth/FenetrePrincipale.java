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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import com.event.auth.QuitListener;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

import java.awt.Insets;

public class FenetrePrincipale {
	private McdGraph m_mcd;
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
		m_mcd = new McdGraph();
		frame.getContentPane().add(m_mcd);
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
		
		JButton boutonInsertionEntite = new JButton("");
		boutonInsertionEntite.setSize(new Dimension(32, 327));
		boutonInsertionEntite.setPreferredSize(new Dimension(32, 32));
		boutonInsertionEntite.setMinimumSize(new Dimension(32, 32));
		boutonInsertionEntite.setMaximumSize(new Dimension(32, 32));
		boutonInsertionEntite.setMargin(new Insets(0, 0, 0, 0));
		boutonInsertionEntite.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/objet.png")));
		boutonInsertionEntite.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				m_mcd.setState(McdGraphStateE.INSERT_ENTITE);
			}
		});
		toolBar.add(boutonInsertionEntite);
		
		JButton boutonInsertionRelation = new JButton("");
		boutonInsertionRelation.setMargin(new Insets(0, 0, 0, 0));
		boutonInsertionRelation.setMinimumSize(new Dimension(32, 32));
		boutonInsertionRelation.setMaximumSize(new Dimension(32, 32));
		boutonInsertionRelation.setPreferredSize(new Dimension(32, 32));
		boutonInsertionRelation.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/relation.png")));
		boutonInsertionRelation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				m_mcd.setState(McdGraphStateE.INSERT_RELATION);
			}
		});
		toolBar.add(boutonInsertionRelation);
		
		JButton boutonEdition = new JButton("");
		boutonEdition.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/edition.png")));
		boutonEdition.setPreferredSize(new Dimension(32, 32));
		boutonEdition.setMinimumSize(new Dimension(32, 32));
		boutonEdition.setMaximumSize(new Dimension(32, 32));
		boutonEdition.setMargin(new Insets(0, 0, 0, 0));
		boutonEdition.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				m_mcd.setState(McdGraphStateE.EDIT);
			}
		});
		toolBar.add(boutonEdition);
		
	}

	public void quitter() {
		frame.dispose();
	}

}
