package com.mcd_edition_fenetre.auth;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JLabel;


import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.mcd_composent_graph.auth.HeritageGraph;
import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Heritage;
import com.mcd_log.auth.HeritageType;
import javax.swing.border.BevelBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class FenetreEditionHeritage extends JDialog{
	private McdGraph m_mcd;
	private HeritageGraph m_heritageGraph;
	private Heritage m_heritage;
	private JComboBox m_type;
	private JComboBox m_entiteMere;
	private DefaultListModel m_model;
	private JList m_lstEnfant;

	public FenetreEditionHeritage(McdGraph mcd, HeritageGraph her) {
		m_mcd = mcd;
		m_heritageGraph = her;
		m_heritage = her.getHeritage();
		
		setTitle("Edition Héritage");
		setMinimumSize(new Dimension(0, 380));
		setBounds(100, 100, 555, 381);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		getContentPane().setLayout(new MigLayout("", "[188px][150px][50px][grow]", "[68px][][][255px,grow][35px]"));
		
		
		JLabel lblNewLabel_1 = new JLabel("Type:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(lblNewLabel_1, "cell 0 0,alignx right");
		
		m_type = new JComboBox();
		int i=0;
		for (HeritageType s : HeritageType.values()){
			m_type.addItem(s);
			if (s.toString().equals(m_heritage.getType().toString()))
				m_type.setSelectedIndex(i);
			
			++i;
		}
		getContentPane().add(m_type, "cell 1 0");
		
		
		JLabel lblNewLabel = new JLabel("Entité Mère:");
		getContentPane().add(lblNewLabel, "cell 0 1,alignx right");
		
		m_model = new DefaultListModel();
		for (Entite e : m_heritage.getEnfants())
			m_model.addElement(e);
		
		m_entiteMere = new JComboBox();
		i=0;
		for (Entite e : m_heritage.getEnfants()){
			m_entiteMere.addItem(e);
			if (e == m_heritage.getMere())
				m_entiteMere.setSelectedIndex(i);
			
			++i;
		}
		getContentPane().add(m_entiteMere, "cell 1 1");
		
		JLabel lblEntitFilles = new JLabel("Entité Filles:");
		getContentPane().add(lblEntitFilles, "cell 1 2,alignx center");
		lblEntitFilles.setHorizontalAlignment(SwingConstants.LEFT);
		
		m_lstEnfant = new JList();
		m_lstEnfant.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		m_lstEnfant.setModel(m_model);
		getContentPane().add(m_lstEnfant, "cell 1 3,grow");
		
		JButton button = new JButton("Supprimer");
		button.addActionListener(new BoutonSupprimerListener());
		button.setPreferredSize(new Dimension(300, button.getHeight()));
		getContentPane().add(button, "cell 2 3");
		
		JPanel panel_3 = new JPanel();
		getContentPane().add(panel_3, "cell 0 4 5 1,growx,aligny top");
		
		JButton btnOk = new JButton("Ok");
		btnOk.setHorizontalAlignment(SwingConstants.RIGHT);
		btnOk.addActionListener(new BoutonOkListener());
		panel_3.add(btnOk);
		
		JButton btnAnnuler = new JButton("Annuler");
		btnAnnuler.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				FenetreEditionHeritage.this.setVisible(false);
				m_mcd.repaint();
			}
		});
		panel_3.add(btnAnnuler);
	}
	
	private class BoutonSupprimerListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if (m_lstEnfant.getSelectedValue() != null){
				Entite e = (Entite) m_lstEnfant.getSelectedValue();
				m_model.removeElement(e);
				m_entiteMere.removeItem(e);
				
				m_heritage.getEnfants().clear();
				for (int i=0; i<m_model.getSize(); ++i)
					m_heritage.addEnfant((Entite) m_model.get(i));
				
				m_heritageGraph.update();
			}
		}	
	}
	
	private class BoutonOkListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			m_heritage.setType((HeritageType) m_type.getSelectedItem());
			
			for (Entite enfant : m_heritage.getEnfants()){
				if (enfant.getName().equals(m_entiteMere.getSelectedItem().toString()))
					enfant.setMere(true);
				else
					enfant.setMere(false);
			}
			
			m_heritageGraph.update();
			m_mcd.repaint();
			FenetreEditionHeritage.this.setVisible(false);			
		}	
	}
}
