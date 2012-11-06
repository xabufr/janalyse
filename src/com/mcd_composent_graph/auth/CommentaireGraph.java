package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class CommentaireGraph extends McdComposentGraphique implements
		FormeGeometrique {
	private LinkedList<String> m_commentaire;
	private FormeGeometriqueRectangle m_rectangle;
	public CommentaireGraph() {
		m_rectangle = new FormeGeometriqueRectangle(new Rectangle());
		m_commentaire = new LinkedList<String>();
		m_commentaire.add("Ceci est un commentaire");
	}
	public Boolean contient(Point p) {
		return m_rectangle.contient(p);
	}
	public void setPosition(Point p) {
		m_rectangle.setPosition(p);
	}
	public Point getPosition() {
		return m_rectangle.getPosition();
	}
	public void dessiner(Graphics g) {
		Dimension dim = new Dimension();
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		boolean degrade = (Boolean) prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.GRADIANT_COLOR);
		Graphics2D g2 = (Graphics2D) g;
		if(m_focus)
			g.setFont(prefs.getFont(PGroupe.COMMENTAIRE_COMPONENT, PCle.FONT_FOCUS));
		else
			g.setFont(prefs.getFont(PGroupe.COMMENTAIRE_COMPONENT, PCle.FONT));
		FontMetrics fm = g.getFontMetrics();
		dim.height = fm.getHeight()*m_commentaire.size();
		for(String s : m_commentaire){
			int w = fm.stringWidth(s);
			if(dim.width<w)
				dim.width=w;
		}
		dim.height+=20;
		dim.width+=20;
		m_rectangle.setDimension(dim);
		if(!degrade){
			if(m_focus)
				g.setColor((Color) prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.COLOR_FOCUS));
			else
				g.setColor((Color) prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.COLOR));
		}
		else{
			GradientPaint paint=null;
			if(!m_focus){
				paint = new GradientPaint(getPosition().x, 0, 
						(Color)prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.COLOR), 
						getPosition().x+m_rectangle.getDimension().width, 0, 
						(Color)prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.COLOR_2));
			}
			else{
				paint = new GradientPaint(getPosition().x, 0, 
						(Color)prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.COLOR_FOCUS), 
						getPosition().x+m_rectangle.getDimension().width, 0, 
						(Color)prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.COLOR_2_FOCUS));
			}
			g2.setPaint(paint);
		}
		g.fillRoundRect(getPosition().x, getPosition().y, dim.width, dim.height, 10, 10);
		if(m_focus)
			g.setColor((Color) prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.COLOR_CONTOUR_FOCUS));
		else
			g.setColor((Color) prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.COLOR_CONTOUR));
		g.drawRoundRect(getPosition().x, getPosition().y, dim.width, dim.height, 10, 10);
		Point cursor = new Point(m_rectangle.getPosition().x+10, m_rectangle.getPosition().y+10);
		if(m_focus)
			g.setColor((Color) prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.FONT_COLOR_FOCUS));
		else
			g.setColor((Color) prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.FONT_COLOR));
		for(String s : m_commentaire){
			g.drawString(s, cursor.x, cursor.y+fm.getHeight());
			cursor.y+=fm.getHeight();
		}
	}
	public void dessinerOmbre(Graphics g) {		
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		if(!(Boolean) prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.OMBRE))
			return;
		g.setColor((Color)prefs.get(PGroupe.COMMENTAIRE_COMPONENT, PCle.OMBRE_COLOR));
		g.fillRoundRect(getPosition().x+5, getPosition().y+5, m_rectangle.getDimension().width, m_rectangle.getDimension().height, 10, 10); 
	}
	public void prepareDelete() {		
	}
	public String getCommentaire() {
		String ret="";
		boolean first=true;
		for(String s : m_commentaire){
			if(!first)
				ret+="\n";
			first=false;
			ret+=s;
		}
		return ret;
	}
	public void setCommentaire(String commentaire) {
		m_commentaire.clear();
		for(String s : commentaire.split("\n")){
			m_commentaire.add(s);
		}
	}
	public Rectangle getRectangle(){
		return (Rectangle) m_rectangle.getRectangle().clone();
	}
}
