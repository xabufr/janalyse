package com.mcd_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Hashtable;

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
import com.mcd_log.auth.Relation;
import com.mcd_log.auth.Entite;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;


@SuppressWarnings("serial")
public class McdGraph extends JPanel{
	private McdComposentGraphique m_focus;
	private Point m_deltaSelect;
	private Hashtable<McdGraphStateE, McdGraphState> m_states;
	private McdGraphStateE m_currentState;
	private Hashtable<Object, McdComposentGraphique> m_logicObjects;
	
	private ArrayList<McdComposentGraphique> m_components, m_componentsFirst, m_componentsSecond;
	
	public McdGraph() {
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		
		prefs.setFont(PGroupe.HERITAGE, PCle.FONT, "TimesRoman", Font.PLAIN, 5);
		prefs.set(PGroupe.HERITAGE,  PCle.COLOR, Color.GREEN);
		prefs.set(PGroupe.HERITAGE, PCle.COLOR_CONTOUR, Color.RED);
		prefs.set(PGroupe.HERITAGE, PCle.FONT_COLOR, Color.BLACK);
		
		prefs.setFont(PGroupe.CARDINALITE, PCle.FONT, "TimesRoman", Font.PLAIN, 10);
		prefs.set(PGroupe.CARDINALITE, PCle.FONT_COLOR, Color.BLACK);
		prefs.set(PGroupe.CARDINALITE, PCle.COLOR, Color.BLACK);
		
		prefs.setFont(PGroupe.RELATION, PCle.FONT, "TimesRoman", Font.PLAIN, 10);
		prefs.setFont(PGroupe.RELATION, PCle.FONT_NOM, "TimesRoman", Font.PLAIN, 10);
		prefs.set(PGroupe.RELATION, PCle.FONT_COLOR, Color.GRAY);
		prefs.set(PGroupe.RELATION, PCle.FONT_NOM_COLOR, Color.BLACK);
		prefs.set(PGroupe.RELATION, PCle.COLOR, Color.GREEN);
		prefs.set(PGroupe.RELATION, PCle.COLOR_CONTOUR, Color.BLACK);
		
		prefs.setFont(PGroupe.ENTITE, PCle.FONT, "TimesRoman", Font.PLAIN, 10);
		prefs.setFont(PGroupe.ENTITE, PCle.FONT_NOM, "TimesRoman", Font.PLAIN, 12);
		prefs.set(PGroupe.ENTITE,  PCle.COLOR, Color.GREEN);
		prefs.set(PGroupe.ENTITE, PCle.COLOR_CONTOUR, Color.RED);
		prefs.set(PGroupe.ENTITE, PCle.FONT_COLOR, Color.RED);
		prefs.set(PGroupe.ENTITE, PCle.FONT_NOM_COLOR, Color.BLACK);
		
		m_states = new Hashtable<McdGraphStateE,McdGraphState>();
		m_states.put(McdGraphStateE.EDIT, new McdGraphStateEdit());
		m_states.put(McdGraphStateE.INSERT_ENTITE, new McdGraphStateInsertEntite());
		m_states.put(McdGraphStateE.INSERT_RELATION, new McdGraphStateInsertRelation());
		m_states.put(McdGraphStateE.INSERT_LIEN, new McdGraphStateInsertLien());
		m_states.put(McdGraphStateE.INSERT_CONTRAINTE, new McdGraphStateInsertContrainte());
		m_currentState = McdGraphStateE.INVALID;

		m_components = new ArrayList<McdComposentGraphique>();
		m_componentsFirst = new ArrayList<McdComposentGraphique>();
		m_componentsSecond = new ArrayList<McdComposentGraphique>();
		
		m_logicObjects = new Hashtable<Object, McdComposentGraphique> ();
		m_focus = null;
		m_deltaSelect = new Point();
		
		this.setSize(new Dimension(80, 80));
		this.setState(McdGraphStateE.EDIT);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		for(McdComposentGraphique component : m_componentsFirst){
			component.dessiner(g);
		}
		for(McdComposentGraphique component : m_componentsSecond){
			component.dessiner(g);
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
	public void setState(McdGraphStateE s){
		if(m_currentState!=McdGraphStateE.INVALID){
			m_states.get(this.m_currentState).leftState();
			this.removeMouseListener(m_states.get(this.m_currentState));
			this.removeMouseMotionListener(m_states.get(this.m_currentState));
		}
		m_currentState=s;
		this.addMouseMotionListener(m_states.get(this.m_currentState));
		this.addMouseListener(m_states.get(this.m_currentState));
		m_states.get(this.m_currentState).enterState();
	}
	
	
	
	
	
	
	private abstract class McdGraphState implements MouseListener, MouseMotionListener{
		public void enterState(){ }
		public void leftState() { }
	}
	private abstract class McdGraphStateInsert extends McdGraphState{
		protected int m_last;
		public McdGraphStateInsert(){
			m_last=0;
		}
	}
	private class McdGraphStateInsertEntite extends McdGraphStateInsert{

		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			EntiteGraph eg = new EntiteGraph();
			eg.setEntite(new Entite("Entite"+(m_last++)));
			eg.setPosition(e.getPoint());
			eg.setMcd(McdGraph.this);
			m_components.add(eg);
			m_componentsSecond.add(eg);
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			
		}

		public void mouseDragged(MouseEvent arg0) {
			
		}

		public void mouseMoved(MouseEvent arg0) {
			
		}
	}
	private class McdGraphStateInsertRelation extends McdGraphStateInsert{
		
		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent arg0) {
			
		}

		public void mouseExited(MouseEvent arg0) {
			
		}

		public void mousePressed(MouseEvent e) {
			RelationGraph eg = new RelationGraph();
			eg.setRelation(new Relation("Relation"+(m_last++)));
			eg.setPosition(e.getPoint());
			eg.setMcd(McdGraph.this);
			m_components.add(eg);
			m_componentsSecond.add(eg);
			repaint();
		}

		public void mouseReleased(MouseEvent arg0) {
			
		}

		public void mouseDragged(MouseEvent arg0) {
			
		}

		public void mouseMoved(MouseEvent arg0) {
			
		}
		
	}
	private class McdGraphStateEdit extends McdGraphState{
		public void enterState(){
			m_focus=null;
		}
		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			for (McdComposentGraphique composant : m_components){
				FormeGeometrique forme = (FormeGeometrique)composant;
				if (forme.contient(e.getPoint())){
					m_focus = composant;
					m_deltaSelect.x = e.getPoint().x - forme.getPosition().x;
					m_deltaSelect.y = e.getPoint().y - forme.getPosition().y;
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			//m_focus = null; //Pour gérer la suppression, il faut garder le dernier click
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

		public void mouseMoved(MouseEvent e) {
			
		}
	}

	class McdGraphStateInsertLien extends McdGraphState{
		private McdComposentGraphique m_objects[];
		private int m_current;
		public McdGraphStateInsertLien(){
			m_objects = new McdComposentGraphique[2];
		}
		public void enterState(){
			m_current=0;
		}
		private void clear(){
			m_objects[0]=null;
			m_objects[1]=null;
			m_current=0;
		}
		public void mouseClicked(MouseEvent e) {
			
		}
		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			for(McdComposentGraphique component : m_components){
				if(((FormeGeometrique)component).contient(e.getPoint())){
					if(component.isLinkable()){
						m_objects[m_current++] = component;
						break;
					}

				}
			}
			if(m_current > 1){ //Si on a 2 objets de linkés
				//On vérifie que les deux objets sont bien différents
				if(m_objects[0]==m_objects[1]){
					clear();
					return;
				}
				//Cas relation entite/relation
				if((m_objects[0] instanceof EntiteGraph &&
						m_objects[1] instanceof RelationGraph)||
						(m_objects[1] instanceof EntiteGraph &&
						m_objects[0] instanceof RelationGraph)){
					EntiteGraph ent = (EntiteGraph) ((m_objects[0] instanceof EntiteGraph) ?
							m_objects[0]:m_objects[1]);
					RelationGraph rel = (RelationGraph) ((m_objects[0] instanceof RelationGraph) ?
							m_objects[0]:m_objects[1]);
					
					CardinaliteGraph cardG = new CardinaliteGraph();
					Cardinalite card = new Cardinalite();
					card.setEntite(ent.getEntite());
					card.setRelation(rel.getRelation());
					cardG.setCardinalite(card);
					cardG.setMcd(McdGraph.this);
					
					m_components.add(cardG);
					m_componentsFirst.add(cardG);
				}
				else if(m_objects[0] instanceof ContrainteGraph || m_objects[1] instanceof ContrainteGraph){
					ContrainteGraph contrainte;
				}
				clear();
			}
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			
		}

		public void mouseDragged(MouseEvent e) {
			
		}

		public void mouseMoved(MouseEvent e) {
			
		}
	}
	private class McdGraphStateInsertContrainte extends McdGraphState{

		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			ContrainteGraph contG = new ContrainteGraph();
			Contrainte cont = new Contrainte("X");
			
			contG.setContrainte(cont);
			contG.setPosition(e.getPoint());
			contG.setMcd(McdGraph.this);
			
			m_components.add(contG);
			m_componentsFirst.add(contG);
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			
		}

		public void mouseDragged(MouseEvent e) {
			
		}

		public void mouseMoved(MouseEvent e) {
			
		}
		
	}
}
