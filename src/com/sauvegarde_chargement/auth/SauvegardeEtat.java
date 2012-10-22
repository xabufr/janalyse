package com.sauvegarde_chargement.auth;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.mcd_graph.auth.McdGraph;

public class SauvegardeEtat {
	private List<String> m_nomsMcd;
	private int m_num;
	public SauvegardeEtat(ArrayList<McdGraph> mcds, int num) {
		m_num = num;
		m_nomsMcd = new ArrayList<String>();
		if (mcds != null)
			for (McdGraph m : mcds)
				if (m.isSaved())
					m_nomsMcd.add(m.getFile().getName());
		
		File file = new File("src/save/prog.xml");
		
		try {
			Document doc = arborescence();
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		    sortie.output(doc, new FileOutputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private Document arborescence(){
		Element prog = new Element("Prog");
		Document doc = new Document(prog);
		
		prog.setAttribute(new Attribute("num", String.valueOf(m_num)));
		
		for (String s : m_nomsMcd){
			Element mcd = new Element("Mcd");
			mcd.setAttribute(new Attribute("file", s));
			prog.addContent(mcd);
		}
		
		
		return doc;
	}
	public List<String> getNomsMcd() {
		return m_nomsMcd;
	}
	public void setNomsMcd(List<String> nomsMcd) {
		m_nomsMcd = nomsMcd;
	}

}
