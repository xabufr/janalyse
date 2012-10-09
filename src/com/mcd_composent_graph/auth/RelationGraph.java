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

import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;

public class RelationGraph extends FormeGeometriqueRectangle implements McdComposentGraphique{

	private Relation m_relation;
	private int m_lastPropsNumber, m_heightNom, m_widthNom;
	private ArrayList<ProprieteGraph> m_proprietes;

	public RelationGraph() {
		super(new Rectangle());
		m_proprietes = new ArrayList<ProprieteGraph>();
		m_lastPropsNumber=-999;
		m_heightNom = 0;
	}
	public void actualiser(){
		m_proprietes.clear();
		List<Propriete> props = m_relation.getProprietes();
		this.m_lastPropsNumber=props.size();
		for(int i=0;i<m_lastPropsNumber;++i)
		{
			ProprieteGraph _p = new ProprieteGraph();
			_p.setPropriete(props.get(i));
			m_proprietes.add(_p);
		}
	}
	public void setRelation(Relation rel){
		m_relation=rel;
		this.m_lastPropsNumber=-999;
	}
	public Relation getRelation(){
		return m_relation;
	}
	public void Dessiner(Graphics g) {
		if(this.m_lastPropsNumber!=this.m_relation.getProprietes().size())
		{
			this.actualiser();
	
			Dimension dim = new Dimension(0,0), dimEC;
			Font font = new Font("TimesRoman", Font.PLAIN, 12);
			
			FontMetrics metric = g.getFontMetrics(font);
			m_widthNom = dim.width = metric.stringWidth(m_relation.getNom());
			m_heightNom = metric.getHeight();
			
			font = new Font("TimesRoman", Font.PLAIN, 10);
			
			for(int i=0;i<m_lastPropsNumber;++i){
				dimEC=m_proprietes.get(i).getDimension(g, font);
				if(dimEC.width>dim.width)
					dim.width=dimEC.width;
				
				dim.height+=dimEC.height+4;
			}
			
			dim.height *= 2;
			dim.width += 60;
			setDimension(dim);
		}
		Point pos = getPosition();
		Dimension dim = getDimension();
		
		
		g.setColor(Color.GREEN);
		
		if (dim.height > dim.width)
			dim.width = dim.height*2;
		else
			dim.height = dim.width/2;
		
		g.fillOval(pos.x, pos.y, dim.width, dim.height);
		g.setColor(Color.BLACK);
		g.drawOval(pos.x, pos.y, dim.width, dim.height);
		
		Point cursor = new Point(pos);
		cursor.x += (dim.width/2)-(m_widthNom/2);
		cursor.y += (dim.height/4)+(m_heightNom/2);
		g.setFont(new Font("TimesRoman", Font.BOLD, 12));
		g.drawString(m_relation.getNom(), cursor.x, cursor.y);
		cursor.y = pos.y+(dim.height/2);
		
		g.drawLine(pos.x, cursor.y, pos.x+dim.width, cursor.y);
		
		Font font = new Font("TimesRoman", Font.PLAIN, 10);
		Dimension dimEC;
		cursor.y-=3;
		for(int i=0;i<this.m_lastPropsNumber;++i){
			dimEC = this.m_proprietes.get(i).getDimension(g, font);
			cursor.y+= 2 + dimEC.height;
			cursor.x = pos.x+(dim.width/2)-(dimEC.width/2);
			this.m_proprietes.get(i).dessiner(g, font, Color.BLACK, cursor);
		}

	}
}
