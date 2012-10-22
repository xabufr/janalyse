package com.mld.auth;

import java.util.Hashtable;

import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Heritage;

public class HeritageMld extends Heritage {

	public HeritageMld(Heritage h, Hashtable<Object, Object> correspondances) {
		super(h.getType());
		for(Entite e :h.getEnfants()){
			addEnfant((Entite) correspondances.get(e));
		}
		if(h.getMere()!=null)
			setMere((Entite) correspondances.get(h.getMere()));
	}
	public void migrer(){
		Entite mere = getMere();
		if(mere==null)
			return;
		for(Entite e : getEnfants()){
			e.addPropriete(new ProprieteCleEtrangere(mere, false));
		}
	}
}
