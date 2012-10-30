package com.mld.auth;

import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.ProprieteTypeE;

public class ProprieteCleEtrangere extends Propriete {
	private Entite m_entite;
	public ProprieteCleEtrangere(Entite e, Boolean isNullable) {
		super("#"+e.getName(), ProprieteTypeE.INT);
		m_entite=e;
		setNull(isNullable);
	}
	public Entite getEntite() {
		return m_entite;
	}
	public void setEntite(Entite entite) {
		m_entite = entite;
	}

}
