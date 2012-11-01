package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.mcd_log.auth.Propriete;

public class ProprieteGraph implements FormeGeometrique{
	private Propriete m_propriete;
	private Point m_position;
	private FormeGeometriqueRectangle m_geometrie;
	public ProprieteGraph() {
		m_geometrie = new FormeGeometriqueRectangle(new Rectangle());
		m_position = new Point();
	}
	
	public Propriete getPropriete() {
		return m_propriete;
	}

	public void setPropriete(Propriete propriete) {
		m_propriete = propriete;
	}
	public Dimension getDimension(Graphics g, Font f, String proprietaire){
		FontMetrics metric = g.getFontMetrics(f);
		int width = metric.stringWidth(m_propriete.getVirtualName(proprietaire));
		int height = metric.getHeight();
		return new Dimension(width, height);
	}
	
	public void dessiner(Graphics g, Font f, Color c, Point p, String proprietaire)
	{
		g.setColor(c);
		g.setFont(f);
		g.drawString(m_propriete.getVirtualName(proprietaire), p.x, p.y);
		if(m_propriete.isClePrimaire()){
			g.drawLine(p.x, p.y + 2, p.x + getDimension(g,f, proprietaire).width, p.y + 2);
		}
		setPosition(p);
		m_geometrie.setRectangle(new Rectangle(p.x, p.y-getDimension(g, f, proprietaire).height, getDimension(g, f, proprietaire).width, getDimension(g, f, proprietaire).height));
	}

	public Boolean contient(Point p) {
		return m_geometrie.getRectangle().contains(p);
	}

	public void setPosition(Point p) {
		m_position = p;
	}

	public Point getPosition() {
		return m_position;
	}
	
	public void setRectangle(Rectangle r) {
		m_geometrie.setRectangle(r);
	}

	public Rectangle getRectangle() {
		return m_geometrie.getRectangle();
	}

}
