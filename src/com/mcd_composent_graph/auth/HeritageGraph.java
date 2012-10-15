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
import java.util.List;
import java.util.Vector;

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
		Font font = prefs.getFont(PGroupe.HERITAGE, PCle.FONT);
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
					/*if (e.isMere())
						m_entiteGraphMere = (EntiteGraph) m_mcd.getGraphicComponent(e);
					else*/
						m_entitesGraph.add((EntiteGraph) m_mcd.getGraphicComponent(e));
				}
			}
		}
		
		g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.COLOR_LINE));
		Point h = getPosition();
		h.x += getDimension().width/2;
		h.y += getDimension().height/2;
		
		/*if (m_entiteGraphMere != null){
			Point e = m_entiteGraphMere.getPosition();
			e.x += m_entiteGraphMere.getDimension().width / 2;
			e.y += m_entiteGraphMere.getDimension().height / 2;
			
			Rectangle obj = m_entiteGraphMere.getRectangle();
			Line2D l = new Line2D.Double(h, e),
					haut = new Line2D.Double(obj.x, obj.y, obj.x+obj.width, obj.y),
					bas = new Line2D.Double(obj.x, obj.y+obj.height, obj.x+obj.width, obj.y+obj.height),
					gauche = new Line2D.Double(obj.x, obj.y, obj.x, obj.y+obj.height),
					droite = new Line2D.Double(obj.x+obj.width, obj.y, obj.x+obj.width, obj.y+obj.height);
			
			if (l.intersectsLine(haut)){
				e.y -= obj.height/2;
			}
			else if (l.intersectsLine(bas)){
				e.y += obj.height/2;
			}
			else if (l.intersectsLine(gauche)){
				e.x -= obj.width/2;
			}
			else if (l.intersectsLine(droite)){
				e.x += obj.width/2;
			}
			
			g.drawLine(h.x, h.y, e.x, e.y);
		}*/
		
		if (m_entitesGraph != null){
			for (EntiteGraph eg : m_entitesGraph){
				Point e = eg.getPosition();
				e.x += eg.getDimension().width / 2;
				e.y += eg.getDimension().height / 2;
				
				Rectangle obj = eg.getRectangle();
				Line2D l = new Line2D.Double(h, e),
						haut = new Line2D.Double(obj.x, obj.y, obj.x+obj.width, obj.y),
						bas = new Line2D.Double(obj.x, obj.y+obj.height, obj.x+obj.width, obj.y+obj.height),
						gauche = new Line2D.Double(obj.x, obj.y, obj.x, obj.y+obj.height),
						droite = new Line2D.Double(obj.x+obj.width, obj.y, obj.x+obj.width, obj.y+obj.height);
				
				if (l.intersectsLine(haut)){
					e.y -= obj.height/2; 
				}
				else if (l.intersectsLine(bas)){
					e.y += obj.height/2;			
				}
				else if (l.intersectsLine(gauche)){
					e.x -= obj.width/2;
				}
				else if (l.intersectsLine(droite)){
					e.x += obj.height/2;
				}
				
				g.drawLine(h.x, h.y, e.x, e.y);
			}
		}
		g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.COLOR));
		g.fillArc(pos.x, pos.y, dim.width, dim.height, 0, 180);
		g.fillRect(pos.x, pos.y+dim.height/2, dim.width, dim.height/2);
		
		g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.COLOR_CONTOUR));
		g.drawArc(pos.x, pos.y, dim.width, dim.height, 0, 180);
		g.drawLine(pos.x, pos.y+dim.height/2, pos.x, pos.y+dim.height-1);
		g.drawLine(pos.x+dim.width, pos.y+dim.height/2, pos.x+dim.width, pos.y+dim.height-1);
		g.drawLine(pos.x, pos.y+dim.height-1, pos.x+dim.width, pos.y+dim.height-1);
		
		g.setFont(font);
		g.setColor((Color) prefs.get(PGroupe.HERITAGE, PCle.FONT_COLOR));
		
		FontMetrics metric = g.getFontMetrics(g.getFont());
		int widthType = metric.stringWidth(m_heritage.getType().toString());

		g.drawString(m_heritage.getType().toString(), pos.x+dim.width/2-widthType/2, pos.y+dim.height-2);
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
	McdGraph m_mcd;
}
