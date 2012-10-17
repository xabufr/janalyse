package com.mcd_composent_graph.auth;

public enum CardinaliteGraphType {
	CARDINALITE_NORMALE{
		public String toString(){
			return "Normal";
		}
	},
	CARDINALITE_COUPE_SIMPLE{
		public String toString(){
			return "Une cassure";
		}
	},
	CARDINALITE_COUPE_DOUBLE{
		public String toString(){
			return "Deux cassures";
		}
	}
}
