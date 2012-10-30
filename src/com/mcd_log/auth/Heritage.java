package com.mcd_log.auth;

import java.util.ArrayList;

public class Heritage implements Cloneable{
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
	public Heritage clone() throws CloneNotSupportedException{
		Heritage h = (Heritage) super.clone();
		ArrayList<Entite> enfants = new ArrayList<>();
		for (Entite e : getEnfants())
			enfants.add(e);
		
		h.setEnfants(enfants);
		return h;
	}
	public Entite getMere() {
		return m_mere;
	}
	public void setMere(Entite mere) {
		m_mere = mere;
	}
	private HeritageType m_type;
	private ArrayList<Entite> m_enfants;
	private Entite m_mere;
}
