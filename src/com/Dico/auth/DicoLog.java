package com.Dico.auth;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;

public class DicoLog {
	private Hashtable<String, List<String>> m_lstRelationPropriete;
	private Hashtable<String, List<String>> m_lstEntitePropriete;
	
	public DicoLog(McdGraph mcd) {
		m_lstEntitePropriete = new Hashtable<String, List<String>>();
		m_lstRelationPropriete = new Hashtable<String, List<String>>();
		
		for (McdComposentGraphique c : mcd.getMcdComponents()){
			if (c instanceof EntiteGraph){
				Entite e = ((EntiteGraph)c).getEntite();
				
				List<String> props = new ArrayList<String>();
				for (Propriete p : e.getProprietes()){
					props.add(p.getName());
				}
				m_lstEntitePropriete.put(e.getName(), props);
			}
			else if (c instanceof RelationGraph){
				Relation r = ((RelationGraph)c).getRelation();
				
				List<String> props = new ArrayList<String>();
				for (Propriete p : r.getProprietes()){
					props.add(p.getName());
				}
				m_lstRelationPropriete.put(r.getNom(), props);
			}
		}
	}
	
	public String toString(){
		String dico = "<html><head></head><body>";
		
		Enumeration<String> key = m_lstEntitePropriete.keys();
		while (key.hasMoreElements()){
			String s = key.nextElement();
			dico += "<p>"+s+"</p>";
			dico += "<hr/>";
			
			for (String p : m_lstEntitePropriete.get(s)){
				dico += "<p>"+p+"</p>";
			}
			dico += "<br/>";
		}
		
		key = m_lstRelationPropriete.keys();
		while (key.hasMoreElements()){
			String s = key.nextElement();
			dico += "<p>"+s+"</p>";
			dico += "<hr/>";
			
			for (String p : m_lstRelationPropriete.get(s)){
				dico += "<p>"+p+"</p>";
			}
			dico += "<br/>";
		}
		
		return dico;
	}

	public Hashtable<String, List<String>> getLstRelationPropriete() {
		return m_lstRelationPropriete;
	}

	public void setLstRelationPropriete(Hashtable<String, List<String>> lstRelationPropriete) {
		m_lstRelationPropriete = lstRelationPropriete;
	}

	public Hashtable<String, List<String>> getLstEntitePropriete() {
		return m_lstEntitePropriete;
	}

	public void setLstEntitePropriete(Hashtable<String, List<String>> lstEntitePropriete) {
		m_lstEntitePropriete = lstEntitePropriete;
	}

}
