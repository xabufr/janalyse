package com.mld.auth;

import java.util.ArrayList;
import java.util.Enumeration;
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
	private String m_erreurs;
	public MldLog(McdGraph mcd) {
		m_erreurs = "";
		m_mcd = mcd;
		m_isValid=true;
		analyserCreerMldLogique();
	}
	public String getErreurs(){
		return m_erreurs;
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
		{
			ArrayList<String> names = new ArrayList<String>();
			for(Object o : logicObject){
				if(o instanceof Entite){
					try {
						Entite e = ((Entite) o).clone();
						m_entites.add(e);
						m_correspondances.put(o, e);
						if(names.contains(e.getName())){
							m_erreurs+="<p>Doublon dans le nom des entités: "+e.getName()+"</p>";
							m_isValid=false;
						}
						names.add(e.getName());
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		for(Object o : logicObject){
			if(o instanceof Heritage){
				HeritageMld heritage = new HeritageMld((Heritage) o, m_correspondances);
				if(heritage.getMere()==null)
				{
					m_isValid=false;
					if(heritage.getEnfants().size()>0){
						m_erreurs+="<p>Les entités ";
						Boolean premier=true;
						for(Entite e:heritage.getEnfants()){
							if(!premier)
								m_erreurs+=", ";
							m_erreurs+=e.getName();
							premier=false;
						}
						m_erreurs+=" n'ont pas d'entité mère</p>";
					}
				}
				heritage.migrer();
			}
		}
		{//Test des clés
			Enumeration<Object> keys = m_correspondances.keys();
			while(keys.hasMoreElements()){
				Entite e = (Entite) keys.nextElement();
				Entite eMld = (Entite) m_correspondances.get(e);
				Boolean hasPK=false;
				for(Propriete p : eMld.getProprietes()){
					if(p instanceof ProprieteCleEtrangere||p.isClePrimaire()){
						hasPK=true;
						break;
					}
				}
				if(!hasPK){
					m_erreurs+="<p>L'entité " + e.getName()+" n'a pas de clé primaire</p>";
					m_isValid=false;
				}
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
			if(!r.isValid()){
				m_isValid=false;
				Enumeration<Object> keys = m_correspondances.keys();
				while(keys.hasMoreElements()){
					Object k = keys.nextElement();
					if(m_correspondances.get(k)==r){
						m_erreurs += "<p>La relation " + ((Relation) k).getNom() + " n'est pas valide</p>";
					}
				}
			}
			if(!r.needToCreateNewEntity()){
				r.migrer();
			}
			else{
				m_entites.add(r.createEntity());
			}
		}
	}
}
