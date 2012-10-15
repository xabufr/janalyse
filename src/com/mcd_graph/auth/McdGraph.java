package com.mcd_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mcd_composent_graph.auth.CardinaliteGraph;
import com.mcd_composent_graph.auth.ContrainteGraph;
import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.HeritageGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_composent_graph.auth.FormeGeometrique;
import com.mcd_edition_fenetre.auth.FenetreEditionEntite;
import com.mcd_edition_fenetre.auth.FenetreEditionHeritage;
import com.mcd_edition_fenetre.auth.FenetreEditionCardinalite;
import com.mcd_edition_fenetre.auth.FenetreEditionRelation;
import com.mcd_log.auth.Cardinalite;
import com.mcd_log.auth.Contrainte;
import com.mcd_log.auth.ContrainteType;
import com.mcd_log.auth.Heritage;
import com.mcd_log.auth.HeritageType;
import com.mcd_log.auth.Relation;
import com.mcd_log.auth.Entite;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class McdGraph extends JPanel{
	private McdComposentGraphique m_focus;
	private Point m_deltaSelect;
	private Hashtable<McdGraphStateE, McdGraphState> m_states;
	private McdGraphStateE m_currentState;
	private Hashtable<Object, McdComposentGraphique> m_logicObjects;
	private FenetrePrincipale m_fenetrePrincipale;
	private ArrayList<McdComposentGraphique> m_components, m_componentsFirst, m_componentsSecond;
	private Boolean m_isMoving;
	
	public McdGraph(FenetrePrincipale fenPrinc) {
		m_fenetrePrincipale = fenPrinc;
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		
		prefs.setFont(PGroupe.HERITAGE, PCle.FONT, "TimesRoman", Font.PLAIN, 10);
		prefs.set(PGroupe.HERITAGE,  PCle.COLOR, Color.GREEN);
		prefs.set(PGroupe.HERITAGE, PCle.COLOR_CONTOUR, Color.RED);
		prefs.set(PGroupe.HERITAGE, PCle.COLOR_LINE, Color.BLACK);
		prefs.set(PGroupe.HERITAGE, PCle.FONT_COLOR, Color.BLACK);
		prefs.set(PGroupe.HERITAGE, PCle.COLOR_CONTOUR_FOCUS, Color.RED);
		
		prefs.setFont(PGroupe.CONTRAINTE, PCle.FONT, "TimesRoman", Font.PLAIN, 10);
		prefs.set(PGroupe.CONTRAINTE,  PCle.COLOR, Color.CYAN);
		prefs.set(PGroupe.CONTRAINTE, PCle.COLOR_CONTOUR, Color.BLACK);
		prefs.set(PGroupe.CONTRAINTE, PCle.COLOR_LINE, Color.BLACK);
		prefs.set(PGroupe.CONTRAINTE, PCle.FONT_COLOR, Color.BLACK);
		prefs.set(PGroupe.CONTRAINTE, PCle.COLOR_CONTOUR_FOCUS, Color.RED);
		
		prefs.setFont(PGroupe.CARDINALITE, PCle.FONT, "TimesRoman", Font.PLAIN, 10);
		prefs.set(PGroupe.CARDINALITE, PCle.FONT_COLOR, Color.BLACK);
		prefs.set(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR, Color.BLACK);
		prefs.set(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR_FOCUS, Color.RED);
		
		prefs.setFont(PGroupe.RELATION, PCle.FONT, "TimesRoman", Font.PLAIN, 10);
		prefs.setFont(PGroupe.RELATION, PCle.FONT_NOM, "TimesRoman", Font.PLAIN, 10);
		prefs.setFont(PGroupe.RELATION, PCle.FONT_FOCUS, "TimesRoman", Font.PLAIN, 10);
		prefs.setFont(PGroupe.RELATION, PCle.FONT_NOM_FOCUS, "TimesRoman", Font.PLAIN, 10);
		prefs.set(PGroupe.RELATION, PCle.FONT_COLOR, Color.GRAY);
		prefs.set(PGroupe.RELATION, PCle.FONT_NOM_COLOR, Color.BLACK);
		prefs.set(PGroupe.RELATION, PCle.COLOR, Color.GREEN);
		prefs.set(PGroupe.RELATION, PCle.COLOR_CONTOUR, Color.BLACK);
		prefs.set(PGroupe.RELATION, PCle.COLOR_CONTOUR_FOCUS, Color.RED);
		
		prefs.setFont(PGroupe.ENTITE, PCle.FONT, "TimesRoman", Font.PLAIN, 10);
		prefs.setFont(PGroupe.ENTITE, PCle.FONT_NOM, "TimesRoman", Font.PLAIN, 12);
		prefs.setFont(PGroupe.ENTITE, PCle.FONT_FOCUS, "TimesRoman", Font.PLAIN, 10);
		prefs.setFont(PGroupe.ENTITE, PCle.FONT_NOM_FOCUS, "TimesRoman", Font.PLAIN, 12);
		prefs.set(PGroupe.ENTITE,  PCle.COLOR, Color.GREEN);
		prefs.set(PGroupe.ENTITE, PCle.COLOR_CONTOUR, Color.RED);
		prefs.set(PGroupe.ENTITE, PCle.FONT_COLOR, Color.RED);
		prefs.set(PGroupe.ENTITE, PCle.FONT_NOM_COLOR, Color.BLACK);
		prefs.set(PGroupe.ENTITE, PCle.COLOR_CONTOUR_FOCUS, Color.RED);
		
		m_states = new Hashtable<McdGraphStateE,McdGraphState>();
		m_states.put(McdGraphStateE.MOVE, new McdGraphStateMove());
		m_states.put(McdGraphStateE.INSERT_ENTITE, new McdGraphStateInsertEntite());
		m_states.put(McdGraphStateE.INSERT_RELATION, new McdGraphStateInsertRelation());
		m_states.put(McdGraphStateE.INSERT_LIEN, new McdGraphStateInsertLien());
		m_states.put(McdGraphStateE.INSERT_CONTRAINTE, new McdGraphStateInsertContrainte());
		m_states.put(McdGraphStateE.INSERT_HERITAGE, new McdGraphStateInsertHeritage());
		m_states.put(McdGraphStateE.EDIT, new McdGraphStateEdit());
		m_currentState = McdGraphStateE.INVALID;

		m_components = new ArrayList<McdComposentGraphique>();
		m_componentsFirst = new ArrayList<McdComposentGraphique>();
		m_componentsSecond = new ArrayList<McdComposentGraphique>();
		
		m_logicObjects = new Hashtable<Object, McdComposentGraphique> ();
		m_focus = null;
		m_isMoving = false;
		m_deltaSelect = new Point();
		
		this.setSize(new Dimension(80, 80));
		this.setState(McdGraphStateE.MOVE);
		this.setFocusable(true);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		Point min = new Point(0,0), max = new Point(0,0);
		for(McdComposentGraphique component : m_componentsFirst){
			component.dessiner(g);
			if(component instanceof FormeGeometrique){
				FormeGeometrique forme = (FormeGeometrique) component;
				if(min.x>forme.getPosition().x){
					min.x=forme.getPosition().x-50;
				}
				if(min.y>forme.getPosition().y){
					min.y=forme.getPosition().y-50;
				}
				if(max.x<forme.getPosition().x){
					max.x=forme.getPosition().x+50;
				}
				if(max.y<forme.getPosition().y){
					max.y=forme.getPosition().y+50;
				}
			}
		}
		for(McdComposentGraphique component : m_componentsSecond){
			component.dessiner(g);
			if(component instanceof FormeGeometrique){
				FormeGeometrique forme = (FormeGeometrique) component;
				
				if(component instanceof EntiteGraph){
					EntiteGraph eg = (EntiteGraph) component;
					Rectangle r = eg.getRectangle();
					if(min.x>r.x){
						min.x=r.x;
					}
					if(min.y>r.y){
						min.y=r.y;
					}
					if(max.x<r.x+r.width){
						max.x=r.x+r.width;
					}
					if(max.y<r.y+r.height){
						max.y=r.y+r.height;
					}
				}
				else if(component instanceof RelationGraph){
					RelationGraph rg = (RelationGraph) component;
					Rectangle r = rg.getRectangle();
					if(min.x>r.x){
						min.x=r.x;
					}
					if(min.y>r.y){
						min.y=r.y;
					}
					if(max.x<r.x+r.width){
						max.x=r.x+r.width;
					}
					if(max.y<r.y+r.height){
						max.y=r.y+r.height;
					}
				}
				else{
					if(min.x>forme.getPosition().x){
						min.x=forme.getPosition().x;
					}
					if(min.y>forme.getPosition().y){
						min.y=forme.getPosition().y;
					}
					if(max.x<forme.getPosition().x){
						max.x=forme.getPosition().x+50;
					}
					if(max.y<forme.getPosition().y){
						max.y=forme.getPosition().y+50;
					}
				}
			}
		}
		Dimension nouvelleDim = new Dimension(max.x,max.y);
		if(min.x<0)
			nouvelleDim.width-=min.x;
		if(min.y<0)
			nouvelleDim.height-=min.y;
		if(!m_isMoving&&!getPreferredSize().equals(nouvelleDim))
		{
			if(min.x<0){
				for(McdComposentGraphique component : m_components){
					if(component instanceof EntiteGraph){
						EntiteGraph eg = (EntiteGraph) component;
						eg.setPosition(new Point(eg.getPosition().x-min.x, eg.getPosition().y));
					}
				}
			}
			if(min.y<0){
				for(McdComposentGraphique component : m_components){
					if(component instanceof EntiteGraph){
						EntiteGraph eg = (EntiteGraph) component;
						eg.setPosition(new Point(eg.getPosition().x, eg.getPosition().y-min.y));
					}
				}
			}
			setPreferredSize(nouvelleDim);
			m_fenetrePrincipale.updateScrollBar();
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
			this.removeKeyListener(m_states.get(this.m_currentState));
		}
		m_currentState=s;
		this.addMouseMotionListener(m_states.get(this.m_currentState));
		this.addMouseListener(m_states.get(this.m_currentState));
		this.addKeyListener(m_states.get(this.m_currentState));
		m_states.get(this.m_currentState).enterState();
	}
	public McdGraphStateE getState(){
		return m_currentState;
	}
		
	private abstract class McdGraphState implements MouseListener, MouseMotionListener, KeyListener{
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
		public void keyPressed(KeyEvent arg0) {
			
		}
		public void keyReleased(KeyEvent arg0) {
					
		}
		public void keyTyped(KeyEvent arg0) {
			
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

		public void keyPressed(KeyEvent e) {
			
		}

		public void keyReleased(KeyEvent e) {
			
		}

		public void keyTyped(KeyEvent e) {
			
		}
		
	}
	private class McdGraphStateMove extends McdGraphState{
		public void enterState(){
			setMcdComposentGraphiquetFocus(null);
		}
		public void leftState(){
			m_isMoving=false;
		}
		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			for (McdComposentGraphique composant : m_components){
				if(!composant.isMovable())
					continue;
				FormeGeometrique forme = (FormeGeometrique)composant;
				if (forme.contient(
						e.getPoint())){
					setMcdComposentGraphiquetFocus(composant);
					m_deltaSelect.x = e.getPoint().x - forme.getPosition().x;
					m_deltaSelect.y = e.getPoint().y - forme.getPosition().y;
					m_isMoving=true;
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			setMcdComposentGraphiquetFocus(null); //Pour gérer la suppression, il faut garder le dernier click
			m_isMoving=false;
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
		public void keyPressed(KeyEvent e) {
			
		}
		public void keyReleased(KeyEvent e) {
			
		}
		public void keyTyped(KeyEvent e) {
			
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
			setMcdComposentGraphiquetFocus(null);
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
						setMcdComposentGraphiquetFocus(component);
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
					ContrainteGraph contrainte = (ContrainteGraph) (m_objects[0] instanceof ContrainteGraph ?
							m_objects[0]:m_objects[1]);
					if(m_objects[0] instanceof RelationGraph || m_objects[1] instanceof RelationGraph){
						RelationGraph relation = (RelationGraph) (m_objects[0] instanceof RelationGraph ?
								m_objects[0]:m_objects[1]);
						contrainte.getContrainte().addRelation(relation.getRelation());
					}
					else if(m_objects[0] instanceof EntiteGraph || m_objects[1] instanceof EntiteGraph){
						EntiteGraph entite = (EntiteGraph) (m_objects[0] instanceof EntiteGraph ?
								m_objects[0]:m_objects[1]);
						contrainte.getContrainte().addEntite(entite.getEntite());
					}
					contrainte.update();
				}
				else if(m_objects[0] instanceof HeritageGraph || m_objects[1] instanceof HeritageGraph){
					HeritageGraph heritage = (HeritageGraph) (m_objects[0] instanceof HeritageGraph ?
							m_objects[0]:m_objects[1]);
					if(m_objects[0] instanceof EntiteGraph || m_objects[1] instanceof EntiteGraph){
						EntiteGraph entite = (EntiteGraph) (m_objects[0] instanceof EntiteGraph ?
								m_objects[0]:m_objects[1]);
						heritage.getHeritage().addEnfant(entite.getEntite());
					}
					heritage.update();
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
		public void keyPressed(KeyEvent e) {
			
		}
		public void keyReleased(KeyEvent e) {
			
		}
		public void keyTyped(KeyEvent e) {
			
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
			Contrainte cont = new Contrainte(ContrainteType.X);
			
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

		public void keyPressed(KeyEvent e) {
			
		}

		public void keyReleased(KeyEvent e) {
			
		}

		public void keyTyped(KeyEvent e) {
			
		}
		
	}
	private class McdGraphStateInsertHeritage extends McdGraphState{

		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}

		public void mousePressed(MouseEvent e) {
			HeritageGraph herG = new HeritageGraph();
			Heritage her = new Heritage(HeritageType.XT);
			
			herG.setHeritage(her);
			herG.setPosition(e.getPoint());
			herG.setMcd(McdGraph.this);
			
			m_components.add(herG);
			m_componentsFirst.add(herG);
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			
		}

		public void mouseDragged(MouseEvent e) {
			
		}

		public void mouseMoved(MouseEvent e) {
			
		}

		public void keyPressed(KeyEvent e) {
			
		}

		public void keyReleased(KeyEvent e) {
			
		}

		public void keyTyped(KeyEvent e) {
			
		}
	}
	private class McdGraphStateEdit extends McdGraphState{
		private long m_time, m_interval;
		
		public McdGraphStateEdit() {
			m_interval = 500;
		}
		public void enterState(){
			setMcdComposentGraphiquetFocus(null);
		}
		public void mouseClicked(MouseEvent arg0) {
			
		}

		public void mouseEntered(MouseEvent arg0) {
			
		}

		public void mouseExited(MouseEvent arg0) {
			
		}

		public void mousePressed(MouseEvent e) {
			Boolean found=false;
			for(McdComposentGraphique component : m_componentsSecond){
				if(((FormeGeometrique)component).contient(e.getPoint())){
					found=true;
					if(component!=m_focus){
						m_time=System.currentTimeMillis();
						setMcdComposentGraphiquetFocus(component);
						break;
					}
				}
			}
			if(!found){
				for(McdComposentGraphique component : m_componentsFirst){
					if(((FormeGeometrique)component).contient(e.getPoint())){
						found=true;
						if(component!=m_focus){
							m_time=System.currentTimeMillis();
							setMcdComposentGraphiquetFocus(component);
							break;
						}
					}
				}
			}
			if(found&&(System.currentTimeMillis()-m_time>=m_interval)||
					e.getClickCount()==2)
			{
				if(m_focus instanceof RelationGraph)
				{
					new FenetreEditionRelation(McdGraph.this, (RelationGraph)m_focus).setVisible(true);
					((RelationGraph)m_focus).actualiser();
				}
				else if (m_focus instanceof ContrainteGraph){
					String nom;
					nom = JOptionPane.showInputDialog(null, "Type de contrainte:", "Edition Contrainte", JOptionPane.PLAIN_MESSAGE, null, ContrainteType.values(), ContrainteType.PLUS).toString();
					((ContrainteGraph) m_focus).getContrainte().setNom(ContrainteType.valueOf(nom));
					((ContrainteGraph) m_focus).update();
				}
				else if (m_focus instanceof HeritageGraph){
					new FenetreEditionHeritage(McdGraph.this, (HeritageGraph)m_focus).setVisible(true);
					((HeritageGraph)m_focus).update();
				}
				else if (m_focus instanceof EntiteGraph){
					new FenetreEditionEntite(McdGraph.this, (EntiteGraph)m_focus).setVisible(true);
				}
				else if(m_focus instanceof CardinaliteGraph){
					new FenetreEditionCardinalite(McdGraph.this, (CardinaliteGraph)m_focus).setVisible(true);
				}
				m_focus=null;
				setMcdComposentGraphiquetFocus(null);
			}
			else if(!found){
				setMcdComposentGraphiquetFocus(null);
			}
			
		}

		public void mouseReleased(MouseEvent arg0) {
			
		}

		public void mouseDragged(MouseEvent arg0) {
			
		}

		public void mouseMoved(MouseEvent arg0) {
			
		}
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_DELETE&&m_focus!=null){
				deleteMcdComposent(m_focus);
			}
		}
		public void keyReleased(KeyEvent e) {
			
		}
		public void keyTyped(KeyEvent e) {
			
		}
	}
	private void deleteMcdComposent(McdComposentGraphique comp){
		if(comp==m_focus)
			setMcdComposentGraphiquetFocus(null);
		comp.prepareDelete();
		m_components.remove(comp);
		if(m_componentsFirst.contains(comp))
			m_componentsFirst.remove(comp);
		else
			m_componentsSecond.remove(comp);
	}
	private void setMcdComposentGraphiquetFocus(McdComposentGraphique comp){
		if(m_focus!=null){
			m_focus.setFocus(false);
		}
		m_focus=comp;
		if(m_focus!=null){
			m_focus.setFocus(true);
		}
		repaint();
	}
}
