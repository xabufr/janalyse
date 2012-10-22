package com.export.auth;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JDialog;

import com.Dico.auth.DicoLog;
import com.mcd_graph.auth.McdGraph;
import com.mld.auth.MldLog;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.filechooser.FileFilter;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

public class ExporterHTML extends JDialog {
	McdGraph m_mcd;
	public ExporterHTML(McdGraph mcd) {
		m_mcd=mcd;
		setTitle("Exporter en HTML");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 358, 185);
		getContentPane().setLayout(new MigLayout("", "[][]", "[][][][][]"));
		
		JLabel lblSlectionnezLeslments = new JLabel("Sélectionnez les éléments a exporter");
		getContentPane().add(lblSlectionnezLeslments, "cell 0 0 2 1");
		
		final JCheckBox chckbxMcd = new JCheckBox("MCD");
		chckbxMcd.setSelected(true);
		getContentPane().add(chckbxMcd, "cell 0 1");
		
		final JCheckBox chckbxDictionnaireDesProprits = new JCheckBox("Dictionnaire des propriétés");
		chckbxDictionnaireDesProprits.setSelected(true);
		getContentPane().add(chckbxDictionnaireDesProprits, "cell 1 1");
		
		final JCheckBox chckbxMld = new JCheckBox("MLD");
		chckbxMld.setSelected(true);
		getContentPane().add(chckbxMld, "cell 0 2");
		
		JCheckBox chckbxSql = new JCheckBox("SQL");
		chckbxSql.setSelected(true);
		getContentPane().add(chckbxSql, "cell 1 2");
		
		JButton btnAnnuler = new JButton("Annuler");
		btnAnnuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExporterHTML.this.setVisible(false);
			}
		});
		getContentPane().add(btnAnnuler, "cell 0 4");
		
		JButton btnExporter = new JButton("Exporter");
		btnExporter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage image = ExportPng.getImage(m_mcd);
				ExporterHTML.this.setVisible(false);
				
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileFilter(){
					public boolean accept(File arg0) {
						if(arg0.isDirectory())
							return true;
						String ext = ExportPng.getExtension(arg0);
						if(ext==null)
							return false;
						if(ext.equals("html"))
							return true;
						return false;
					}
			
					public String getDescription() {
						return "HTML Only";
					}
					
				});
				if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
					File file=chooser.getSelectedFile();
					FileWriter fileWriter=null;
					try {
						fileWriter = new FileWriter(file);
						MldLog mld = new MldLog(m_mcd);
						
						fileWriter.write("<html><head></head><body>");
						if(chckbxMcd.isSelected()){
							ByteArrayOutputStream os = new ByteArrayOutputStream();
							OutputStream b64 = new com.export.auth.Base64.OutputStream(os);
							ImageIO.write(ExportPng.getImage(m_mcd), "png", b64);
							fileWriter.write("<p class='mcd'><h1>MCD</h1>\n<img src='data:image/png;base64,"+
							os.toString("UTF-8")+
							"'/>\n</p>\n\n");
						}
						if(chckbxDictionnaireDesProprits.isSelected()){
							fileWriter.write("<p class='dico'>\n<h1>Dictionnaire des propriétés</h1>\n"+
									new DicoLog(m_mcd).toString()+"\n</p>");
						}
						if(chckbxMld.isSelected()){
							fileWriter.write("<p class='mld'>\n<h1>Troisième forme normale</h1>\n"+
						mld.getHTML()+
						"\n</p>");
						}
						fileWriter.write("</body></html>");
						fileWriter.close();
					} catch (IOException e1) {
						e1.printStackTrace();
						return;
					}
				}
				
			}
		});
		getContentPane().add(btnExporter, "cell 1 4");
	}
}
