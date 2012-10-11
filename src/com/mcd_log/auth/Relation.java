package com.mcd_log.auth;

import java.util.ArrayList;

public class Relation {
	public Relation(){
		this("");
		this.m_cardinalites = new Cardinalite[2];
		this.m_cardinalites[0] = new Cardinalite();
		this.m_cardinalites[1] = new Cardinalite();
		m_proprietes = new ArrayList<Propriete> ();
	}
	public Relation(String nom){
		m_nom=nom;
		this.m_cardinalites = new Cardinalite[2];
		this.m_cardinalites[0] = new Cardinalite();
		this.m_cardinalites[1] = new Cardinalite();
		m_proprietes = new ArrayList<Propriete> ();
	}
	public Relation(Relation r){
		m_nom=r.m_nom;
		m_commentaire=r.m_commentaire;
		m_proprietes = new ArrayList<Propriete> ();
		for(Propriete p : r.m_proprietes){
			m_proprietes.add(new Propriete(p));
		}
	}
	public void copyFrom(Relation r){
		m_nom=r.m_nom;
		m_commentaire=r.m_commentaire;
		m_proprietes=r.m_proprietes;
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
	public ArrayList<Propriete> getProprietes() {
		return m_proprietes;
	}
	public void setProprietes(ArrayList<Propriete> proprietes) {
		m_proprietes = proprietes;
	}
	public void addPropriete(Propriete p){
		m_proprietes.add(p);
	}
	private String m_nom;
	private String m_commentaire;
	private Cardinalite m_cardinalites[];
	private ArrayList<Propriete> m_proprietes;
	
}
