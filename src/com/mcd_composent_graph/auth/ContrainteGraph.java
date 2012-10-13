package com.mcd_composent_graph.auth;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;


import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Contrainte;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Relation;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class ContrainteGraph extends McdComposentGraphique implements FormeGeometrique{
	private Contrainte m_contrainte;
	private List<EntiteGraph> m_entiteGraph;
	private List<RelationGraph> m_relationGraph;
	private Boolean m_needUpdateGraphic;
	private McdGraph m_mcd;
	private FormeGeometriqueRectangle m_geometrie;
	private final Point m_centre = new Point();

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
	public ContrainteGraph(){
		m_geometrie = new FormeGeometriqueRectangle(new Rectangle());
		m_entiteGraph = new ArrayList<EntiteGraph>();
		m_relationGraph = new ArrayList<RelationGraph>();
		m_needUpdateGraphic = true;
		
	}
	public void update(){
		m_needUpdateGraphic=true;
	}
	
	public void dessiner(Graphics g) {
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		Dimension dim = new Dimension(0,0);
		Point pos = getPosition();
		FontMetrics font = g.getFontMetrics();
		Graphics2D g2 = (Graphics2D) g;
		float style[] = {5.0f};
		BasicStroke dashed = new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f,
                style,
                0); 
		
		if(m_needUpdateGraphic&&m_mcd!=null){
			for(EntiteGraph e : m_entiteGraph){
				e.removeLien(this);
			}
			m_entiteGraph.clear();
			for (Relation r : m_contrainte.getRelations())
				m_relationGraph.add((RelationGraph) m_mcd.getGraphicComponent(r));
			
			for (Entite e : m_contrainte.getEntites()){
				EntiteGraph ent = (EntiteGraph) m_mcd.getGraphicComponent(e);
				m_entiteGraph.add(ent);
				ent.addLien(this, m_centre);
			}
			
			m_needUpdateGraphic=false;
		}
		
		dim.width = dim.height = 30;
		
		m_centre.setLocation(getPosition());
		Point centreObjet;
		
		m_centre.x += dim.width / 2;
		m_centre.y += dim.height / 2;
		
		if (m_contrainte.getNom().equals("T") || m_contrainte.getNom().equals("+") || m_contrainte.getNom().equals("1") || m_contrainte.getNom().equals("X")){
			g.setColor((Color)prefs.get(PGroupe.CONTRAINTE, PCle.COLOR_LINE));
			for (RelationGraph r : m_relationGraph){
				centreObjet = r.getPosition();
				centreObjet.x += r.getDimension().width / 2;
				centreObjet.y += r.getDimension().height / 2;
				
				g.drawLine(m_centre.x, m_centre.y, centreObjet.x, centreObjet.y);
			}
			
			g2.setStroke(dashed);
			
			for (EntiteGraph e : m_entiteGraph){
				Point p = e.getValidLinkPosition(this);
				g2.draw(new Line2D.Double(m_centre.x, m_centre.y, p.x, p.y));
			}
			g2.setStroke(new BasicStroke(1.0f));
		}
		
		g.setColor((Color)prefs.get(PGroupe.CONTRAINTE, PCle.COLOR));
		g.fillOval(pos.x, pos.y, dim.width, dim.height);
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR_FOCUS));
		g.drawOval(pos.x, pos.y, dim.width, dim.height);
		
		this.setDimension(dim);
		
		if (m_contrainte.getNom().equals("T") || m_contrainte.getNom().equals("X") || m_contrainte.getNom().equals("1")){
			pos.x += (dim.width/2)-(font.getStringBounds(m_contrainte.getNom(), g).getWidth()/3);
			pos.y += (dim.height/2)+(font.getStringBounds(m_contrainte.getNom(), g).getHeight()/2)-1;
		}
		else if (m_contrainte.getNom().equals("I") || m_contrainte.getNom().equals("=")){
			pos.x += (dim.width/2)-(font.getStringBounds(m_contrainte.getNom(), g).getWidth()/3)+1;
			pos.y += (dim.height/2)+(font.getStringBounds(m_contrainte.getNom(), g).getHeight()/2)-1;
		}
		else if (m_contrainte.getNom().equals("+") || m_contrainte.getNom().equals("1")){
			pos.x += (dim.width/2)-(font.getStringBounds(m_contrainte.getNom(), g).getWidth()/3)-1;
			pos.y += (dim.height/2)+(font.getStringBounds(m_contrainte.getNom(), g).getHeight()/2)-1;
		}
		
		g.setFont((Font)prefs.getFont(PGroupe.CONTRAINTE, PCle.FONT));
		g.setColor((Color)prefs.get(PGroupe.CONTRAINTE, PCle.FONT_COLOR));
		g.drawString(m_contrainte.getNom(), pos.x, pos.y);
	}

	public void setMcd(McdGraph mcd) {
		if(m_mcd!=null)
			m_mcd.removeLogic(m_contrainte);
		m_mcd=mcd;
		m_mcd.registerLogic(m_contrainte, this);
		m_needUpdateGraphic=true;
	}

	public Contrainte getContrainte() {
		return m_contrainte;
	}

	public void setContrainte(Contrainte contrainte) {
		m_contrainte = contrainte;
		m_needUpdateGraphic=true;
	}

	public Boolean isLinkable() {
		return true;
	}
	public void prepareDelete() {
		m_mcd.removeLogic(m_contrainte);
		m_entiteGraph=null;
		m_relationGraph=null;
		m_contrainte=null;
	}
	
}
