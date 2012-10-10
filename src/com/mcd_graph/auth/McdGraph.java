package com.mcd_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JPanel;

import com.mcd_composent_graph.auth.CardinaliteGraph;
import com.mcd_composent_graph.auth.ContrainteGraph;
import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.HeritageGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_composent_graph.auth.FormeGeometrique;
import com.mcd_log.auth.Cardinalite;
import com.mcd_log.auth.Contrainte;
import com.mcd_log.auth.Heritage;
import com.mcd_log.auth.HeritageType;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;
import com.mcd_log.auth.Entite;


public class McdGraph extends JPanel{
	private EntiteGraph entite;
	private RelationGraph rel;
	private EntiteGraph entite2;
	private RelationGraph rel2;
	private CardinaliteGraph card;
	private CardinaliteGraph card2;
	private CardinaliteGraph card3;
	private CardinaliteGraph card4;
	private ContrainteGraph cont;
	private HeritageGraph her;
	private McdComposentGraphique m_focus;
	private Point m_deltaSelect;
	
	public McdGraph() {
		m_logicObjects = new Hashtable<Object, McdComposentGraphique> ();
		m_focus = null;
		m_deltaSelect = new Point();
		
		entite = new EntiteGraph();
		entite2 = new EntiteGraph();
		
		Entite e = new Entite("Entite1");
		e.addPropriete(new Propriete("id_Entite1", null, 255, true, true, false));
		e.addPropriete(new Propriete("nom", null));
		e.addPropriete(new Propriete("prénom", null));
		
		Entite e2 = new Entite("Entite2");
		e2.addPropriete(new Propriete("id_Entite2", null, 255, true, true, false));
		e2.addPropriete(new Propriete("nom", null));
		e2.addPropriete(new Propriete("prénom", null));
		
		entite.setEntite(e);
		entite2.setEntite(e2);
		
		entite.setPosition(new Point(10, 60));
		entite2.setPosition(new Point(370, 60));
		
		rel = new RelationGraph();
		rel2 = new RelationGraph();

		her = new HeritageGraph();
		her.setHeritage(new Heritage(null, HeritageType.T));

		Relation test = new Relation("Realation1");
		Relation test2 = new Relation("Realation2");
		test.addPropriete(new Propriete("propriété1", null));
		test.addPropriete(new Propriete("propriété1", null));
		rel.setRelation(test);
		rel.setPosition(new Point(160,20));
		rel2.setRelation(test2);
		rel2.setPosition(new Point(160,120));

		cont = new ContrainteGraph();
		
		Contrainte c = new Contrainte("X");
		List<Entite> lstE = new ArrayList<Entite>();
		List<Relation> lstR = new ArrayList<Relation>();
		lstE.add(entite.getEntite());
		lstE.add(entite2.getEntite());
		lstR.add(test);
		lstR.add(test2);
		c.setEntites(lstE);
		c.setRelations(lstR);
		cont.setContrainte(c);
		cont.setPosition(new Point(120, 88));
		
		
		card = new CardinaliteGraph();
		
		Cardinalite ca = new Cardinalite();
		ca.setEntite(entite.getEntite());
		ca.setRelation(rel.getRelation());
		
		card2 = new CardinaliteGraph();
		
		Cardinalite ca2 = new Cardinalite();
		ca2.setEntite(entite2.getEntite());
		ca2.setRelation(rel2.getRelation());
		
		card3 = new CardinaliteGraph();
		
		Cardinalite ca3 = new Cardinalite();
		ca3.setEntite(entite.getEntite());
		ca3.setRelation(rel2.getRelation());
		
		card4 = new CardinaliteGraph();
		
		Cardinalite ca4 = new Cardinalite();
		ca4.setEntite(entite2.getEntite());
		ca4.setRelation(rel.getRelation());
		
		rel.setMcd(this);
		entite.setMcd(this);
		cont.setMcd(this);
		
		card.setCardinalite(ca);
		card.setMcd(this);
		
		card2.setCardinalite(ca2);
		card2.setMcd(this);
		
		card3.setCardinalite(ca3);
		card3.setMcd(this);
		
		card4.setCardinalite(ca4);
		card4.setMcd(this);

		/*test.addPropriete(new Propriete("prop2", null));
		test.addPropriete(new Propriete("prop3", null));
		test.addPropriete(new Propriete("prop4", null));
		test.addPropriete(new Propriete("prop5", null));
		test.addPropriete(new Propriete("prop6 super longue pour tester la redimenssion", null));*/
		
		m_logicObjects.put(entite.getEntite(), entite);
		m_logicObjects.put(rel.getRelation(), rel);
		m_logicObjects.put(cont.getContrainte(), cont);
		m_logicObjects.put(entite2.getEntite(), entite2);
		m_logicObjects.put(rel2.getRelation(), rel2);
		
		this.setSize(new Dimension(80, 80));
		this.addMouseMotionListener(new mouseMove());
		this.addMouseListener(new mouseClick());
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		//her.Dessiner(g);

		card.dessiner(g);
		card2.dessiner(g);
		card3.dessiner(g);
		card4.dessiner(g);
		
		cont.dessiner(g, g.getFont(), Color.CYAN);
		
		rel.dessiner(g);
		rel2.dessiner(g);
		
		entite.dessiner(g, g.getFont(), Color.YELLOW);
		entite2.dessiner(g, g.getFont(), Color.YELLOW);

	}
	
	private class mouseMove implements MouseMotionListener{
		
		public mouseMove(){
		}

		public void mouseDragged(MouseEvent e) {
			if (m_focus != null){
				FormeGeometrique forme = (FormeGeometrique)m_focus;
				Point tmp = new Point();
				tmp.x = e.getPoint().x - m_deltaSelect.x;
				tmp.y = e.getPoint().y - m_deltaSelect.y;
				forme.setPosition(tmp);
				repaint(); //Intéressant n'est-ce pas ? Note que le McdGraph.this. est facultatif ici...
			}
		}

		public void mouseMoved(MouseEvent e) {}		
	}
	
	private class mouseClick implements MouseListener{

		public void mouseClicked(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			for (McdComposentGraphique composant : m_logicObjects.values()){
				FormeGeometrique forme = (FormeGeometrique)composant;
				if (forme.contient(e.getPoint())){
					m_focus = composant;
					m_deltaSelect.x = e.getPoint().x - forme.getPosition().x;
					m_deltaSelect.y = e.getPoint().y - forme.getPosition().y;
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			m_focus = null;
		
		}
	
	}
	
	public void registerLogic(Object o, McdComposentGraphique g){
		m_logicObjects.put(o, g);
	}
	public McdComposentGraphique getGraphicComponent(Object o){
		return this.m_logicObjects.get(o);
	}
	public void removeLogic(Object o){
		this.m_logicObjects.remove(o);
	}
	private Hashtable<Object, McdComposentGraphique> m_logicObjects;
}
