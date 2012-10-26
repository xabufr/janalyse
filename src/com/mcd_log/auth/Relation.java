package com.mcd_log.auth;

import java.util.ArrayList;

public class Relation implements Cloneable{
	public Relation(){
		this("");
	}
	public Relation(String nom){
		m_nom=nom;
		m_proprietes = new ArrayList<Propriete> ();
		setCommentaire("");
		m_isCif=false;
	}
	public Relation(Relation r){
		m_nom=r.m_nom;
		m_commentaire=r.m_commentaire;
		m_proprietes = new ArrayList<Propriete> ();
		for(Propriete p : r.m_proprietes){
			m_proprietes.add(new Propriete(p));
		}
		m_isCif=false;
	}
	public void copyFrom(Relation r){
		m_nom=r.m_nom;
		m_commentaire=r.m_commentaire;
		m_proprietes=r.m_proprietes;
	}
	public String getNom() {
		if(!m_isCif)
			return m_nom;
		return "CIF";
	}
	public void setNom(String nom) {
		this.m_nom = nom;
	}
	public String getNomReel(){
		return m_nom;
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
	public Relation clone() throws CloneNotSupportedException{
		Relation r = (Relation) super.clone();
		return r;
	}
	public Boolean isCif(){
		return m_isCif;
	}
	public void setCif(Boolean c){
		m_isCif=c;
	}
	private String m_nom;
	private String m_commentaire;
	private ArrayList<Propriete> m_proprietes;
	private Boolean m_isCif;
}
