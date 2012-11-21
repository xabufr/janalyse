package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Heritage;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;
import com.utils.auth.Utils;

public class HeritageGraph extends McdComposentGraphique implements FormeGeometrique, Collisable{
	private Heritage m_heritage;
	private EntiteGraph m_entiteGraphMere;
	private List<EntiteGraph> m_entitesGraph;
	private Boolean m_needUpdateGraphic;
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
	public HeritageGraph() {
		m_geometrie = new FormeGeometriqueRectangle(new Rectangle());
		m_entitesGraph = new ArrayList<EntiteGraph>();
		m_needUpdateGraphic = false;
		m_lignesLiens = new ArrayList<Line2D>();
	}
	public Heritage getHeritage() {
		return m_heritage;
	}
	public void setHeritage(Heritage heritage) {
		m_heritage = heritage;
		
	}
	public void update(){
		m_needUpdateGraphic=true;
	}
	public void dessiner(Graphics g) {
		Dimension dim = new Dimension();
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		Font font = null;
		if(!m_focus)
			font=prefs.getFont(PGroupe.HERITAGE, PCle.FONT);
		else
			font=prefs.getFont(PGroupe.HERITAGE, PCle.FONT_FOCUS);
		if(!m_needUpdateGraphic)
		{
			FontMetrics metric = g.getFontMetrics(font);
			dim.width = metric.stringWidth(" XT ");
			dim.height = metric.getHeight();
			setDimension(dim);
			m_needUpdateGraphic = true;
		}
		dim = getDimension();
		Point pos = getPosition();
		
		if (m_needUpdateGraphic && m_mcd != null){
			if (m_heritage.getEnfants() != null){
				m_entitesGraph.clear();
				for (Entite e : m_heritage.getEnfants()){	
					m_entitesGraph.add((EntiteGraph) m_mcd.getGraphicComponent(e));
				}
			}
			if(m_heritage.getMere()!=null){
				m_entiteGraphMere = (EntiteGraph) m_mcd.getGraphicComponent(m_heritage.getMere());
			}			
			reloadGraph();
		}
		
		g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.COLOR_LINE));
		m_centre.setLocation(getPosition());
		m_centre.x += getDimension().width/2;
		m_centre.y += getDimension().height/2;
		m_lignesLiens.clear();
		if (m_entiteGraphMere != null){	
			Point e = m_entiteGraphMere.getValidLinkPosition(this);
			Point e1 = new Point();
			Point p[] = new Point[2];
			Graphics2D g2 = (Graphics2D)g;
			p[0] = new Point();
			p[1] = new Point();
			double a;
			
			
			e1.x = e.x+20;
			e1.y = e.y;
			a = Utils.angle(m_centre, e1, e)*-1;
			
			p[0].x = 10;
			p[0].y = -8;
			
			p[1].x = 10;
			p[1].y = 8;
			
			int px[] = {0, p[0].x, p[1].x};
			int py[] = {0, p[0].y, p[1].y};
			
			g.drawLine(m_centre.x, m_centre.y, e.x, e.y);
			m_lignesLiens.add(new Line2D.Double(m_centre.x, m_centre.y, e.x, e.y));
			AffineTransform transform = (AffineTransform) g2.getTransform().clone();
			g2.translate(e.x, e.y);
			g2.rotate(a);
			g.fillPolygon(px, py, 3);
			g2.setTransform(transform);
		}
		
		for (EntiteGraph eg : m_entitesGraph){
			Point e = eg.getValidLinkPosition(this);
			g.drawLine(m_centre.x, m_centre.y, e.x, e.y);
			m_lignesLiens.add(new Line2D.Double(m_centre.x, m_centre.y, e.x, e.y));
		}
		
		
		if(!(Boolean) prefs.get(PGroupe.HERITAGE, PCle.GRADIANT_COLOR)){
			if(!m_focus)
				g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.COLOR));
			else
				g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.COLOR_FOCUS));
		}
		else{
			Graphics2D g2 = (Graphics2D) g;
			GradientPaint paint=null;
			if(!m_focus){
				paint = new GradientPaint(getPosition().x, 0, 
						(Color)prefs.get(PGroupe.HERITAGE, PCle.COLOR), 
						getPosition().x+getDimension().width, 0, 
						(Color)prefs.get(PGroupe.HERITAGE, PCle.COLOR_2));
			}
			else{
				paint = new GradientPaint(getPosition().x, 0, 
						(Color)prefs.get(PGroupe.HERITAGE, PCle.COLOR_FOCUS), 
						getPosition().x+getDimension().width, 0, 
						(Color)prefs.get(PGroupe.HERITAGE, PCle.COLOR_2_FOCUS));
			}
			g2.setPaint(paint);
		}
		g.fillArc(pos.x, pos.y, dim.width, dim.height, 0, 180);
		g.fillRect(pos.x, pos.y+dim.height/2, dim.width, dim.height/2);
		
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.COLOR_CONTOUR_FOCUS));
		g.drawArc(pos.x, pos.y, dim.width, dim.height, 0, 180);
		g.drawLine(pos.x, pos.y+dim.height/2, pos.x, pos.y+dim.height-1);
		g.drawLine(pos.x+dim.width, pos.y+dim.height/2, pos.x+dim.width, pos.y+dim.height-1);
		g.drawLine(pos.x, pos.y+dim.height-1, pos.x+dim.width, pos.y+dim.height-1);
		
		g.setFont(font);
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.FONT_COLOR));
		else
			g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.FONT_COLOR_FOCUS));
		
		FontMetrics metric = g.getFontMetrics(g.getFont());
		int widthType = metric.stringWidth(m_heritage.getType().getName());

		g.drawString(m_heritage.getType().getName(), pos.x+dim.width/2-widthType/2, pos.y+dim.height-2);
	}

	private void reloadGraph(){
		if(m_entiteGraphMere!=null){
			m_entiteGraphMere.removeLien(this);
		}
		for(EntiteGraph e : m_entitesGraph){
			e.removeLien(this);
		}
		m_entitesGraph.clear();
		if (m_heritage.getEnfants() != null){
			for (Entite e : m_heritage.getEnfants()){
				EntiteGraph ent = (EntiteGraph) m_mcd.getGraphicComponent(e);
				m_entitesGraph.add(ent);
				ent.addLien(this, m_centre);
			}
		}
		if(m_heritage.getMere()!=null){
			Entite e = m_heritage.getMere();
			EntiteGraph ent = (EntiteGraph) m_mcd.getGraphicComponent(e);
			m_entitesGraph.add(ent);
			ent.addLien(this, m_centre);
			m_entiteGraphMere = (EntiteGraph) m_mcd.getGraphicComponent(e);
			m_entiteGraphMere.addLien(this, m_centre);
			
		}
	}

	public void setMcd(McdGraph mcd) {
		if(m_mcd!=null)
			m_mcd.removeLogic(m_heritage);
		m_mcd=mcd;
		m_mcd.registerLogic(m_heritage, this);
	}
	public Boolean isLinkable() {
		return true;
	}

	public void prepareDelete() {
		m_mcd.removeLogic(m_heritage);
		m_heritage.setEnfants(null);
		m_heritage.setMere(null);
		m_heritage=null;
		m_entiteGraphMere=null;
		m_entitesGraph=null;
	}
	public void setFocus(Boolean f){
		super.setFocus(f);
		update();
	}
	public void removeEntiteGraph(EntiteGraph e){
		if(m_entitesGraph.contains(e)){
			m_entitesGraph.remove(e);
			e.removeLien(this);
		}
		if(m_entiteGraphMere==e){
			m_entiteGraphMere=null;
			e.removeLien(this);
		}
	}
	public void clearEnfants(){
		for(EntiteGraph e : m_entitesGraph)
			e.removeLien(this);
		m_entitesGraph.clear();
		m_heritage.getEnfants().clear();
	}

	public void dessinerOmbre(Graphics g) {
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		if ((Boolean)prefs.get(PGroupe.HERITAGE, PCle.OMBRE)){
			g.setColor((Color)prefs.get(PGroupe.HERITAGE, PCle.OMBRE_COLOR));
			g.fillRect(this.getPosition().x+2, this.getPosition().y+this.getDimension().height/2, this.getDimension().width, this.getDimension().height/2+2);

		}
	}
	public ArrayList<Line2D> getLignesLiens(){
		return m_lignesLiens;
	}
	public boolean collision() {
		Rectangle rect1, rect2;
		rect1 = this.getRectangle();
		rect2 = new Rectangle();
		int i=1;
		for (McdComposentGraphique c : m_mcd.getMcdComponents()){
			if (!(c instanceof Collisable)){
				++i;
				continue;
			}
		
			rect2 = ((Collisable)c).getRectangle();

			if (rect1.x > rect2.x + rect2.width
					|| rect1.x + rect1.width < rect2.x
					|| rect1.y > rect2.y + rect2.height
					|| rect1.y + rect1.height < rect2.y)
				++i;
		}
		if (i == m_mcd.getMcdComponents().size())
			return false;
		else
			return true;
	}
}
