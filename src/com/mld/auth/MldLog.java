package com.mld.auth;

import java.util.ArrayList;
import java.util.Hashtable;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Cardinalite;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Heritage;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;

public class MldLog {
	private McdGraph m_mcd;
	ArrayList<Entite> m_entites;
	private Boolean m_isValid;
	public MldLog(McdGraph mcd) {
		m_mcd = mcd;
		analyserCreerMldLogique();
	}
	public String getString(){
		String analyse="<html><head></head><body style='font-family:Monospaced;'>";
		for(Entite e : m_entites){
			analyse+="<p>"+e.getName();
			if(!e.getProprietes().isEmpty()){
				analyse+="(";
				Boolean hasPrec=false;
				for(Propriete p : e.getProprietes()){
					if(hasPrec)
						analyse+=", ";
					/*if(p.isClePrimaire()||p instanceof ProprieteCleEtrangere)
						analyse+="<u>";*/
					if(p instanceof ProprieteCleEtrangere){
						Boolean underline=true;
						for(int i=0;i<p.getName().length();++i, underline=!underline){
							if(underline)
								analyse+="<u>"+p.getName().charAt(i)+"</u>";
							else
								analyse+=p.getName().charAt(i);
						}
					}
					else if(p.isClePrimaire())
						analyse+="<u>"+p.getName()+"</u>";
					else
						analyse+=p.getName();
					/*if(p.isClePrimaire()||p instanceof ProprieteCleEtrangere)
						analyse+="</u>";*/
					hasPrec=true;
				}
				analyse+=")";
			}
			analyse+="<br /></p>";
		}
		return analyse+"</body></html>";
	}
	public Boolean isValid(){
		return m_isValid;
	}
	public ArrayList<Entite> getEntites(){
		return m_entites;
	}
	private void analyserCreerMldLogique(){
		m_entites = new ArrayList<Entite>();
		ArrayList<RelationMld> relationsMld = new ArrayList<RelationMld>();
		Hashtable<Object, Object> m_correspondances = new Hashtable<Object, Object>(); //<ancien,nouveau>
		ArrayList<Object> logicObject = m_mcd.getLogic();
				
		for(Object o : logicObject){
			if(o instanceof Entite){
				try {
					Entite e = ((Entite) o).clone();
					m_entites.add(e);
					m_correspondances.put(o, e);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
		for(Object o : logicObject){
			if(o instanceof Heritage){
				HeritageMld heritage = new HeritageMld((Heritage) o, m_correspondances);
				heritage.migrer();
			}
		}
		for(Object o : logicObject){
			if(o instanceof Relation){
				RelationMld r = new RelationMld((Relation) o);
				relationsMld.add(r);
				m_correspondances.put(o, r);
			}
		}
		for(Object o : logicObject){
			if(o instanceof Cardinalite){
				Relation r = ((Cardinalite) o).getRelation();
				Entite e = ((Cardinalite) o).getEntite();
				((RelationMld)m_correspondances.get(r)).addCardinalite(
						(Entite)m_correspondances.get(e),
						((Cardinalite) o).getMin(),
						((Cardinalite) o).getMax(),
						((Cardinalite) o).isRelatif());
			}
		}
		for(RelationMld r : relationsMld){
			if(!r.needToCreateNewEntity()){
				r.migrer();
			}
			else{
				m_entites.add(r.createEntity());
			}
		}
	}
}
