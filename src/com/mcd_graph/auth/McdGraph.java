package com.mcd_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import javax.swing.JPanel;

import com.event.auth.SelectionMultiple;
import com.mcd_composent_graph.auth.CardinaliteGraph;
import com.mcd_composent_graph.auth.Collisable;
import com.mcd_composent_graph.auth.CommentableComponent;
import com.mcd_composent_graph.auth.CommentaireGraph;
import com.mcd_composent_graph.auth.ContrainteGraph;
import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.HeritageGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.ProprieteGraph;
import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_composent_graph.auth.FormeGeometrique;
import com.mcd_edition_fenetre.auth.FenetreEditionCommentaire;
import com.mcd_edition_fenetre.auth.FenetreEditionContrainte;
import com.mcd_edition_fenetre.auth.FenetreEditionEntite;
import com.mcd_edition_fenetre.auth.FenetreEditionHeritage;
import com.mcd_edition_fenetre.auth.FenetreEditionCardinalite;
import com.mcd_edition_fenetre.auth.FenetreEditionRelation;
import com.mcd_log.auth.Cardinalite;
import com.mcd_log.auth.Contrainte;
import com.mcd_log.auth.ContrainteType;
import com.mcd_log.auth.Heritage;
import com.mcd_log.auth.HeritageType;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.ProprieteTypeE;
import com.mcd_log.auth.Relation;
import com.mcd_log.auth.Entite;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;
import com.sauvegarde_chargement.auth.Sauvegarde;

@SuppressWarnings("serial")
public class McdGraph extends JPanel{
	private List<McdComposentGraphique> m_focus;
	private List<McdComposentGraphique> m_copie;
	private List<Point> m_deltaSelect;
	private Hashtable<McdGraphStateE, McdGraphState> m_states;
	private McdGraphStateE m_currentState;
	private Hashtable<Object, McdComposentGraphique> m_logicObjects;
	private FenetrePrincipale m_fenetrePrincipale;
	private ArrayList<McdComposentGraphique> m_components, m_componentsFirst, m_componentsSecond;
	private Stack<Hashtable<Object, McdComposentGraphique>> m_listeAnnuler, m_listeRefaire;
	private boolean m_isMoving, m_isSaved;
	private File m_file;
	private SelectionMultiple m_selectMulti;
	private double m_zoom;
	
