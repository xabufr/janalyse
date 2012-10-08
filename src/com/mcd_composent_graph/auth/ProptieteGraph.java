package com.mcd_composent_graph.auth;

import java.awt.Dimension;

import com.mcd_log.auth.Propriete;

public class ProptieteGraph {

	public ProptieteGraph() {
		
	}
	
	public Propriete getPropriete() {
		return m_propriete;
	}

	public void setPropriete(Propriete propriete) {
		m_propriete = propriete;
	}
	public Dimension getDimension(){
		return new Dimension();
	}

	private Propriete m_propriete;

}
