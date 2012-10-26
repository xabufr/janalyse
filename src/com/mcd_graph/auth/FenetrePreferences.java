package com.mcd_graph.auth;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import net.miginfocom.swing.MigLayout;
import javax.swing.JTabbedPane;

import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.ProprieteTypeE;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

import say.swing.JFontChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JSplitPane;

import java.awt.Color;
import javax.swing.JTextField;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextArea;
import java.awt.Insets;
import java.awt.GridLayout;

@SuppressWarnings("serial")
public class FenetrePreferences extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField m_textField;
	private JTextField m_previsualisationProp;
	private JPanel m_panelEntites;
	private FenetrePrincipale m_parent;
	private JTextArea m_cssHTML;

	public FenetrePreferences(FenetrePrincipale princ) {
		m_parent = princ;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Préférences");
		McdPreferencesManager.getInstance().push();

		setBounds(100, 100, 658, 328);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JTabbedPane tabbedPaneGeneral = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPaneGeneral);
			{
				JTabbedPane tabbedPaneMcd = new JTabbedPane(JTabbedPane.TOP);
				tabbedPaneMcd.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
				tabbedPaneGeneral.addTab("MCD", null, tabbedPaneMcd, null);
				{
					m_panelEntites = new JPanel();
					tabbedPaneMcd.addTab("Entités", null, m_panelEntites, null);
					m_panelEntites.setLayout(new MigLayout("", "[398.00px,grow]", "[][grow]"));
					initializeWithProperties(m_panelEntites, PGroupe.ENTITE);
					
				}
				{
					JPanel panelRelations = new JPanel();
					tabbedPaneMcd.addTab("Relations", null, panelRelations, null);
					initializeWithProperties(panelRelations, PGroupe.RELATION);
				}
				{
					JPanel panelCardinalite = new JPanel();
					tabbedPaneMcd.addTab("Cardinalités", null, panelCardinalite, null);
					initializeWithoutPropertiesWithoutBackground(panelCardinalite, PGroupe.CARDINALITE);
				}
				{
					JPanel panelContraines = new JPanel();
					tabbedPaneMcd.addTab("Contraintes", null, panelContraines, null);
					initializeWithoutProperties(panelContraines, PGroupe.CONTRAINTE);
				}
				{
					JPanel panelHeritage = new JPanel();
					tabbedPaneMcd.addTab("Héritages", null, panelHeritage, null);
					initializeWithoutProperties(panelHeritage, PGroupe.HERITAGE);
				}
			}
			{
				JPanel panel = new JPanel();
				tabbedPaneGeneral.addTab("Général", null, panel, null);
				panel.setLayout(new MigLayout("", "[149px,grow][grow]", "[19px][][grow]"));
				{
					JLabel lblNommageProprits = new JLabel("Nommage propriétés");
					panel.add(lblNommageProprits, "cell 0 0,alignx left,aligny center");
				}
				{
					m_textField = new JTextField();
					panel.add(m_textField, "cell 1 0,growx,aligny top");
					m_textField.addCaretListener(new CaretListener() {
						public void caretUpdate(CaretEvent arg0) {
							McdPreferencesManager.getInstance().set(PGroupe.PROPRIETE, PCle.SCHEMA, m_textField.getText());
							Propriete p = new Propriete("Propriete", ProprieteTypeE.NONE);
							m_previsualisationProp.setText(p.getVirtualName("Entite"));
						}
					});
					m_textField.setColumns(10);
				}
				{
					m_previsualisationProp = new JTextField();
					panel.add(m_previsualisationProp, "cell 1 1,growx");
					m_previsualisationProp.setColumns(10);
					m_textField.setText((String) McdPreferencesManager.getInstance().get(PGroupe.PROPRIETE, PCle.SCHEMA));
				}
				{
					JTextPane txtpnpProprit = new JTextPane();
					txtpnpProprit.setEditable(false);
					txtpnpProprit.setText("%p : propriété\n%P: PROPRIÉTÉ\n%q : Propriété\n%e: Entité\n%E: ENTITÉ\n%r: entité\n%[0E3] : Ent");
					panel.add(txtpnpProprit, "cell 0 2 2 1,grow");
				}
			}
			{
				JPanel panel = new JPanel();
				tabbedPaneGeneral.addTab("Export HTML", null, panel, null);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.columnWidths = new int[]{0, 0};
				gbl_panel.rowHeights = new int[]{0, 0, 0};
				gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
				gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
				panel.setLayout(gbl_panel);
				{
					JLabel lblCssDuHtml = new JLabel("CSS du HTML, pour plus d'infos sur les balises voir le manuel");
					GridBagConstraints gbc_lblCssDuHtml = new GridBagConstraints();
					gbc_lblCssDuHtml.insets = new Insets(0, 0, 5, 0);
					gbc_lblCssDuHtml.gridx = 0;
					gbc_lblCssDuHtml.gridy = 0;
					panel.add(lblCssDuHtml, gbc_lblCssDuHtml);
				}
				{
					m_cssHTML = new JTextArea();
					JScrollPane scroll = new JScrollPane(m_cssHTML);
					GridBagConstraints gbc_cssHTML = new GridBagConstraints();
					gbc_cssHTML.fill = GridBagConstraints.BOTH;
					gbc_cssHTML.gridx = 0;
					gbc_cssHTML.gridy = 1;
					panel.add(scroll, gbc_cssHTML);
					m_cssHTML.setText((String) McdPreferencesManager.getInstance().get(PGroupe.HTML, PCle.CSS));
				}
			}
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
						setVisible(false);
						McdPreferencesManager.getInstance().set(PGroupe.PROPRIETE, PCle.SCHEMA, m_textField.getText());
						McdPreferencesManager.getInstance().set(PGroupe.HTML, PCle.CSS, m_cssHTML.getText());
						McdPreferencesManager.getInstance().save();
						if(m_parent.getMcd()!=null)
							m_parent.getMcd().repaint();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						McdPreferencesManager.getInstance().pop();
						FenetrePreferences.this.setVisible(false);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}
	private void changeFont(PGroupe groupe, PCle cle){
		final McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		JFontChooser fontChooser = new JFontChooser();
		fontChooser.setSelectedFont(prefs.getFont(groupe, cle));
		if(fontChooser.showDialog(FenetrePreferences.this)==JFontChooser.OK_OPTION){
			prefs.setFont(groupe, cle, 
					fontChooser.getSelectedFontFamily(),
					fontChooser.getSelectedFontStyle(),
					fontChooser.getSelectedFontSize());
		}
	}
	private void changeColor(PGroupe g, PCle c, String titre, JButton b){
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		Color color = JColorChooser.showDialog(null, titre, (Color) prefs.get(g, c));
		if(color!=null){
			prefs.set(g, c, color);
			b.setBackground(color);
		}
	}
	private void initButtonColor(JButton b, PGroupe g, PCle c){
		b.setBackground((Color)McdPreferencesManager.getInstance().get(g, c));
	}
	private void initializeWithoutProperties(JPanel parentPanel, final PGroupe g){
		final McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		parentPanel.setLayout(new MigLayout("", "[398.00px,grow]", "[][grow]"));
		{
			JLabel lblOmbre = new JLabel("Ombre");
			parentPanel.add(lblOmbre, "flowx,cell 0 0");
			
			final JCheckBox ombre = new JCheckBox("");
			ombre.setSelected((Boolean)prefs.get(g, PCle.OMBRE));
			parentPanel.add(ombre, "cell 0 0");
			
			final JButton couleurOmbre = new JButton("Couleur");
			initButtonColor(couleurOmbre, g, PCle.OMBRE_COLOR);
			couleurOmbre.setEnabled((Boolean)prefs.get(g, PCle.OMBRE));
			couleurOmbre.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					changeColor(g, PCle.OMBRE_COLOR, "Couleur de l'ombre", couleurOmbre);
				}
			});
			parentPanel.add(couleurOmbre, "cell 0 0");
			ombre.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					McdPreferencesManager prefs = McdPreferencesManager.getInstance();
					prefs.set(g, PCle.OMBRE, ombre.isSelected());
					if (ombre.isSelected())
						couleurOmbre.setEnabled(true);
					else
						couleurOmbre.setEnabled(false);
				}
			});
			if(!g.equals(PGroupe.CONTRAINTE))
			{
				JLabel lblDeg = new JLabel("Dégradé");
				parentPanel.add(lblDeg, "cell 0 0");
				
				final JCheckBox deg = new JCheckBox("");
				deg.setSelected((Boolean)prefs.get(g, PCle.GRADIANT_COLOR));
				parentPanel.add(deg, "cell 0 0");
				deg.addActionListener(new ActionListener() {					
					public void actionPerformed(ActionEvent e) {
						prefs.set(g, PCle.GRADIANT_COLOR, deg.isSelected());
					}
				});
			}
			
			JSplitPane splitPane = new JSplitPane();
			parentPanel.add(splitPane, "cell 0 1,grow");
			{
				JPanel panel = new JPanel();
				splitPane.setLeftComponent(panel);
				panel.setLayout(new MigLayout("", "[][][][]", "[][][][][]"));
				{
					JLabel lblSansFocus_1 = new JLabel("Sans focus");
					panel.add(lblSansFocus_1, "cell 0 0");
				}
				{
					JLabel lblPoliceNom_1 = new JLabel("Police nom");
					panel.add(lblPoliceNom_1, "flowx,cell 0 1");
				}
				{
					JButton choisirPNom = new JButton("Choisir");
					choisirPNom.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent arg0) {
							changeFont(g, PCle.FONT);
						}
					});
					panel.add(choisirPNom, "flowx,cell 1 1");
				}
				{
					final JButton btnCouleurNom = new JButton("Couleur");
					initButtonColor(btnCouleurNom, g, PCle.FONT_COLOR);
					btnCouleurNom.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.FONT_COLOR, "Couleur nom entité", btnCouleurNom);
						}
					});
					panel.add(btnCouleurNom, "cell 3 1");
				}
				{
					JLabel lblCouleurFond_1 = new JLabel("Couleur fond");
					panel.add(lblCouleurFond_1, "cell 0 3");
				}
				{
					final JButton choisirCFond = new JButton("Choisir");
					initButtonColor(choisirCFond, g, PCle.COLOR);
					choisirCFond.addActionListener(new ActionListener() {									
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR, "Couleur fond entité", choisirCFond);
						}
					});
					panel.add(choisirCFond, "cell 1 3 3 1");
				}
				if(!g.equals(PGroupe.CONTRAINTE))
				{
					final JButton choisirCFond = new JButton("Choisir");
					initButtonColor(choisirCFond, g, PCle.COLOR_2);
					choisirCFond.addActionListener(new ActionListener() {									
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_2, "Couleur fond entité", choisirCFond);
						}
					});
					panel.add(choisirCFond, "cell 2 3 3 1");
				}
				{
					JLabel lblCouleurContours = new JLabel("Couleur contours");
					panel.add(lblCouleurContours, "cell 0 4");
				}
				{
					final JButton choisirCContour = new JButton("Choisir");
					initButtonColor(choisirCContour, g, PCle.COLOR_CONTOUR);
					choisirCContour.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_CONTOUR, "Couleur contours entité", choisirCContour);
							
						}
					});
					panel.add(choisirCContour, "cell 1 4 3 1");
				}
			}
			{
				JPanel panel = new JPanel();
				splitPane.setRightComponent(panel);
				panel.setLayout(new MigLayout("", "[][][]", "[][][][][]"));
				{
					JLabel lblAvecFocus_1 = new JLabel("Avec focus");
					panel.add(lblAvecFocus_1, "cell 0 0");
				}
				{
					JLabel label = new JLabel("Police nom");
					panel.add(label, "cell 0 1");
				}
				{
					JButton focusChoisirPNom = new JButton("Choisir");
					focusChoisirPNom.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							changeFont(g, PCle.FONT_FOCUS);
							
						}
					});
					panel.add(focusChoisirPNom, "cell 1 1");
				}
				{
					final JButton btnCouleurNomFocus = new JButton("Couleur");
					initButtonColor(btnCouleurNomFocus, g, PCle.FONT_COLOR_FOCUS);
					btnCouleurNomFocus.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.FONT_COLOR_FOCUS, "Couleur nom entité focus", btnCouleurNomFocus);
						}
					});
					panel.add(btnCouleurNomFocus, "cell 2 1");
				}
				{
					JLabel lblCouleurFond_2 = new JLabel("Couleur fond");
					panel.add(lblCouleurFond_2, "cell 0 3");
				}
				{
					final JButton focusChoisirCFond = new JButton("Choisir");
					initButtonColor(focusChoisirCFond, g, PCle.COLOR_FOCUS);
					focusChoisirCFond.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_FOCUS, "Couleur fond entités focus", focusChoisirCFond);
							
						}
					});
					panel.add(focusChoisirCFond, "cell 1 3");
				}
				if(!g.equals(PGroupe.CONTRAINTE))
				{
					final JButton choisirCFond = new JButton("Choisir");
					initButtonColor(choisirCFond, g, PCle.COLOR_2_FOCUS);
					choisirCFond.addActionListener(new ActionListener() {									
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_2_FOCUS, "Couleur fond entités focus", choisirCFond);
						}
					});
					panel.add(choisirCFond, "cell 2 3");
				}
				{
					JLabel lblCouleurContours_1 = new JLabel("Couleur contours");
					panel.add(lblCouleurContours_1, "cell 0 4");
				}
				{
					final JButton focusChoisirCContour = new JButton("Choisir");
					initButtonColor(focusChoisirCContour, g, PCle.COLOR_CONTOUR_FOCUS);
					focusChoisirCContour.addActionListener(new ActionListener() {						
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_CONTOUR_FOCUS, "Couleur contours entités focus", focusChoisirCContour);
							
						}
					});
					panel.add(focusChoisirCContour, "cell 1 4");
				}
			}
		}
	}
	private void initializeWithoutPropertiesWithoutBackground(JPanel parentPanel, final PGroupe g){
		parentPanel.setLayout(new MigLayout("", "[398.00px,grow]", "[grow]"));
		{
			JSplitPane splitPane = new JSplitPane();
			parentPanel.add(splitPane, "cell 0 0,grow");
			{
				JPanel panel = new JPanel();
				splitPane.setLeftComponent(panel);
				panel.setLayout(new MigLayout("", "[][][][]", "[][][][][]"));
				{
					JLabel lblSansFocus_1 = new JLabel("Sans focus");
					panel.add(lblSansFocus_1, "cell 0 0");
				}
				{
					JLabel lblPoliceNom_1 = new JLabel("Police nom");
					panel.add(lblPoliceNom_1, "flowx,cell 0 1");
				}
				{
					JButton choisirPNom = new JButton("Choisir");
					choisirPNom.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent arg0) {
							changeFont(g, PCle.FONT);
						}
					});
					panel.add(choisirPNom, "flowx,cell 1 1");
				}
				{
					final JButton btnCouleurNom = new JButton("Couleur");
					initButtonColor(btnCouleurNom, g, PCle.FONT_COLOR);
					btnCouleurNom.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.FONT_COLOR, "Couleur nom entité", btnCouleurNom);
						}
					});
					panel.add(btnCouleurNom, "cell 3 1");
				}
				{
					JLabel lblCouleurContours = new JLabel("Couleur contours");
					panel.add(lblCouleurContours, "cell 0 4");
				}
				{
					final JButton choisirCContour = new JButton("Choisir");
					initButtonColor(choisirCContour, g, PCle.COLOR_CONTOUR);
					choisirCContour.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_CONTOUR, "Couleur contours entité", choisirCContour);
							
						}
					});
					panel.add(choisirCContour, "cell 1 4 3 1");
				}
			}
			{
				JPanel panel = new JPanel();
				splitPane.setRightComponent(panel);
				panel.setLayout(new MigLayout("", "[][][]", "[][][][][]"));
				{
					JLabel lblAvecFocus_1 = new JLabel("Avec focus");
					panel.add(lblAvecFocus_1, "cell 0 0");
				}
				{
					JLabel label = new JLabel("Police nom");
					panel.add(label, "cell 0 1");
				}
				{
					JButton focusChoisirPNom = new JButton("Choisir");
					focusChoisirPNom.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							changeFont(g, PCle.FONT_FOCUS);
							
						}
					});
					panel.add(focusChoisirPNom, "cell 1 1");
				}
				{
					final JButton btnCouleurNomFocus = new JButton("Couleur");
					initButtonColor(btnCouleurNomFocus, g, PCle.FONT_COLOR_FOCUS);
					btnCouleurNomFocus.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.FONT_COLOR_FOCUS, "Couleur nom entité focus", btnCouleurNomFocus);
						}
					});
					panel.add(btnCouleurNomFocus, "cell 2 1");
				}
				{
					JLabel lblCouleurContours_1 = new JLabel("Couleur contours");
					panel.add(lblCouleurContours_1, "cell 0 4");
				}
				{
					final JButton focusChoisirCContour = new JButton("Choisir");
					initButtonColor(focusChoisirCContour, g, PCle.COLOR_CONTOUR_FOCUS);
					focusChoisirCContour.addActionListener(new ActionListener() {						
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_CONTOUR_FOCUS, "Couleur contours entités focus", focusChoisirCContour);
						}
					});
					panel.add(focusChoisirCContour, "cell 1 4");
				}
			}
		}
	}
	private void initializeWithProperties(JPanel parentPanel, final PGroupe g){
		final McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		parentPanel.setLayout(new MigLayout("", "[276px,grow]", "[grow]"));
		{
			JLabel lblOmbre = new JLabel("Ombre");
			parentPanel.add(lblOmbre, "flowx");

			final JCheckBox ombre = new JCheckBox("");
			ombre.setSelected((Boolean)prefs.get(g, PCle.OMBRE));
			parentPanel.add(ombre, "cell 0 0");

			final JButton couleurOmbre = new JButton("Couleur");
			initButtonColor(couleurOmbre, g, PCle.OMBRE_COLOR);
			couleurOmbre.setEnabled((Boolean)prefs.get(g, PCle.OMBRE));
			couleurOmbre.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					changeColor(g, PCle.OMBRE_COLOR, "Couleur de l'ombre", couleurOmbre);
				}
			});
			parentPanel.add(couleurOmbre, "cell 0 0");
			ombre.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					McdPreferencesManager prefs = McdPreferencesManager.getInstance();
					prefs.set(g, PCle.OMBRE, ombre.isSelected());
					if (ombre.isSelected())
						couleurOmbre.setEnabled(true);
					else
						couleurOmbre.setEnabled(false);
				}
			});
			{
				JLabel lblDeg = new JLabel("Dégradé");
				parentPanel.add(lblDeg, "cell 0 0");
				
				final JCheckBox deg = new JCheckBox("");
				deg.setSelected((Boolean)prefs.get(g, PCle.GRADIANT_COLOR));
				parentPanel.add(deg, "cell 0 0");
				deg.addActionListener(new ActionListener() {					
					public void actionPerformed(ActionEvent e) {
						prefs.set(g, PCle.GRADIANT_COLOR, deg.isSelected());
					}
				});
			}
			if(g.equals(PGroupe.RELATION)){
				JLabel lblCif = new JLabel("Nommage automatique des CIF");
				parentPanel.add(lblCif, "cell 0 0");
				
				final JCheckBox cif = new JCheckBox("");
				cif.setSelected((Boolean)prefs.get(g, PCle.CIF));
				parentPanel.add(cif, "cell 0 0");
				cif.addActionListener(new ActionListener() {					
					public void actionPerformed(ActionEvent e) {
						prefs.set(g, PCle.CIF, cif.isSelected());
					}
				});
			}
		}
		{
			JSplitPane splitPane = new JSplitPane();
			parentPanel.add(splitPane, "cell 0 1");
			{
				JPanel panel = new JPanel();
				splitPane.setLeftComponent(panel);
				panel.setLayout(new MigLayout("", "[121px][83px][]", "[15px][25px][25px][25px][25px]"));
				{
					JLabel lblSansFocus = new JLabel("Sans focus");
					panel.add(lblSansFocus, "cell 0 0,alignx left,aligny top");
				}
				{
					JLabel lblPoliceNom = new JLabel("Police nom");
					panel.add(lblPoliceNom, "cell 0 1,alignx left,aligny center");
				}
				{
					JButton btnChoisirPNom = new JButton("Choisir");
					btnChoisirPNom.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent arg0) {
							changeFont(g, PCle.FONT_NOM);
						}
					});
					panel.add(btnChoisirPNom, "cell 1 1,alignx left,aligny top");
				}
				{
					final JButton btnCouleurNom = new JButton("Couleur");
					initButtonColor(btnCouleurNom, g, PCle.FONT_NOM_COLOR);
					btnCouleurNom.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.FONT_NOM_COLOR, "Couleur nom relations", btnCouleurNom);
						}
					});
					panel.add(btnCouleurNom, "cell 2 1");
				}
				{
					JLabel lblPoliceProprits = new JLabel("Police propriétés");
					panel.add(lblPoliceProprits, "cell 0 2,alignx left,aligny center");
				}
				{
					JButton btnChoisirPPropriete = new JButton("Choisir");
					btnChoisirPPropriete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeFont(g, PCle.FONT);
						}
					});
					panel.add(btnChoisirPPropriete, "cell 1 2,alignx left,aligny top");
				}
				{
					final JButton couleurProp = new JButton("Couleur");
					initButtonColor(couleurProp, g, PCle.FONT_NOM_COLOR);
					couleurProp.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.FONT_NOM_COLOR, "Couleur propriété relation", couleurProp);
						}
					});
					panel.add(couleurProp, "cell 2 2");
				}
				{
					JLabel lblCouleurFond = new JLabel("Couleur fond");
					panel.add(lblCouleurFond, "cell 0 3,alignx left,aligny top");
				}
				{
					final JButton btnChoisirCFond = new JButton("Choisir");
					initButtonColor(btnChoisirCFond, g, PCle.COLOR);
					btnChoisirCFond.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR, "Couleur fond relation", btnChoisirCFond);
						}
					});
					panel.add(btnChoisirCFond, "cell 1 3,alignx center,aligny center");
				}
				{
					final JButton btnChoisirCFond = new JButton("Choisir");
					initButtonColor(btnChoisirCFond, g, PCle.COLOR_2);
					btnChoisirCFond.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_2, "Couleur fond relation", btnChoisirCFond);
						}
					});
					panel.add(btnChoisirCFond, "cell 2 3,alignx center,aligny center");
				}
				{
					JLabel lblCouleurContour = new JLabel("Couleur contour");
					panel.add(lblCouleurContour, "cell 0 4,alignx left,aligny top");
				}
				{
					final JButton btnChoisirCContour = new JButton("Choisir");
					initButtonColor(btnChoisirCContour, g, PCle.COLOR_CONTOUR);
					btnChoisirCContour.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_CONTOUR, "Couleur contours relations", btnChoisirCContour);
						}
					});
					panel.add(btnChoisirCContour, "cell 1 4,alignx center,aligny center");
				}
			}
			{
				JPanel panel = new JPanel();
				splitPane.setRightComponent(panel);
				panel.setLayout(new MigLayout("", "[][][]", "[][][][][]"));
				{
					JLabel lblAvecFocus = new JLabel("Avec focus");
					panel.add(lblAvecFocus, "cell 0 0");
				}
				{
					JLabel label = new JLabel("Police nom");
					panel.add(label, "cell 0 1");
				}
				{
					JButton focusChoisirPNom = new JButton("Choisir");
					focusChoisirPNom.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeFont(g, PCle.FONT_NOM_FOCUS);
						}
					});
					panel.add(focusChoisirPNom, "cell 1 1");
				}
				{
					final JButton couleurNomFocus = new JButton("Couleur");
					initButtonColor(couleurNomFocus, g, PCle.FONT_NOM_COLOR_FOCUS);
					couleurNomFocus.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.FONT_NOM_COLOR_FOCUS, "Couleur nom relations focus", couleurNomFocus);
						}
					});
					panel.add(couleurNomFocus, "cell 2 1");
				}
				{
					JLabel label = new JLabel("Police propriétés");
					panel.add(label, "cell 0 2");
				}
				{
					JButton focusChoisirPPropriete = new JButton("Choisir");
					focusChoisirPPropriete.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeFont(g, PCle.FONT_FOCUS);
						}
					});
					panel.add(focusChoisirPPropriete, "cell 1 2");
				}
				{
					final JButton couleurPropFocus = new JButton("Couleur");
					initButtonColor(couleurPropFocus, g, PCle.FONT_COLOR_FOCUS);
					couleurPropFocus.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.FONT_COLOR_FOCUS, "Couleur propriétés relations focus", couleurPropFocus);
						}
					});
					panel.add(couleurPropFocus, "cell 2 2");
				}
				{
					JLabel label = new JLabel("Couleur fond");
					panel.add(label, "cell 0 3");
				}
				{
					final JButton focusChoisirCFond = new JButton("Choisir");
					initButtonColor(focusChoisirCFond, g, PCle.COLOR_FOCUS);
					focusChoisirCFond.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_FOCUS, "Couleur fond relation focus", focusChoisirCFond);
						}
					});
					panel.add(focusChoisirCFond, "cell 1 3");
				}
				{
					final JButton focusChoisirCFond = new JButton("Choisir");
					initButtonColor(focusChoisirCFond, g, PCle.COLOR_2_FOCUS);
					focusChoisirCFond.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_2_FOCUS, "Couleur fond relation focus", focusChoisirCFond);
						}
					});
					panel.add(focusChoisirCFond, "cell 2 3");
				}
				{
					JLabel label = new JLabel("Couleur contour");
					panel.add(label, "cell 0 4");
				}
				{
					final JButton focusChoisirCContour = new JButton("Choisir");
					initButtonColor(focusChoisirCContour, g, PCle.COLOR_CONTOUR_FOCUS);
					focusChoisirCContour.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeColor(g, PCle.COLOR_CONTOUR_FOCUS, "Couleur contours relation focus", focusChoisirCContour);
						}
					});
					panel.add(focusChoisirCContour, "cell 1 4");
				}
			}
		}
	}
}
