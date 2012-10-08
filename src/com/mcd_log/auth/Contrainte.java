package com.mcd_log.auth;

import java.util.ArrayList;
import java.util.List;

public class Contrainte {
	private String m_nom;
	private List<Entite> m_entites;
	private List<Relation> m_relations;
	
	public Contrainte(String nom){
		setNom(nom);
		setEntites(new ArrayList<Entite>());
		setRelations(new ArrayList<Relation>());
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
	
	public void addEntite(Entite e){
		m_entites.add(e);
	}
	
	public void delEntite(Entite e){
		m_entites.remove(e);
	}

	public List<Relation> getRelations() {
		return m_relations;
	}

	public void setRelations(List<Relation> relations) {
		m_relations = relations;
	}
	
	public void addRelation(Relation r){
		m_relations.add(r);
	}
	
	public void delRelation(Relation r){
		m_relations.remove(r);
	}
}
