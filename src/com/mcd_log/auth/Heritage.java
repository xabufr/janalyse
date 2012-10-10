package com.mcd_log.auth;

import java.util.ArrayList;

public class Heritage {
	public Heritage(HeritageType t){
		setType(t);
	}
	public Entite getParent() {
		return m_parent;
	}
	public void setParent(Entite parent) {
		m_parent = parent;
	}
	public ArrayList<Entite> getEnfants() {
		return m_enfants;
	}
	public void setEnfants(ArrayList<Entite> enfants) {
		m_enfants = enfants;
	}
	public void addEnfant(Entite enfant){
		m_enfants.add(enfant);
	}
	public Boolean delEnfant(Entite e){
		return m_enfants.remove(e);
	}
	public HeritageType getType() {
		return m_type;
	}
	public void setType(HeritageType type) {
		m_type = type;
	}
	private HeritageType m_type;
	private Entite m_parent;
	private ArrayList<Entite> m_enfants;
}
