package com.mcd_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.HeritageGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.ProprieteGraph;
import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_log.auth.Heritage;
import com.mcd_log.auth.HeritageType;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;

public class McdGraph extends JPanel{
	private EntiteGraph entite;
	private RelationGraph rel;
	private HeritageGraph her;
	private McdComposentGraphique m_focus;
	private Point m_deltaSelect;
	
	public McdGraph() {
		m_focus = null;
		m_deltaSelect = new Point();
		entite = new EntiteGraph(new Rectangle(20 , 30, 120, 130));
		rel = new RelationGraph();
		her = new HeritageGraph();
		her.setHeritage(new Heritage(null, HeritageType.T));
		Relation test = new Relation("               ");
		rel.setRelation(test);
		test.addPropriete(new Propriete("                     ", null));
		/*test.addPropriete(new Propriete("prop2", null));
		test.addPropriete(new Propriete("prop3", null));
		test.addPropriete(new Propriete("prop4", null));
		test.addPropriete(new Propriete("prop5", null));
		test.addPropriete(new Propriete("prop6 super longue pour tester la redimenssion", null));*/
		
		this.setSize(new Dimension(80, 80));
		this.addMouseMotionListener(new mouseMove(this));
		this.addMouseListener(new mouseClick());
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		entite.Dessiner(g);
		//rel.Dessiner(g);
		her.Dessiner(g);
	}
	
	private class mouseMove implements MouseMotionListener{
		private McdGraph parent;
		
		public mouseMove(McdGraph p){
			parent = p;
		}

		public void mouseDragged(MouseEvent e) {
			if (m_focus != null){
				Point tmp = new Point();
				tmp.x = e.getPoint().x - m_deltaSelect.x;
				tmp.y = e.getPoint().y - m_deltaSelect.y;
				entite.setPosition(tmp);
				parent.repaint();
			}
		}

		public void mouseMoved(MouseEvent e) {}		
	}
	
	private class mouseClick implements MouseListener{

		public void mouseClicked(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			if (entite.contient(e.getPoint())){
				m_focus = entite;
				m_deltaSelect.x = e.getPoint().x - entite.getRectangle().x;
				m_deltaSelect.y = e.getPoint().y - entite.getRectangle().y;
			}
		}

		public void mouseReleased(MouseEvent e) {
			m_focus = null;
		
		}
	
	}
}
