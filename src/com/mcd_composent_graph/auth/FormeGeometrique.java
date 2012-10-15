package com.mcd_composent_graph.auth;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

public interface FormeGeometrique {
	public abstract Boolean contient(Point p);
	public abstract void setPosition(Point p);
	public abstract Point getPosition();
}

class FormeGeometriqueRectangle implements FormeGeometrique{
	private Rectangle m_rect;
	
	public FormeGeometriqueRectangle(Rectangle r){
		setRectangle(r);
	}
	public void setPosition(Point p){
		m_rect.x=p.x;
		m_rect.y=p.y;
	}
	public Point getPosition(){
		return new Point(m_rect.x, m_rect.y);
	}
	public void setRectangle(Rectangle r){
		m_rect=r;
	}
	public void setDimension(Dimension d){
		m_rect.width=d.width;
		m_rect.height=d.height;
	}
	public Dimension getDimension(){
		return new Dimension(m_rect.width, m_rect.height);
	}
	public Rectangle getRectangle(){
		return m_rect;
	}
	public Boolean contient(Point p) {
		return m_rect.contains(p);
	}
}

class FormeGeometriqueLigne implements FormeGeometrique{
	private Point m_a, m_b;
	private Line2D m_ligne;
	private float m_tolerance;
	public FormeGeometriqueLigne(Point a, Point b){
		m_ligne = new Line2D.Double();
		m_ligne.setLine(a, b);
		m_a=new Point(a);
		m_b=new Point(b);
		m_tolerance=(float) 10.0;
	}
	public void setPointA(Point a){
		m_a=a;
		m_ligne.setLine(a, getPointB());
	}
	public void setPointB(Point b){
		m_b=b;
		m_ligne.setLine(getPointA(), b);
	}
	public Point getPointA(){
		return m_a;
		//return new Point((int)m_ligne.getP1().getX(), (int)m_ligne.getP1().getY());
	}
	public Point getPointB(){
		return m_b;
		//return new Point((int)m_ligne.getP2().getX(), (int)m_ligne.getP2().getY());
	}
	public Boolean contient(Point p) {
		if(m_a==p||m_b==p)
			return true;
		int min = m_a.y<m_b.y?m_a.y:m_b.y;
		int max = m_a.y>m_b.y?m_a.y:m_b.y;
		
		if(m_a.x == m_b.x)
		{
			if(p.x<m_a.x-m_tolerance||p.x>m_a.x+m_tolerance)
				return false;
			if(p.y < min || p.y>max)
				return false;
			return true;
		}
		if(m_a.y==m_b.y){
			min = m_a.x<m_b.x?m_a.x:m_b.x;
			max = m_a.x>m_b.x?m_a.x:m_b.x;
			if(p.x>=min&&p.x<=max&&p.y>=m_a.y-m_tolerance&&p.y<=m_a.y+m_tolerance)
				return true;
			return false;
		}
		
		float coefDirecteur, b;
		
		coefDirecteur=((float)m_a.y-m_b.y)/((float)m_a.x-m_b.x);
		b=m_a.y-(coefDirecteur*m_a.x);
		float y = (p.x*coefDirecteur)+b;
		if(y >= p.y-m_tolerance && y <= p.y + m_tolerance && y >= min && y <= max)
			return true;
		
		return false;
		//return m_ligne.contains(p);
	}
	@Override
	public void setPosition(Point p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point getPosition() {
		// TODO Auto-generated method stub
		return null;
	}
	
}