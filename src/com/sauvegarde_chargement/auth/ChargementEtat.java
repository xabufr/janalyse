package com.sauvegarde_chargement.auth;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JTabbedPane;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.mcd_graph.auth.FenetrePrincipale;
import com.mcd_graph.auth.McdGraph;

public class ChargementEtat {
	private FenetrePrincipale m_parent;
	
	public ChargementEtat(FenetrePrincipale parent) {
		m_parent = parent;
		
		File file = new File("src/save/prog.xml");
		SAXBuilder sax = new SAXBuilder();
		try {
			Document doc = sax.build(file);
			if (doc != null){
				charger(doc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}
	
	private void charger(Document doc){
		Element racine = doc.getRootElement();
		List<Element> lstMcd = racine.getChildren("Mcd");
		
		for (Element e : lstMcd){
			m_parent.createNewMcd();
			m_parent.getTabs().setSelectedIndex(m_parent.getTabs().getComponents().length-1);
			
			String file = e.getAttributeValue("file");
			
			new Chargement(m_parent.getMcd(), file);
			m_parent.updateMcdNames();
		}
		
		int num = Integer.parseInt(racine.getAttributeValue("num"));
		if (m_parent.getTabs().getComponents().length !=0)
			m_parent.getTabs().setSelectedIndex(num);
	}

}
