package com.mcd_edition_fenetre.auth;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;

import com.mcd_composent_graph.auth.CardinaliteGraph;
import com.mcd_composent_graph.auth.CardinaliteGraphType;
import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Cardinalite;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class FenetreEditionCardinalite extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JSpinner m_max;
	private JSpinner m_min;
	private JCheckBox m_relatif;
	private McdGraph m_mcd;
	private CardinaliteGraph m_cardinaliteGraph;
	private Cardinalite m_cardinalite;
	private JComboBox m_selectStyle;

	/**
	 * Create the dialog.
	 */
	public FenetreEditionCardinalite(McdGraph mcd, CardinaliteGraph cardinalite) {
		setMinimumSize(new Dimension(0, 185));
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		m_mcd=mcd;
		m_cardinaliteGraph=cardinalite;
		m_cardinalite = m_cardinaliteGraph.getCardinalite();

		setTitle("Edition cardinalité");
		setBounds(100, 100, 248, 185);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow][73.00,grow]", "[][][][][]"));
		{
			JLabel lblCardinalitMin = new JLabel("Cardinalité min");
			contentPanel.add(lblCardinalitMin, "cell 0 0");
		}
		{
			m_min = new JSpinner();
			m_min.setModel(new SpinnerNumberModel(0, 0, 2, 1));
			m_min.setMinimumSize(new Dimension(40, 20));
			m_min.setValue(m_cardinalite.getMin());
			m_min.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if(m_max.getValue().toString()!="n"){
						int val = Integer.parseInt(m_max.getValue().toString());
						if(val<(Integer)m_min.getValue()){
							if((Integer)m_min.getValue()>2){
								m_max.setValue("n");
							} else {
								m_max.setValue(String.valueOf((Integer)m_min.getValue()));
							}
						}
					}
				}
			});
			contentPanel.add(m_min, "cell 1 0");
		}
		{
			JLabel lblCardinalitMax = new JLabel("Cardinalité max");
			contentPanel.add(lblCardinalitMax, "cell 0 1");
		}
		{
			m_max = new JSpinner();
			m_max.setModel(new SpinnerListModel(new String[] {"1", "2", "n"}));
			m_max.setMinimumSize(new Dimension(40, 20));
			if(this.m_cardinalite.getMax()==-1){
				m_max.getModel().setValue("n");
			}
			else{
				m_max.setValue(String.valueOf(m_cardinalite.getMax()));
			}
			m_max.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					if(m_max.getValue().toString()!="n"){
						int val = Integer.parseInt(m_max.getValue().toString());
						if(val<(Integer)m_min.getValue()){
							m_min.setValue(Integer.parseInt(m_max.getValue().toString()));
						}
					}
				}
			});
			contentPanel.add(m_max, "cell 1 1");
		}
		{
			JLabel lblRelatif = new JLabel("Relatif");
			contentPanel.add(lblRelatif, "flowy,cell 0 2");
		}
		{
			m_relatif = new JCheckBox("");
			m_relatif.setSelected(m_cardinalite.isRelatif());
			contentPanel.add(m_relatif, "cell 1 2");
		}
		{
			JLabel lblStyle = new JLabel("Style");
			contentPanel.add(lblStyle, "cell 0 3");
		}
		{
			m_selectStyle = new JComboBox();
			for(CardinaliteGraphType s: CardinaliteGraphType.values()){
				m_selectStyle.addItem(s);
			}
			m_selectStyle.setSelectedItem(m_cardinaliteGraph.getTypeDessin());
			contentPanel.add(m_selectStyle, "cell 1 3,growx");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						m_cardinalite.setRelatif(m_relatif.isSelected());
						m_cardinalite.setMin((Integer) m_min.getValue());
						m_cardinaliteGraph.setTypeDessin((CardinaliteGraphType) m_selectStyle.getSelectedItem());
						if(m_max.getValue().toString() != "n"){
							m_cardinalite.setMax(Integer.parseInt(m_max.getValue().toString()));
						}
						else{
							m_cardinalite.setMax(-1);
						}
						setVisible(false);
						m_mcd.repaint();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

}
