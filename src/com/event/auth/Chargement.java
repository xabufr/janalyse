package com.event.auth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.mcd_graph.auth.McdGraph;

public class Chargement{

	public Chargement(McdGraph mcd) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter(){
			public boolean accept(File arg0) {
				if(arg0.isDirectory())
					return true;
				String ext = getExtension(arg0);
				if(ext==null)
					return false;
				if(ext.equals("xml"))
					return true;
				return false;
			}

			public String getDescription() {
				return "XML Only";
			}
			
		});
		if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			if(getExtension(file)==null||!getExtension(file).equals("xml"))
				file = new File(file.getAbsolutePath()+".xml");
				SAXBuilder sax = new SAXBuilder();
				try {
					Document doc = sax.build(file);
					charger(mcd, doc);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JDOMException e) {
					e.printStackTrace();
				}
		}
	}
	
	private void charger(McdGraph mcd, Document doc){
		Element racine = doc.getRootElement().getChild("Mcd");
		List<Element> entites = racine.getChild("All-entite").getChildren("Entite");
		List<Element> relations = racine.getChild("All-relation").getChildren("Relation");
		List<Element> cardinalites = racine.getChild("All-cardinalite").getChildren("Cardinalite");
		List<Element> heritages = racine.getChild("All-héritage").getChildren("Héritage");
		List<Element> contraintes = racine.getChild("All-contrainte").getChildren("Contrainte");

		mcd.setName(racine.getAttributeValue("nom"));
		
		for (Element courant : entites){
			
		}
	}
	
	private String getExtension(File f){
		String ext=f.getName();
		
		int index = ext.lastIndexOf(".");
		String ret = null;
		if(index>0&&index<ext.length()-1){
			ret = ext.substring(index+1).toLowerCase();
		}
		
		return ret;
	}

}
