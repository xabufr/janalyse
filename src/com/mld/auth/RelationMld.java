package com.mld.auth;

import java.util.Enumeration;
import java.util.Hashtable;

import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;

public class RelationMld extends Relation {
	Hashtable<Entite, CardinalitePropriete> m_entites;
	int m_min,m_max, m_minMax, m_nb;
	public RelationMld(Relation r) {
		super(r);
		m_entites = new Hashtable<Entite, CardinalitePropriete>();
		m_nb=0;
		m_min=1;
		m_max=1;
		m_minMax = -1;
	}
	public void addCardinalite(Entite e, int min, int max, Boolean rel){
		m_entites.put(e, new CardinalitePropriete(min, max, rel));
	}
	public Boolean needToCreateNewEntity(){
		if(m_nb==2){
			return m_min>1 || m_minMax>1||m_minMax==-1||(m_min==1&&m_max==1);
		}
		return true;
	}
	public void migrer(){
		Enumeration<Entite> keys = m_entites.keys();
		Entite ent = null;
		while(keys.hasMoreElements()){
			ent = keys.nextElement();
			CardinalitePropriete c = m_entites.get(ent);
			if(c.max==1){
				for(Propriete p : super.getProprietes()){
					ent.addPropriete(p);
				}
				Enumeration<Entite> entites = m_entites.keys();
				while(entites.hasMoreElements()){
					Entite curEnt = entites.nextElement();
					if(ent!=curEnt){
						ent.addPropriete(new ProprieteCleEtrangere(curEnt, false));
						break;
					}
				}
				break;
			}
		}
	}
	public Entite createEntity(){
		String nom = getNom();
		Entite ent = new Entite(getNom());
		for(Propriete p : getProprietes()){
			ent.addPropriete(p);
		}
		Enumeration<Entite> entites = m_entites.keys();
		while(entites.hasMoreElements()){
			Entite entite = entites.nextElement();
			nom+="_"+entite.getName();
			ent.addPropriete(new ProprieteCleEtrangere(entite, false));
		}
		ent.setName(nom);
		return ent;
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
			if(m_min>min)
				m_min=min;
			if((m_max<max&&m_max!=-1)||max==-1)
				m_max=max;
			if(m_minMax>max&&max!=-1)
				m_minMax=max;
			if(m_minMax==-1&&max!=-1)
				m_minMax=max;
			++m_nb;
		}
	}
}
