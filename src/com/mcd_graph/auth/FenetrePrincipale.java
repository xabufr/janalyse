package com.mcd_graph.auth;

import java.awt.Event;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.dico.auth.DicoPanel;
import com.export.auth.ExportSql;
import com.export.auth.ExportPng;
import com.export.auth.ExporterHTML;
import com.export.auth.ParserSql;
import com.mcd_composent_graph.auth.ContrainteGraph;
import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.HeritageGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.RelationGraph;
import com.mld.auth.MLDPanel;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;
import com.sauvegarde_chargement.auth.Chargement;
import com.ui_help.auth.APropos;
import com.ui_help.auth.HelpDialog;
import com.version.auth.FenetreMiseAJour;
import com.version.auth.Updater;

import java.awt.Insets;
import java.util.ArrayList;
import java.awt.event.InputEvent;
import java.io.File;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSlider;
import java.awt.Component;
import javax.swing.Box;

public class FenetrePrincipale {
	private McdGraph m_mcd;
	private JFrame frame;
	private JButton m_boutonInsertionEntite;
	private JButton m_boutonInsertionRelation;
	private JButton m_boutonInsertionLien;
	private JButton m_boutonInsertionContrainte;
	private JButton m_boutonInsertionHeritage;
	private ArrayList<JButton> m_stateButtons;
	private JButton m_boutonEdition;
	private final JTabbedPane m_mcdContener = new JTabbedPane();
	private JMenuItem m_mntmAnnuler;
	private JMenuItem m_mntmRefaire;
	private JSplitPane m_splitPane;
	private JButton m_btnMcd;
	private JButton m_btnMld;
	private JButton m_btnDico;
	private JButton m_boutonInsertionCommentaire;
	private JSlider m_zoom;
	
