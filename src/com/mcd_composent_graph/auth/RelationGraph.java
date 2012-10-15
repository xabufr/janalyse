package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class RelationGraph extends McdComposentGraphique implements FormeGeometrique{

	private Relation m_relation;
	private int m_lastPropsNumber, m_heightNom, m_widthNom;
	private ArrayList<ProprieteGraph> m_proprietes;
	private FormeGeometriqueRectangle m_geometrie;
	private Boolean m_calculerTaille;

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
	public RelationGraph() {
		m_geometrie = new FormeGeometriqueRectangle(new Rectangle());
		m_proprietes = new ArrayList<ProprieteGraph>();
		m_lastPropsNumber=-999;
		m_heightNom = 0;
		m_focus=false;
		m_calculerTaille=true;
	}
	public void actualiser(){
		m_proprietes.clear();
		List<Propriete> props = m_relation.getProprietes();
		this.m_lastPropsNumber=props.size();
		for(int i=0;i<m_lastPropsNumber;++i)
		{
			ProprieteGraph _p = new ProprieteGraph();
			_p.setPropriete(props.get(i));
			m_proprietes.add(_p);
		}
		m_calculerTaille=true;
	}
	public void setRelation(Relation rel){
		m_relation=rel;
		this.m_lastPropsNumber=-999;
	}
	public Relation getRelation(){
		return m_relation;
	}
	public void dessiner(Graphics g) {
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		if(this.m_lastPropsNumber!=this.m_relation.getProprietes().size()||m_calculerTaille)
		{
			this.actualiser();
	
			Dimension dim = new Dimension(0,0), dimEC;
			Font font;
			if(!m_focus)
				font = prefs.getFont(PGroupe.RELATION, PCle.FONT_NOM);
			else
				font = prefs.getFont(PGroupe.RELATION, PCle.FONT_NOM_FOCUS);
			
			
			FontMetrics metric = g.getFontMetrics(font);
			m_widthNom = dim.width = metric.stringWidth(m_relation.getNom());
			m_heightNom = metric.getHeight();
			
			if(!m_focus)
				font = prefs.getFont(PGroupe.RELATION, PCle.FONT);
			else
				font = prefs.getFont(PGroupe.RELATION, PCle.FONT_FOCUS);
			
			for(int i=0;i<m_lastPropsNumber;++i){
				dimEC=m_proprietes.get(i).getDimension(g, font);
				if(dimEC.width>dim.width)
					dim.width=dimEC.width;
				
				dim.height+=dimEC.height+4;
			}
			
			dim.height *= 2;
			dim.width += 60;
			setDimension(dim);
			m_calculerTaille=false;
		}
		Point pos = getPosition();
		Dimension dim = getDimension();
		
		if (dim.height == 0)
			dim.height = dim.width/2;
		
		if (dim.height > dim.width)
			dim.width = dim.height*2;
		
		setDimension(dim);
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR));
		else
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_FOCUS));
		
		g.fillOval(pos.x, pos.y, dim.width, dim.height);
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR_FOCUS));
		g.drawOval(pos.x, pos.y, dim.width, dim.height);
		
		Point cursor = new Point(pos);
		cursor.x += (dim.width/2)-(m_widthNom/2);
		cursor.y += (dim.height/4)+(m_heightNom/2);
		if(!m_focus){
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.FONT_NOM_COLOR));
			g.setFont(prefs.getFont(PGroupe.RELATION, PCle.FONT_NOM));
		}
		else{
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.FONT_NOM_COLOR_FOCUS));
			g.setFont(prefs.getFont(PGroupe.RELATION, PCle.FONT_NOM_FOCUS));
		}
		g.drawString(m_relation.getNom(), cursor.x, cursor.y);
		cursor.y = pos.y+(dim.height/2);
		
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR_FOCUS));
		g.drawLine(pos.x, cursor.y, pos.x+dim.width, cursor.y);
		
		Font font;
		Color couleur;
		if(!m_focus){
			font = prefs.getFont(PGroupe.RELATION, PCle.FONT);
			couleur = (Color) prefs.get(PGroupe.RELATION, PCle.FONT_COLOR);
		}
		else{
			font = prefs.getFont(PGroupe.RELATION, PCle.FONT_FOCUS);
			couleur = (Color) prefs.get(PGroupe.RELATION, PCle.FONT_COLOR_FOCUS);
		}
		Dimension dimEC;
		cursor.y-=3;
		for(int i=0;i<this.m_lastPropsNumber;++i){
			dimEC = this.m_proprietes.get(i).getDimension(g, font);
			cursor.y+= 2 + dimEC.height;
			cursor.x = pos.x+(dim.width/2)-(dimEC.width/2);
			this.m_proprietes.get(i).dessiner(g, font, couleur, cursor);
		}

	}
	public Boolean isLinkable() {
		return true;
	}
	public void setMcd(McdGraph mcd) {
		if(m_mcd!=null)
			m_mcd.removeLogic(m_relation);
		m_mcd=mcd;
		m_mcd.registerLogic(m_relation, this);
	}

	public void prepareDelete() {
		m_mcd.removeLogic(m_relation);
		m_relation.setProprietes(null);
		m_relation=null;
	}
	
	public void setFocus(Boolean f){
		super.setFocus(f);
		m_calculerTaille=true;
	}
}
