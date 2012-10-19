package com.mcd_edition_fenetre.auth;

import java.awt.Dimension;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Relation;

public class FenetreSelection extends JDialog{
	private DefaultListModel m_model;
	private String m_objet;
	private JList m_list;
	
	public FenetreSelection(List<McdComposentGraphique> lstComp, String s, DefaultListModel m) {
		m_model = m;
		m_objet = s;
		
		setMinimumSize(new Dimension(0, 380));
		setBounds(100, 100, 555, 381);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		
		getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow][]"));
		DefaultListModel model = new DefaultListModel();
		JLabel lblNewLabel = new JLabel("");
		if (m_objet.equals("Entite")){
			setTitle("Selecteur d'Entite");
			lblNewLabel.setText("Entités:");
			for (McdComposentGraphique c : lstComp)
				if (c instanceof EntiteGraph){
					EntiteGraph eg = (EntiteGraph)c;
					model.addElement(eg.getEntite());
				}
		}
		else{
			setTitle("Selecteur de Relation");
			lblNewLabel.setText("Relation:");
			for (McdComposentGraphique c : lstComp)
				if (c instanceof RelationGraph){
					RelationGraph eg = (RelationGraph)c;
					model.addElement(eg.getRelation());
				}
		}
		
		getContentPane().add(lblNewLabel, "cell 0 0,alignx center");
		
		m_list = new JList();
		
		m_list.setModel(model);
		m_list.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		getContentPane().add(m_list, "cell 0 1,grow");
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (m_objet.equals("Entite")){
					for (Object o : m_list.getSelectedValuesList())
						if (o instanceof Entite){
							Entite en = (Entite)o;
							m_model.addElement(en);
						}
				}
				else{
					for (Object o : m_list.getSelectedValuesList())
						if (o instanceof Relation){
							Relation en = (Relation)o;
							m_model.addElement(en);
						}
				}
				FenetreSelection.this.setVisible(false);
			}
		});
		getContentPane().add(btnOk, "flowx,cell 0 2,alignx center");
		
		JButton btnAnnuler = new JButton("Annuler");
		btnAnnuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FenetreSelection.this.setVisible(false);
			}
		});
		getContentPane().add(btnAnnuler, "cell 0 2");
	}
}
