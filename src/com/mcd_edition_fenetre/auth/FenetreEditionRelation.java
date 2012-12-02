package com.mcd_edition_fenetre.auth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextPane;
import javax.swing.JComboBox;

import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.ProprieteTypeE;
import com.mcd_log.auth.Relation;

import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import java.awt.Dimension;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class FenetreEditionRelation extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField m_nom;
	private JTextField m_nomPropriete;
	private JTextField m_commentaire;
	private JComboBox  m_typePropriete;
	private JList m_listeProprietes;
	private McdGraph m_mcd;
	private Relation m_relationCopie, m_relation;
	private DefaultListModel m_model;
	private Propriete m_currentPropriete;
	private JCheckBox m_nullable;
	private JSpinner m_taille[];
	private JTextPane m_txtpnCommentaire;
	private JCheckBox m_autoIncrement;
	private JButton m_btnCreer;
	private JPanel m_panel, m_panelTaille;
	private JButton m_okButton;
	private JLabel m_lblTaille;
	/**
	 * Create the dialog.
	 */
	public FenetreEditionRelation(McdGraph mcd, RelationGraph relation) {
		relation.getRelation().setCif(false);
		setMinimumSize(new Dimension(0, 380));
		m_relation = relation.getRelation();
		m_relationCopie = new Relation(relation.getRelation());
		m_mcd=mcd;
		
		setTitle("Edition Relation");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		
		setBounds(100, 100, 555, 381);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[218px,grow][200px,grow]", "[grow][][228px,grow]"));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new LineBorder(Color.GRAY));
			contentPanel.add(panel, "cell 0 0 2 1,grow");
			panel.setLayout(new MigLayout("", "[][grow]", "[][grow][36.00][18.00]"));
			{
				JLabel lblNom = new JLabel("Nom");
				panel.add(lblNom, "cell 0 0,alignx trailing");
			}
			{
				m_okButton = new JButton("OK");
				m_nom = new JTextField();
				panel.add(m_nom, "cell 1 0,growx");
				m_nom.setColumns(10);
				m_nom.setText(m_relationCopie.getNom());
				m_nom.addKeyListener(new KeyListener() {
					public void keyTyped(KeyEvent arg0) {						
					}
					public void keyReleased(KeyEvent arg0) {						
					}
					public void keyPressed(KeyEvent arg0) {
						if(arg0.getKeyCode()==KeyEvent.VK_ENTER&&
								!m_nom.getText().trim().isEmpty()){
							m_okButton.doClick();
						}
					}
				});
			}
			{
				JLabel lblCommentaire = new JLabel("Commentaire");
				panel.add(lblCommentaire, "cell 0 1");
			}
			{
				m_commentaire = new JTextField();
				m_commentaire.setColumns(10);
				m_commentaire.setText(m_relationCopie.getCommentaire());
				panel.add(m_commentaire, "cell 1 1 1 3,growx");
			}
		}
		{
			m_panel = new JPanel();
			m_panel.setBorder(new LineBorder(Color.GRAY));
			contentPanel.add(m_panel, "cell 0 2,grow");
			m_panel.setLayout(new MigLayout("", "[][grow,center]", "[][][][][grow][][][]"));
			{
				JLabel lblProprietes = new JLabel("Propriete");
				m_panel.add(lblProprietes, "cell 0 0 2 1,alignx center");
			}
			{
				JLabel lblNom_1 = new JLabel("Nom");
				m_panel.add(lblNom_1, "cell 0 1");
			}
			{
				m_nomPropriete = new JTextField();
				m_nomPropriete.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							m_btnCreer.doClick();
						}
					}
				});
				m_panel.add(m_nomPropriete, "cell 1 1,growx");
				m_nomPropriete.setColumns(10);
			}
			{
				JLabel lblType = new JLabel("Type");
				m_panel.add(lblType, "cell 0 2");
			}
			{
				m_typePropriete = new JComboBox();
				

				m_panel.add(m_typePropriete, "cell 1 2,growx");
				m_panelTaille = new JPanel();
				
				for(ProprieteTypeE type : ProprieteTypeE.values()){
					m_typePropriete.addItem(type.getName());
				}
			}
			{
				JButton btnModifier = new JButton("Modifier");
				btnModifier.addActionListener(new modifierPropriete());
				{
					m_lblTaille = new JLabel("Taille");
					m_panel.add(m_lblTaille, "cell 0 3");
					m_panel.add(m_panelTaille, "cell 1 3");
					m_lblTaille.setVisible(false);
				}
				m_typePropriete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						changeTaille(ProprieteTypeE.getValue((String) m_typePropriete.getSelectedItem()));
					}
				});
				{
					m_taille = null;
				}
				{
					JLabel lblCommentaire_1 = new JLabel("Commentaire");
					m_panel.add(lblCommentaire_1, "cell 0 4");
				}
				{
					m_txtpnCommentaire = new JTextPane();
					m_panel.add(m_txtpnCommentaire, "cell 1 4,grow");
				}
				{
					JLabel lblNull = new JLabel("Null");
					m_panel.add(lblNull, "cell 0 5");
				}
				{
					m_nullable = new JCheckBox("");
					m_panel.add(m_nullable, "cell 1 5");
				}
				{
					JLabel lblAutoIncrement = new JLabel("Auto Increment");
					m_panel.add(lblAutoIncrement, "cell 0 6");
				}
				{
					m_autoIncrement = new JCheckBox("");
					m_panel.add(m_autoIncrement, "cell 1 6");
				}
				m_panel.add(btnModifier, "cell 0 7,growx");
			}
			{
				m_btnCreer = new JButton("Créer");
				m_panel.add(m_btnCreer, "cell 1 7,growx");
				m_btnCreer.addActionListener(new createurPropriete());
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new LineBorder(Color.GRAY));
			contentPanel.add(panel, "cell 1 2,grow");
			panel.setLayout(new MigLayout("", "[181px][]", "[100px][][][][]"));
			{
				m_listeProprietes = new JList();
				JScrollPane scrollPane = new JScrollPane(m_listeProprietes);
				scrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				m_model = new DefaultListModel();
				for(Propriete prop : m_relationCopie.getProprietes()){
					m_model.addElement(prop);
				}
				panel.add(scrollPane, "cell 0 0 1 5,grow");
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
				panel.add(btnUp, "cell 1 1");
			}
			{
				JButton btnSupprimer = new JButton("Sup.");
				btnSupprimer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Object o = m_listeProprietes.getSelectedValue();
						if(o==null)
							return;
						m_model.remove(m_model.indexOf(o));
						reloadRelationProprietes();
						if(m_model.size()!=0)
							m_listeProprietes.setSelectedIndex(0);
					}
				});
				panel.add(btnSupprimer, "cell 1 2");
			}
			panel.add(btnDown, "cell 1 3");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				m_okButton.setActionCommand("OK");
				m_okButton.addActionListener(new validerModifications());
				buttonPane.add(m_okButton);
			}
			{
				JButton cancelButton = new JButton("Annuler");
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
		m_typePropriete.setSelectedItem(p.getType().getName());
		changeTaille(p.getType());
		alimenterTaille(p);
		
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
			if(m_nomPropriete.getText().trim().isEmpty()){
				JOptionPane.showMessageDialog(null, "Veuillez insérer un nom de propriété", "Erreur nom propriété", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(proprieteExiste(m_nomPropriete.getText())){
				JOptionPane.showMessageDialog(null, "La propriété existe déjà", "Erreur nom propriété", JOptionPane.ERROR_MESSAGE);
				return;
			}
				
			ProprieteTypeE type = ProprieteTypeE.getValue(m_typePropriete.getSelectedItem().toString());
			Propriete prop = new Propriete(m_nomPropriete.getText(), type);
			prop.setAutoIncrement(m_autoIncrement.isSelected());
			prop.setNull(m_nullable.isSelected());
			prop.setCommentaire(m_txtpnCommentaire.getText());
			alimenterPropriete(prop);

			m_model.addElement(prop);
			m_relationCopie.addPropriete(prop);
			m_nomPropriete.setText("");
		}
	}
	private class modifierPropriete implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			if(m_currentPropriete==null)
				return;
			
			if (!m_nomPropriete.getText().equals(""))
				m_currentPropriete.setName(m_nomPropriete.getText());
			else{
				JOptionPane.showMessageDialog(null, "Veuillez insérer un nom de propriété", "Erreur nom propriété", JOptionPane.ERROR_MESSAGE);
				return;
			}
				
			ProprieteTypeE t = ProprieteTypeE.getValue(m_typePropriete.getSelectedItem().toString());
			m_currentPropriete.setType(t);
			m_currentPropriete.setAutoIncrement(m_autoIncrement.isSelected());
			m_currentPropriete.setNull(m_nullable.isSelected());
			m_currentPropriete.setCommentaire(m_txtpnCommentaire.getText());
			alimenterPropriete(m_currentPropriete);
			m_listeProprietes.updateUI();
		}
	}
	private class validerModifications implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (!m_nom.getText().equals(""))
				m_relationCopie.setNom(m_nom.getText());
			else{
				JOptionPane.showMessageDialog(null, "Veuillez insérer un nom de relation", "Erreur nom relation", JOptionPane.ERROR_MESSAGE);
				return;
			}
			m_relationCopie.setCommentaire(m_commentaire.getText());
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
	private void changeTaille(ProprieteTypeE type){
		m_panelTaille.removeAll();
		if(type.getNombreTaille()==0){
			m_taille=null;
			m_lblTaille.setVisible(false);
			
		}
		else{
			m_taille=new JSpinner[type.getNombreTaille()];
			for(int i=0;i<type.getNombreTaille();++i){
				m_taille[i]=new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
				m_panelTaille.add(m_taille[i], "cell "+(i+1)+" 3,growx");
			}
			m_lblTaille.setVisible(true);
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
	private boolean proprieteExiste(String p){
		for(int i=0;i<m_model.getSize();++i){
			if(((Propriete)m_model.elementAt(i)).getName().equals(p))
				return true;
		}
		return false;
	}
}
