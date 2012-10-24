package com.mcd_log.auth;

public enum ProprieteTypeE {
	NONE(0),
	INT(1), BIGINT(1), SMALLINT(1), TINYINT(1),
	VARCHAR(1), CHAR(1),
	FLOAT(2), REAL(2), DOUBLE(2), 
	DATE(0), DATETIME(0), TIME(0), TIMESTAMP(0),
	TEXT(0), BLOB(0);
	
	private int m_taille;
	private ProprieteTypeE(int taille) {
		m_taille=taille;
	}
	public String getName(){
		if(toString()=="NONE")
			return "";
		return toString();
	}
	static public ProprieteTypeE getValue(String s) {
		if(s.equals(""))
			return NONE;
		return valueOf(s);
	}
	public int getNombreTaille(){
		return m_taille;
	}
}
