package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

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
	private FormeGeometriqueLigne m_geometrieSimple;
	private ArrayList<FormeGeometriqueLigne> m_geometrieComplexe;
	private Boolean m_registeredEntite;
	private CardinaliteGraphType m_typeDessin;
	private final Point m_posRelation = new Point();
	
	public void setPointA(Point p){
		m_geometrieSimple.setPointA(p);
	}
	public void setPointB(Point p){
		m_geometrieSimple.setPointB(p);
	}
	public Point getPointA(){
		return m_geometrieSimple.getPointA();
	}
	public Point getPointB(){
		return m_geometrieSimple.getPointB();
	}
	public CardinaliteGraph() {
		m_geometrieSimple = new FormeGeometriqueLigne(new Point(), new Point());
		m_needUpdateGraphic=true;
		m_registeredEntite=false;
		m_typeDessin=CardinaliteGraphType.CARDINALITE_COUPE_DOUBLE;
		m_geometrieComplexe = new ArrayList<FormeGeometriqueLigne>();
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
		
		Font font=null;
		if(!m_focus)
			font= prefs.getFont(PGroupe.CARDINALITE, PCle.FONT);
		else
			font= prefs.getFont(PGroupe.CARDINALITE, PCle.FONT_FOCUS);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		dimCardinalite.height = fm.getHeight();
		dimCardinalite.width = fm.stringWidth(m_cardinalite.toString());		
		
		
		this.setPointA(m_entiteGraph.getValidLinkPosition(this));
		this.setPointB(m_posRelation);
		
		Point positionCard = new Point(getPointA().x, getPointA().y);
		Face face = m_entiteGraph.getFace(this);
		switch(face){
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
		
		Point a = getPointA(),
				b = getPointB();
		m_geometrieComplexe.clear();
		
		switch(m_typeDessin){
		case CARDINALITE_NORMALE:
			g.drawLine(getPointA().x, getPointA().y, getPointB().x, getPointB().y);
			break;
		case CARDINALITE_COUPE_SIMPLE:
			if(face==Face.HAUT||face==Face.BAS){
				g.drawLine(getPointA().x, getPointA().y, getPointA().x, getPointB().y);
				g.drawLine(getPointA().x, getPointB().y, getPointB().x, getPointB().y);
				
				m_geometrieComplexe.add(new FormeGeometriqueLigne(a, new Point(a.x,b.y)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(a.x,b.y), b));
			}
			else{
				g.drawLine(getPointA().x, getPointA().y, getPointB().x, getPointA().y);
				g.drawLine(getPointB().x, getPointA().y, getPointB().x, getPointB().y);
				
				m_geometrieComplexe.add(new FormeGeometriqueLigne(a, new Point(b.x,a.y)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(b.x,a.y), b));
			}
			break;
		case CARDINALITE_COUPE_DOUBLE:
			if(face==Face.HAUT||face==Face.BAS){
				g.drawLine(a.x, a.y, a.x, b.y+(-b.y+a.y)/2);
				g.drawLine(a.x, b.y+(-b.y+a.y)/2, b.x, b.y+(-b.y+a.y)/2);
				g.drawLine(b.x, b.y+(-b.y+a.y)/2, b.x, b.y);
				
				m_geometrieComplexe.add(new FormeGeometriqueLigne(a, new Point(a.x, b.y+(-b.y+a.y)/2)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(a.x, b.y+(-b.y+a.y)/2), new Point(b.x, b.y+(-b.y+a.y)/2)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(b.x, b.y+(-b.y+a.y)/2), b));
			}
			else{
				g.drawLine(a.x, a.y, b.x+(-b.x+a.x)/2, a.y);
				g.drawLine(b.x+(-b.x+a.x)/2, a.y, b.x+(-b.x+a.x)/2, b.y);
				g.drawLine(b.x+(-b.x+a.x)/2, b.y, b.x, b.y);
				
				m_geometrieComplexe.add(new FormeGeometriqueLigne(a, new Point(b.x+(-b.x+a.x)/2, a.y)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(b.x+(-b.x+a.x)/2, a.y), new Point(b.x+(-b.x+a.x)/2, b.y)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(b.x+(-b.x+a.x)/2, b.y), b));
			}
			break;
		}
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.FONT_COLOR));
		else
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.FONT_COLOR_FOCUS));
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
		if(m_typeDessin==CardinaliteGraphType.CARDINALITE_NORMALE){
			return m_geometrieSimple.contient(p);
		}
		for(FormeGeometriqueLigne ligne : m_geometrieComplexe){
			if(ligne.contient(p))
				return true;
		}
		return false;
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
	public CardinaliteGraphType getTypeDessin() {
		return m_typeDessin;
	}
	public void setTypeDessin(CardinaliteGraphType typeDessin) {
		m_typeDessin = typeDessin;
	}
}
