package com.mcd_log.auth;

public class ProprieteType {
	private ProprieteTypeE m_type;
	
	public ProprieteType(ProprieteTypeE type){
		setType(type);
	}
	public ProprieteType(ProprieteType p){
		setType(p.getType());
	}

	public ProprieteTypeE getType() {
		return m_type;
	}

	public void setType(ProprieteTypeE type) {
		m_type = type;
	}
}
