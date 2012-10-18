package com.mcd_log.auth;

public enum ProprieteTypeE {
	NONE(""),VARCHAR("VARCHAR"),INT("INT"),FLOAT("FLOAT");
	
	String m_sql;
	private ProprieteTypeE(String sql) {
		m_sql=sql;
	}
	public String toSql(){
		return m_sql;
	}
}
