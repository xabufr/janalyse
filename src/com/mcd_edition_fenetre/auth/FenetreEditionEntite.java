package com.mcd_edition_fenetre.auth;

import javax.swing.JDialog;

import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.ProprieteTypeE;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class FenetreEditionEntite extends JDialog{
	private JTextField m_nomEntite;
	private JTextField m_commentaireEntite;
	private JTextField m_nomPropriete;
	private JTextField m_commentairePropriete;
	private McdGraph m_mcd;
	private Entite m_entite;
	private DefaultListModel<Propriete> m_model;
	private JComboBox<String> m_type;
	private JSpinner m_taille[];
	private JCheckBox m_isCle;
	private JCheckBox m_isNull;
	private JCheckBox m_isAutoIncremente;
	private JList<Propriete> m_lstPropriete;
	private JButton m_boutonCreer;
	private JButton m_boutonModifier;
	private JPanel m_panel;
	private JPanel m_panelTaille;

	public FenetreEditionEntite(McdGraph mcd, EntiteGraph entite) {
		
		m_mcd = mcd;
		m_entite = entite.getEntite();
		
		setTitle("Edition Entité");
		setMinimumSize(new Dimension(0, 380));
		setBounds(100, 100, 555, 394);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		
		JPanel content = new JPanel();
		getContentPane().add(content, BorderLayout.CENTER);
		content.setLayout(new MigLayout("", "[314px,fill][237px]", "[56px][267px][35px]"));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new LineBorder(Color.GRAY));
			content.add(panel, "cell 0 0 2 1,growx,aligny top");
			panel.setLayout(new MigLayout("", "[][grow]", "[][]"));
			{
				JLabel lblNom = new JLabel("Nom");
				panel.add(lblNom, "cell 0 0,alignx trailing");
				
				m_nomEntite = new JTextField();
				m_nomEntite.setText(m_entite.getName());
				panel.add(m_nomEntite, "cell 1 0,growx");
				m_nomEntite.setColumns(10);
				
				JLabel lblCommentaire = new JLabel("Commentaire");
				panel.add(lblCommentaire, "cell 0 1,alignx trailing");
				
				m_commentaireEntite = new JTextField();
				if (m_entite.getCommentaire() != null)
					m_commentaireEntite.setText(m_entite.getCommentaire());
				panel.add(m_commentaireEntite, "cell 1 1,growx");
				m_commentaireEntite.setColumns(10);
			}
			m_panel = new JPanel();
			m_panel.setBorder(new LineBorder(Color.GRAY));
			content.add(m_panel, "cell 0 1,grow");
			m_panel.setLayout(new MigLayout("", "[][137.00,grow,center]", "[][][][grow][][][][][][]"));
			{
				m_type = new JComboBox<String>();
				
				JLabel lblProprit = new JLabel("Propriété");
				m_panel.add(lblProprit, "cell 0 0 2 1,alignx center");
				
				JLabel lblNom_1 = new JLabel("Nom");
				m_panel.add(lblNom_1, "cell 0 1");
				for (ProprieteTypeE p : ProprieteTypeE.values())
					m_type.addItem(p.getName());
				
				m_nomPropriete = new JTextField();
				m_nomPropriete.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							m_boutonCreer.doClick();
						}
					}
				});
				m_panel.add(m_nomPropriete, "cell 1 1,growx");
				m_nomPropriete.setColumns(10);
				
				JLabel lblType = new JLabel("Type");
				m_panel.add(lblType, "cell 0 2,growx");
				
				
				
				m_panel.add(m_type, "cell 1 2,growx");
				
				JLabel lblTaille = new JLabel("Taille");
				m_panel.add(lblTaille, "cell 0 3");
				
				m_taille = null;
				
				m_panelTaille = new JPanel();
				m_panel.add(m_panelTaille, "cell 1 3,grow");
				
				m_type.addActionListener(new ActionListener() {					
					public void actionPerformed(ActionEvent arg0) {
						changeTaille(ProprieteTypeE.getValue((String) m_type.getSelectedItem()));
					}
				});
				
				JLabel lblCommentaire_1 = new JLabel("Commentaire");
				m_panel.add(lblCommentaire_1, "cell 0 4");
				
				m_commentairePropriete = new JTextField();
				m_panel.add(m_commentairePropriete, "cell 1 4,growx");
				m_commentairePropriete.setColumns(10);
				
				JLabel lblClPrimaire = new JLabel("Clé primaire");
				m_panel.add(lblClPrimaire, "cell 0 5");
				
				m_isCle = new JCheckBox("");
				m_panel.add(m_isCle, "cell 1 5");
				
				JLabel lblNull = new JLabel("Null");
				m_panel.add(lblNull, "cell 0 6");
				
				m_isNull = new JCheckBox("");
				m_panel.add(m_isNull, "cell 1 6,alignx center");
				
				JLabel lblAutoincrment = new JLabel("Auto-incrémenté");
				m_panel.add(lblAutoincrment, "cell 0 7");
				
				m_isAutoIncremente = new JCheckBox("");
				m_panel.add(m_isAutoIncremente, "cell 1 7,alignx center");
				
				m_boutonModifier = new JButton("Modifier");
				m_boutonModifier.addActionListener(new BoutonModifierListener());
				m_panel.add(m_boutonModifier, "cell 0 8,growx");
			}
			
			m_boutonCreer = new JButton("Créer");
			m_boutonCreer.addActionListener(new BoutonCreerListener());
			m_panel.add(m_boutonCreer, "cell 1 8,growx");
				JPanel panel_2 = new JPanel();
				content.add(panel_2, "cell 0 2 2 1,growx,aligny top");
				{
				JButton btnOk = new JButton("Ok");
				btnOk.addActionListener(new BoutonOkListener());
				panel_2.add(btnOk);
				
				JButton btnAnnuler = new JButton("Annuler");
				btnAnnuler.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						FenetreEditionEntite.this.setVisible(false);
						m_mcd.repaint();
					}
				});
				panel_2.add(btnAnnuler);
			}
			JPanel panel_3 = new JPanel();
			panel_3.setBorder(new LineBorder(Color.GRAY));
			content.add(panel_3, "cell 1 1,grow");
			panel_3.setLayout(new MigLayout("", "[grow][]", "[grow][][grow][][grow]"));
			{
				JButton button = new JButton("↑");
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						int i = m_lstPropriete.getSelectedIndex();
						Propriete p;
						if (i == 0)
							return;
						p = (Propriete)m_lstPropriete.getSelectedValue();
						m_model.add(i-1, p);
						m_model.remove(i+1);
						m_entite.getProprietes().clear();
						for (int j=0; j<m_model.getSize(); ++j)
							m_entite.addPropriete((Propriete) m_model.get(j));
					}
				});
				panel_3.add(button, "cell 1 1");
				
				JButton btnSup = new JButton("Sup.");
				btnSup.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
						if (!m_lstPropriete.getSelectedValue().equals(null)){
							Propriete p = (Propriete)m_lstPropriete.getSelectedValue();
							m_model.removeElement(p);
							m_entite.delPropriete(p);
						}
					}
				});
				panel_3.add(btnSup, "cell 1 2");
				
				JButton button_1 = new JButton("↓");
				button_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int i = m_lstPropriete.getSelectedIndex();
						Propriete p;
						if (i == m_model.getSize()-1)
							return;
						p = (Propriete)m_lstPropriete.getSelectedValue();
						m_model.add(i+2, p);
						m_model.remove(i);
						m_entite.getProprietes().clear();
						for (int j=0; j<m_model.getSize(); ++j)
							m_entite.addPropriete((Propriete) m_model.get(j));
					}
				});
				panel_3.add(button_1, "cell 1 3");
				
				m_model = new DefaultListModel<Propriete>();
				for (Propriete p : m_entite.getProprietes())
					m_model.addElement(p);
				
				m_lstPropriete = new JList<Propriete>();
				JScrollPane scrollPane = new JScrollPane(m_lstPropriete);
				scrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				m_lstPropriete.setModel(m_model);
				m_lstPropriete.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (m_lstPropriete.getSelectedValue() != null){
							Propriete p = (Propriete) m_lstPropriete.getSelectedValue();
							m_nomPropriete.setText(p.getName());
							m_type.setSelectedItem(p.getType().getName());
							changeTaille(p.getType());
							alimenterTaille(p);
							m_commentairePropriete.setText(p.getCommentaire());
							m_isCle.setSelected(p.isClePrimaire());
							m_isNull.setSelected(p.isNull());
							m_isAutoIncremente.setSelected(p.isAutoIncrement());
						}
					}
				});
				panel_3.add(scrollPane, "cell 0 0 1 5,grow");
			}
		}
		
	}
	
	private class BoutonOkListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if (!m_nomEntite.getText().equals(null) && !m_nomEntite.getText().equals(""))
				m_entite.setName(m_nomEntite.getText());
			else{
				JOptionPane.showMessageDialog(null, "Veuillez insérer un nom d'entité", "Erreur nom entité", JOptionPane.ERROR_MESSAGE);
				return;	
			}
				
			if (!m_commentaireEntite.getText().equals(null))
				m_entite.setCommentaire(m_commentaireEntite.getText());
				
			m_entite.getProprietes().clear();
			for (int i=0; i<m_model.size(); ++i)
				m_entite.addPropriete((Propriete) m_model.get(i));
				
			FenetreEditionEntite.this.setVisible(false);
			m_mcd.repaint();
		}
	}
	
	private class BoutonModifierListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			Propriete p = (Propriete)m_lstPropriete.getSelectedValue();
			if (!m_nomPropriete.getText().equals(null) && !m_nomPropriete.getText().equals(""))
				p.setName(m_nomPropriete.getText());
			else{
				JOptionPane.showMessageDialog(null, "Veuillez insérer un nom de propriété", "Erreur nom propriété", JOptionPane.ERROR_MESSAGE);
				return;	
			}
				
			if (!m_commentairePropriete.getText().equals(null))
				p.setCommentaire(m_commentairePropriete.getText());
			
			ProprieteTypeE t = ProprieteTypeE.getValue(m_type.getSelectedItem().toString());
			p.setType(t);
			
			alimenterPropriete(p);
			
			p.setClePrimaire(m_isCle.isSelected());
			p.setNull(m_isNull.isSelected());
			p.setAutoIncrement(m_isAutoIncremente.isSelected());
		}
	}
	
	private class BoutonCreerListener implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			Propriete p;
			if (!m_nomPropriete.getText().equals(null) && !m_nomPropriete.getText().equals("")){
				ProprieteTypeE t = ProprieteTypeE.getValue(m_type.getSelectedItem().toString());
				p = new Propriete(m_nomPropriete.getText(), t);
				
				if (!m_commentairePropriete.getText().equals(null))
					p.setCommentaire(m_commentairePropriete.getText());
				
				alimenterPropriete(p);
				
				p.setClePrimaire(m_isCle.isSelected());
				p.setNull(m_isCle.isSelected());
				p.setAutoIncrement(m_isAutoIncremente.isSelected());
				
				m_model.addElement(p);
				
				m_nomPropriete.setText("");
				m_type.setSelectedIndex(0);
				changeTaille(ProprieteTypeE.NONE);
				m_commentairePropriete.setText("");
				m_isCle.setSelected(false);
				m_isNull.setSelected(false);
				m_isAutoIncremente.setSelected(false);
			}
			else{
				JOptionPane.showMessageDialog(null, "Veuillez insérer un nom de propriété", "Erreur nom propriété", JOptionPane.ERROR_MESSAGE);
				return;	
			}
		}
	}
	private void changeTaille(ProprieteTypeE type){
		m_panelTaille.removeAll();
		if(type.getNombreTaille()==0){
			m_taille=null;
		}
		else{
			m_taille=new JSpinner[type.getNombreTaille()];
			for(int i=0;i<type.getNombreTaille();++i){
				m_taille[i]=new JSpinner();
				m_panelTaille.add(m_taille[i]);
			}
		}
		m_panelTaille.updateUI();
	}
	private void alimenterTaille(Propriete p){
		for(int i=0;i<p.getType().getNombreTaille();++i){
			m_taille[i].setValue(p.getTaille(i));
		}
	}
	private void alimenterPropriete(Propriete p){
		for(int i=0;i<p.getType().getNombreTaille();++i){
			p.setTaille(i, (Integer) m_taille[i].getValue());
		}
	}
}
