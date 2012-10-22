package com.mcd_graph.auth;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.Dico.auth.DicoPanel;
import com.Export.auth.ExportSql;
import com.event.auth.Chargement;
import com.event.auth.QuitListener;
import com.export.auth.ExportPng;
import com.export.auth.ExporterHTML;
import com.mld.auth.MLDPanel;
import com.ui_help.auth.APropos;

import java.awt.Insets;
import java.util.ArrayList;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

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
		
		m_btnDico = new JButton("Dictionnaire des propriete");
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
			}
		});
		mnFichier.add(menuItem);
		
		JMenuItem mntmOuvrir = new JMenuItem("Ouvrir");
		mntmOuvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmOuvrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createNewMcd();
				Chargement charge = new Chargement(m_mcd);
				updateMcdNames();
				m_mcd.repaint();
			}
		});
		mnFichier.add(mntmOuvrir);
		
		JSeparator separator = new JSeparator();
		mnFichier.add(separator);
		
		JMenuItem mntmSauvegarder = new JMenuItem("Sauvegarder");
		mntmSauvegarder.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		mntmSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (m_mcd!=null){
					m_mcd.saveMcdComposent();
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
		
		JSeparator separator_1 = new JSeparator();
		mnFichier.add(separator_1);
		
		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.addActionListener(new QuitListener(this));
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
				if(m_mcd != null)
					m_mcd.annuler();
			}
		});
		mnEdition.add(m_mntmAnnuler);
		
		m_mntmRefaire = new JMenuItem("Refaire");
		m_mntmRefaire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd != null)
					m_mcd.refaire();
			}
		});
		m_mntmRefaire.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnEdition.add(m_mntmRefaire);
		
		JSeparator separator_3 = new JSeparator();
		mnEdition.add(separator_3);
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
		m_boutonEdition.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(m_mcd==null)
					return;
				m_mcd.setState(McdGraphStateE.EDIT);
				setEnabledButton(m_boutonEdition);
			}
		});
		setEnabledButton(m_boutonInsertionEntite);
		
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
				// TODO Auto-generated method stub
				
			}
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void windowClosing(WindowEvent arg0) {
				quitter();
				
			}
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public void quitter() {
		ArrayList<McdGraph> mcds = new ArrayList<McdGraph>();
		int nb = m_mcdContener.getTabCount();
		for(int i=0;i<nb;++i){
			mcds.add((McdGraph) ((JScrollPane)m_mcdContener.getComponentAt(i)).getViewport().getView());
		}
		for(McdGraph mcd : mcds){
			if(!fermerMcd(mcd)){
				return;
			}
		}
		frame.dispose();
		System.exit(0);
	}
	private void createNewMcd(){
		McdGraph mcd = new McdGraph(this);
		mcd.setState(McdGraphStateE.INSERT_ENTITE);
		mcd.setName("Nouveau MCD");
		m_mcdContener.addTab("", new JScrollPane(mcd));
		updateMcdNames();
	}
	public void updateScrollBar(){
		int nb = m_mcdContener.getTabCount();
		for(int i=0;i<nb;++i){
			JScrollPane sp = (JScrollPane) m_mcdContener.getComponentAt(i);
			if(sp.getViewport().getView()==m_mcd){
				sp.updateUI();
			}
		}
	}
	private void updateMcdNames(){
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
							 m_mcd.getName());
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
	private String getExtension(File f){
		String ext=f.getName();
		
		int index = ext.lastIndexOf(".");
		String ret = null;
		if(index>0&&index<ext.length()-1){
			ret = ext.substring(index+1).toLowerCase();
		}
		
		return ret;
	}
	private Boolean fermerMcd(McdGraph mcd){
		if(mcd==null)
			return false;
		if(!mcd.isSaved()){
			if(JOptionPane.showConfirmDialog(null,"MCD non sauvegardé, voulez-vous le fermer ?")!=
					JOptionPane.OK_OPTION)
				return false;
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
			return false;
		m_mcdContener.remove(index);
		if(mcd==m_mcd){
			m_mcd = null;
			if(m_mcdContener.getTabCount()!=0){
				m_mcdContener.setSelectedIndex(0);
				m_mcd = (McdGraph) ((JScrollPane)m_mcdContener.getSelectedComponent()).getViewport().getView();
			}
		}
		return true;
	}
	public void updateMcdUi(McdGraph mcd){
		if(m_mcd==mcd&&m_mcd!=null){
			m_mntmAnnuler.setEnabled(m_mcd.peutAnnuler());
			m_mntmRefaire.setEnabled(m_mcd.peutRefaire());
		}
		updateMcdNames();
	}
}
