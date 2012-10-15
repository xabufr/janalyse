package com.mcd_graph.auth;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTabbedPane;

import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

import say.swing.JFontChooser;

import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.prefs.PreferencesFactory;
import javax.swing.JLabel;
import javax.swing.JSplitPane;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dialog.ModalityType;

@SuppressWarnings("serial")
public class FenetrePreferences extends JDialog {

	private FenetrePrincipale m_fenPrinc;
	private final JPanel contentPanel = new JPanel();

	public FenetrePreferences(FenetrePrincipale princ) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Préférences");
		m_fenPrinc = princ;
		McdPreferencesManager.getInstance().push();

		setBounds(100, 100, 674, 316);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		{
			JTabbedPane tabbedPaneGeneral = new JTabbedPane(JTabbedPane.TOP);
			contentPanel.add(tabbedPaneGeneral, "cell 0 0,grow");
			{
				JTabbedPane tabbedPaneMcd = new JTabbedPane(JTabbedPane.TOP);
				tabbedPaneMcd.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
				tabbedPaneGeneral.addTab("MCD", null, tabbedPaneMcd, null);
				{
					JPanel panelEntites = new JPanel();
					tabbedPaneMcd.addTab("Entités", null, panelEntites, null);
					panelEntites.setLayout(new MigLayout("", "[398.00px,grow]", "[grow]"));
					{
						JSplitPane splitPane = new JSplitPane();
						panelEntites.add(splitPane, "cell 0 0,grow");
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
										changeFont(PGroupe.ENTITE, PCle.FONT_NOM);
									}
								});
								panel.add(choisirPNom, "flowx,cell 1 1");
							}
							{
								JButton btnCouleurNom = new JButton("Couleur");
								btnCouleurNom.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.ENTITE, PCle.FONT_NOM_COLOR, "Couleur nom entité");
									}
								});
								panel.add(btnCouleurNom, "cell 3 1");
							}
							{
								JLabel lblPoliceProprit = new JLabel("Police propriétés");
								panel.add(lblPoliceProprit, "cell 0 2");
							}
							{
								JButton choisirPProp = new JButton("Choisir");
								choisirPProp.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeFont(PGroupe.ENTITE, PCle.FONT);
										
									}
								});
								panel.add(choisirPProp, "flowx,cell 1 2");
							}
							{
								JButton btnCouleurProp = new JButton("Couleur");
								btnCouleurProp.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.ENTITE, PCle.FONT_NOM_COLOR, "Couleur propriétés entité");
									}
								});
								panel.add(btnCouleurProp, "cell 3 2");
							}
							{
								JLabel lblCouleurFond_1 = new JLabel("Couleur fond");
								panel.add(lblCouleurFond_1, "cell 0 3");
							}
							{
								JButton choisirCFond = new JButton("Choisir");
								choisirCFond.addActionListener(new ActionListener() {									
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.ENTITE, PCle.COLOR, "Couleur fond entité");
									}
								});
								panel.add(choisirCFond, "cell 1 3 3 1");
							}
							{
								JLabel lblCouleurContours = new JLabel("Couleur contours");
								panel.add(lblCouleurContours, "cell 0 4");
							}
							{
								JButton choisirCContour = new JButton("Choisir");
								choisirCContour.addActionListener(new ActionListener() {
									
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.ENTITE, PCle.COLOR_CONTOUR, "Couleur contours entité");
										
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
										changeFont(PGroupe.ENTITE, PCle.FONT_NOM_FOCUS);
										
									}
								});
								panel.add(focusChoisirPNom, "cell 1 1");
							}
							{
								JButton btnCouleurNomFocus = new JButton("Couleur");
								btnCouleurNomFocus.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.ENTITE, PCle.FONT_COLOR_FOCUS, "Couleur nom entité focus");
									}
								});
								panel.add(btnCouleurNomFocus, "cell 2 1");
							}
							{
								JLabel lblPoliceProprits_1 = new JLabel("Police propriétés");
								panel.add(lblPoliceProprits_1, "cell 0 2");
							}
							{
								JButton focusChoisirPProp = new JButton("Choisir");
								focusChoisirPProp.addActionListener(new ActionListener() {
									
									public void actionPerformed(ActionEvent e) {
										changeFont(PGroupe.ENTITE, PCle.FONT_FOCUS);
										
									}
								});
								panel.add(focusChoisirPProp, "cell 1 2");
							}
							{
								JButton btnCouleurPropFocus = new JButton("Couleur");
								btnCouleurPropFocus.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.ENTITE, PCle.FONT_COLOR_FOCUS, "Couleurs propriétés entités focus");
									}
								});
								panel.add(btnCouleurPropFocus, "cell 2 2");
							}
							{
								JLabel lblCouleurFond_2 = new JLabel("Couleur fond");
								panel.add(lblCouleurFond_2, "cell 0 3");
							}
							{
								JButton focusChoisirCFond = new JButton("Choisir");
								focusChoisirCFond.addActionListener(new ActionListener() {
									
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.ENTITE, PCle.COLOR_FOCUS, "Couleur fond entités focus");
										
									}
								});
								panel.add(focusChoisirCFond, "cell 1 3");
							}
							{
								JLabel lblCouleurContours_1 = new JLabel("Couleur contours");
								panel.add(lblCouleurContours_1, "cell 0 4");
							}
							{
								JButton focusChoisirCContour = new JButton("Choisir");
								focusChoisirCContour.addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.ENTITE, PCle.COLOR_CONTOUR_FOCUS, "Couleur contours entités focus");
										
									}
								});
								panel.add(focusChoisirCContour, "cell 1 4");
							}
						}
					}
				}
				{
					JPanel panelRelations = new JPanel();
					tabbedPaneMcd.addTab("Relations", null, panelRelations, null);
					panelRelations.setLayout(new MigLayout("", "[276px,grow]", "[grow]"));
					{
						JSplitPane splitPane = new JSplitPane();
						panelRelations.add(splitPane, "cell 0 0,grow");
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
										changeFont(PGroupe.RELATION, PCle.FONT_NOM);
									}
								});
								panel.add(btnChoisirPNom, "cell 1 1,alignx left,aligny top");
							}
							{
								JButton btnCouleurNom = new JButton("Couleur");
								btnCouleurNom.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.RELATION, PCle.FONT_NOM_COLOR, "Couleur nom relations");
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
										changeFont(PGroupe.RELATION, PCle.FONT);
									}
								});
								panel.add(btnChoisirPPropriete, "cell 1 2,alignx left,aligny top");
							}
							{
								JButton couleurProp = new JButton("Couleur");
								couleurProp.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.RELATION, PCle.FONT_NOM_COLOR, "Couleur propriété relation");
									}
								});
								panel.add(couleurProp, "cell 2 2");
							}
							{
								JLabel lblCouleurFond = new JLabel("Couleur fond");
								panel.add(lblCouleurFond, "cell 0 3,alignx left,aligny top");
							}
							{
								JButton btnChoisirCFond = new JButton("Choisir");
								btnChoisirCFond.addActionListener(new ActionListener() {
									
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.RELATION, PCle.COLOR, "Couleur fond relation");
									}
								});
								panel.add(btnChoisirCFond, "cell 1 3,alignx center,aligny center");
							}
							{
								JLabel lblCouleurContour = new JLabel("Couleur contour");
								panel.add(lblCouleurContour, "cell 0 4,alignx left,aligny top");
							}
							{
								JButton btnChoisirCContour = new JButton("Choisir");
								btnChoisirCContour.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.RELATION, PCle.COLOR_CONTOUR, "Couleur contours relations");
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
										changeFont(PGroupe.RELATION, PCle.FONT_NOM_FOCUS);
									}
								});
								panel.add(focusChoisirPNom, "cell 1 1");
							}
							{
								JButton couleurNomFocus = new JButton("Couleur");
								couleurNomFocus.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.RELATION, PCle.FONT_NOM_COLOR_FOCUS, "Couleur nom relations focus");
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
										changeFont(PGroupe.RELATION, PCle.FONT_FOCUS);
									}
								});
								panel.add(focusChoisirPPropriete, "cell 1 2");
							}
							{
								JButton couleurPropFocus = new JButton("Couleur");
								couleurPropFocus.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.RELATION, PCle.FONT_COLOR_FOCUS, "Couleur propriétés relations focus");
									}
								});
								panel.add(couleurPropFocus, "cell 2 2");
							}
							{
								JLabel label = new JLabel("Couleur fond");
								panel.add(label, "cell 0 3");
							}
							{
								JButton focusChoisirCFond = new JButton("Choisir");
								focusChoisirCFond.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.RELATION, PCle.COLOR_FOCUS, "Couleur fond relation focus");
									}
								});
								panel.add(focusChoisirCFond, "cell 1 3");
							}
							{
								JLabel label = new JLabel("Couleur contour");
								panel.add(label, "cell 0 4");
							}
							{
								JButton focusChoisirCContour = new JButton("Choisir");
								focusChoisirCContour.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										changeColor(PGroupe.RELATION, PCle.COLOR_CONTOUR_FOCUS, "Couleur contours relation focus");
									}
								});
								panel.add(focusChoisirCContour, "cell 1 4");
							}
						}
					}
				}
				{
					JPanel panelCardinalite = new JPanel();
					tabbedPaneMcd.addTab("Cardinalités", null, panelCardinalite, null);
				}
				{
					JPanel panelContraines = new JPanel();
					tabbedPaneMcd.addTab("Contraintes", null, panelContraines, null);
				}
				{
					JPanel panelHeritage = new JPanel();
					tabbedPaneMcd.addTab("Héritages", null, panelHeritage, null);
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
	private void changeColor(PGroupe g, PCle c, String titre){
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		Color color = JColorChooser.showDialog(null, titre, (Color) prefs.get(g, c));
		if(color!=null){
			prefs.set(g, c, color);
		}
	}

}
