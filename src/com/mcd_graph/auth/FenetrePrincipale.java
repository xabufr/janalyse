package com.mcd_graph.auth;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import com.event.auth.QuitListener;

import java.awt.Insets;
import java.util.ArrayList;
import java.awt.event.InputEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class FenetrePrincipale {
	private McdGraph m_mcd;
	private JFrame frame;
	private JButton m_boutonInsertionEntite;
	private JButton m_boutonInsertionRelation;
	private JButton m_boutonDeplacer;
	private JButton m_boutonInsertionLien;
	private JButton m_boutonInsertionContrainte;
	private JButton m_boutonInsertionHeritage;
	private ArrayList<JButton> m_stateButtons;
	private JButton m_boutonEdition;
	private final JTabbedPane m_mcdContener = new JTabbedPane();
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
		m_stateButtons = new ArrayList<JButton>();
		initialize();
		m_mcdContener.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				m_mcd = (McdGraph) m_mcdContener.getSelectedComponent();
				/*On remmet les boutons de mode d'édition en place en fonction du nouveau MCD*/
				switch(m_mcd.getState()){
				case EDIT:
					setEnabledButton(m_boutonEdition);
					break;
				case INSERT_CONTRAINTE:
					setEnabledButton(m_boutonInsertionContrainte);
					break;
				case INVALID:
					m_mcd.setState(McdGraphStateE.INSERT_ENTITE);
				case INSERT_ENTITE:
					setEnabledButton(m_boutonInsertionEntite);
					break;
				case INSERT_HERITAGE:
					setEnabledButton(m_boutonInsertionHeritage);
					break;
				case INSERT_LIEN:
					setEnabledButton(m_boutonInsertionLien);
					break;
				case INSERT_RELATION:
					setEnabledButton(m_boutonInsertionRelation);
					break;
				case MOVE:
					setEnabledButton(m_boutonDeplacer);
					break;
				}
			}
		});
	}
	private void setEnabledButton(JButton b){
		for(JButton button : m_stateButtons){
			button.setEnabled(button!=b);
		}
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
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewMcd();
			}
		});
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
		
		JSeparator separator_2 = new JSeparator();
		mnEdition.add(separator_2);
		
		JMenuItem mntmPrfrences = new JMenuItem("Préférences");
		mntmPrfrences.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mntmPrfrences.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FenetrePreferences(FenetrePrincipale.this).setVisible(true);
			}
		});
		mnEdition.add(mntmPrfrences);
		
		JMenu mnAide = new JMenu("Aide");
		mnAide.setMnemonic('A');
		menuBar.add(mnAide);
		
		JMenuItem mntmAPropos = new JMenuItem("A propos...");
		mnAide.add(mntmAPropos);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		m_boutonInsertionEntite = new JButton("");
		m_boutonInsertionEntite.setSize(new Dimension(32, 327));
		m_boutonInsertionEntite.setPreferredSize(new Dimension(32, 32));
		m_boutonInsertionEntite.setMinimumSize(new Dimension(32, 32));
		m_boutonInsertionEntite.setMaximumSize(new Dimension(32, 32));
		m_boutonInsertionEntite.setMargin(new Insets(0, 0, 0, 0));
		m_boutonInsertionEntite.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/objet.png")));
		m_stateButtons.add(m_boutonInsertionEntite);
		toolBar.add(m_boutonInsertionEntite);
		
		m_boutonInsertionRelation = new JButton("");
		m_boutonInsertionRelation.setMargin(new Insets(0, 0, 0, 0));
		m_boutonInsertionRelation.setMinimumSize(new Dimension(32, 32));
		m_boutonInsertionRelation.setMaximumSize(new Dimension(32, 32));
		m_boutonInsertionRelation.setPreferredSize(new Dimension(32, 32));
		m_boutonInsertionRelation.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/relation.png")));
		m_stateButtons.add(m_boutonInsertionRelation);
		toolBar.add(m_boutonInsertionRelation);
		
		m_boutonDeplacer = new JButton("");
		m_boutonDeplacer.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/edition.png")));
		m_boutonDeplacer.setPreferredSize(new Dimension(32, 32));
		m_boutonDeplacer.setMinimumSize(new Dimension(32, 32));
		m_boutonDeplacer.setMaximumSize(new Dimension(32, 32));
		m_boutonDeplacer.setMargin(new Insets(0, 0, 0, 0));
		m_stateButtons.add(m_boutonDeplacer);
		toolBar.add(m_boutonDeplacer);
		
		m_boutonInsertionLien = new JButton("");
		m_boutonInsertionLien.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/lien.png")));
		m_boutonInsertionLien.setPreferredSize(new Dimension(32, 32));
		m_boutonInsertionLien.setMinimumSize(new Dimension(32, 32));
		m_boutonInsertionLien.setMaximumSize(new Dimension(32, 32));
		m_boutonInsertionLien.setMargin(new Insets(0, 0, 0, 0));
		m_stateButtons.add(m_boutonInsertionLien);
		toolBar.add(m_boutonInsertionLien);
		
		m_boutonInsertionContrainte = new JButton("");
		m_boutonInsertionContrainte.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/contrainte.png")));
		m_boutonInsertionContrainte.setPreferredSize(new Dimension(32, 32));
		m_boutonInsertionContrainte.setMinimumSize(new Dimension(32, 32));
		m_boutonInsertionContrainte.setMaximumSize(new Dimension(32, 32));
		m_boutonInsertionContrainte.setMargin(new Insets(0, 0, 0, 0));
		m_stateButtons.add(m_boutonInsertionContrainte);
		toolBar.add(m_boutonInsertionContrainte);
		
		m_boutonInsertionHeritage = new JButton("");
		m_boutonInsertionHeritage.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/heritage.png")));
		m_boutonInsertionHeritage.setPreferredSize(new Dimension(32, 32));
		m_boutonInsertionHeritage.setMinimumSize(new Dimension(32, 32));
		m_boutonInsertionHeritage.setMaximumSize(new Dimension(32, 32));
		m_boutonInsertionHeritage.setMargin(new Insets(0, 0, 0, 0));
		m_stateButtons.add(m_boutonInsertionHeritage);
		toolBar.add(m_boutonInsertionHeritage);
		
		m_boutonEdition = new JButton("");
		m_boutonEdition.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/edit.png")));
		m_boutonEdition.setSize(new Dimension(32, 327));
		m_boutonEdition.setPreferredSize(new Dimension(32, 32));
		m_boutonEdition.setMinimumSize(new Dimension(32, 32));
		m_boutonEdition.setMaximumSize(new Dimension(32, 32));
		m_boutonEdition.setMargin(new Insets(0, 0, 0, 0));
		m_stateButtons.add(m_boutonEdition);
		toolBar.add(m_boutonEdition);
		
		
		m_boutonInsertionEntite.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd==null)
					return;
				m_mcd.setState(McdGraphStateE.INSERT_ENTITE);
				setEnabledButton(m_boutonInsertionEntite);
			}
		});
		m_boutonInsertionRelation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd==null)
					return;
				m_mcd.setState(McdGraphStateE.INSERT_RELATION);
				setEnabledButton(m_boutonInsertionRelation);
			}
		});
		m_boutonInsertionContrainte.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd==null)
					return;
				m_mcd.setState(McdGraphStateE.INSERT_CONTRAINTE);
				setEnabledButton(m_boutonInsertionContrainte);
			}
		});
		m_boutonInsertionHeritage.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd==null)
					return;
				m_mcd.setState(McdGraphStateE.INSERT_HERITAGE);
				setEnabledButton(m_boutonInsertionHeritage);
			}
		});
		m_boutonInsertionLien.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent arg0) {
			if(m_mcd==null)
				return;
			m_mcd.setState(McdGraphStateE.INSERT_LIEN);
			setEnabledButton(m_boutonInsertionLien);
		}
		});
		m_boutonDeplacer.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd==null)
					return;
				m_mcd.setState(McdGraphStateE.MOVE);
				setEnabledButton(m_boutonDeplacer);
			}
		});
		m_boutonEdition.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd==null)
					return;
				m_mcd.setState(McdGraphStateE.EDIT);
				setEnabledButton(m_boutonEdition);
			}
		});
		setEnabledButton(m_boutonDeplacer);
		
		frame.getContentPane().add(m_mcdContener);
	}

	public void quitter() {
		frame.dispose();
	}
	private void createNewMcd(){
		McdGraph mcd = new McdGraph();
		mcd.setState(McdGraphStateE.INSERT_ENTITE);
		m_mcdContener.addTab("Nouveau MCD*", mcd);
	}

}
