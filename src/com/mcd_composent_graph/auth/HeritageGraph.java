package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.mcd_log.auth.Heritage;

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
	public void Dessiner(Graphics g) {
		
		Dimension dim = new Dimension();
		if(!m_initialise)
		{
			Font font = new Font("TimesRoman", Font.PLAIN, 20);

			FontMetrics metric = g.getFontMetrics(font);
			dim.width = metric.stringWidth(" XT ");
			dim.height = metric.getHeight();
			setDimension(dim);
			m_initialise = true;
		}
		dim = getDimension();
		Point pos = getPosition();
		g.setColor(Color.GREEN);
		g.fillArc(pos.x, pos.y, dim.width, dim.height, 0, 180);
		g.fillRect(pos.x, pos.y+dim.height/2, dim.width, dim.height/2);
		
		g.setColor(Color.BLACK);
		g.drawArc(pos.x, pos.y, dim.width, dim.height, 0, 180);
		g.drawLine(pos.x, pos.y+dim.height/2, pos.x, pos.y+dim.height);
		g.drawLine(pos.x+dim.width, pos.y+dim.height/2, pos.x+dim.width, pos.y+dim.height);
		g.drawLine(pos.x, pos.y+dim.height, pos.x+dim.width, pos.y+dim.height);
		
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		
		FontMetrics metric = g.getFontMetrics(g.getFont());
		this.m_widthType = metric.stringWidth(m_heritage.getType().toString());

		g.drawString(m_heritage.getType().toString(), pos.x+dim.width/2-this.m_widthType/2, pos.y+dim.height-2);
	}

}
