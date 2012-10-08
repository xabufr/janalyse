package com.mcd_log.auth;

import java.util.ArrayList;
import java.util.List;

public class Entite {
	private String m_name;
	private String m_commentaire;
	private List<Propriete> m_proprietes;
	
	public Entite(String name){
		setProprietes(new ArrayList<Propriete>());
		
		setName(name);
	}

	public String getName() {
		return m_name;
	}

	public void setName(String m_name) {
		this.m_name = m_name;
	}
	
	public String getCommentaire() {
		return m_commentaire;
	}

	public void setCommentaire(String commentaire) {
		m_commentaire = commentaire;
	}

	public List<Propriete> getProprietes() {
		return m_proprietes;
	}

	public void setProprietes(List<Propriete> proprietes) {
		this.m_proprietes = proprietes;
	}
	
	public void addPropriete(Propriete p){
		m_proprietes.add(p);
	}
	
	public void delPropriete(Propriete p){
		m_proprietes.remove(p);
	}
}
