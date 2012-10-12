package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

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
	}

	public void dessiner(Graphics g) {
		if(m_needUpdateGraphic&&m_mcd!=null){
			m_relationGraph = (RelationGraph) m_mcd.getGraphicComponent(m_cardinalite.getRelation());
			m_entiteGraph = (EntiteGraph) m_mcd.getGraphicComponent(m_cardinalite.getEntite());
			m_needUpdateGraphic=false;
		}
		if(m_relationGraph==null||m_entiteGraph==null)
			return;
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		
		Point a=m_entiteGraph.getPosition(), b = m_relationGraph.getPosition();
		Rectangle r=m_entiteGraph.getRectangle(),
				r2=m_relationGraph.getRectangle();
		a.x+=r.width/2;
		a.y+=r.height/2;
		b.x+=r2.width/2;
		b.y+=r2.height/2;
		
		Line2D l = new Line2D.Double(a,b);
		Line2D haut = new Line2D.Double(r.x, r.y, r.x+r.width, r.y),
				bas = new Line2D.Double(r.x, r.y+r.height,r.x+r.width,r.y+r.height),
				gauche = new Line2D.Double(r.x, r.y, r.x, r.y+r.height),
				droite = new Line2D.Double(r.x+r.width, r.y, r.x+r.width, r.y+r.height)
				;
		a=m_entiteGraph.getPosition();
		Point positionCard = new Point();
		Dimension dimCardinalite = new Dimension();
		
		Font font = prefs.getFont(PGroupe.CARDINALITE, PCle.FONT);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		dimCardinalite.height = fm.getHeight();
		dimCardinalite.width = fm.stringWidth(m_cardinalite.toString());
		
		if(l.intersectsLine(haut)){
			a.x=r.x+r.width/2;
			positionCard=(Point) a.clone();
			positionCard.y-=2;
		}
		else if(l.intersectsLine(bas)){
			a.y=r.y+r.height;
			a.x = r.x+r.width/2;
			positionCard=(Point) a.clone();
			positionCard.y+=dimCardinalite.height+2;
		}
		else if(l.intersectsLine(gauche)){
			a.y=r.y+r.height/2;
			positionCard=(Point) a.clone();
			positionCard.x-=dimCardinalite.width + 2;
		}
		else if(l.intersectsLine(droite)){
			a.y=r.y+r.height/2;
			a.x = r.x+r.width;
			positionCard=(Point) a.clone();
			positionCard.x+= 2;
		}


		
		this.setPointA(a);
		this.setPointB(b);
		
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
	McdGraph m_mcd;

	public Boolean contient(Point p) {
		return m_geometrie.contient(p);
	}

	public void setPosition(Point p) {
		
	}

	public Point getPosition() {
		return null;
	}
}
