package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Cardinalite;
import com.preferences_mcd_logique.auth.McdPreferencesManager;

public class CardinaliteGraph extends FormeGeometriqueLigne implements McdComposentGraphique{
	private Cardinalite m_cardinalite;
	private EntiteGraph m_entiteGraph;
	private RelationGraph m_relationGraph;
	
	public CardinaliteGraph() {
		super(new Point(), new Point());
	}

	public void Dessiner(Graphics g) {
		if(m_relationGraph==null||m_entiteGraph==null)
			return;
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		
		this.setPointA(m_entiteGraph.getPosition());
		this.setPointB(m_relationGraph.getPosition());
		
		g.setColor(Color.BLACK);
		g.drawLine(getPointA().x, getPointA().y, getPointB().x, getPointB().y);
		
	}

	public Cardinalite getCardinalite() {
		return m_cardinalite;
	}

	public void setCardinalite(Cardinalite cardinalite) {
		m_cardinalite = cardinalite;
	}

	@Override
	public void setMcd(McdGraph mcd) {
		if(m_mcd!=null)
			m_mcd.removeLogic(m_cardinalite);
		m_mcd=mcd;
		m_mcd.registerLogic(m_cardinalite, this);
		m_relationGraph = (RelationGraph) m_mcd.getGraphicComponent(m_cardinalite.getRelation());
		m_entiteGraph = (EntiteGraph) m_mcd.getGraphicComponent(m_cardinalite.getEntite());
	}
	McdGraph m_mcd;

}
