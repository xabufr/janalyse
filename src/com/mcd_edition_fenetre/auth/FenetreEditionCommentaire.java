package com.mcd_edition_fenetre.auth;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;

import com.mcd_composent_graph.auth.CommentaireGraph;
import com.mcd_graph.auth.McdGraph;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class FenetreEditionCommentaire extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextPane m_commentaireContenu;

	public FenetreEditionCommentaire(final McdGraph mcdGraph, final CommentaireGraph com) {
		CommentaireGraph commentaire = com;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[6px,grow,fill]", "[21px,grow,fill]"));
		{
			m_commentaireContenu = new JTextPane();
			m_commentaireContenu.setText(commentaire.getCommentaire());
			contentPanel.add(m_commentaireContenu, "cell 0 0,alignx left,aligny top");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						com.setCommentaire(m_commentaireContenu.getText());
						mcdGraph.repaint();
						FenetreEditionCommentaire.this.dispose();
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						FenetreEditionCommentaire.this.dispose();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

}
