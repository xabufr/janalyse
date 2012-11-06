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
import com.utils.auth.Utils;

public class ContrainteGraph extends McdComposentGraphique implements FormeGeometrique{
	private Contrainte m_contrainte;
	private List<EntiteGraph> m_entiteGraph;
	private List<RelationGraph> m_relationGraph;
	private Boolean m_needUpdateGraphic;
	private McdGraph m_mcd;
	private FormeGeometriqueRectangle m_geometrie;
	private ArrayList<Line2D> m_lignesLiens;
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
		m_lignesLiens = new ArrayList<Line2D>();
		
	}
	public void update(){
		m_needUpdateGraphic=true;
	}
	
	public void dessiner(Graphics g) {
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		Dimension dim = new Dimension(0,0);
		Point pos = getPosition();
		
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
			m_relationGraph.clear();
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
		
		g.setColor((Color)prefs.get(PGroupe.CONTRAINTE, PCle.COLOR_LINE));
		m_lignesLiens.clear();
		for (RelationGraph r : m_relationGraph){
			centreObjet = r.getPosition();
			centreObjet.x += r.getDimension().width / 2;
			centreObjet.y += r.getDimension().height / 2;
			
			double a;
			int t, b, c;
			Point e1 = new Point();
			Point p = new Point();
			
			b = r.getDimension().width/2;
			c = r.getDimension().height/2;
			t = b*c;				
			
			e1.x = centreObjet.x+20;
			e1.y = centreObjet.y;
			a = Utils.angle(m_centre, e1, centreObjet)*-1;
			t /= (int)Math.sqrt((Math.pow(c, 2)*Math.pow(Math.cos(a), 2))+(Math.pow(b, 2)*Math.pow(Math.sin(a), 2)));
			
			p.x = (int)((t*Math.cos(a))+centreObjet.x);
			p.y = (int)((t*Math.sin(a))+centreObjet.y);
			
			m_lignesLiens.add(new Line2D.Double(m_centre.x, m_centre.y, p.x, p.y));
			g.drawLine(m_centre.x, m_centre.y, p.x, p.y);
			if (m_contrainte.getSens() instanceof Relation && r.getRelation().equals((Relation)m_contrainte.getSens())){
				Point p1[] = new Point[2];
				p1[0] = new Point();
				p1[1] = new Point();

				b = r.getDimension().width/2;
				c = r.getDimension().height/2;
				t = b*c;				
				
				e1.x = centreObjet.x+20;
				e1.y = centreObjet.y;
				a = Utils.angle(m_centre, e1, centreObjet)*-1;
				t /= (int)Math.sqrt((Math.pow(c, 2)*Math.pow(Math.cos(a), 2))+(Math.pow(b, 2)*Math.pow(Math.sin(a), 2)));
				
				p1[0].x = 10;
				p1[0].y = -8;
				
				p1[1].x = 10;
				p1[1].y = 8;
				
				int px[] = {0, p1[0].x, p1[1].x};
				int py[] = {0, p1[0].y, p1[1].y};
				
				g2.setStroke(new BasicStroke(1.0f));
				g2.translate((t*Math.cos(a))+centreObjet.x, (t*Math.sin(a))+centreObjet.y);
				g2.rotate(a);
				g.fillPolygon(px, py, 3);
				g2.rotate(-a);
				g2.translate(-((t*Math.cos(a))+centreObjet.x), -((t*Math.sin(a))+centreObjet.y));
			}
		}
		
		for (EntiteGraph e : m_entiteGraph){
			Point p = e.getValidLinkPosition(this);
			if (!m_contrainte.getType().toString().equals("1"))
				g2.setStroke(dashed);
			m_lignesLiens.add(new Line2D.Double(m_centre.x, m_centre.y, p.x, p.y));
			g2.draw(new Line2D.Double(m_centre.x, m_centre.y, p.x, p.y));
			if (m_contrainte.getSens() instanceof Entite && e.getEntite().equals((Entite)m_contrainte.getSens())){
				Point e1 = new Point();
				Point p1[] = new Point[2];
				p1[0] = new Point();
				p1[1] = new Point();
				double a;
				
				
				e1.x = p.x+20;
				e1.y = p.y;
				a = Utils.angle(m_centre, e1, p)*-1;
				
				p1[0].x = 10;
				p1[0].y = -8;
				
				p1[1].x = 10;
				p1[1].y = 8;
				
				int px[] = {0, p1[0].x, p1[1].x};
				int py[] = {0, p1[0].y, p1[1].y};
				
				g2.setStroke(new BasicStroke(1.0f));
				g2.translate(p.x, p.y);
				g2.rotate(a);
				g.fillPolygon(px, py, 3);
				g2.rotate(-a);
				g2.translate(-p.x, -p.y);
			}
		}
		g2.setStroke(new BasicStroke(1.0f));
		
		if(!m_focus)
			g.setColor((Color)prefs.get(PGroupe.CONTRAINTE, PCle.COLOR));
		else
			g.setColor((Color)prefs.get(PGroupe.CONTRAINTE, PCle.COLOR_FOCUS));
		
		g.fillOval(pos.x, pos.y, dim.width, dim.height);
		
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.CONTRAINTE, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.CONTRAINTE, PCle.COLOR_CONTOUR_FOCUS));
		
		g.drawOval(pos.x, pos.y, dim.width, dim.height);
		FontMetrics font = g.getFontMetrics();
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
		
		if(!m_focus){
			g.setFont((Font)prefs.getFont(PGroupe.CONTRAINTE, PCle.FONT));
			g.setColor((Color)prefs.get(PGroupe.CONTRAINTE, PCle.FONT_COLOR));
		}
		else{
			g.setFont((Font)prefs.getFont(PGroupe.CONTRAINTE, PCle.FONT_FOCUS));
			g.setColor((Color)prefs.get(PGroupe.CONTRAINTE, PCle.FONT_COLOR_FOCUS));
		}
		
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
	}

	public void dessinerOmbre(Graphics g) {
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		if ((Boolean)prefs.get(PGroupe.CONTRAINTE, PCle.OMBRE)){
			g.setColor((Color)prefs.get(PGroupe.CONTRAINTE, PCle.OMBRE_COLOR));
			g.fillOval(this.getPosition().x+2, this.getPosition().y+2, this.getDimension().width, this.getDimension().height);
		}
	}
	public ArrayList<Line2D> getLigneLiens(){
		return m_lignesLiens;
	}
}
