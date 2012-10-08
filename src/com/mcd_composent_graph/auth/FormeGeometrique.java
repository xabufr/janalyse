package com.mcd_composent_graph.auth;

import java.awt.Point;
import java.awt.Rectangle;

public abstract class FormeGeometrique {

	public FormeGeometrique() {
	}
	public abstract Boolean contient(Point p);

}

class FormeGeometriqueRectangle extends FormeGeometrique{
	private Rectangle m_rect;
	
	public FormeGeometriqueRectangle(Rectangle r){
		m_rect=r;
	}
	public Boolean contient(Point p) {
		return m_rect.contains(p);
	}
}

class FormeGeometriqueLigne extends FormeGeometrique{
	private Point m_a, m_b;
	private float m_tolerance;
	public FormeGeometriqueLigne(Point a, Point b){
		m_a=a;
		m_b=b;
		m_tolerance=(float) 1.0;
	}
	public Boolean contient(Point p) {
		if(m_a==p||m_b==p)
			return true;
		int min = m_a.y<m_b.y?m_a.y:m_b.y;
		int max = m_a.y>m_b.y?m_a.y:m_b.y;
		
		if(m_a.x == m_b.x)
		{
			if(m_a.x != p.x)
				return false;
			if(p.y < min || p.y>max)
				return false;
			return true;
		}
		
		float coefDirecteur, b;
		
		coefDirecteur=(m_a.y-m_b.y)/(m_a.x-m_b.x);
		b=m_a.y-(coefDirecteur*m_a.x);
		float y = p.x*coefDirecteur+b;
		if(y >= p.y-m_tolerance && y <= p.y + m_tolerance && y >= min && y >= max)
			return true;
		
		return false;
	}
	
}