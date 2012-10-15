package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Propriete;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class EntiteGraph extends McdComposentGraphique implements FormeGeometrique{
	private Entite m_entite;
	private FormeGeometriqueRectangle m_geometrie;
	private Hashtable<Face, ArrayList<McdComposentGraphique>> m_liens;
	private Hashtable<McdComposentGraphique, Point> m_liensB;
	private Hashtable<Face, Integer> m_nombreParFace;
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
		m_liens = new Hashtable<EntiteGraph.Face, ArrayList<McdComposentGraphique>>();
		m_liensB = new Hashtable<McdComposentGraphique, Point>();
		for(Face f : Face.values()){
			m_liens.put(f, new ArrayList<McdComposentGraphique>());
		}
		m_nombreParFace = new Hashtable<EntiteGraph.Face, Integer>();
	}

	public void dessiner(Graphics g) {
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		Point pos = getPosition();
		int x, y, widthMax=0, heightMax=0;
		
		
		/*Calcul taille propriétés*/
		if(!m_focus)
			g.setFont(prefs.getFont(PGroupe.ENTITE, PCle.FONT));
		else
			g.setFont(prefs.getFont(PGroupe.ENTITE, PCle.FONT_FOCUS));
		FontMetrics font = g.getFontMetrics();
		ProprieteGraph dessinPropriete = new ProprieteGraph();
		
		for (Propriete propriete : m_entite.getProprietes()){
			if (font.stringWidth(propriete.getName()) > widthMax)
				widthMax = font.stringWidth(propriete.getName());
			
			heightMax+=font.getHeight();
		}
		
		/*Ajout taille nom*/
		if(!m_focus)
			g.setFont(prefs.getFont(PGroupe.ENTITE, PCle.FONT_NOM));
		else
			g.setFont(prefs.getFont(PGroupe.ENTITE, PCle.FONT_NOM_FOCUS));
		font = g.getFontMetrics();
		
		if (font.stringWidth(m_entite.getName()) > widthMax)
			widthMax = font.stringWidth(m_entite.getName());
		
		widthMax += 10;
		heightMax += font.getHeight() + 4;
		heightMax += 30;
		
		//cadre
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.ENTITE, PCle.COLOR));
		else
			g.setColor((Color) prefs.get(PGroupe.ENTITE, PCle.COLOR_FOCUS));
		g.fillRect(pos.x, pos.y, widthMax, heightMax);
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.ENTITE, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.ENTITE, PCle.COLOR_CONTOUR_FOCUS));
		g.drawRect(pos.x, pos.y, widthMax, heightMax);
		this.setRectangle(new Rectangle(pos.x, pos.y, widthMax, heightMax));
		g.drawLine(pos.x, (pos.y+font.getHeight()+6), (pos.x+widthMax), (pos.y+font.getHeight()+6));
		//titre
		x = (pos.x + (widthMax / 2)) - (font.stringWidth(m_entite.getName()) / 2);
		y = (pos.y + font.getHeight());
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.ENTITE, PCle.FONT_NOM_COLOR));
		else
			g.setColor((Color) prefs.get(PGroupe.ENTITE, PCle.FONT_NOM_COLOR_FOCUS));
		g.drawString(m_entite.getName(), x, y);
		//propriete
		Font ffont;
		Color col;
		if(!m_focus){
			ffont = prefs.getFont(PGroupe.ENTITE, PCle.FONT);
			col = (Color) prefs.get(PGroupe.ENTITE, PCle.FONT_COLOR);
		}
		else{
			ffont = prefs.getFont(PGroupe.ENTITE, PCle.FONT_FOCUS);
			col = (Color) prefs.get(PGroupe.ENTITE, PCle.FONT_COLOR_FOCUS);
		}
			
		font = g.getFontMetrics(ffont);
		x = pos.x+5;
		y += font.getHeight()+10;
		for (Propriete propriete : m_entite.getProprietes()){
			dessinPropriete.setPropriete(propriete);
			dessinPropriete.dessiner(g,
					ffont,
					col,
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

	public void prepareDelete() {
		m_mcd.removeLogic(m_entite);
		m_entite=null;
	}
	public void addLien(McdComposentGraphique comp, Point p){
		m_liensB.put(comp, p);
	}
	public void removeLien(McdComposentGraphique comp){
		if(m_liensB.contains(comp))
			m_liensB.remove(comp);
		updateLienFaces();
	}
	private void updateLienFaces(){
		for(Face f : Face.values())
			m_nombreParFace.put(f, 0);
		Enumeration<McdComposentGraphique> keys = m_liensB.keys();
		while (keys.hasMoreElements()){
			McdComposentGraphique comp = keys.nextElement();
			Face f = getFace(comp);
			m_nombreParFace.put(f, m_nombreParFace.get(f)+1);
		}
	}
	public Face getFace(McdComposentGraphique comp){
		Point posC = (Point)m_liensB.get(comp);
		Rectangle r = getRectangle();
		Line2D haut = new Line2D.Double(r.x, r.y, r.x+r.width, r.y),
				bas = new Line2D.Double(r.x, r.y+r.height,r.x+r.width,r.y+r.height),
				gauche = new Line2D.Double(r.x, r.y, r.x, r.y+r.height),
				l = new Line2D.Double(posC.x, posC.y, r.x+r.width/2, r.y+r.height/2);
		if(l.intersectsLine(haut)){
			return Face.HAUT;
		}
		else if(l.intersectsLine(bas)){
			return Face.BAS;
		}
		else if(l.intersectsLine(gauche)){
			return Face.GAUCHE;
		}

		return Face.DROITE;
	}
	
	public Point getValidLinkPosition(McdComposentGraphique comp){
		updateLienFaces();
		Face curFace = getFace(comp);
		int nombre = m_nombreParFace.get(curFace);
		Enumeration<McdComposentGraphique> key = m_liensB.keys();
		Point points[] = new Point[nombre];
		points[0]=m_liensB.get(comp);
		Point curPoint = points[0];

		int i=1;
		while(key.hasMoreElements()){
			McdComposentGraphique c = key.nextElement();
			if(c!=comp&&getFace(c)==curFace){
				points[i++] = m_liensB.get(c);
			}
		}
		if(curFace == Face.HAUT||curFace == Face.BAS){
			Arrays.sort(points, new Comparator<Point>(){
				public int compare(Point a, Point b) {
					if(a.x<b.x)
						return-1;
					else if(a.x>=b.x)
						return 1;
					return 0;
				}
			});
		}
		else{
			Arrays.sort(points, new Comparator<Point>(){
				public int compare(Point a, Point b) {
					if(a.y<b.y)
						return-1;
					else if(a.y>=b.y)
						return 1;
					return 0;
				}
			});
		}
		int index;
		for(index=0;index<points.length;++index){
			if(points[index]==curPoint)
				break;
		}
		if(index>=points.length)
			return new Point(0,0);
		Point returnPoint = new Point(getPosition());
		Rectangle r = getRectangle();
		if(curFace==Face.HAUT){
			returnPoint.x+= ((float)(r.width/(nombre+1)))*(index+1);
		}
		else if(curFace==Face.BAS){
			returnPoint.x+= ((float)(r.width/(nombre+1)))*(index+1);
			returnPoint.y+=r.height;
		}
		else if(curFace==Face.GAUCHE){
			returnPoint.y+= ((float)(r.height/(nombre+1)))*(index+1);
		}
		else{
			returnPoint.y+= ((float)(r.height/(nombre+1)))*(index+1);
			returnPoint.x+=r.width;
		}
		return returnPoint;
	}
	
	enum Face{
		HAUT, BAS, GAUCHE, DROITE
	}
}
