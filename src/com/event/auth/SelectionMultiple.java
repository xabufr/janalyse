package com.event.auth;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.mcd_composent_graph.auth.CardinaliteGraph;
import com.mcd_composent_graph.auth.ContrainteGraph;
import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.HeritageGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.RelationGraph;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class SelectionMultiple {
	private Point m_depart;
	private Point m_posCurseur;
	private Rectangle m_rect;
	private boolean m_draw;
	private boolean m_trace;
	private List<McdComposentGraphique> m_composents;
	
	public SelectionMultiple() {
		setDepart(new Point());
		setPosCurseur(new Point());
		setDraw(false);
		setTrace(false);
		m_rect = new Rectangle();
		m_composents = new ArrayList<McdComposentGraphique>();
	}
	
	public void dessiner(Graphics g, List<McdComposentGraphique> composents){
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		Dimension dim = new Dimension();
		
		if (m_depart.x > m_posCurseur.x && m_depart.y > m_posCurseur.y){
			dim.width = m_depart.x - m_posCurseur.x;
			dim.height = m_depart.y - m_posCurseur.y;
			drawRect(g, prefs, m_posCurseur.x, m_posCurseur.y, dim.width, dim.height);
		}
		else if (m_depart.x > m_posCurseur.x && m_depart.y < m_posCurseur.y){
			dim.width = m_depart.x - m_posCurseur.x;
			dim.height = m_posCurseur.y - m_depart.y;
			drawRect(g, prefs, m_posCurseur.x, m_depart.y, dim.width, dim.height);
		}
		else if (m_depart.x < m_posCurseur.x && m_depart.y < m_posCurseur.y){
			dim.width = m_posCurseur.x - m_depart.x;
			dim.height = m_posCurseur.y - m_depart.y;
			drawRect(g, prefs, m_depart.x, m_depart.y, dim.width, dim.height);
		}
		else{
			dim.width = m_posCurseur.x - m_depart.x;
			dim.height = m_depart.y - m_posCurseur.y;
			drawRect(g, prefs, m_depart.x, m_posCurseur.y, dim.width, dim.height);
		}
		
		List<McdComposentGraphique> tmp = new ArrayList();
		for (McdComposentGraphique c : m_composents)
			tmp.add(c);
		
		for (McdComposentGraphique c : tmp){
			if (!contains(c)){
				m_composents.remove(c);
				c.setFocus(false);
			}
		}
		
		for (McdComposentGraphique c : composents){
			if (!(c instanceof CardinaliteGraph) && contains(c) && !m_composents.contains(c)){
				m_composents.add(c);
				c.setFocus(true);
			}
		}
	}
	
	private void drawRect(Graphics g, McdPreferencesManager prefs, int x, int y, int width, int height){
		Graphics2D g2 = (Graphics2D) g;
		float style[] = {5.0f};
		BasicStroke dashed = new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f,
                style,
                0);
		
		m_rect.setBounds(x, y, width, height);
		g.setColor((Color)prefs.get(PGroupe.SELECTEUR, PCle.COLOR_CONTOUR));
		if (!(Boolean)prefs.get(PGroupe.SELECTEUR, PCle.LIGNE_CONTINUE))
			g2.setStroke(dashed);
		
		if ((Boolean)prefs.get(PGroupe.SELECTEUR, PCle.FORME_ARRONDIE))
			g.drawRoundRect(x, y, width, height, 5, 5);
		else
			g.drawRect(x, y, width, height);
	}
	
	private boolean contains(McdComposentGraphique c){
		boolean in = false;
		if (c instanceof EntiteGraph){
			if (m_rect.contains(((EntiteGraph)c).getRectangle()))
				in = true;
		}
		else if (c instanceof RelationGraph){
			if (m_rect.contains(((RelationGraph)c).getRectangle()))
				in = true;
		}
		else if (c instanceof CardinaliteGraph){
			if (m_rect.contains(((CardinaliteGraph)c).getPointA()))
				in = true;
		}
		else if (c instanceof ContrainteGraph){
			if (m_rect.contains(((ContrainteGraph)c).getRectangle()))
				in = true;
		}
		else if (c instanceof HeritageGraph){
			if (m_rect.contains(((HeritageGraph)c).getRectangle()))
				in = true;
		}
		return in;
	}
	
	public List<McdComposentGraphique> getFocus(){
		return m_composents;
	}

	public Point getDepart() {
		return m_depart;
	}

	public void setDepart(Point depart) {
		m_depart = depart;
	}

	public Point getPosCurseur() {
		return m_posCurseur;
	}

	public void setPosCurseur(Point posCurseur) {
		m_posCurseur = posCurseur;
	}

	public boolean isDraw() {
		return m_draw;
	}

	public void setDraw(boolean draw) {
		m_draw = draw;
	}
	
	public void reset(){
		setDepart(new Point());
		setDraw(false);
		setTrace(false);
		setPosCurseur(new Point());
		m_rect = new Rectangle();
		m_composents = new ArrayList<McdComposentGraphique>();
	}
	
	public void resetList(){
		m_composents = new ArrayList<McdComposentGraphique>();
	}

	public boolean isTrace() {
		return m_trace;
	}

	public void setTrace(boolean trace) {
		m_trace = trace;
	}
}
