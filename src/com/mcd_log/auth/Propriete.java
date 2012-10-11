package com.mcd_log.auth;

public class Propriete implements Cloneable{
	private String m_name;
	private String m_commentaire;
	private ProprieteType m_proprieteType;
	private int m_taille;
	private boolean m_clePrimaire;
	private boolean m_autoIncrement;
	private boolean m_null;
	
	public Propriete(String name, ProprieteType proprieteType){
		setType(proprieteType);	
		setName(name);
		setTaille(255);
		setClePrimaire(false);
		setAutoIncrement(false);
		setNull(false);
	}
	
	public Propriete(String name, ProprieteType proprieteType, int taille, boolean clePrimaire, boolean autoIncrement, boolean zero){
		setType(proprieteType);	
		setName(name);
		setTaille(taille);
		setClePrimaire(clePrimaire);
		setAutoIncrement(autoIncrement);
		setNull(zero);
	}
	public Propriete(Propriete p){
		setType(new ProprieteType(p.m_proprieteType));
		setCommentaire(p.m_commentaire);
		setName(p.m_name);
		setTaille(p.m_taille);
		setClePrimaire(p.m_clePrimaire);
		setAutoIncrement(p.m_autoIncrement);
		setNull(p.m_autoIncrement);
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

	public ProprieteType getType() {
		return m_proprieteType;
	}

	public void setType(ProprieteType m_type) {
		this.m_proprieteType = m_type;
	}

	public int getTaille() {
		return m_taille;
	}

	public void setTaille(int m_taille) {
		this.m_taille = m_taille;
	}

	public boolean isClePrimaire() {
		return m_clePrimaire;
	}

	public void setClePrimaire(boolean m_clePrimaire) {
		this.m_clePrimaire = m_clePrimaire;
	}

	public boolean isAutoIncrement() {
		return m_autoIncrement;
	}

	public void setAutoIncrement(boolean m_autoIncrement) {
		this.m_autoIncrement = m_autoIncrement;
	}

	public boolean isNull() {
		return m_null;
	}

	public void setNull(boolean m_null) {
		this.m_null = m_null;
	}
	public String toString(){
		return m_name;
	}
	
	public Propriete clone() throws CloneNotSupportedException{
		Propriete p = (Propriete) super.clone();
		p.m_proprieteType = new ProprieteType(m_proprieteType);
		return p;
	}
}
