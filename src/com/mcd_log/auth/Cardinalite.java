package com.mcd_log.auth;

public class Cardinalite {
	
	public Cardinalite(int min, int max, Boolean rel){
		setMin(min);
		setMax(max);
		setRelatif(rel);
	}
	public Cardinalite(){
		this(0, -1, false);
	}
	public int getMin() {
		return m_min;
	}
	public void setMin(int m_min) {
		if(m_min>=0)
			this.m_min = m_min;
	}
	public int getMax() {
		return m_max;
	}
	public void setMax(int m_max) {
		if(m_max != 0)
			this.m_max = m_max;
	}
	public Boolean isRelatif() {
		return m_relatif;
	}
	public void setRelatif(Boolean m_relatif) {
		this.m_relatif = m_relatif;
	}
	
	public Entite getEntite() {
		return m_entite;
	}
	public void setEntite(Entite entite) {
		m_entite = entite;
	}

	public Relation getRelation() {
		return m_relation;
	}
	public void setRelation(Relation relation) {
		m_relation = relation;
	}
	
	public String toString(){
		String ret = String.valueOf(m_min)+",";
		if(m_max>0)
			ret+=String.valueOf(m_max);
		else
			ret+="n";
		return ret;
	}

	private Boolean m_relatif;
	private int m_min, m_max; //m_max: (-1) = inf
	private Entite m_entite;
	private Relation m_relation;
}
