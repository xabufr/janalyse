package com.mcd_log.auth; 

public class Contrainte {
	private String m_nom;
	
	public Contrainte(String nom){
		setNom(nom);
	}
	
	public String getNom() {
		return m_nom;
	}
	
	public void setNom(String nom) {
		m_nom = nom;
	}
}
