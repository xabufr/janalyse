package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.List;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Heritage;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class HeritageGraph extends FormeGeometriqueRectangle implements McdComposentGraphique {
	private Heritage m_heritage;
	private EntiteGraph m_entiteGraphMere;
	private List<EntiteGraph> m_entiteGraphFilles;
	private Boolean m_initialise;
	public HeritageGraph() {
		super(new Rectangle());
		m_initialise = false;
	}
	public Heritage getHeritage() {
		return m_heritage;
	}
	public void setHeritage(Heritage heritage) {
		m_heritage = heritage;
		
	}
	public void dessiner(Graphics g) {
		Dimension dim = new Dimension();
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		Font font = prefs.getFont(PGroupe.HERITAGE, PCle.FONT);
		if(!m_initialise)
		{
			FontMetrics metric = g.getFontMetrics(font);
			dim.width = metric.stringWidth(" XT ");
			dim.height = metric.getHeight();
			setDimension(dim);
			m_initialise = true;
		}
		dim = getDimension();
		Point pos = getPosition();
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
		
		//liaison à l'entité mère
		if (m_initialise && m_mcd != null)
			m_entiteGraphMere = (EntiteGraph) m_mcd.getGraphicComponent(m_heritage.getParent());
		
		Point h = getPosition();
		h.x += getDimension().width/2;
		h.y += getDimension().height/2;
		
		Point e = m_entiteGraphMere.getPosition();
		e.x += m_entiteGraphMere.getDimension().width / 2;
		e.y += m_entiteGraphMere.getDimension().height / 2;
		
		Rectangle obj = m_entiteGraphMere.getRectangle();
		Line2D l = new Line2D.Double(h, e),
				haut = new Line2D.Double(obj.x, obj.y, obj.x+obj.width, obj.y),
				bas = new Line2D.Double(obj.x, obj.y+obj.height, obj.x+obj.width, obj.y+obj.height),
				gauche = new Line2D.Double(obj.x, obj.y, obj.x, obj.y+obj.height),
				droite = new Line2D.Double(obj.x+obj.width, obj.y, obj.x+obj.width, obj.y+obj.height);
		
		int s1X, s1Y, s2X, s2Y, s3X, s3Y;
		s1X = s2X = s3X = e.x;
		s1Y = s2Y = s3Y = e.y;
		
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
		
		int ptsX[] = {s1X, s2X, s3X};
        int ptsY[] = {s1Y, s2Y, s3Y};
		
		g.drawLine(h.x, h.y, e.x, e.y);
		g.fillPolygon(ptsX,ptsY,3);
	}
	@Override
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
