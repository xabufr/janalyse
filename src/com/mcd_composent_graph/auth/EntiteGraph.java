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
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

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
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		Rectangle rect = getRectangle();
		int x, y, widthMax=0, heightMax=0;
		
		
		/*Calcul taille propriétés*/
		g.setFont(prefs.getFont(PGroupe.ENTITE, PCle.FONT));
		FontMetrics font = g.getFontMetrics();
		ProprieteGraph dessinPropriete = new ProprieteGraph();
		
		for (Propriete propriete : m_entite.getProprietes()){
			if (font.stringWidth(propriete.getName()) > widthMax)
				widthMax = font.stringWidth(propriete.getName());
			
			heightMax+=font.getHeight();
		}
		
		/*Ajout taille nom*/
		g.setFont(prefs.getFont(PGroupe.ENTITE, PCle.FONT_NOM));
		font = g.getFontMetrics();
		
		if (font.stringWidth(m_entite.getName()) > widthMax)
			widthMax = font.stringWidth(m_entite.getName());
		
		widthMax += 10;
		heightMax += font.getHeight() + 4;
		heightMax += 30;
		
		//cadre
		g.setColor((Color) prefs.get(PGroupe.ENTITE, PCle.COLOR));
		g.fillRect((int)rect.getX(), (int)rect.getY(), widthMax, heightMax);
		g.setColor((Color) prefs.get(PGroupe.ENTITE, PCle.COLOR_CONTOUR));
		g.drawRect((int)rect.getX(), (int)rect.getY(), widthMax, heightMax);
		this.setRectangle(new Rectangle((int)rect.getX(), (int)rect.getY(), widthMax, heightMax));
		g.drawLine((int)rect.getX(), ((int)rect.getY()+font.getHeight()+6), ((int)rect.getX()+widthMax), ((int)rect.getY()+font.getHeight()+6));
		//titre
		x = ((int)rect.getX() + (widthMax / 2)) - (font.stringWidth(m_entite.getName()) / 2);
		y = ((int)rect.getY() + font.getHeight());
		g.setColor((Color) prefs.get(PGroupe.ENTITE, PCle.FONT_NOM_COLOR));
		g.drawString(m_entite.getName(), x, y);
		//propriete
		g.setFont(prefs.getFont(PGroupe.ENTITE, PCle.FONT));
		font = g.getFontMetrics();
		x = (int)rect.getX()+5;
		y += font.getHeight()+10;
		for (Propriete propriete : m_entite.getProprietes()){
			dessinPropriete.setPropriete(propriete);
			
			dessinPropriete.dessiner(g,
					prefs.getFont(PGroupe.ENTITE, PCle.FONT),
					(Color) prefs.get(PGroupe.ENTITE, PCle.FONT_COLOR),
					new Point(x, y));
			
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
