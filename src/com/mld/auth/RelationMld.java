package com.mld.auth;

import java.util.Enumeration;
import java.util.Hashtable;

import com.mcd_log.auth.Cardinalite;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;

public class RelationMld extends Relation {
	Hashtable<Entite, CardinalitePropriete> m_entites;
	public RelationMld(Relation r) {
		super(r);
		m_entites = new Hashtable<Entite, CardinalitePropriete>();
	}
	public void addCardinalite(Entite e, int min, int max, Boolean rel){
		m_entites.put(e, new CardinalitePropriete(min, max, rel));
	}
	public Boolean needToCreateNewEntity(){
		for(CardinalitePropriete c : m_entites.values()){
			if(c.max==1)
				return false;
		}
		return true;
	}
	public void migrer(){
		Enumeration<Entite> keys = m_entites.keys();
		while(keys.hasMoreElements()){
			Entite e = keys.nextElement();
			CardinalitePropriete c = m_entites.get(e);
			if(c.max!=1){
				for(Propriete p : getProprietes()){
					e.addPropriete(p);
				}
			}
		}
	}
	private class CardinalitePropriete{
		@SuppressWarnings("unused")
		public int min, max;
		@SuppressWarnings("unused")
		public Boolean relatif;
		public CardinalitePropriete(int m,int a, Boolean r) {
			min=m;
			max=a;
			relatif=r;
		}
	}
}
