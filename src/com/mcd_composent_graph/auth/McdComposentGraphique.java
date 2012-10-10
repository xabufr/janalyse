package com.mcd_composent_graph.auth;

import java.awt.Graphics;
import com.mcd_graph.auth.McdGraph;

public interface McdComposentGraphique {
	public void dessiner(Graphics g);
	public void setMcd(McdGraph mcd);
	public Boolean isLinkable();
}
