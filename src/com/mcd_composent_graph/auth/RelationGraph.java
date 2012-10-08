package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;

public class RelationGraph implements McdComposentGraphique{

	private Relation m_relation;
	public RelationGraph() {
		m_proprietes = new ArrayList<ProprieteGraph>();
		m_position = new Point(0,0);
	}
	public void actualiser(){
		m_proprietes.clear();
		ArrayList<Propriete> props = m_relation.getProprietes();
		for(int i=0;i<props.size();++i)
		{
			ProprieteGraph _p = new ProprieteGraph();
			_p.setPropriete(props.get(i));
		}
	}
	public void setRelation(Relation rel){
		m_relation=rel;
		actualiser();
	}
	public Relation getRelation(){
		return m_relation;
	}
	public void Dessiner(Graphics g) {
		g.setColor(Color.GREEN);
		
		Dimension dim = new Dimension(0,0), dimEC;
		Font font = new Font("TimesRoman", Font.PLAIN, 10);
		
		FontMetrics metric = g.getFontMetrics(font);
		dim.width = metric.stringWidth(m_relation.getNom());
		int height = metric.getHeight();
		dim.height+=height;
		
		for(int i=0;i<m_proprietes.size();++i){
			dimEC=m_proprietes.get(i).getDimension(g, font);
			if(dimEC.width>dim.width)
				dim.width=dimEC.width;
			dim.height+=dimEC.height;
		}
		
		g.fillRect(m_position.x, m_position.y, dim.width+21, dim.height+21);
		
		g.setColor(Color.BLACK);
		
		Point cursor = new Point(m_position);
		cursor.x += 10;
		cursor.y += height + 2;
		g.drawString(m_relation.getNom(), cursor.x, cursor.y);
		++cursor.y;
		g.drawLine(m_position.x, cursor.y, m_position.x+dim.width+20, cursor.y);
		
	}
	
	public Point getPosition() {
		return m_position;
	}
	public void setPosition(Point position) {
		m_position = position;
	}

	private Point m_position;
	private ArrayList<ProprieteGraph> m_proprietes;

}
