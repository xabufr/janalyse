package com.mcd_log.auth;

public enum ContrainteType {
	T,
	X,
	I,
	PLUS{
		public String toString(){
			return "+";
		}
	},
	UNICITE{
		public String toString(){
			return "1";
		}
	},
	EGALITE{
		public String toString(){
			return "=";
		}
	}
}
