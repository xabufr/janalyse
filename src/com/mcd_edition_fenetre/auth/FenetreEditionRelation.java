package com.mcd_edition_fenetre.auth;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JSeparator;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.JComboBox;

import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.ProprieteType;
import com.mcd_log.auth.ProprieteTypeE;
import com.mcd_log.auth.Relation;

import java.awt.Dialog.ModalityType;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import java.awt.Dimension;
import javax.swing.SpinnerNumberModel;

public class FenetreEditionRelation extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField m_nom;
	private JTextField m_nomPropriete;
	private JTextPane m_commentaire;
	private JComboBox m_typePropriete;
	private JList m_listeProprietes;
	private McdGraph m_mcd;
	private Relation m_relationCopie, m_relation;
	private DefaultListModel m_model;
	private Propriete m_currentPropriete;
	private JCheckBox m_nullable;
	private JSpinner m_taille;
	private JTextPane m_txtpnCommentaire;
	private JCheckBox m_autoIncrement;
	/**
	 * Create the dialog.
	 */
	public FenetreEditionRelation(McdGraph mcd, RelationGraph relation) {
		setMinimumSize(new Dimension(0, 380));
		m_relation = relation.getRelation();
		m_relationCopie = new Relation(relation.getRelation());
		m_mcd=mcd;
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		
		setBounds(100, 100, 555, 381);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[218px,grow][200px,grow]", "[grow][][228px,grow]"));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "cell 0 0 2 1,grow");
			panel.setLayout(new MigLayout("", "[][grow]", "[][grow][36.00][]"));
			{
				JLabel lblNom = new JLabel("Nom");
				panel.add(lblNom, "cell 0 0,alignx trailing");
			}
			{
				m_nom = new JTextField();
				panel.add(m_nom, "cell 1 0,growx");
				m_nom.setColumns(10);
				m_nom.setText(m_relationCopie.getNom());
			}
			{
				JLabel lblCommentaire = new JLabel("Commentaire");
				panel.add(lblCommentaire, "cell 0 1");
			}
			{
				m_commentaire = new JTextPane();
				m_commentaire.setText(m_relationCopie.getCommentaire());
				panel.add(m_commentaire, "cell 1 1 1 3,grow");
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "cell 0 2,grow");
			panel.setLayout(new MigLayout("", "[][grow]", "[][][][][grow][][][]"));
			{
				JLabel lblProprietes = new JLabel("Propriete");
				panel.add(lblProprietes, "cell 0 0");
			}
			{
				JLabel lblNom_1 = new JLabel("Nom");
				panel.add(lblNom_1, "cell 0 1,alignx trailing");
			}
			{
				m_nomPropriete = new JTextField();
				panel.add(m_nomPropriete, "cell 1 1,growx");
				m_nomPropriete.setColumns(10);
			}
			{
				JLabel lblType = new JLabel("Type");
				panel.add(lblType, "cell 0 2,alignx trailing");
			}
			{
				m_typePropriete = new JComboBox();
				panel.add(m_typePropriete, "cell 1 2,growx");
				for(ProprieteTypeE type : ProprieteTypeE.values()){
					m_typePropriete.addItem(type);
				}
			}
			{
				JButton btnModifier = new JButton("Modifier");
				btnModifier.addActionListener(new modifierPropriete());
				{
					JLabel lblTaille = new JLabel("Taille");
					panel.add(lblTaille, "cell 0 3");
				}
				{
					m_taille = new JSpinner();
					m_taille.setModel(new SpinnerNumberModel(0, 0, 255, 1));
					panel.add(m_taille, "cell 1 3");
				}
				{
					JLabel lblCommentaire_1 = new JLabel("Commentaire");
					panel.add(lblCommentaire_1, "cell 0 4");
				}
				{
					m_txtpnCommentaire = new JTextPane();
					panel.add(m_txtpnCommentaire, "cell 1 4,grow");
				}
				{
					JLabel lblNull = new JLabel("Null");
					panel.add(lblNull, "cell 0 5");
				}
				{
					m_nullable = new JCheckBox("");
					panel.add(m_nullable, "cell 1 5");
				}
				{
					JLabel lblAutoIncrement = new JLabel("Auto Increment");
					panel.add(lblAutoIncrement, "cell 0 6");
				}
				{
					m_autoIncrement = new JCheckBox("");
					panel.add(m_autoIncrement, "cell 1 6");
				}
				panel.add(btnModifier, "cell 0 7");
			}
			{
				JButton btnCrer = new JButton("Créer");
				panel.add(btnCrer, "cell 1 7");
				btnCrer.addActionListener(new createurPropriete());
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "cell 1 2,grow");
			panel.setLayout(new MigLayout("", "[181px][]", "[100px][][][][]"));
			{
				m_listeProprietes = new JList();
				m_model = new DefaultListModel();
				for(Propriete prop : m_relationCopie.getProprietes()){
					m_model.addElement(prop);
				}
				panel.add(m_listeProprietes, "cell 0 0 1 5,grow");
				m_listeProprietes.setModel(m_model);
				m_listeProprietes.addListSelectionListener(new SelectionProprieteListener());
			}

			JButton btnDown = new JButton("↓");
			btnDown.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int index = m_listeProprietes.getSelectedIndex();
					if(index==m_model.getSize()-1)
						return;
					Propriete p = (Propriete)m_listeProprietes.getSelectedValue();
					m_model.add(index+2, p);
					m_model.remove(index);
					m_listeProprietes.setSelectedIndex(index+1);
					reloadRelationProprietes();
				}
			});
			btnDown.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				}
			});
			{
				JButton btnUp = new JButton("↑");
				btnUp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						int index = m_listeProprietes.getSelectedIndex();
						if(index==0)
							return;
						Propriete p = (Propriete)m_listeProprietes.getSelectedValue();
						m_model.add(index-1, p);
						m_model.remove(index+1);
						m_listeProprietes.setSelectedIndex(index-1);
						reloadRelationProprietes();
					}
				});
				panel.add(btnUp, "cell 1 2");
			}
			panel.add(btnDown, "cell 1 3");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new validerModifications());
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new annulerModifications());
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	private void loadPropriete(Propriete p){
		if(p==null)
			return;
		m_nomPropriete.setText(p.getName());
		m_autoIncrement.setSelected(p.isAutoIncrement());
		m_txtpnCommentaire.setText(p.getCommentaire());
		m_nullable.setSelected(p.isNull());
		m_taille.setValue((Integer)p.getTaille());
		
		int nbItems = this.m_typePropriete.getItemCount();
		for(int i=0;i<nbItems;++i){
			if(((ProprieteTypeE)m_typePropriete.getItemAt(i))==p.getType().getType()){
				m_typePropriete.setSelectedIndex(i);
				break;
			}
		}
		
		m_currentPropriete = p;
	}
	private void reloadRelationProprietes(){
		m_relationCopie.getProprietes().clear();
		for(int i=0;i<m_model.size();++i){
			m_relationCopie.addPropriete((Propriete) m_model.get(i));
		}
	}
	private class SelectionProprieteListener implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent e) {
			loadPropriete((Propriete) m_listeProprietes.getSelectedValue());
		}
	}
	private class createurPropriete implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(m_nomPropriete.getText().trim().isEmpty())
				return;
			ProprieteTypeE type = (ProprieteTypeE) m_typePropriete.getSelectedItem();
			Propriete prop = new Propriete(m_nomPropriete.getText(), new ProprieteType(type));
			prop.setAutoIncrement(m_autoIncrement.isSelected());
			prop.setNull(m_nullable.isSelected());
			prop.setCommentaire(m_txtpnCommentaire.getText());
			prop.setTaille(((Integer)m_taille.getValue()).intValue());

			m_model.addElement(prop);
			m_relationCopie.addPropriete(prop);
		}
	}
	private class modifierPropriete implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if(m_currentPropriete==null)
				return;
			m_currentPropriete.setName(m_nomPropriete.getText());
			m_currentPropriete.getType().setType((ProprieteTypeE)m_typePropriete.getSelectedItem());
			m_currentPropriete.setAutoIncrement(m_autoIncrement.isSelected());
			m_currentPropriete.setNull(m_nullable.isSelected());
			m_currentPropriete.setCommentaire(m_txtpnCommentaire.getText());
			m_currentPropriete.setTaille(((Integer)m_taille.getValue()).intValue());
			m_listeProprietes.updateUI();
		}
	}
	private class validerModifications implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			m_relation.copyFrom(m_relationCopie);
			FenetreEditionRelation.this.setVisible(false);
			m_mcd.repaint();
		}
	}
	private class annulerModifications implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			FenetreEditionRelation.this.setVisible(false);
			m_mcd.repaint();
		}
	}
}
