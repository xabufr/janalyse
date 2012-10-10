package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Heritage;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class HeritageGraph extends FormeGeometriqueRectangle implements McdComposentGraphique {
	private Heritage m_heritage;
	private Boolean m_initialise;
	private int m_widthType;
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
		this.m_widthType = metric.stringWidth(m_heritage.getType().toString());

		g.drawString(m_heritage.getType().toString(), pos.x+dim.width/2-this.m_widthType/2, pos.y+dim.height-2);
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
