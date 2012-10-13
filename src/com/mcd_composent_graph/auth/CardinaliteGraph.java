package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import com.mcd_composent_graph.auth.EntiteGraph.Face;
import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Cardinalite;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class CardinaliteGraph extends McdComposentGraphique implements FormeGeometrique {
	private Cardinalite m_cardinalite;
	private EntiteGraph m_entiteGraph;
	private RelationGraph m_relationGraph;
	private Boolean m_needUpdateGraphic;
	private FormeGeometriqueLigne m_geometrie;
	private Boolean m_registeredEntite;
	private Point m_posRelation;
	
	public void setPointA(Point p){
		m_geometrie.setPointA(p);
	}
	public void setPointB(Point p){
		m_geometrie.setPointB(p);
	}
	public Point getPointA(){
		return m_geometrie.getPointA();
	}
	public Point getPointB(){
		return m_geometrie.getPointB();
	}
	public CardinaliteGraph() {
		m_geometrie = new FormeGeometriqueLigne(new Point(), new Point());
		m_needUpdateGraphic=true;
		m_registeredEntite=false;
		m_posRelation = new Point();
	}

	public void dessiner(Graphics g) {
		if(m_needUpdateGraphic&&m_mcd!=null){
			if(m_entiteGraph!=null){
				m_entiteGraph.removeLien(this);
			}
			m_relationGraph = (RelationGraph) m_mcd.getGraphicComponent(m_cardinalite.getRelation());
			m_entiteGraph = (EntiteGraph) m_mcd.getGraphicComponent(m_cardinalite.getEntite());
			m_entiteGraph.addLien(this, m_posRelation);
			m_needUpdateGraphic=false;
		}
		if(m_relationGraph==null||m_entiteGraph==null)
			return;
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		
		Rectangle rec=m_relationGraph.getRectangle();
		
		m_posRelation.x=rec.x+rec.width/2;
		m_posRelation.y=rec.y+rec.height/2;
		
		
		Dimension dimCardinalite = new Dimension();
		
		Font font = prefs.getFont(PGroupe.CARDINALITE, PCle.FONT);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		dimCardinalite.height = fm.getHeight();
		dimCardinalite.width = fm.stringWidth(m_cardinalite.toString());		
		
		
		this.setPointA(m_entiteGraph.getValidLinkPosition(this));
		this.setPointB(m_posRelation);
		
		Point positionCard = new Point(getPointA().x, getPointA().y);
		switch(m_entiteGraph.getFace(this)){
		case BAS:
			positionCard.y+=4+dimCardinalite.height;
		case HAUT:
			positionCard.x-=dimCardinalite.width/2;
			positionCard.y-=2;
			break;
		
		case GAUCHE:
			positionCard.x-=2+dimCardinalite.width;
		case DROITE:
			positionCard.y+=dimCardinalite.height/2;
			positionCard.x+=2;
			break;
		}
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR_FOCUS));
		g.drawLine(getPointA().x, getPointA().y, getPointB().x, getPointB().y);
		g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.FONT_COLOR));
		g.drawString(m_cardinalite.toString(), positionCard.x, positionCard.y);
	}

	public Cardinalite getCardinalite() {
		return m_cardinalite;
	}

	public void setCardinalite(Cardinalite cardinalite) {
		m_cardinalite = cardinalite;
		m_needUpdateGraphic=true;
	}

	public void setMcd(McdGraph mcd) {
		if(m_mcd!=null)
			m_mcd.removeLogic(m_cardinalite);
		m_mcd=mcd;
		m_mcd.registerLogic(m_cardinalite, this);
		m_needUpdateGraphic=true;
	}
	public Boolean isLinkable() {
		return false;
	}
	public Boolean isMovable(){
		return false;
	}

	public Boolean contient(Point p) {
		return m_geometrie.contient(p);
	}

	public void setPosition(Point p) {
		
	}

	public Point getPosition() {
		return null;
	}
	public void prepareDelete() {
		m_mcd.removeLogic(m_cardinalite);
		if(m_registeredEntite)
			m_entiteGraph.removeLien(this);
		m_cardinalite.setEntite(null);
		m_cardinalite.setRelation(null);
		m_cardinalite=null;
	}
}
