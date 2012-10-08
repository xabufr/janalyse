package com.mcd_log.auth;

public class Propriete {
	private String m_name;
	private String m_commentaire;
	private Type m_type;
	private int m_taille;
	private boolean m_clePrimaire;
	private boolean m_autoIncrement;
	private boolean m_null;
	
	public Propriete(String name, Type type){
		setType(type);	
		setName(name);
		setTaille(255);
		setClePrimaire(false);
		setAutoIncrement(false);
		setNull(false);
	}
	
	public Propriete(String name, Type type, int taille, boolean clePrimaire, boolean autoIncrement, boolean zero){
		setType(type);	
		setName(name);
		setTaille(taille);
		setClePrimaire(clePrimaire);
		setAutoIncrement(autoIncrement);
		setNull(zero);
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

	public Type getType() {
		return m_type;
	}

	public void setType(Type m_type) {
		this.m_type = m_type;
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
}
