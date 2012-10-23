package com.mcd_log.auth;

public enum ProprieteTypeE {
	NONE,INT, BIGINT, SMALLINT, TINYINT,
	VARCHAR, CHAR,
	FLOAT, REAL, DOUBLE, 
	DATE, DATETIME, TIME, TIMESTAMP,
	TEXT, BLOB;
	
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
}
