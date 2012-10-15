package com.mcd_log.auth;

import java.util.ArrayList;

public class Heritage {
	public Heritage(HeritageType t){
		setType(t);
		m_enfants = new ArrayList<Entite>();
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
	public void delEnfant(int i){
		m_enfants.remove(i);
	}
	public HeritageType getType() {
		return m_type;
	}
	public void setType(HeritageType type) {
		m_type = type;
	}
	private HeritageType m_type;
	private ArrayList<Entite> m_enfants;
}
