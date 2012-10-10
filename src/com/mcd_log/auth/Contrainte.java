package com.mcd_log.auth; 

import java.util.List;

public class Contrainte {
	private String m_nom;
	private List<Entite> m_entites;
	private List<Relation> m_relations;
	
	public Contrainte(ContrainteType t){
		setNom(t.toString());
	}
	
	public String getNom() {
		return m_nom;
	}
	
	public void setNom(String nom) {
		m_nom = nom;
	}

	public List<Entite> getEntites() {
		return m_entites;
	}

	public void setEntites(List<Entite> entites) {
		m_entites = entites;
	}

	public List<Relation> getRelations() {
		return m_relations;
	}

	public void setRelations(List<Relation> relations) {
		m_relations = relations;
	}
}
