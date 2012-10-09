package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Propriete;

public class EntiteGraph extends FormeGeometriqueRectangle implements McdComposentGraphique{
	private Entite m_entite;

	public EntiteGraph(Rectangle r, String s) {
		super(r);
		m_entite = new Entite(s);
		
		m_entite.addPropriete(new Propriete("argfarzgf", null));
		m_entite.addPropriete(new Propriete("qsdfqdf", null));
		m_entite.addPropriete(new Propriete("za	erfez", null));
		m_entite.addPropriete(new Propriete("qsdfdqds", null));
		m_entite.addPropriete(new Propriete("azefzeaffzaefza", null));
	}

	public void dessiner(Graphics g, Font f, Color c) {
		Rectangle rect = getRectangle();
		int x, y, widthMax=0, heightMax=0;
		FontMetrics font = g.getFontMetrics();
		ProprieteGraph dessinPropriete = new ProprieteGraph();
		
		g.setColor(c);
		g.setFont(f);
		
		for (Propriete propriete : m_entite.getProprietes()){
			if (font.stringWidth(propriete.getName()) > widthMax)
				widthMax = font.stringWidth(propriete.getName());
			
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
		for (Propriete propriete : m_entite.getProprietes()){
			dessinPropriete.setPropriete(propriete);
			dessinPropriete.dessiner(g, f, Color.BLACK, new Point(x, y));
			y += font.getHeight() + 4;
		}
	}
	
	public void setEntite (Entite entite){
		m_entite = entite;
	}
	
	public Entite getEntite(){
		return m_entite;
	}

	@Override
	public void dessiner(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMcd(McdGraph mcd) {
		if(m_mcd != null){
			m_mcd.removeLogic(m_entite);
		}
		m_mcd=mcd;
		m_mcd.registerLogic(m_entite, this);
	}
	McdGraph m_mcd;
}
