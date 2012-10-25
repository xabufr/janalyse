package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
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
	private FormeGeometriqueRectangle m_geometreCoupe[];
	private Boolean m_registeredEntite;
	private CardinaliteGraphType m_typeDessin;
	private Boolean m_estCoupe;
	private final Point m_posRelation = new Point();
	static private String m_lastCarac;
	
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
		m_estCoupe=false;
		m_typeDessin=CardinaliteGraphType.CARDINALITE_COUPE_DOUBLE;
		m_geometrieComplexe = new ArrayList<FormeGeometriqueLigne>();
		m_geometreCoupe = new FormeGeometriqueRectangle[2];
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
			m_geometrieComplexe.add(m_geometrieSimple);
			break;
		case CARDINALITE_COUPE_SIMPLE:
			if(face==Face.HAUT||face==Face.BAS){
				m_geometrieComplexe.add(new FormeGeometriqueLigne(a, new Point(a.x,b.y)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(a.x,b.y), b));
			}
			else{
				m_geometrieComplexe.add(new FormeGeometriqueLigne(a, new Point(b.x,a.y)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(b.x,a.y), b));
			}
			break;
		case CARDINALITE_COUPE_DOUBLE:
			if(face==Face.HAUT||face==Face.BAS){
				m_geometrieComplexe.add(new FormeGeometriqueLigne(a, new Point(a.x, b.y+(-b.y+a.y)/2)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(a.x, b.y+(-b.y+a.y)/2), new Point(b.x, b.y+(-b.y+a.y)/2)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(b.x, b.y+(-b.y+a.y)/2), b));
			}
			else{
				m_geometrieComplexe.add(new FormeGeometriqueLigne(a, new Point(b.x+(-b.x+a.x)/2, a.y)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(b.x+(-b.x+a.x)/2, a.y), new Point(b.x+(-b.x+a.x)/2, b.y)));
				m_geometrieComplexe.add(new FormeGeometriqueLigne(new Point(b.x+(-b.x+a.x)/2, b.y), b));
			}
			break;
		}
		if(findIntersection()){
			m_estCoupe=true;
			drawCoupe(g, face);
		}
		else{
			m_estCoupe=false;
			for(FormeGeometriqueLigne ligne : m_geometrieComplexe){
				g.drawLine(ligne.getPointA().x, ligne.getPointA().y, ligne.getPointB().x, ligne.getPointB().y);
			}
		}
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.FONT_COLOR));
		else
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.FONT_COLOR_FOCUS));
		g.drawString(m_cardinalite.toString(), positionCard.x, positionCard.y);
	}
	private void drawCoupe(Graphics g, Face f){
		int distance=30;
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		Point centreLettre = new Point(getPointA());
		Point posRelation = new Point(getPointB());
		
		double angle = angle(centreLettre, posRelation, new Point(posRelation.x+100, posRelation.y));
		Point posLettre = new Point();
		
		
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(getPointA().x, getPointA().y);
		g2.rotate(-angle);
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR_FOCUS));
		g2.drawLine(distance, 0, 0, 0);
		g2.translate(distance,0);
		g2.rotate(angle);
		g2.getTransform().transform(new Point(0,0), posLettre);
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.FONT_COLOR));
		else
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.FONT_COLOR_FOCUS));
		m_geometreCoupe[0] = new FormeGeometriqueRectangle(new Rectangle(posLettre));
		m_geometreCoupe[0].setDimension(drawLetter(g, new Point(0,0), m_lastCarac));
		g2.rotate(-angle);
		g2.translate(-distance,0);
		g2.rotate(angle);
		g2.translate(-getPointA().x, -getPointA().y);
		
		distance+=m_relationGraph.getDimension().width/2;
		
		g2.translate(getPointB().x, getPointB().y);
		g2.rotate(-angle+3.14);
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR_FOCUS));
		g2.drawLine(distance, 0, 0, 0);
		g2.translate(distance,0);
		g2.rotate(angle-3.14);
		posLettre = new Point();
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.FONT_COLOR));
		else
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.FONT_COLOR_FOCUS));
		g2.getTransform().transform(new Point(0,0), posLettre);
		m_geometreCoupe[1] = new FormeGeometriqueRectangle(new Rectangle(posLettre));
		m_geometreCoupe[1].setDimension(drawLetter(g, new Point(0,0), m_lastCarac));
		g2.rotate(-angle+3.14);
		g2.translate(-distance,0);
		g2.rotate(angle-3.14);
		g2.translate(-getPointB().x, -getPointB().y);
		incermenterCompteurLettre();
		
		for(int i=0;i<2;++i){
			m_geometreCoupe[i].setPosition(new Point(
					m_geometreCoupe[i].getPosition().x-m_geometreCoupe[i].getDimension().width/2,
					m_geometreCoupe[i].getPosition().y-m_geometreCoupe[i].getDimension().height/2));
		}
	}
	private double angle(Point p1, Point p2, Point p3){
		double a, a1, a2;
		
		a1 = Math.atan2(p1.y-p3.y, p1.x-p3.x);
		a2 = Math.atan2(p2.y-p3.y, p2.x-p3.x);
		a = a2 - a1;
		
		return a;
	}
	private Dimension drawLetter(Graphics g, Point centre, String letter){
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		int height = g.getFontMetrics().getHeight()+4;
		g.setColor(Color.white);
		g.fillOval(centre.x-height/2, centre.y-height/2, height, height);
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR_FOCUS));
		g.drawOval(centre.x-height/2, centre.y-height/2, height, height);
		g.drawString(letter, centre.x+4-height/2, centre.y+height/3);
		return new Dimension(height,height);
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
		if(!m_estCoupe){
			for(FormeGeometriqueLigne ligne : m_geometrieComplexe){
				if(ligne.contient(p))
					return true;
			}
		}
		else{
			for(int i=0;i<2;++i){
				if(m_geometreCoupe[i].contient(p))
					return true;
			}
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
	private Boolean intersects(CardinaliteGraph other){
		for(FormeGeometriqueLigne mesFomes : m_geometrieComplexe){
			for(FormeGeometriqueLigne sesFormes : other.m_geometrieComplexe){
				if(mesFomes.intersect(sesFormes))
					return true;
			}
		}
		return false;
	}
	private boolean intersects(Line2D l){
		for(FormeGeometriqueLigne mesFomes : m_geometrieComplexe){
			if(mesFomes.getLine2D().intersectsLine(l))
				return true;
		}
		return false;
	}
	private boolean intersects(Rectangle r){
		Rectangle2D rec = new Rectangle2D.Double(r.x, r.y, r.width, r.height);
		for(FormeGeometriqueLigne forme : m_geometrieComplexe){
			if(rec.intersectsLine(forme.getLine2D()))
				return true;
		}
		return false;
	}
	private boolean intersects(HeritageGraph her){
		if(intersects(her.getRectangle()))
			return true;
		for(Line2D line  :her.getLignesLiens()){
			if(intersects(line))
				return true;
		}
		return false;
	}
	private boolean intersects(ContrainteGraph cont){
		if(intersects(cont.getRectangle()))
			return true;
		for(Line2D l : cont.getLigneLiens()){
			if(intersects(l))
				return true;
		}
		return false;
	}
	private boolean findIntersection(){
		ArrayList<McdComposentGraphique> comps = m_mcd.getMcdComponents();
		for(McdComposentGraphique comp : comps){
			if(comp instanceof CardinaliteGraph){
				CardinaliteGraph c = (CardinaliteGraph) comp;
				if(c==this||c.m_estCoupe||c.m_cardinalite.getRelation()==m_cardinalite.getRelation())
					continue;
				if(c.intersects(this))
					return true;
			}
			else if(comp instanceof EntiteGraph){
				if(comp != m_entiteGraph && intersects(((EntiteGraph) comp).getRectangle()))
					return true;
			}
			else if(comp instanceof RelationGraph){
				if(comp != m_relationGraph && intersects(((RelationGraph) comp).getRectangle()))
					return true;
			}
			else if(comp instanceof HeritageGraph){
				if(intersects((HeritageGraph)comp))
					return true;
			}
			else if(comp instanceof ContrainteGraph){
				if(intersects((ContrainteGraph)comp))
					return true;
			}
		}
		return false;
	}
	public static void resetCompteurLettre(){
		m_lastCarac="A";
	}
	private static void incermenterCompteurLettre(){
		char carac= m_lastCarac.charAt(0);
		m_lastCarac=String.valueOf(++carac);
	}

	public void dessinerOmbre(Graphics g) {}
}
