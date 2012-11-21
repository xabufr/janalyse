package com.mcd_log.auth;

public enum HeritageType {
	XT,
	T,
	X,
	NONE;
	
	public String getName(){
		if (toString() == "NONE")
			return "";
		return toString();
	}
}
