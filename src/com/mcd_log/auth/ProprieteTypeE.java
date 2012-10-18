package com.mcd_log.auth;

public enum ProprieteTypeE {
	NONE,VARCHAR,INT,FLOAT;
	
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
