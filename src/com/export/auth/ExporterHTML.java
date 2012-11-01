package com.export.auth;

import javax.imageio.ImageIO;
import javax.swing.JDialog;

import com.dico.auth.DicoLog;
import com.export.auth.ExportSql;
import com.export.auth.Base64.InputStream;
import com.mcd_graph.auth.McdGraph;
import com.mld.auth.MldLog;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.filechooser.FileFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ExporterHTML extends JDialog {
	McdGraph m_mcd;
	public ExporterHTML(McdGraph mcd) {

		m_shCore=getResourceContent("/ressources/javascript/shCore.js",false);
		m_brushSql=getResourceContent("/ressources/javascript/shBrushSql.js",true);
		m_cssSql=getResourceContent("/ressources/css/shCore.css",true);
		m_cssThemeSh=getResourceContent("/ressources/css/shThemeDefault.css", true);
		
		
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
		
		final JCheckBox chckbxSql = new JCheckBox("SQL");
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
						
						String cssHTML = (String) McdPreferencesManager.getInstance().get(PGroupe.HTML, PCle.CSS);
						
						fileWriter.write("<html><head><meta http-equiv='content-type' content='text/html; charset=utf-8'/><style type='text/css'>"+
								cssHTML+
								"</style>" +
								"<style> " +
								m_cssSql+
								"</style>" +
								"<style>" +
								m_cssThemeSh+
								"</style>" +
								"<script type='text/javascript'>\n"+
								m_shCore+
								"</script><script type='text/javascript'>\n" +
								m_brushSql+
								"</script>\n"+
								"<script language='JavaScript' type='text/javascript'>SyntaxHighlighter.config.bloggerMode = true;SyntaxHighlighter.all();</script>"+
								"</head><body>");
						if(chckbxMcd.isSelected()){
							ByteArrayOutputStream os = new ByteArrayOutputStream();
							OutputStream b64 = new com.export.auth.Base64.OutputStream(os);
							ImageIO.write(ExportPng.getImage(m_mcd), "png", b64);
							fileWriter.write("<div class='mcd'><h1>MCD</h1>\n<img src='data:image/png;base64,"+
							os.toString("UTF-8")+
							"'/>\n</div>\n\n");
						}
						if(chckbxDictionnaireDesProprits.isSelected()){
							fileWriter.write("<div class='dico'>\n<h1>Dictionnaire des propriétés</h1>\n"+
									new DicoLog(m_mcd).toHTML()+"\n</div>");
						}
						if(chckbxMld.isSelected()){
							fileWriter.write("<div class='mld'>\n<h1>Troisième forme normale</h1>\n"+
						mld.getHTML()+
						"\n</div>");
						}
						if(chckbxSql.isSelected()){
							ExportSql sql = new ExportSql(m_mcd.getLogicName(), m_mcd);
							fileWriter.write("<div class='sql'><h1>SQL</h1>" +
									"<pre class='brush:sql'>"+
									sql.getSql()+
									"</pre>" +
									"</div>"
									);
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
	
	public String getResourceContent(String r, Boolean n){
		java.io.InputStream is = getClass().getResourceAsStream(r);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String input, ret;
		ret="";
		try {
			while((input = br.readLine())!=null){
				ret+=input;
				if(n)
					ret+="\n";
			}
			br.close();
			is.close();
		} catch (IOException e2) {
		}
		return ret;
	}
	
	private String m_shCore, m_cssSql, m_brushSql, m_cssThemeSh;
}
