package com.mcd_log.auth;

public class Type {
	private String m_nom;
	private String m_eqSql;
	
	public Type(String nom, String eqSql){
		setNom(nom);
		setEqSql(eqSql);
	}
	
	public String getNom() {
		return m_nom;
	}
	
	public void setNom(String m_nom) {
		this.m_nom = m_nom;
	}
	
	public String getEqSql() {
		return m_eqSql;
	}
	
	public void setEqSql(String m_eqSql) {
		this.m_eqSql = m_eqSql;
	}
}
