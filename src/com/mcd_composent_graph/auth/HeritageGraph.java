package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Heritage;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class HeritageGraph extends McdComposentGraphique implements FormeGeometrique{
	private Heritage m_heritage;
	private EntiteGraph m_entiteGraphMere;
	private List<EntiteGraph> m_entitesGraph;
	private Boolean m_needUpdateGraphic;
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
	public HeritageGraph() {
		m_geometrie = new FormeGeometriqueRectangle(new Rectangle());
		m_entitesGraph = new ArrayList<EntiteGraph>();
		m_needUpdateGraphic = false;
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

		if (m_entiteGraphMere != null){	
			Point e = m_entiteGraphMere.getValidLinkPosition(this);
			g.drawLine(m_centre.x, m_centre.y, e.x, e.y);
		}
		
		for (EntiteGraph eg : m_entitesGraph){
			Point e = eg.getValidLinkPosition(this);
			g.drawLine(m_centre.x, m_centre.y, e.x, e.y);
		}
		
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.COLOR));
		else
			g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.COLOR_FOCUS));
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
		int widthType = metric.stringWidth(m_heritage.getType().toString());

		g.drawString(m_heritage.getType().toString(), pos.x+dim.width/2-widthType/2, pos.y+dim.height-2);
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
}
