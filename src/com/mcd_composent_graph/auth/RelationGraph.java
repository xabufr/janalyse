package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Graphics;

import com.mcd_log.auth.Relation;

public class RelationGraph implements McdComposentGraphique{

	private Relation m_relation;
	public RelationGraph() {
		// TODO Auto-generated constructor stub
	}
	public void setRelation(Relation rel){
		m_relation=rel;
	}
	public void Dessiner(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(m_x, m_y, 10, 10);

	}
	
	public void setX(int x){
		m_x=x;
	}
	public int getX(){
		return m_x;
	}
	public void setY(int y){
		m_y=y;
	}
	public int getY(){
		return m_y;
	}
	
	private int m_x, m_y;

}
