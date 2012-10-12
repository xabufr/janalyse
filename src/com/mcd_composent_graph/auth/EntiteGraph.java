package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
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

public class EntiteGraph extends McdComposentGraphique implements FormeGeometrique{
	private Entite m_entite;
	private FormeGeometriqueRectangle m_geometrie;

	public Rectangle getRectangle(){
		return m_geometrie.getRectangle();
	}
	public void setRectangle(Rectangle r){
		m_geometrie.setRectangle(r);
	}
	public Point getPosition(){
		return m_geometrie.getPosition();
	}
	public void setPosition(Point p){
		m_geometrie.setPosition(p);
	}
	public Boolean contient(Point p){
		return m_geometrie.contient(p);
	}
	public Dimension getDimension(){
		return m_geometrie.getDimension();
	}
	public void setDimension(Dimension d){
		m_geometrie.setDimension(d);
	}

	public EntiteGraph() {
		m_geometrie = new FormeGeometriqueRectangle(new Rectangle());
	}

	public void dessiner(Graphics g) {
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		Point pos = getPosition();
		Dimension dim = getDimension();
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
		g.fillRect(pos.x, pos.y, widthMax, heightMax);
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR_FOCUS));
		g.drawRect(pos.x, pos.y, widthMax, heightMax);
		this.setRectangle(new Rectangle(pos.x, pos.y, widthMax, heightMax));
		g.drawLine(pos.x, (pos.y+font.getHeight()+6), (pos.x+widthMax), (pos.y+font.getHeight()+6));
		//titre
		x = (pos.x + (widthMax / 2)) - (font.stringWidth(m_entite.getName()) / 2);
		y = (pos.y + font.getHeight());
		g.setColor((Color) prefs.get(PGroupe.ENTITE, PCle.FONT_NOM_COLOR));
		g.drawString(m_entite.getName(), x, y);
		//propriete
		g.setFont(prefs.getFont(PGroupe.ENTITE, PCle.FONT));
		font = g.getFontMetrics();
		x = pos.x+5;
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
	public void setMcd(McdGraph mcd) {
		if(m_mcd != null){
			m_mcd.removeLogic(m_entite);
		}
		m_mcd=mcd;
		m_mcd.registerLogic(m_entite, this);
	}
	public Boolean isLinkable() {
		return true;
	}
	McdGraph m_mcd;

	public void prepareDelete() {
		m_mcd.removeLogic(m_entite);
		m_entite=null;
	}
}