	public McdGraph(FenetrePrincipale fenPrinc) {
		m_fenetrePrincipale = fenPrinc;
		m_selectMulti = new SelectionMultiple();
		
		m_states = new Hashtable<McdGraphStateE,McdGraphState>();
		m_states.put(McdGraphStateE.INSERT_ENTITE, new McdGraphStateInsertEntite());
		m_states.put(McdGraphStateE.INSERT_RELATION, new McdGraphStateInsertRelation());
		m_states.put(McdGraphStateE.INSERT_LIEN, new McdGraphStateInsertLien());
		m_states.put(McdGraphStateE.INSERT_CONTRAINTE, new McdGraphStateInsertContrainte());
		m_states.put(McdGraphStateE.INSERT_HERITAGE, new McdGraphStateInsertHeritage());
		m_states.put(McdGraphStateE.EDIT, new McdGraphStateEdit());
		m_states.put(McdGraphStateE.INSERT_COMMENTAIRE, new McdGraphStateInsertCommentaire());
		m_currentState = McdGraphStateE.INVALID;

		m_components = new ArrayList<McdComposentGraphique>();
		m_componentsFirst = new ArrayList<McdComposentGraphique>();
		m_componentsSecond = new ArrayList<McdComposentGraphique>();
		
		m_logicObjects = new Hashtable<Object, McdComposentGraphique> ();
		m_listeAnnuler = new Stack<Hashtable<Object,McdComposentGraphique>>();
		m_listeRefaire = new Stack<Hashtable<Object,McdComposentGraphique>>();
		m_focus = new ArrayList<McdComposentGraphique>();
		m_copie = new ArrayList<McdComposentGraphique>();
		m_isMoving = false;
		m_isSaved=false;
		m_zoom=1.0;
		m_deltaSelect = new ArrayList<Point>();
		setFile(null);
		
		this.setSize(new Dimension(80, 80));
		this.setState(McdGraphStateE.EDIT);
		this.setFocusable(true);
		saveAnnulerModification();
		
		this.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				if((arg0.getModifiers()&InputEvent.CTRL_MASK)!=0)
				{
					setZoom(m_zoom+arg0.getWheelRotation()*0.05);
					m_fenetrePrincipale.zoomChanged(McdGraph.this);
				}
			}
		});
	}
	public void paintComponent(Graphics g){
		((Graphics2D) g).scale(m_zoom, m_zoom);
		CardinaliteGraph.resetCompteurLettre();
		g.setColor((Color) McdPreferencesManager.getInstance().get(PGroupe.MCD, PCle.COLOR));
		g.fillRect(0, 0, (int)(this.getWidth()/m_zoom), (int)(this.getHeight()/m_zoom));
		
		Point min = new Point(0,0), max = new Point(0,0);

		for(McdComposentGraphique component : m_components)
			component.dessinerOmbre(g);
		
		for(McdComposentGraphique component : m_componentsFirst){
			if (!(component instanceof ContrainteGraph))
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
		for(McdComposentGraphique component : m_componentsFirst){
			if (component instanceof ContrainteGraph)
				component.dessiner(g);
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
					if(component instanceof CardinaliteGraph)
						continue;
					FormeGeometrique eg = (FormeGeometrique) component;
					eg.setPosition(new Point(eg.getPosition().x-min.x, eg.getPosition().y));
				}
			}
			if(min.y<0){
				for(McdComposentGraphique component : m_components){
					if(component instanceof CardinaliteGraph)
						continue;
					FormeGeometrique eg = (FormeGeometrique) component;
					eg.setPosition(new Point(eg.getPosition().x, eg.getPosition().y-min.y));
					
				}
			}
			setPreferredSize(nouvelleDim);
			revalidate();
		}			
		
		//affichage commentaire
		if ((Boolean)McdPreferencesManager.getInstance().get(PGroupe.COMMENTAIRE, PCle.SHOW)){
			for (McdComposentGraphique c : m_componentsSecond){
				if(c instanceof CommentableComponent){
					FormeGeometrique forme = (FormeGeometrique) c;
					CommentableComponent com = (CommentableComponent) c;
					if(getMousePosition() != null&&
							forme.contient(getMousePosition())){
						boolean show = true;
						for(ProprieteGraph p: com.getProprietesGraphList()){
							if(p.contient(getMousePosition())&&
									!p.getPropriete().getCommentaire().trim().isEmpty()){
								showCommentaire(g, getMousePosition(), p.getPropriete().getCommentaire());
								show=false;
								break;
							}
						}
						if (show){
							showCommentaire(g, getMousePosition(), com.getCommentaire());
						}
						break;
					}
				}
			}
		}
		
		if (m_selectMulti.isDraw())
			m_selectMulti.dessiner(g, m_components);
		
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
		private EntiteGraph m_lastEg=null;
		public void mouseClicked(MouseEvent e) {
			
		}
		public void mouseEntered(MouseEvent e) {
			
		}
		public void mouseExited(MouseEvent e) {
			
		}
		public void mousePressed(MouseEvent e) {
			if(e.getClickCount()==1){
				EntiteGraph eg = new EntiteGraph();
				m_lastEg=eg;
				eg.setEntite(new Entite("Entite"+(m_last++)));
				eg.setPosition(transformToLocal(e.getPoint()));
				eg.setMcd(McdGraph.this);
				addMcdComponents(eg);
				repaint();
			}
			else{
				new FenetreEditionEntite(McdGraph.this, m_lastEg).setVisible(true);
			}
			saveAnnulerModification();
		}
		public void mouseReleased(MouseEvent e) {

		}
		public void mouseDragged(MouseEvent arg0) {
			
		}
		public void mouseMoved(MouseEvent arg0) {
			McdPreferencesManager prefs = McdPreferencesManager.getInstance();
			if (mouseInComponent() && (Boolean)prefs.get(PGroupe.COMMENTAIRE, PCle.SHOW))
				repaint();
		}
		public void keyPressed(KeyEvent arg0) {
			
		}
		public void keyReleased(KeyEvent arg0) {
					
		}
		public void keyTyped(KeyEvent arg0) {
			
		}
	}
	private class McdGraphStateInsertCommentaire extends McdGraphStateInsert{
		private CommentaireGraph m_lastCom=null;
		public void mouseClicked(MouseEvent arg0) {			
		}
		public void mouseEntered(MouseEvent arg0) {			
		}
		public void mouseExited(MouseEvent arg0) {			
		}
		public void mousePressed(MouseEvent arg0) {
			if(arg0.getClickCount()==1){
				CommentaireGraph com = new CommentaireGraph();
				m_lastCom=com;
				com.setMcd(McdGraph.this);
				com.setPosition(transformToLocal(arg0.getPoint()));
				addMcdComponents(com);
			}
			else
				new FenetreEditionCommentaire(McdGraph.this, m_lastCom).setVisible(true);
			repaint();
			saveAnnulerModification();
		}
		public void mouseReleased(MouseEvent arg0) {			
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
		private RelationGraph m_lastRg=null;
		public void mouseClicked(MouseEvent e) {
			
		}

		public void mouseEntered(MouseEvent arg0) {
			
		}

		public void mouseExited(MouseEvent arg0) {
			
		}

		public void mousePressed(MouseEvent e) {
			if(e.getClickCount()==1){
				RelationGraph eg = new RelationGraph();
				m_lastRg=eg;
				eg.setRelation(new Relation("Relation"+(m_last++)));
				eg.setPosition(transformToLocal(e.getPoint()));
				eg.setMcd(McdGraph.this);
				addMcdComponents(eg);
			}
			else
				new FenetreEditionRelation(McdGraph.this, m_lastRg).setVisible(true);
			repaint();
			saveAnnulerModification();
			
		}

		public void mouseReleased(MouseEvent arg0) {
			
		}

		public void mouseDragged(MouseEvent arg0) {
			
		}

		public void mouseMoved(MouseEvent arg0) {
			McdPreferencesManager prefs = McdPreferencesManager.getInstance();
			if (mouseInComponent() && (Boolean)prefs.get(PGroupe.COMMENTAIRE, PCle.SHOW))
				repaint();
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
			Point p = transformToLocal(e.getPoint());
			for(McdComposentGraphique component : m_components){
				if(((FormeGeometrique)component).contient(p)){
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
					
					addMcdComponents(cardG);
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
						if(heritage.getHeritage().getMere()!=null)
							heritage.getHeritage().addEnfant(entite.getEntite());
						else 
							heritage.getHeritage().setMere(entite.getEntite());
					}
					heritage.update();
				}
				clear();
			}
			repaint();
			saveAnnulerModification();
		}

		public void mouseReleased(MouseEvent e) {
			
		}

		public void mouseDragged(MouseEvent e) {
			
		}

		public void mouseMoved(MouseEvent e) {
			McdPreferencesManager prefs = McdPreferencesManager.getInstance();
			if (mouseInComponent() && (Boolean)prefs.get(PGroupe.COMMENTAIRE, PCle.SHOW))
				repaint();
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
			contG.setPosition(transformToLocal(e.getPoint()));
			contG.setMcd(McdGraph.this);
			
			addMcdComponents(contG);
			repaint();
			saveAnnulerModification();
		}

		public void mouseReleased(MouseEvent e) {
			
		}

		public void mouseDragged(MouseEvent e) {
			
		}

		public void mouseMoved(MouseEvent e) {
			McdPreferencesManager prefs = McdPreferencesManager.getInstance();
			if (mouseInComponent() && (Boolean)prefs.get(PGroupe.COMMENTAIRE, PCle.SHOW))
				repaint();
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
			herG.setPosition(transformToLocal(e.getPoint()));
			herG.setMcd(McdGraph.this);
			
			addMcdComponents(herG);
			repaint();
			saveAnnulerModification();
		}

		public void mouseReleased(MouseEvent e) {
			
		}

		public void mouseDragged(MouseEvent e) {
			
		}

		public void mouseMoved(MouseEvent e) {
			McdPreferencesManager prefs = McdPreferencesManager.getInstance();
			if (mouseInComponent() && (Boolean)prefs.get(PGroupe.COMMENTAIRE, PCle.SHOW))
				repaint();
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
			Point pos = transformToLocal(e.getPoint());
			if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0){
				for (McdComposentGraphique c : m_components){
					if (!(c instanceof CardinaliteGraph) && ((FormeGeometrique)c).contient(e.getPoint())){
						c.setFocus(true);
						m_focus.add(c);
						break;
					}
				}
				m_selectMulti.resetList();
				m_selectMulti.setTrace(true);
				m_selectMulti.setDepart(transformToLocal(e.getPoint()));
				return;
			}
			for(McdComposentGraphique component : m_componentsSecond){
				if(((FormeGeometrique)component).contient(pos)){
					found=true;
					if(!m_focus.contains(component)){
						setMcdComposentGraphiquetFocus(null);
						m_time=System.currentTimeMillis();
						setMcdComposentGraphiquetFocus(component);
						break;
					}
				}
			}
			if(!found){
				for(McdComposentGraphique component : m_componentsFirst){
					if(((FormeGeometrique)component).contient(pos)){
						found=true;
						if(!m_focus.contains(component)){
							setMcdComposentGraphiquetFocus(null);
							m_time=System.currentTimeMillis();
							setMcdComposentGraphiquetFocus(component);
							break;
						}
					}
				}
				
				if(!found){
					m_selectMulti.resetList();
					m_selectMulti.setTrace(true);
					m_selectMulti.setDepart(transformToLocal(e.getPoint()));
				}
			}
			if(m_focus.size()!=0&&e.getClickCount()==1&&found){ // 1 click -> mode move
				for (McdComposentGraphique composent : m_focus){
					if(!composent.isMovable())
						return;
					FormeGeometrique forme = (FormeGeometrique)composent;
					if (forme.contient(pos)){
						for (McdComposentGraphique comp : m_focus){
							if (comp instanceof CardinaliteGraph)
								return;
							FormeGeometrique f = (FormeGeometrique)comp;
							setMcdComposentGraphiquetFocus(comp);
							Point p = new Point();
							p.x = pos.x - f.getPosition().x;
							if (pos.y > f.getPosition().y)
								p.y = pos.y - f.getPosition().y;
							else
								p.y = pos.y - f.getPosition().y;
							m_deltaSelect.add(p);
							m_isMoving=true;
							saveAnnulerModification();
						}
					}
				}
			}
			else if(found&&(System.currentTimeMillis()-m_time>=m_interval)|| // doubleclick
					e.getClickCount()==2)
			{
				for (McdComposentGraphique composent : m_focus){
					if(composent instanceof RelationGraph)
					{
						new FenetreEditionRelation(McdGraph.this, (RelationGraph)composent).setVisible(true);
						((RelationGraph)composent).actualiser();
						saveAnnulerModification();
					}
					else if (composent instanceof ContrainteGraph){
						new FenetreEditionContrainte(McdGraph.this, (ContrainteGraph)composent).setVisible(true);
						((ContrainteGraph) composent).update();
						saveAnnulerModification();
					}
					else if (composent instanceof HeritageGraph){
						new FenetreEditionHeritage(McdGraph.this, (HeritageGraph)composent).setVisible(true);
						((HeritageGraph)composent).update();
						saveAnnulerModification();
					}
					else if (composent instanceof EntiteGraph){
						new FenetreEditionEntite(McdGraph.this, (EntiteGraph)composent).setVisible(true);
						saveAnnulerModification();
					}
					else if(composent instanceof CardinaliteGraph){
						new FenetreEditionCardinalite(McdGraph.this, (CardinaliteGraph)composent).setVisible(true);
						saveAnnulerModification();
					}
					else if(composent instanceof CommentaireGraph){
						new FenetreEditionCommentaire(McdGraph.this, (CommentaireGraph) composent).setVisible(true);
						saveAnnulerModification();
					}
				}
				setMcdComposentGraphiquetFocus(null);
			}
			else if(!found){//Click en dehors
				if ((e.getModifiers() & KeyEvent.CTRL_MASK) == 0)
					setMcdComposentGraphiquetFocus(null);
			}
		}

		public void mouseReleased(MouseEvent arg0) {
			m_isMoving=false;
			m_deltaSelect.clear();
			if (m_selectMulti.getFocus().size() != 0)
				for (McdComposentGraphique c : m_selectMulti.getFocus())
					m_focus.add(c);
			m_selectMulti.reset();
			repaint();
		}

		public void mouseDragged(MouseEvent e) {
			if (m_focus.size() != 0&&m_isMoving){
				int i=0;
				for (McdComposentGraphique composent : m_focus){
					if (composent instanceof CardinaliteGraph)
						return;
					FormeGeometrique forme = (FormeGeometrique)composent;
					Point tmp = new Point(transformToLocal(e.getPoint()));
					
					tmp.x -= m_deltaSelect.get(i).x;
					tmp.y -= m_deltaSelect.get(i).y;
					forme.setPosition(tmp);
					
					if (composent instanceof Collisable)
						if (((Collisable)composent).collision())
							return;

					++i;
				}
			}
			
			if (m_selectMulti.isTrace()){
				m_selectMulti.setPosCurseur(transformToLocal(e.getPoint()));
				m_selectMulti.setDraw(true);
			}
			repaint();
		}

		public void mouseMoved(MouseEvent arg0) {
			McdPreferencesManager prefs = McdPreferencesManager.getInstance();
			if (mouseInComponent() && (Boolean)prefs.get(PGroupe.COMMENTAIRE, PCle.SHOW))
				repaint();
		}
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_DELETE&&m_focus.size()!=0){
				for (McdComposentGraphique composent : m_focus)
					deleteMcdComposent(composent);
				
				m_focus.clear();
				repaint();
			}
			if((e.getModifiers() & KeyEvent.CTRL_MASK) != 0&&e.getKeyCode() == KeyEvent.VK_A){
				m_focus.clear();
				for (McdComposentGraphique composent : m_components)
					if (!(composent instanceof CardinaliteGraph)){
						composent.setFocus(true);
						m_focus.add(composent);
					}
				
				repaint();
			}
		}
		public void keyReleased(KeyEvent e) {
			
		}
		public void keyTyped(KeyEvent e) {
			
		}
	}
	private void deleteMcdComposent(McdComposentGraphique comp){
		if(comp instanceof EntiteGraph){
			Entite e = ((EntiteGraph) comp).getEntite();
			for(McdComposentGraphique composant : m_components){
				if(composant instanceof ContrainteGraph){
					Contrainte contr = ((ContrainteGraph) composant).getContrainte();
					if(contr.getEntites().contains(e)){
						contr.getEntites().remove(e);
						((ContrainteGraph) composant).update();
					}
				}
				else if(composant instanceof HeritageGraph){
					Heritage h = ((HeritageGraph) composant).getHeritage();
					if(h.getEnfants().contains(e)){
						((HeritageGraph) composant).removeEntiteGraph((EntiteGraph)comp);
						h.getEnfants().remove(e);
					}
					if(h.getMere()==e){
						((HeritageGraph) composant).removeEntiteGraph((EntiteGraph)comp);
						h.setMere(null);
					}
					((HeritageGraph) composant).update();
				}
				else if(composant instanceof CardinaliteGraph){
					Cardinalite c = ((CardinaliteGraph)composant).getCardinalite();
					if (c.getEntite().equals(e)){
						composant.prepareDelete();
						m_componentsFirst.remove(composant);
					}
				}
			}
		}
		else if(comp instanceof RelationGraph){
			Relation r = ((RelationGraph) comp).getRelation();
			for(McdComposentGraphique composant : m_components){
				if(composant instanceof ContrainteGraph){
					Contrainte c = ((ContrainteGraph) composant).getContrainte();
					if(c.getRelations().contains(r)){
						c.getRelations().remove(r);
						((ContrainteGraph) composant).update();
					}
				}
				else if(composant instanceof CardinaliteGraph){
					Cardinalite c = ((CardinaliteGraph)composant).getCardinalite();
					if (c.getRelation().equals(r)){
						composant.prepareDelete();
						m_componentsFirst.remove(composant);
					}
				}
			}
		}
		comp.prepareDelete();
		if(m_componentsFirst.contains(comp))
			m_componentsFirst.remove(comp);
		else
			m_componentsSecond.remove(comp);
		
		m_components.clear();
		m_components.addAll(m_componentsFirst);
		m_components.addAll(m_componentsSecond);

		saveAnnulerModification();
	}
	public void copyMcdComposent(){
		List<McdComposentGraphique> tmp = new ArrayList<McdComposentGraphique>();
		for (McdComposentGraphique c : m_focus)
			tmp.add(c);
		for (McdComposentGraphique composent : m_components){
			if (!(composent instanceof CardinaliteGraph))
				continue;
			
			Cardinalite c = ((CardinaliteGraph)composent).getCardinalite();
			for (McdComposentGraphique comp : tmp){
				if (comp instanceof EntiteGraph){
					if (((EntiteGraph) comp).getEntite().equals(c.getEntite())){
						for (McdComposentGraphique comp2 : tmp){
							if (comp2 instanceof RelationGraph)
								if (((RelationGraph) comp2).getRelation().equals(c.getRelation()) && !(m_focus.contains(composent)))
									m_focus.add(composent);
						}
					}
				}	
			}	
		}
		m_copie = m_focus;
	}
	public void pastMcdComposent() throws CloneNotSupportedException{
		if (m_copie.size() == 0)
			return;
		
		Point pos = null;
		Point newPos = new Point();
		Hashtable <Object, Object> lstObject = new Hashtable<Object, Object>();
		
		for (McdComposentGraphique composent : m_copie){
			if (composent instanceof CardinaliteGraph || composent instanceof HeritageGraph || composent instanceof ContrainteGraph)
				continue;
			saveAnnulerModification();
			
			if (pos == null)
				pos = ((FormeGeometrique)composent).getPosition();
			
			newPos.x = (getMousePosition().x + (((FormeGeometrique)composent).getPosition().x-pos.x));
			newPos.y = (getMousePosition().y + (((FormeGeometrique)composent).getPosition().y-pos.y));
			
			if (composent instanceof EntiteGraph){
				EntiteGraph eg = new EntiteGraph();
				Entite e = ((EntiteGraph) composent).getEntite().clone();
				
				eg.setEntite(e);
				eg.setPosition(newPos);
				eg.setMcd(McdGraph.this);
				
				m_components.add(eg);
				m_componentsSecond.add(eg);
				lstObject.put(((EntiteGraph) composent).getEntite(), e);
			}
			else if (composent instanceof RelationGraph){
				RelationGraph rg = new RelationGraph();
				Relation r = ((RelationGraph) composent).getRelation().clone();
				
				rg.setRelation(r);
				rg.setPosition(newPos);
				rg.setMcd(McdGraph.this);
				
				m_components.add(rg);
				m_componentsSecond.add(rg);
				lstObject.put(((RelationGraph) composent).getRelation(), r);
			}
		}
		
		for (McdComposentGraphique composent : m_copie){
			
			if (composent instanceof EntiteGraph || composent instanceof RelationGraph)
				continue;
			
			if (!(composent instanceof CardinaliteGraph)){
				newPos.x = (getMousePosition().x + (((FormeGeometrique)composent).getPosition().x-pos.x));
				newPos.y = (getMousePosition().y + (((FormeGeometrique)composent).getPosition().y-pos.y));
			}
			
			if (composent instanceof ContrainteGraph){
				ContrainteGraph cg = new ContrainteGraph();
				Contrainte c = ((ContrainteGraph) composent).getContrainte().clone();
				
				cg.setContrainte(c);
				cg.setPosition(newPos);
				List<Object> lst = new ArrayList<Object>();
				for (Entite e : c.getEntites())
					lst.add(e);
				for (Relation r : c.getRelations())
					lst.add(r);
				c.getEntites().clear();
				c.getRelations().clear();
				for (Object o : lst){
					if (o instanceof Entite)
						c.addEntite((Entite)lstObject.get(o));
					else
						c.addRelation((Relation)lstObject.get(o));
				}
					
				if (!c.getSens().equals(null))
					c.setSens(lstObject.get(c.getSens()));
				
				cg.setMcd(McdGraph.this);
				
				m_components.add(cg);
				m_componentsFirst.add(cg);
			}
			else if (composent instanceof HeritageGraph){
				HeritageGraph hg = new HeritageGraph();
				Heritage h = ((HeritageGraph) composent).getHeritage().clone();
				
				hg.setHeritage(h);
				hg.setPosition(newPos);
				List<Object> lst = new ArrayList<Object>();
				for (Entite e : h.getEnfants())
					lst.add(e);
				
				h.getEnfants().clear();
				for (Object e : lst)
					h.addEnfant((Entite)lstObject.get(e));
				
				h.setMere((Entite)lstObject.get(h.getMere()));
				hg.setMcd(McdGraph.this);
				
				m_components.add(hg);
				m_componentsFirst.add(hg);
			}
			else if (composent instanceof CardinaliteGraph){
				CardinaliteGraph cg = new CardinaliteGraph();
				Cardinalite c = ((CardinaliteGraph) composent).getCardinalite().clone();
				
				c.setEntite((Entite)lstObject.get(((CardinaliteGraph)composent).getCardinalite().getEntite()));
				c.setRelation((Relation)lstObject.get(((CardinaliteGraph)composent).getCardinalite().getRelation()));
				cg.setCardinalite(c);
				cg.setMcd(McdGraph.this);
				
				m_components.add(cg);
				m_componentsFirst.add(cg);
			}
			saveAnnulerModification();
		}
		repaint();
	}
	
	public ArrayList<McdComposentGraphique> getMcdComponents(){
		return m_components;
	}
	public ArrayList<CardinaliteGraph> getCardinalitesGraph(){
		ArrayList<CardinaliteGraph> ret = new ArrayList<CardinaliteGraph>();
		for(McdComposentGraphique c : m_components){
			if(c instanceof CardinaliteGraph){
				ret.add((CardinaliteGraph) c);
			}
		}
		return ret;
	}
	
	public void addMcdComponents(McdComposentGraphique c){
		if (c instanceof EntiteGraph ||c instanceof RelationGraph)
			m_componentsSecond.add(c);
		else
			m_componentsFirst.add(c);
		
		m_components.add(c);
	}
	
	public void saveMcdComposent(boolean saveAs){
		new Sauvegarde(this, saveAs);
	}
	private void setMcdComposentGraphiquetFocus(McdComposentGraphique comp){
		if(m_focus.size()!=0){
			for (McdComposentGraphique composent : m_focus)
				composent.setFocus(false);
		}
		
		if (comp == null)
			m_focus.clear();
			
		if (!m_focus.contains(comp) && comp != null)
			m_focus.add(comp);
		
		if(m_focus.size()!=0){
			for (McdComposentGraphique composent : m_focus)
				composent.setFocus(true);
		}
		repaint();
	}
	public void saveAnnulerModification(){
		m_listeAnnuler.push(copyLogicGraph(m_logicObjects));
		int tailleFile = (Integer) McdPreferencesManager.getInstance().get(PGroupe.MCD,PCle.TAILLE_ANNULER);
		if(m_listeAnnuler.size()>tailleFile){
			m_listeAnnuler.remove(0);
		}
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
				eg.setDimension(((EntiteGraph) from.get(key)).getDimension());
				tmp.put(e, eg);
				correspondances.put(key, e);
			}
			else if(key instanceof Relation){
				Relation r = new Relation((Relation)key);
				RelationGraph rg = new RelationGraph();
				rg.setRelation(r);
				rg.setPosition(((RelationGraph)from.get(key)).getPosition());
				rg.setDimension(((RelationGraph)from.get(key)).getDimension());
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
				if(h.getMere()!=null){
					Entite nmere = (Entite) correspondances.get(h.getMere());
					h.setMere(nmere);
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
	public boolean isSaved(){
		return m_isSaved;
	}
	public File getFile() {
		return m_file;
	}
	public void setFile(File file) {
		m_file = file;
	}
	public boolean peutAnnuler(){
		return !m_listeAnnuler.isEmpty();
	}
	public boolean peutRefaire(){
		return !m_listeRefaire.isEmpty();
	}
	public ArrayList<Object> getLogic(){
		ArrayList<Object> ret = new ArrayList<Object>();
		Enumeration<Object> keys = m_logicObjects.keys();
		while(keys.hasMoreElements()){
			ret.add(keys.nextElement());
		}
		return ret;
	}
	public void reorganiser(){
		new McdGraphOrganizer(this);
		repaint();
	}
	private boolean mouseInComponent(){
		for (McdComposentGraphique c : m_componentsSecond)
			if (c instanceof EntiteGraph 
					&& getMousePosition() != null
					&& ((EntiteGraph)c).contient(getMousePosition())
					&& ((EntiteGraph)c).getEntite().getCommentaire() != "")
				return true;
			else if (c instanceof RelationGraph 
					&& getMousePosition() != null
					&& ((RelationGraph)c).contient(getMousePosition())
					&& ((RelationGraph)c).getRelation().getCommentaire() != "")
				return true;
		
		return false;
	}
	private void showCommentaire(Graphics g, Point c, String com){
		if(com.trim().isEmpty())
			return;
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		FontMetrics font = g.getFontMetrics();
		Dimension dim = new Dimension();
		Point p = new Point();
		String s = "Commentaire:\n"+com;
		int ligne = 0, maxWidth = 0;
		String lg = "";
		
		for (int i=0; i<s.toCharArray().length; ++i)
			if (s.toCharArray()[i] == '\n' || i == s.toCharArray().length-1){
				if (font.stringWidth(lg) > maxWidth)
					maxWidth = font.stringWidth(lg);
				++ligne;
				lg = "";
			}
			else
				lg += s.toCharArray()[i];
		
		dim.width = maxWidth+10;
		dim.height = (font.getHeight()*ligne)+10;
		
		p.x = c.x;
		p.y = c.y-dim.height;
		
		if ((Boolean)prefs.get(PGroupe.COMMENTAIRE, PCle.OMBRE)){
			g.setColor((Color)prefs.get(PGroupe.COMMENTAIRE, PCle.OMBRE_COLOR));
			g.fillRect(p.x+5, p.y+5, dim.width, dim.height);
		}
		
		if(!(Boolean) prefs.get(PGroupe.COMMENTAIRE, PCle.GRADIANT_COLOR))
			g.setColor((Color) prefs.get(PGroupe.COMMENTAIRE, PCle.COLOR));
		else{
			Graphics2D g2 = (Graphics2D) g;
			GradientPaint paint=null;

			paint = new GradientPaint(p.x, 0, 
			(Color)prefs.get(PGroupe.COMMENTAIRE, PCle.COLOR), 
			p.x+dim.width, 0, 
			(Color)prefs.get(PGroupe.COMMENTAIRE, PCle.COLOR_2));
			g2.setPaint(paint);
		}

		g.fillRect(p.x, p.y, dim.width, dim.height);
		g.setColor((Color)prefs.get(PGroupe.COMMENTAIRE, PCle.COLOR_CONTOUR));
		g.drawRect(p.x, p.y, dim.width, dim.height);
		p.x += 5;
		
		int j = 1, old = p.y;
		lg = "";
		g.setFont(prefs.getFont(PGroupe.COMMENTAIRE, PCle.FONT));
		g.setColor((Color)prefs.get(PGroupe.COMMENTAIRE, PCle.FONT_COLOR));
		for (int i=0; i<s.toCharArray().length; ++i)
			if (s.toCharArray()[i] == '\n'){
				p.y = old+(font.getHeight()*j);
				g.drawString(lg, p.x, p.y);
				lg = "";
				++j;
			}
			else
				lg += s.toCharArray()[i];
		
		p.y = old+(font.getHeight()*j);
		g.drawString(lg, p.x, p.y);
	}
	public void importSql(Hashtable<String, List<List<String>>> lst_entites){
		Enumeration<String> entites = lst_entites.keys();
		List<List<String>> props;
		Hashtable<String, String> relations = new Hashtable<String, String>();
		Propriete prop;
		Entite entite;
		EntiteGraph entiteGraph;
		RelationGraph relationGraph;
		CardinaliteGraph cardinaliteGraph;
		Point lastPoint = new Point();
		int size = 0;
		while(entites.hasMoreElements()){
			entite = new Entite((String)entites.nextElement());
			props = lst_entites.get(entite.getName());
			for (List<String> values : props){
				if (values.get(values.size()-1).matches("foreign_.*")){
					relations.put(entite.getName(), values.get(values.size()-1).substring(8));
					continue;
				}
				prop = new Propriete(values.get(0), ProprieteTypeE.valueOf(values.get(1)));
				if (values.contains("primary"))
					prop.setClePrimaire(true);
				if (values.get(2).toLowerCase().equals("null"))
					prop.setNull(true);
				if (values.size()>3){
					if (values.get(3).toLowerCase().contains("identity")
							|| values.get(3).toLowerCase().contains("auto_increment"))
						prop.setAutoIncrement(true);
				}
				entite.addPropriete(prop);
			}
			entiteGraph = new EntiteGraph();
			entiteGraph.setEntite(entite);
			if (m_componentsSecond.size() == 0)
				entiteGraph.setPosition(new Point(10,10));
			else{
				lastPoint = ((EntiteGraph)m_components.get(m_components.size()-1)).getPosition();
				size = ((EntiteGraph)m_components.get(m_components.size()-1)).getRectangle().width;
				entiteGraph.setPosition(new Point(lastPoint.x+size+50,10));
			}
			entiteGraph.setMcd(this);
			addMcdComponents(entiteGraph);
		}
		entites = relations.keys();
		while (entites.hasMoreElements()){
			String nom = (String)entites.nextElement();
			relationGraph = new RelationGraph();
			cardinaliteGraph = new CardinaliteGraph();
			relationGraph.setRelation(new Relation(nom+"_"+relations.get(nom)));
			relationGraph.setMcd(this);
			addMcdComponents(relationGraph);
			cardinaliteGraph.setCardinalite(new Cardinalite());
			cardinaliteGraph.getCardinalite().setMax(1);
			cardinaliteGraph.getCardinalite().setMin(1);
			cardinaliteGraph.getCardinalite().setRelation(relationGraph.getRelation());
			for (McdComposentGraphique c : m_componentsSecond)
				if (c instanceof EntiteGraph)
					if (((EntiteGraph)c).getEntite().getName().equals(nom))
						cardinaliteGraph.getCardinalite().setEntite(((EntiteGraph)c).getEntite());
			cardinaliteGraph.setMcd(this);
			addMcdComponents(cardinaliteGraph);
			cardinaliteGraph = new CardinaliteGraph();
			cardinaliteGraph.setCardinalite(new Cardinalite());
			cardinaliteGraph.getCardinalite().setMax(-1);
			cardinaliteGraph.getCardinalite().setMin(0);
			cardinaliteGraph.getCardinalite().setRelation(relationGraph.getRelation());
			for (McdComposentGraphique c : m_componentsSecond)
				if (c instanceof EntiteGraph)
					if (((EntiteGraph)c).getEntite().getName().equals(relations.get(nom)))
						cardinaliteGraph.getCardinalite().setEntite(((EntiteGraph)c).getEntite());
			cardinaliteGraph.setMcd(this);
			addMcdComponents(cardinaliteGraph);
		}
	}
	public double getZoom() {
		return m_zoom;
	}
	public void setZoom(double zoom) {
		if(zoom>1.0)
			zoom=1.0;
		if(zoom<=0.0)
			zoom = 0.01;
		m_zoom = zoom;
		repaint();
	}
	public void resetZoom(){
		m_zoom=1.0;
		repaint();
	}
	public void zoomer(){
		m_zoom*=2;
		if(m_zoom>1.0)
			m_zoom=1.0;
		repaint();
		revalidate();
	}
	public void dezoomer(){
		m_zoom/=2;
		repaint();
		revalidate();
	}
	private Point transformToLocal(Point p){
		if(p==null)
			return null;
		Point ret = new Point(p);
		ret.x/=m_zoom;
		ret.y/=m_zoom;
		return ret;
	}
	public Point getMousePosition(){
		return transformToLocal(super.getMousePosition());
	}
	public Dimension getPreferredSize(){
		Dimension ret = (Dimension) super.getPreferredSize().clone();
		ret.width*=m_zoom;
		ret.height*=m_zoom;
		return ret;
	}
}
