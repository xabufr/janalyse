package com.mcd_edition_fenetre.auth;

import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.BevelBorder;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.mcd_composent_graph.auth.ContrainteGraph;
import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Contrainte;
import com.mcd_log.auth.ContrainteType;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Relation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FenetreEditionContrainte extends JDialog{
	private McdGraph m_mcd;
	private ContrainteGraph m_contrainteGraph;
	private Contrainte m_contrainte;
	private JList<Relation> m_lstRelation;
	private JList<Entite> m_lstEntite;
	private JButton m_ajouterEntite;
	private JButton m_supprimerEntite;
	private JButton m_ajouterRelation;
	private JButton m_supprimerRelation;
	private JComboBox m_type;
	private DefaultListModel m_modelEntite;
	private DefaultListModel m_modelRelation;

	public FenetreEditionContrainte(McdGraph mcd,ContrainteGraph cont) {
		m_mcd = mcd;
		m_contrainteGraph = cont;
		m_contrainte = cont.getContrainte();
		
		setTitle("Edition Contrainte");
		setMinimumSize(new Dimension(0, 380));
		setBounds(100, 100, 555, 381);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		
		getContentPane().setLayout(new MigLayout("", "[grow][grow]", "[][grow][]"));
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.GRAY));
		getContentPane().add(panel, "cell 0 0 2 1,grow");
		panel.setLayout(new MigLayout("", "[][grow]", "[]"));
		
		JLabel lblType = new JLabel("Type:");
		panel.add(lblType, "cell 0 0,alignx trailing");
		
		m_type = new JComboBox();
		for (ContrainteType t : ContrainteType.values()){
			m_type.addItem(t);
			if (m_contrainte.getType().equals(t))
				m_type.setSelectedItem(t);
		}
		m_type.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (m_type.getSelectedItem().toString().equals("1"))
					changeState("Relation", false);
				else{
					changeState("Entite", true);
					changeState("Relation", true);
				}
			}
		});
		panel.add(m_type, "cell 1 0,growx");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.GRAY));
		getContentPane().add(panel_1, "cell 0 1,grow");
		panel_1.setLayout(new MigLayout("", "[grow]", "[][grow][]"));
		
		JLabel lblEntits = new JLabel("Entités:");
		panel_1.add(lblEntits, "cell 0 0");
		
		m_modelEntite = new DefaultListModel();
		for (Entite e : m_contrainte.getEntites())
			m_modelEntite.addElement(e);
		m_lstEntite = new JList();
		m_lstEntite.setModel(m_modelEntite);
		m_lstEntite.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_1.add(m_lstEntite, "cell 0 1,grow");
		
		m_ajouterEntite = new JButton("Ajouter");
		m_ajouterEntite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FenetreSelection(m_mcd.getMcdComponents(), "Entite", m_modelEntite).setVisible(true);
			}
		});
		panel_1.add(m_ajouterEntite, "flowx,cell 0 2,alignx center");
		
		m_supprimerEntite = new JButton("Supprimer");
		m_supprimerEntite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Entite ent : m_lstEntite.getSelectedValuesList()){
					m_modelEntite.removeElement(ent);
					if (m_type.getSelectedItem().equals("1") && m_contrainte.getSens().equals(ent))
						m_contrainte.setSens(null);
				}
			}
		});
		panel_1.add(m_supprimerEntite, "cell 0 2");
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.GRAY));
		getContentPane().add(panel_2, "cell 1 1,grow");
		panel_2.setLayout(new MigLayout("", "[grow]", "[][grow][]"));
		
		JLabel lblRelations = new JLabel("Relations:");
		panel_2.add(lblRelations, "cell 0 0");
		
		
		m_modelRelation = new DefaultListModel();
		for (Relation r : m_contrainte.getRelations())
			m_modelRelation.addElement(r);
		m_lstRelation = new JList();
		m_lstRelation.setModel(m_modelRelation);
		m_lstRelation.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_2.add(m_lstRelation, "cell 0 1,grow");
		
		m_ajouterRelation = new JButton("Ajouter");
		m_ajouterRelation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FenetreSelection(m_mcd.getMcdComponents(), "Relation", m_modelRelation).setVisible(true);
			}
		});
		panel_2.add(m_ajouterRelation, "flowx,cell 0 2,alignx center");
		
		m_supprimerRelation = new JButton("Supprimer");
		m_supprimerRelation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Relation r : m_lstRelation.getSelectedValuesList()){
					m_modelRelation.removeElement(r);
					if (m_type.getSelectedItem().equals("I") && m_contrainte.getSens().equals(r))
						m_contrainte.setSens(null);
				}
			}
		});
		panel_2.add(m_supprimerRelation, "cell 0 2");
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (!((ContrainteType)m_type.getSelectedItem()).toString().equals("1") && !((ContrainteType)m_type.getSelectedItem()).toString().equals("I")){
					if (!m_contrainte.getNom().equals("X") && m_modelEntite.size()>1 || m_modelRelation.size()>2){
						JOptionPane.showConfirmDialog(null, "Seul deux relations et une entité peuvent être sélectionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else if (m_contrainte.getNom().equals("X") && m_modelEntite.size()>2 || m_modelRelation.size()>2){
						JOptionPane.showConfirmDialog(null, "Seul deux relations et deux entités peuvent être sélectionné.", "Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else if (m_modelEntite.size()<1 || m_modelRelation.size()<2){
						JOptionPane.showConfirmDialog(null, "Veuillez sélectionner deux relations et une entité.", "Erreur", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				
				m_contrainte.getEntites().clear();
				for (int i=0; i<m_modelEntite.getSize(); ++i){
					Entite e = (Entite)m_modelEntite.get(i);
					m_contrainte.addEntite(e);
				}
				
				m_contrainte.getRelations().clear();
				for (int i=0; i<m_modelRelation.getSize(); ++i){
					Relation e = (Relation)m_modelRelation.get(i);
					m_contrainte.addRelation(e);
				}
				
				m_contrainte.setNom((ContrainteType)m_type.getSelectedItem());
				
				if (m_contrainte.getNom().equals("1")){
					if (m_modelEntite.getSize() > 0){
						Entite e = (Entite)JOptionPane.showInputDialog(null, "Veuillez choisir le sens de lecture:", "Sélection sens de lecture", JOptionPane.PLAIN_MESSAGE, null, m_modelEntite.toArray(), m_modelEntite.getElementAt(0));
						m_contrainte.setSens(e);
					}
				}
				else if (m_contrainte.getNom().equals("I")){
					if (m_modelRelation.getSize() > 0){
						Relation r = (Relation)JOptionPane.showInputDialog(null, "Veuillez choisir le sens de lecture:", "Sélection sens de lecture", JOptionPane.PLAIN_MESSAGE, null, m_modelRelation.toArray(), m_modelRelation.getElementAt(0));
						m_contrainte.setSens(r);
					}
				}
				
				m_contrainteGraph.update();
				m_mcd.repaint();
				FenetreEditionContrainte.this.setVisible(false);
			}
		});
		getContentPane().add(btnOk, "cell 0 2,alignx right");
		
		JButton btnAnnuler = new JButton("Annuler");
		btnAnnuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FenetreEditionContrainte.this.setVisible(false);
			}
		});
		getContentPane().add(btnAnnuler, "cell 1 2");

	}
	
	private void changeState(String n, boolean b){
		if (n.equals("Entite")){
			m_lstEntite.setEnabled(b);
			m_ajouterEntite.setEnabled(b);
			m_supprimerEntite.setEnabled(b);
		}
		else{
			m_lstRelation.setEnabled(b);
			m_ajouterRelation.setEnabled(b);
			m_supprimerRelation.setEnabled(b);
		}
	}

}