	public FenetrePrincipale() {
		m_stateButtons = new ArrayList<JButton>();
		initialize();
		m_splitPane.setRightComponent(m_mcdContener);
		
		JPanel panel = new JPanel();
		m_splitPane.setLeftComponent(panel);
		panel.setLayout(new MigLayout("", "[]", "[][]"));
		
		m_btnMcd = new JButton("MCD");
		m_btnMcd.setEnabled(false);
		m_btnMcd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_btnMcd.setEnabled(false);
				m_btnMld.setEnabled(true);
				m_btnDico.setEnabled(true);
				m_splitPane.setRightComponent(m_mcdContener);
			}
		});
		panel.add(m_btnMcd, "cell 0 0");
		
		m_btnMld = new JButton("MLD");
		m_btnMld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_btnMcd.setEnabled(true);
				m_btnMld.setEnabled(false);
				m_btnDico.setEnabled(true);
				if(m_mcd != null)
					m_splitPane.setRightComponent(new MLDPanel(m_mcd));
				else
					m_splitPane.setRightComponent(new JLabel("Aucun MCD selectionné"));
			}
		});
		panel.add(m_btnMld, "cell 0 1");
		
		m_btnDico = new JButton("Dictionnaire des propriétés");
		m_btnDico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_btnMcd.setEnabled(true);
				m_btnMld.setEnabled(true);
				m_btnDico.setEnabled(false);
				if(m_mcd != null)
					m_splitPane.setRightComponent(new DicoPanel(m_mcd));
				else
					m_splitPane.setRightComponent(new JLabel("Aucun MCD selectionné"));
			}
		});
		panel.add(m_btnDico, "cell 0 2");
		
		m_mcdContener.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				if(m_mcdContener.getSelectedComponent()==null)
					return;
				m_mcd = (McdGraph) ((JScrollPane)m_mcdContener.getSelectedComponent()).getViewport().getView();
				if(m_mcd==null)
					return;
				/*On remmet les boutons de mode d'édition en place en fonction du nouveau MCD*/
				switch(m_mcd.getState()){
				case INVALID:
					m_mcd.setState(McdGraphStateE.EDIT);
				case EDIT:
					setEnabledButton(m_boutonEdition);
					break;
				case INSERT_CONTRAINTE:
					setEnabledButton(m_boutonInsertionContrainte);
					break;
				case INSERT_ENTITE:
					setEnabledButton(m_boutonInsertionEntite);
					break;
				case INSERT_HERITAGE:
					setEnabledButton(m_boutonInsertionHeritage);
					break;
				case INSERT_LIEN:
					setEnabledButton(m_boutonInsertionLien);
					break;
				case INSERT_COMMENTAIRE:
					setEnabledButton(m_boutonInsertionCommentaire);
				case INSERT_RELATION:
					setEnabledButton(m_boutonInsertionRelation);
					break;
				default:
					break;
				}
				m_zoom.setValue((int) (m_mcd.getZoom()*100));
			}
		});
		frame.setVisible(true);
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
		frame.setTitle("JAnalyse");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFichier = new JMenu("Fichier");
		mnFichier.setMnemonic('F');
		menuBar.add(mnFichier);
		
		JMenuItem menuItem = new JMenuItem("Nouveau");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewMcd();
				m_mcdContener.setSelectedIndex(m_mcdContener.getComponents().length-1);
			}
		});
		mnFichier.add(menuItem);
		
		JMenuItem mntmOuvrir = new JMenuItem("Ouvrir");
		mntmOuvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOuvrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				McdGraph newMcd = new McdGraph(FenetrePrincipale.this);
				if(Chargement.charger(newMcd))
				{
					createNewMcd(newMcd);
					m_mcdContener.setSelectedIndex(m_mcdContener.getComponents().length-1);
					updateMcdNames();
				}
			}
		});
		mnFichier.add(mntmOuvrir);
		
		JMenuItem mntmFermer = new JMenuItem("Fermer");
		mntmFermer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fermerMcd(m_mcd);
			}
		});
		mntmFermer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		mnFichier.add(mntmFermer);
		
		JSeparator separator = new JSeparator();
		mnFichier.add(separator);
		
		JMenuItem mntmSauvegarder = new JMenuItem("Enregistrer");
		mntmSauvegarder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		mntmSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (m_mcd!=null){
					m_mcd.saveMcdComposent(false);
					updateMcdNames();
				}
			}
		});
		mnFichier.add(mntmSauvegarder);
		
		JMenuItem mntmExporterEnPng = new JMenuItem("Exporter en PNG");
		mntmExporterEnPng.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mntmExporterEnPng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_mcd==null)
					return;
				ExportPng.ExporterMcd(m_mcd);
			}
		});
		
		JMenuItem mntmEnregistrerSous = new JMenuItem("Enregistrer sous...");
		mntmEnregistrerSous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (m_mcd!=null){
					m_mcd.saveMcdComposent(true);
					updateMcdNames();
				}
			}
		});
		mnFichier.add(mntmEnregistrerSous);
		
		JSeparator separator_6 = new JSeparator();
		mnFichier.add(separator_6);
		mnFichier.add(mntmExporterEnPng);
		
		JMenuItem mntmExporterEnHtml = new JMenuItem("Exporter en HTML");
		mntmExporterEnHtml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd != null)
					new ExporterHTML(m_mcd).setVisible(true);
			}
		});
		mntmExporterEnHtml.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		mnFichier.add(mntmExporterEnHtml);

		
		JMenuItem mntmExporterEnSql = new JMenuItem("Exporter en SQL");
		mntmExporterEnSql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd!=null)
					new ExportSql(m_mcd.getName(), m_mcd).save();
			}
		});
		mnFichier.add(mntmExporterEnSql);
		
		JSeparator separator_7 = new JSeparator();
		mnFichier.add(separator_7);
		
		JMenuItem mntmImportationSql = new JMenuItem("Importation SQL");
		mntmImportationSql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ParserSql.parse();
				createNewMcd();
				m_mcdContener.setSelectedIndex(m_mcdContener.getComponents().length-1);
				m_mcd.importSql(ParserSql.getEntites());
				updateMcdNames();
			}
		});
		mnFichier.add(mntmImportationSql);
		
		JSeparator separator_1 = new JSeparator();
		mnFichier.add(separator_1);
		
		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.addActionListener(new ActionListener() {
	
			public void actionPerformed(ActionEvent arg0) {
				quitter();
			}
		});
		mntmQuitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK));
		mnFichier.add(mntmQuitter);
		
		JMenu mnEdition = new JMenu("Edition");
		mnEdition.setMnemonic('E');
		menuBar.add(mnEdition);
		
		JMenuItem mntmCopier = new JMenuItem("Copier");
		mntmCopier.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK));
		mntmCopier.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if (m_mcd != null)
					m_mcd.copyMcdComposent();
			}
		});
		mnEdition.add(mntmCopier);
		
		JMenuItem mntmColler = new JMenuItem("Coller");
		mntmColler.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
		mntmColler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (m_mcd != null)
					try {
						m_mcd.pastMcdComposent();
					} catch (CloneNotSupportedException e1) {
						e1.printStackTrace();
					}
			}
		});
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
		
		m_mntmAnnuler = new JMenuItem("Annuler");
		m_mntmAnnuler.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		m_mntmAnnuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd != null){
					m_mcd.annuler();
					updateMcdUi(m_mcd);
				}
			}
		});
		mnEdition.add(m_mntmAnnuler);
		
		m_mntmRefaire = new JMenuItem("Refaire");
		m_mntmRefaire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd != null){
					m_mcd.refaire();
					updateMcdUi(m_mcd);
				}
			}
		});
		m_mntmRefaire.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnEdition.add(m_mntmRefaire);
		
		JMenuItem mntmZoomer = new JMenuItem("Zoomer");
		mntmZoomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd!=null){
					m_mcd.zoomer();
					m_zoom.setValue((int) (m_mcd.getZoom()*100));
				}
			}
		});
		
		JSeparator separator_8 = new JSeparator();
		mnEdition.add(separator_8);
		mntmZoomer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_MASK));
		mnEdition.add(mntmZoomer);
		
		JMenuItem mntmDzoomer = new JMenuItem("Dézoomer");
		mntmDzoomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_mcd!=null){
					m_mcd.dezoomer();
					m_zoom.setValue((int) (m_mcd.getZoom()*100));
				}
			}
		});
		mntmDzoomer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_MASK));
		mnEdition.add(mntmDzoomer);
		
		JSeparator separator_3 = new JSeparator();
		mnEdition.add(separator_3);
		
		JMenuItem mntmRorganiser = new JMenuItem("Réorganiser");
		mntmRorganiser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmRorganiser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd!=null)
					m_mcd.reorganiser();
			}
		});
		
		JMenu mnMode = new JMenu("Mode");
		mnEdition.add(mnMode);
		
		JMenuItem mntmInsertionEntit = new JMenuItem("Insertion entité");
		mntmInsertionEntit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD1, 0));
		mntmInsertionEntit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_boutonInsertionEntite.doClick();
			}
		});
		mnMode.add(mntmInsertionEntit);
		
		JMenuItem mntmInsertionRelation = new JMenuItem("Insertion relation");
		mntmInsertionRelation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD2, 0));
		mntmInsertionRelation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_boutonInsertionRelation.doClick();
			}
		});
		mnMode.add(mntmInsertionRelation);
		
		JMenuItem mntmCrationLien = new JMenuItem("Création lien");
		mntmCrationLien.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0));
		mntmCrationLien.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_boutonInsertionLien.doClick();
			}
		});
		mnMode.add(mntmCrationLien);
		
		JMenuItem mntmInsertionContrainte = new JMenuItem("Insertion contrainte");
		mntmInsertionContrainte.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD4, 0));
		mntmInsertionContrainte.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_boutonInsertionContrainte.doClick();
			}
		});
		mnMode.add(mntmInsertionContrainte);
		
		JMenuItem mntmInsertionHritage = new JMenuItem("Insertion héritage");
		mntmInsertionHritage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD5, 0));
		mntmInsertionHritage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_boutonInsertionHeritage.doClick();
			}
		});
		mnMode.add(mntmInsertionHritage);
		
		JMenuItem mntmdition = new JMenuItem("Édition");
		mntmdition.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD7, 0));
		mntmdition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_boutonEdition.doClick();
			}
		});
		
		JMenuItem mntmInsertionCommentaire = new JMenuItem("Insertion commentaire");
		mntmInsertionCommentaire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_boutonInsertionCommentaire.doClick();
			}
		});
		mntmInsertionCommentaire.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD6, 0));
		mnMode.add(mntmInsertionCommentaire);
		mnMode.add(mntmdition);
		mnEdition.add(mntmRorganiser);
		
		JMenuItem mntmStatsMcd = new JMenuItem("Stats MCD");
		mntmStatsMcd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd==null) return;
				int nbRel=0, nbEnt=0, nbHer=0, nbCont=0;
				for(McdComposentGraphique c : m_mcd.getMcdComponents()){
					if(c instanceof RelationGraph)
						++nbRel;
					else if(c instanceof HeritageGraph)
						++nbHer;
					else if(c instanceof EntiteGraph)
						++nbEnt;
					else if(c instanceof ContrainteGraph)
						++nbCont;
				}
				JOptionPane.showMessageDialog(frame, "Le MCD '"+m_mcd.getLogicName()+"' contient:\n"+
						nbEnt+" entités, \n"+
						nbRel+" relations, \n"+
						nbHer+" héritages, \n"+
						nbCont+" contraintes.");
			}
		});
		mnEdition.add(mntmStatsMcd);
		
		JSeparator separator_4 = new JSeparator();
		mnEdition.add(separator_4);
		mnEdition.add(mntmPrfrences);
		
		JMenu mnAide = new JMenu("Aide");
		mnAide.setMnemonic('A');
		menuBar.add(mnAide);
		
		JMenuItem mntmAPropos = new JMenuItem("A propos...");
		mntmAPropos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mntmAPropos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new APropos().setVisible(true);
			}
		});
		mnAide.add(mntmAPropos);
		
		JMenuItem mntmManuel = new JMenuItem("Manuel");
		mntmManuel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new HelpDialog().show(arg0);
			}
		});
		mnAide.add(mntmManuel);
		
		JSeparator separator_5 = new JSeparator();
		mnAide.add(separator_5);
		
		JMenuItem mntmMettreJours = new JMenuItem("Mettre à jours");
		mntmMettreJours.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(Updater.hasNewVersion()){
					//String fichier = null;
					if(JOptionPane.OK_OPTION!=
							JOptionPane.showConfirmDialog(null, 
									"Nouvelle version disponnible, mettre à jours ?"))
						return;
					
					/*if((fichier=Updater.downloadUpdate())!=null)
						Updater.restart(fichier);*/
					new FenetreMiseAJour().setVisible(true);
				}
				else{
					JOptionPane.showMessageDialog(FenetrePrincipale.this.frame, "Aucune mise à jours disponible");
				}
			}
		});
		mnAide.add(mntmMettreJours);
		
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
		m_boutonInsertionEntite.setToolTipText("Insertion d'entité");
		m_stateButtons.add(m_boutonInsertionEntite);
		toolBar.add(m_boutonInsertionEntite);
		
		m_boutonInsertionRelation = new JButton("");
		m_boutonInsertionRelation.setMargin(new Insets(0, 0, 0, 0));
		m_boutonInsertionRelation.setMinimumSize(new Dimension(32, 32));
		m_boutonInsertionRelation.setMaximumSize(new Dimension(32, 32));
		m_boutonInsertionRelation.setPreferredSize(new Dimension(32, 32));
		m_boutonInsertionRelation.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/relation.png")));
		m_boutonInsertionRelation.setToolTipText("Insertion de relation");
		m_stateButtons.add(m_boutonInsertionRelation);
		toolBar.add(m_boutonInsertionRelation);
		
		m_boutonInsertionLien = new JButton("");
		m_boutonInsertionLien.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/lien.png")));
		m_boutonInsertionLien.setPreferredSize(new Dimension(32, 32));
		m_boutonInsertionLien.setMinimumSize(new Dimension(32, 32));
		m_boutonInsertionLien.setMaximumSize(new Dimension(32, 32));
		m_boutonInsertionLien.setMargin(new Insets(0, 0, 0, 0));
		m_boutonInsertionLien.setToolTipText("Insertion de lien");
		m_stateButtons.add(m_boutonInsertionLien);
		toolBar.add(m_boutonInsertionLien);
		
		m_boutonInsertionContrainte = new JButton("");
		m_boutonInsertionContrainte.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/contrainte.png")));
		m_boutonInsertionContrainte.setPreferredSize(new Dimension(32, 32));
		m_boutonInsertionContrainte.setMinimumSize(new Dimension(32, 32));
		m_boutonInsertionContrainte.setMaximumSize(new Dimension(32, 32));
		m_boutonInsertionContrainte.setMargin(new Insets(0, 0, 0, 0));
		m_boutonInsertionContrainte.setToolTipText("Insertion de contrainte");
		m_stateButtons.add(m_boutonInsertionContrainte);
		toolBar.add(m_boutonInsertionContrainte);
		
		m_boutonInsertionHeritage = new JButton("");
		m_boutonInsertionHeritage.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/heritage.png")));
		m_boutonInsertionHeritage.setPreferredSize(new Dimension(32, 32));
		m_boutonInsertionHeritage.setMinimumSize(new Dimension(32, 32));
		m_boutonInsertionHeritage.setMaximumSize(new Dimension(32, 32));
		m_boutonInsertionHeritage.setMargin(new Insets(0, 0, 0, 0));
		m_boutonInsertionHeritage.setToolTipText("Insertion d'héritage");
		m_stateButtons.add(m_boutonInsertionHeritage);
		toolBar.add(m_boutonInsertionHeritage);
		
		m_boutonInsertionCommentaire = new JButton("");
		m_boutonInsertionCommentaire.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/commentaire.png")));
		m_boutonInsertionCommentaire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		m_boutonInsertionCommentaire.setToolTipText("Insertion de commentaire");
		m_boutonInsertionCommentaire.setPreferredSize(new Dimension(32, 32));
		m_boutonInsertionCommentaire.setMinimumSize(new Dimension(32, 32));
		m_boutonInsertionCommentaire.setMaximumSize(new Dimension(32, 32));
		m_boutonInsertionCommentaire.setMargin(new Insets(0, 0, 0, 0));
		m_stateButtons.add(m_boutonInsertionCommentaire);
		toolBar.add(m_boutonInsertionCommentaire);
		
		m_boutonEdition = new JButton("");
		m_boutonEdition.setIcon(new ImageIcon(FenetrePrincipale.class.getResource("/ressources/edit.png")));
		m_boutonEdition.setSize(new Dimension(32, 327));
		m_boutonEdition.setPreferredSize(new Dimension(32, 32));
		m_boutonEdition.setMinimumSize(new Dimension(32, 32));
		m_boutonEdition.setMaximumSize(new Dimension(32, 32));
		m_boutonEdition.setMargin(new Insets(0, 0, 0, 0));
		m_boutonEdition.setToolTipText("Mode d'édition");
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
		m_boutonEdition.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd==null)
					return;
				m_mcd.setState(McdGraphStateE.EDIT);
				setEnabledButton(m_boutonEdition);
			}
		});
		m_boutonInsertionCommentaire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd==null)return;
				m_mcd.setState(McdGraphStateE.INSERT_COMMENTAIRE);
				setEnabledButton(m_boutonInsertionCommentaire);
			}
		});
		setEnabledButton(m_boutonInsertionEntite);
		
		m_zoom = new JSlider();
		m_zoom.setValue(100);
		m_zoom.setMaximumSize(new Dimension(32, 50));
		m_zoom.setPaintLabels(true);
		m_zoom.setMajorTickSpacing(25);
		m_zoom.addChangeListener(new ChangeListener() {			
			public void stateChanged(ChangeEvent arg0) {
				if(m_mcd != null){
					m_mcd.setZoom(m_zoom.getValue()/100.0);
				}
			}
		});
		toolBar.add(m_zoom);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);
		
		m_splitPane = new JSplitPane();
		frame.getContentPane().add(m_splitPane, BorderLayout.CENTER);
		
		m_mcdContener.addMouseListener(new MouseListener() {
			private final menuMcdOptions menu = new menuMcdOptions();
			public void mouseReleased(MouseEvent arg0) {
			}
			public void mousePressed(MouseEvent arg0) {
				if(arg0.getButton()==MouseEvent.BUTTON3)
					menu.show(arg0.getPoint());
			}
			public void mouseExited(MouseEvent arg0) {				
			}
			public void mouseEntered(MouseEvent arg0) {				
			}
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		frame.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent arg0) {
				McdPreferencesManager prefs = McdPreferencesManager.getInstance();
				@SuppressWarnings("unchecked")
				ArrayList<String> lst = (ArrayList<String>)prefs.get(PGroupe.ETAT, PCle.SAVE);
				if (lst == null)
					return;
				for (String s : lst){
					File f = new File(s);
					
					if (!f.exists())
						continue;
					
					createNewMcd();
					getTabs().setSelectedIndex(getTabs().getComponents().length-1);
					
					Chargement.charger(getMcd(), f);
				}
				updateMcdNames();
			}
			public void windowIconified(WindowEvent arg0) {}
			
			public void windowDeiconified(WindowEvent arg0) {}
			
			public void windowDeactivated(WindowEvent arg0) {}
			
			public void windowClosing(WindowEvent arg0) {
				quitter();
				
			}
			
			public void windowClosed(WindowEvent arg0) {}
			
			public void windowActivated(WindowEvent arg0) {}
		});
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addMouseWheelListener(new MouseWheelListener() {			
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				System.out.println("wheel");
				if(m_mcd==null)
					return;
				if((arg0.getModifiers()&Event.CTRL_MASK)!=0)
				{
					float zoomModifier = (float) (arg0.getWheelRotation()*0.1);
					m_mcd.setZoom(m_mcd.getZoom()+zoomModifier);
					m_zoom.setValue((int) (m_mcd.getZoom()*100));
					m_mcd.repaint();
				}
			}
		});
	}
	public McdGraph getMcd(){
		return m_mcd;
	}
	public JTabbedPane getTabs(){
		return m_mcdContener;
	}
	public void quitter() {
		ArrayList<McdGraph> mcds = new ArrayList<McdGraph>();
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		ArrayList<String> lst = new ArrayList<String>();
		int nb = m_mcdContener.getTabCount();
		for(int i=0;i<nb;++i){
			mcds.add((McdGraph) ((JScrollPane)m_mcdContener.getComponentAt(i)).getViewport().getView());
			if(((McdGraph) ((JScrollPane)m_mcdContener.getComponentAt(i)).getViewport().getView()).getFile()!=null){
				String s = ((McdGraph) ((JScrollPane)m_mcdContener.getComponentAt(i)).getViewport().getView()).getFile().getAbsolutePath();
				lst.add(s);
			}
		}
		
		prefs.set(PGroupe.ETAT, PCle.SAVE, lst);
		prefs.save();
		
		for(McdGraph mcd : mcds){
			if(!fermerMcd(mcd)){
				return;
			}
		}
		
		frame.dispose();
		System.exit(0);
	}
	public void createNewMcd(){
		McdGraph mcd = new McdGraph(this);
		mcd.setName("Nouveau MCD");
		createNewMcd(mcd);
	}
	public void createNewMcd(McdGraph mcd){
		JScrollPane scroll = new JScrollPane(mcd);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		m_mcdContener.addTab("", scroll);
		updateMcdNames();
	}
	public void updateMcdNames(){
		int nb = m_mcdContener.getTabCount();
		for(int i=0;i<nb;++i){
			m_mcdContener.setTitleAt(i, 
					((JScrollPane)m_mcdContener.
					getComponentAt(i)).
					getViewport().
					getView().
					getName());
		}
	}
	@SuppressWarnings("serial")
	private class menuMcdOptions extends JPopupMenu{
		public menuMcdOptions() {
			JMenuItem itemRenommer = new JMenuItem("Renommer");
			JMenuItem itemFermer = new JMenuItem("Fermer");
			itemRenommer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(m_mcd==null)
						return;
					 String newName = (String) JOptionPane.showInputDialog(null, 
							 "Entrez le nouveau nom pour le MCD '"+m_mcd.getName()+"'", 
							 "Renommer un MCD", 
							 JOptionPane.PLAIN_MESSAGE, 
							 null, null, 
							 m_mcd.getLogicName());
					if(newName!=null&&!newName.trim().isEmpty()){
						m_mcd.setName(newName);
						updateMcdNames();
					}
				}
			});
			itemFermer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fermerMcd(m_mcd);
				}
			});
			add(itemRenommer);
			add(itemFermer);
		}
		public void show(Point p){
			if(m_mcd==null)
				return;
			super.show(m_mcdContener, p.x, p.y);
		}
	}
	private Boolean fermerMcd(McdGraph mcd){
		if(mcd==null)
			return false;
		if(!mcd.isSaved()){
			int sauv = JOptionPane.showConfirmDialog(null,"MCD non sauvegardé, voulez-vous sauver ?");
			if(sauv==JOptionPane.CANCEL_OPTION)
				return false;
			
			if(mcd==m_mcd){
				m_mcd = null;
				if(m_mcdContener.getTabCount()!=0){
					m_mcdContener.setSelectedIndex(0);
					m_mcd = (McdGraph) ((JScrollPane)m_mcdContener.getSelectedComponent()).getViewport().getView();
				}
			}
			if(sauv==JOptionPane.OK_OPTION){
				mcd.saveMcdComposent(false);
			}
		}
		int index = -1;
		int nb = m_mcdContener.getTabCount();
		for(int i=0;i<nb;++i){
			if(((JScrollPane)m_mcdContener.getComponentAt(i)).getViewport().getView()==mcd){
				index=i;
				break;
			}
		}
		if(index==-1)
			return true;
		m_mcdContener.remove(index);
		return true;
	}
	public void updateMcdUi(McdGraph mcd){
		if(m_mcd==mcd&&m_mcd!=null){
			m_mntmAnnuler.setEnabled(m_mcd.peutAnnuler());
			m_mntmRefaire.setEnabled(m_mcd.peutRefaire());
		}
		updateMcdNames();
	}
	public void zoomChanged(McdGraph mcd){
		if(m_mcd==mcd)
		{
			m_zoom.setValue((int) (mcd.getZoom()*100));
		}
	}
}
