package com.mcd_composent_graph.auth;

import java.awt.Graphics;
import com.mcd_graph.auth.McdGraph;

public abstract class McdComposentGraphique {
	protected Boolean m_focus;
	protected McdGraph m_mcd;
	public abstract void dessiner(Graphics g);
	public abstract void dessinerOmbre(Graphics g);
	public abstract void prepareDelete();
	
	public void setMcd(McdGraph mcd){
		m_mcd=mcd;
	}
	public McdComposentGraphique(){
		m_focus=false;
	}
	public Boolean isLinkable(){
		return true;
	}
	public Boolean isMovable(){
		return true;
	}
	public void setFocus(Boolean f){
		m_focus=f;
	}
	public Boolean getFocus(){
		return m_focus;
	}
	
}
