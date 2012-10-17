package com.mcd_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
//import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.event.auth.Sauvegarde;
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

@SuppressWarnings("serial")
public class McdGraph extends JPanel{
	private McdComposentGraphique m_focus;
	private McdComposentGraphique m_copie;
	private Point m_deltaSelect;
	private Hashtable<McdGraphStateE, McdGraphState> m_states;
	private McdGraphStateE m_currentState;
	private Hashtable<Object, McdComposentGraphique> m_logicObjects;
	private FenetrePrincipale m_fenetrePrincipale;
	private ArrayList<McdComposentGraphique> m_components, m_componentsFirst, m_componentsSecond;
	private Stack<Hashtable<Object, McdComposentGraphique>> m_listeAnnuler, m_listeRefaire;
	private Boolean m_isMoving, m_isSaved;
	private File m_file;
	
	public McdGraph(FenetrePrincipale fenPrinc) {
		m_fenetrePrincipale = fenPrinc;
		
		m_states = new Hashtable<McdGraphStateE,McdGraphState>();
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
		m_listeAnnuler = new Stack<Hashtable<Object,McdComposentGraphique>>();
		m_listeRefaire = new Stack<Hashtable<Object,McdComposentGraphique>>();
		m_focus = null;
		m_isMoving = false;
		m_isSaved=false;
		m_deltaSelect = new Point();
		setFile(null);
		
		this.setSize(new Dimension(80, 80));
		this.setState(McdGraphStateE.INSERT_ENTITE);
		this.setFocusable(true);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		Point min = new Point(0,0), max = new Point(0,0);
		for(McdComposentGraphique component : m_componentsFirst){
			component.dessiner(g);
			if(component instanceof CardinaliteGraph)
				continue;
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
				
				if(component instanceof EntiteGraph||
						component instanceof RelationGraph||
						component instanceof ContrainteGraph||
						component instanceof HeritageGraph){
					Rectangle r = null;
					if(component instanceof EntiteGraph){
						EntiteGraph eg = (EntiteGraph) component;
						r = eg.getRectangle();
					}
					else if(component instanceof RelationGraph){
						RelationGraph rg = (RelationGraph) component;
						r = rg.getRectangle();
					}
					else if(component instanceof ContrainteGraph){
						ContrainteGraph cg = (ContrainteGraph) component;
						r = cg.getRectangle();
					}
					else{
						HeritageGraph hg = (HeritageGraph) component;
						r = hg.getRectangle();
					}
					if(min.x>r.x){
						min.x=r.x-1;
					}
					if(min.y>r.y){
						min.y=r.y-1;
					}
					if(max.x<r.x+r.width){
						max.x=r.x+r.width+1;
					}
					if(max.y<r.y+r.height){
						max.y=r.y+r.height+1;
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
						max.x=forme.getPosition().x+10;
					}
					if(max.y<forme.getPosition().y){
						max.y=forme.getPosition().y+10;
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
			saveAnnulerModification();
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
			saveAnnulerModification();
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
				saveAnnulerModification();
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
			saveAnnulerModification();
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
			saveAnnulerModification();
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
			m_isMoving=false;
		}
		public void leftState(){
			m_isMoving=false;
		}
		public void mouseClicked(MouseEvent arg0) {
			
		}

		public void mouseEntered(MouseEvent arg0) {
			
		}

		public void mouseExited(MouseEvent arg0) {
			
		}

		public void mousePressed(MouseEvent e) {
			requestFocusInWindow();
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
			if(m_focus!=null&&e.getClickCount()==1&&found){ // 1 click -> mode move
					if(!m_focus.isMovable())
						return;
					FormeGeometrique forme = (FormeGeometrique)m_focus;
					if (forme.contient(
							e.getPoint())){
						setMcdComposentGraphiquetFocus(m_focus);
						m_deltaSelect.x = e.getPoint().x - forme.getPosition().x;
						m_deltaSelect.y = e.getPoint().y - forme.getPosition().y;
						m_isMoving=true;
						saveAnnulerModification();
					}
				
			}
			else if(found&&(System.currentTimeMillis()-m_time>=m_interval)|| // doubleclick
					e.getClickCount()==2)
			{
				saveAnnulerModification();
				if(m_focus instanceof RelationGraph)
				{
					new FenetreEditionRelation(McdGraph.this, (RelationGraph)m_focus).setVisible(true);
					((RelationGraph)m_focus).actualiser();
				}
				else if (m_focus instanceof ContrainteGraph){
					ContrainteType type=null;
					type = (ContrainteType) JOptionPane.showInputDialog(null, "Type de contrainte:", "Edition Contrainte", JOptionPane.PLAIN_MESSAGE, null, ContrainteType.values(), ((ContrainteGraph) m_focus).getContrainte().getType());
					((ContrainteGraph) m_focus).getContrainte().setNom(type);
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
				setMcdComposentGraphiquetFocus(null);
			}
			else if(!found){//Click en dehors
				setMcdComposentGraphiquetFocus(null);
			}
			
		}

		public void mouseReleased(MouseEvent arg0) {
			m_isMoving=false;
		}

		public void mouseDragged(MouseEvent e) {
			if (m_focus != null&&m_isMoving){
				FormeGeometrique forme = (FormeGeometrique)m_focus;
				Point tmp = new Point();
				tmp.x = e.getPoint().x - m_deltaSelect.x;
				tmp.y = e.getPoint().y - m_deltaSelect.y;
				forme.setPosition(tmp);
				repaint(); //Intéressant n'est-ce pas ? Note que le McdGraph.this. est facultatif ici...
			}
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
		saveAnnulerModification();
		if(comp==m_focus)
			setMcdComposentGraphiquetFocus(null);
		comp.prepareDelete();
		m_components.remove(comp);
		if(m_componentsFirst.contains(comp))
			m_componentsFirst.remove(comp);
		else
			m_componentsSecond.remove(comp);
	}
	public void copyMcdComposent(){
		m_copie = m_focus;
	}
	public void pastMcdComposent() throws CloneNotSupportedException{
		if (m_copie != null){
			saveAnnulerModification();
			if (m_copie instanceof EntiteGraph){
				EntiteGraph eg = new EntiteGraph();
				Entite e = ((EntiteGraph) m_copie).getEntite().clone();
				
				eg.setEntite(e);
				eg.setPosition(getMousePosition());
				eg.setMcd(McdGraph.this);
				
				m_components.add(eg);
				m_componentsSecond.add(eg);
				repaint();
			}
			else if (m_copie instanceof RelationGraph){
				RelationGraph rg = new RelationGraph();
				Relation r = ((RelationGraph) m_copie).getRelation().clone();
				
				rg.setRelation(r);
				rg.setPosition(getMousePosition());
				rg.setMcd(McdGraph.this);
				
				m_components.add(rg);
				m_componentsSecond.add(rg);
				repaint();
			}
			else if (m_copie instanceof ContrainteGraph){
				ContrainteGraph cg = new ContrainteGraph();
				Contrainte c = ((ContrainteGraph) m_copie).getContrainte().clone();
				
				cg.setContrainte(c);
				cg.setPosition(getMousePosition());
				cg.setMcd(McdGraph.this);
				
				m_components.add(cg);
				m_componentsFirst.add(cg);
				repaint();
			}
			else if (m_copie instanceof HeritageGraph){
				HeritageGraph hg = new HeritageGraph();
				Heritage h = ((HeritageGraph) m_copie).getHeritage().clone();
				
				hg.setHeritage(h);
				hg.setPosition(getMousePosition());
				hg.setMcd(McdGraph.this);
				
				m_components.add(hg);
				m_componentsFirst.add(hg);
				repaint();
			}
		}
	}
	
	public ArrayList<McdComposentGraphique> getMcdComponents(){
		return m_components;
	}
	
	public void saveMcdComposent(){
		Sauvegarde save = new Sauvegarde(this);
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
	public void saveAnnulerModification(){
		m_listeAnnuler.push(copyLogicGraph(m_logicObjects));
		m_listeRefaire.clear();
		m_isSaved=false;
		m_fenetrePrincipale.updateMcdUi(this);
	}
	
	private Hashtable<Object, McdComposentGraphique> copyLogicGraph(Hashtable<Object, McdComposentGraphique> from){
		Hashtable<Object, McdComposentGraphique> tmp = new Hashtable<Object, McdComposentGraphique>();
		Hashtable<Object, Object> correspondances = new Hashtable<Object, Object>();
		Enumeration<Object> keys = from.keys();
		while(keys.hasMoreElements()){
			Object key = keys.nextElement();
			if(key instanceof Entite){
				Entite e=null;
				try {
					e = ((Entite) key).clone();
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
				EntiteGraph eg = new EntiteGraph();
				eg.setEntite(e);
				eg.setPosition(((EntiteGraph)from.get(key)).getPosition());
				tmp.put(e, eg);
				correspondances.put(key, e);
			}
			else if(key instanceof Relation){
				Relation r = new Relation((Relation)key);
				RelationGraph rg = new RelationGraph();
				rg.setRelation(r);
				rg.setPosition(((RelationGraph)from.get(key)).getPosition());
				tmp.put(r, rg);
				correspondances.put(key, r);
			}
		}
		keys = from.keys();
		while(keys.hasMoreElements()){
			Object key = keys.nextElement();
			if(key instanceof Entite||key instanceof Relation){
				continue;
			}
			if(key instanceof Cardinalite){
				Cardinalite c=null;
				try {
					c = ((Cardinalite)key).clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				c.setEntite((Entite) correspondances.get(((Cardinalite)key).getEntite()));
				c.setRelation((Relation) correspondances.get(((Cardinalite)key).getRelation()));
				CardinaliteGraph cg = new CardinaliteGraph();
				CardinaliteGraph acg = (CardinaliteGraph)from.get(key);
				cg.setTypeDessin(acg.getTypeDessin());
				cg.setCardinalite(c);
				tmp.put(c, cg);
			}
			else if(key instanceof Heritage){
				Heritage h=null;
				try {
					h = ((Heritage)key).clone();
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
				Heritage anc = (Heritage)key;
				
				h.setEnfants(new ArrayList<Entite>());
				for(Entite e : anc.getEnfants()){
					Entite nouveau = (Entite) correspondances.get(e);
					h.addEnfant(nouveau);
				}
				
				HeritageGraph hg = new HeritageGraph();
				hg.setPosition(((HeritageGraph)from.get(key)).getPosition());
				hg.setHeritage(h);
				tmp.put(h, hg);
			}
			else if(key instanceof Contrainte){
				Contrainte c = null;
				try {
					c = ((Contrainte)key).clone();
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
				c.setEntites(new ArrayList<Entite>());
				c.setRelations(new ArrayList<Relation>());
				Contrainte anc = (Contrainte)key;
				for(Entite e : anc.getEntites()){
					Entite nouvelle = (Entite) correspondances.get(e);
					c.addEntite(nouvelle);
				}
				for(Relation r : anc.getRelations()){
					Relation nouvelle = (Relation) correspondances.get(r);
					c.addRelation(nouvelle);
				}
				
				ContrainteGraph cg = new ContrainteGraph();
				ContrainteGraph acg = (ContrainteGraph) from.get(key);
				cg.setPosition(acg.getPosition());
				cg.setContrainte(c);
				tmp.put(c, cg);
			}
		}
		return tmp;
	}
	public void annuler(){
		if(m_listeAnnuler.isEmpty())
			return;
		if(m_listeRefaire.isEmpty())
			saveAnnulerModification();
		setMcdComposentGraphiquetFocus(null);
		Hashtable<Object, McdComposentGraphique> nouvelleLogique = m_listeAnnuler.pop();
		m_listeRefaire.push(nouvelleLogique);
		setCurrentObjects(nouvelleLogique);
	}
	public void refaire(){
		if(m_listeRefaire.isEmpty())
			return;
		setMcdComposentGraphiquetFocus(null);
		Hashtable<Object, McdComposentGraphique> nouvelleLogique = m_listeRefaire.pop();
		m_listeAnnuler.push(nouvelleLogique);
		setCurrentObjects(nouvelleLogique);
	}
	private void setCurrentObjects(Hashtable<Object, McdComposentGraphique> objs){
		setMcdComposentGraphiquetFocus(null);
		Hashtable<Object, McdComposentGraphique> nouvelleLogique = copyLogicGraph(objs);
		m_components.clear();
		m_componentsFirst.clear();
		m_componentsSecond.clear();
		for(McdComposentGraphique comp : nouvelleLogique.values()){
			m_components.add(comp);
			if(comp instanceof ContrainteGraph||
					comp instanceof HeritageGraph||
					comp instanceof CardinaliteGraph)
				m_componentsFirst.add(comp);
			else
				m_componentsSecond.add(comp);
		}
		for(McdComposentGraphique comp : m_components)
			comp.setMcd(this);
		m_logicObjects=nouvelleLogique;
		repaint();
	}
	public String getName(){
		String name = super.getName();
		if(!m_isSaved)
			name += "*";
		return name;
	}
	public String getLogicName(){
		return super.getName();
	}
	public void setSaved(Boolean s){
		m_isSaved=s;
	}
	public Boolean isSaved(){
		return m_isSaved;
	}
	public File getFile() {
		return m_file;
	}
	public void setFile(File file) {
		m_file = file;
	}
	public Boolean peutAnnuler(){
		return !m_listeAnnuler.isEmpty();
	}
	public Boolean peutRefaire(){
		return !m_listeRefaire.isEmpty();
	}
}
