package com.mcd_log.auth; 

import java.util.ArrayList;
import java.util.List;

public class Contrainte implements Cloneable{
	private ContrainteType m_type;
	private List<Entite> m_entites;
	private List<Relation> m_relations;
	
	public Contrainte(ContrainteType t){
		setNom(t);
		m_entites = new ArrayList<Entite>();
		m_relations = new ArrayList<Relation>();
	}
	
	public String getNom() {
		return m_type.toString();
	}
	
	public void setNom(ContrainteType nom) {
		m_type = nom;
	}
	public ContrainteType getType(){
		return m_type;
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
	public void addRelation(Relation r){
		m_relations.add(r);
	}
	public void addEntite(Entite e){
		m_entites.add(e);
	}

	public void setRelations(List<Relation> relations) {
		m_relations = relations;
	}
	
	public Contrainte clone() throws CloneNotSupportedException{
		Contrainte c = (Contrainte) super.clone();
		return c;
	}
}
