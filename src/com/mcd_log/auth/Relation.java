package com.mcd_log.auth;

public class Relation {
	public Relation(){
		this.m_cardinalites = new Cardinalite[2];
		this.m_cardinalites[0] = new Cardinalite();
		this.m_cardinalites[1] = new Cardinalite();
	}
	public String getNom() {
		return m_nom;
	}
	public void setNom(String nom) {
		this.m_nom = nom;
	}
	public String getCommentaire() {
		return m_commentaire;
	}
	public void setCommentaire(String m_commentaire) {
		this.m_commentaire = m_commentaire;
	}
	private String m_nom;
	private String m_commentaire;
	private Cardinalite m_cardinalites[];
	
}
