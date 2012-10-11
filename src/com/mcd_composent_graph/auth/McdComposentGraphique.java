package com.mcd_composent_graph.auth;

import java.awt.Graphics;
import com.mcd_graph.auth.McdGraph;

public abstract class McdComposentGraphique {
	public abstract void dessiner(Graphics g);
	public abstract void setMcd(McdGraph mcd);
	public Boolean isLinkable(){
		return true;
	}
	public Boolean isMovable(){
		return true;
	}
}
