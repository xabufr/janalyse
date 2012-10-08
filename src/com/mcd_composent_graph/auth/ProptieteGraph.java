package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

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
	public Dimension getDimension(Graphics g, Font f){
		FontMetrics metric = g.getFontMetrics(f);
		int width = metric.stringWidth(m_propriete.getName());
		int height = metric.getHeight();
		return new Dimension(width, height);
	}
	
	public void dessiner(Graphics g, Font f, Color c)
	{
		g.setColor(c);
		
	}

	private Propriete m_propriete;

}
