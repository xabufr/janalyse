package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.mcd_log.auth.Entite;

public class EntiteGraph extends FormeGeometriqueRectangle implements McdComposentGraphique{
	private Entite m_entite;
	private List<ProprieteGraph> m_proprietesGraph;

	public EntiteGraph(Rectangle r, String s) {
		super(r);
		m_entite = new Entite(s);
		m_proprietesGraph = new ArrayList<ProprieteGraph>();
	}

	public void dessiner(Graphics g, Font f, Color c) {
		Rectangle rect = getRectangle();
		int x, y, widthMax=0, heightMax=0;
		FontMetrics font = g.getFontMetrics();
		
		g.setColor(c);
		g.setFont(f);
		
		for (ProprieteGraph pg : m_proprietesGraph){
			if (font.stringWidth(pg.getPropriete().getName()) > widthMax)
				widthMax = font.stringWidth(pg.getPropriete().getName());
			
			++heightMax;
		}
		
		if (font.stringWidth(m_entite.getName()) > widthMax)
			widthMax = font.stringWidth(m_entite.getName());
		
		widthMax += 10;
		heightMax *= font.getHeight() + 4;
		heightMax += 30;
		//cadre
		g.fillRect((int)rect.getX(), (int)rect.getY(), widthMax, heightMax);
		g.setColor(Color.BLACK);
		g.drawRect((int)rect.getX(), (int)rect.getY(), widthMax, heightMax);
		this.setRectangle(new Rectangle((int)rect.getX(), (int)rect.getY(), widthMax, heightMax));
		g.drawLine((int)rect.getX(), ((int)rect.getY()+30), ((int)rect.getX()+widthMax), ((int)rect.getY()+30));
		//titre
		x = ((int)rect.getX() + (widthMax / 2)) - (font.stringWidth(m_entite.getName()) / 2);
		y = ((int)rect.getY() + (30 / 2)) + (font.getHeight() / 2);
		g.drawString(m_entite.getName(), x, y);
		//propriete
		x = (int)rect.getX()+5;
		y = (int)rect.getY() + 30 + font.getHeight();
		for (ProprieteGraph pg : m_proprietesGraph){
			pg.dessiner(g, f, Color.BLACK, new Point(x, y));
			y += font.getHeight() + 4;
		}
	}
	
	public void addProprieteGraph(ProprieteGraph p){
		m_proprietesGraph.add(p);
		m_entite.addPropriete(p.getPropriete());
	}

	@Override
	public void Dessiner(Graphics g) {
		// TODO Auto-generated method stub
		
	}
}
