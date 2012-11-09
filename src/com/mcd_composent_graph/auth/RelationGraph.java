package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;
import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class RelationGraph extends McdComposentGraphique implements FormeGeometrique, CommentableComponent, Collisable{

	private Relation m_relation;
	private int m_lastPropsNumber, m_heightNom, m_widthNom;
	private ArrayList<ProprieteGraph> m_proprietes;
	private FormeGeometriqueRectangle m_geometrie;
	private Boolean m_calculerTaille;

	public Rectangle getRectangle(){
		return m_geometrie.getRectangle();
	}
	public void setRectangle(Rectangle r){
		if(r!=null)
			m_geometrie.setRectangle(r);
	}
	public Point getPosition(){
		return m_geometrie.getPosition();
	}
	public void setPosition(Point p){
		m_geometrie.setPosition(p);
	}
	public Boolean contient(Point p){
		return m_geometrie.contient(p);
	}
	public Dimension getDimension(){
		return m_geometrie.getDimension();
	}
	public void setDimension(Dimension d){
		m_geometrie.setDimension(d);
	}
	public RelationGraph() {
		m_geometrie = new FormeGeometriqueRectangle(new Rectangle());
		m_proprietes = new ArrayList<ProprieteGraph>();
		m_lastPropsNumber=-999;
		m_heightNom = 0;
		m_focus=false;
		m_calculerTaille=true;
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
		m_calculerTaille=true;
	}
	public void setRelation(Relation rel){
		m_relation=rel;
		this.m_lastPropsNumber=-999;
	}
	public Relation getRelation(){
		return m_relation;
	}
	public void dessiner(Graphics g) {
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		updateCif();
		if(m_calculerTaille)
		{
			this.actualiser();
	
			Dimension dim = new Dimension(0,0), dimEC;
			Font font;
			if(!m_focus)
				font = prefs.getFont(PGroupe.RELATION, PCle.FONT_NOM);
			else
				font = prefs.getFont(PGroupe.RELATION, PCle.FONT_NOM_FOCUS);
			
			
			FontMetrics metric = g.getFontMetrics(font);
			m_widthNom = dim.width = metric.stringWidth(m_relation.getNom());
			m_heightNom = dim.height = metric.getHeight();
			
			if(!m_focus)
				font = prefs.getFont(PGroupe.RELATION, PCle.FONT);
			else
				font = prefs.getFont(PGroupe.RELATION, PCle.FONT_FOCUS);
			
			for(int i=0;i<m_lastPropsNumber;++i){
				dimEC=m_proprietes.get(i).getDimension(g, font, m_relation.getNom());
				if(dimEC.width>dim.width)
					dim.width=dimEC.width;
				
				dim.height+=dimEC.height;
			}
			dim.height+=10;
			dim.width += 60;
			setDimension(dim);
			m_calculerTaille=false;
		}
		Point pos = getPosition();
		Dimension dim = getDimension();
		
		if (dim.height == 0)
			dim.height = dim.width/2;
		
		if (dim.height > dim.width)
			dim.width = (int) (dim.height*1.2);
		
		setDimension(dim);
		
		if(!(Boolean) prefs.get(PGroupe.RELATION, PCle.GRADIANT_COLOR)){
			if(!m_focus)
				g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR));
			else
				g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_FOCUS));
		}
		else{
			Graphics2D g2 = (Graphics2D) g;
			GradientPaint paint=null;
			if(!m_focus){
				paint = new GradientPaint(getPosition().x, 0, 
						(Color)prefs.get(PGroupe.RELATION, PCle.COLOR), 
						getPosition().x+getDimension().width, 0, 
						(Color)prefs.get(PGroupe.RELATION, PCle.COLOR_2));
			}
			else{
				paint = new GradientPaint(getPosition().x, 0, 
						(Color)prefs.get(PGroupe.RELATION, PCle.COLOR_FOCUS), 
						getPosition().x+getDimension().width, 0, 
						(Color)prefs.get(PGroupe.RELATION, PCle.COLOR_2_FOCUS));
			}
			g2.setPaint(paint);
		}
		
		g.fillOval(pos.x, pos.y, dim.width, dim.height);
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR_FOCUS));
		g.drawOval(pos.x, pos.y, dim.width, dim.height);
		
		Point cursor = new Point(pos);
		cursor.x += (dim.width/2)-(m_widthNom/2);
		cursor.y += m_heightNom;
		if(!m_focus){
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.FONT_NOM_COLOR));
			g.setFont(prefs.getFont(PGroupe.RELATION, PCle.FONT_NOM));
		}
		else{
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.FONT_NOM_COLOR_FOCUS));
			g.setFont(prefs.getFont(PGroupe.RELATION, PCle.FONT_NOM_FOCUS));
		}
		g.drawString(m_relation.getNom(), cursor.x, cursor.y);
		cursor.y+=5;
		
		if(!m_focus)
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR));
		else
			g.setColor((Color) prefs.get(PGroupe.RELATION, PCle.COLOR_CONTOUR_FOCUS));
		{ //Dessin corde ellispe
			float a,b;
			a=dim.width/2;
			b=dim.height/2;
			Point2D p = new Point2D.Float(getPosition().x+a, getPosition().y+b);
			float Y = (float) (p.getY()-((float)cursor.y));
			float X = (float) Math.sqrt((1- ((Y*Y)/(b*b)) ) *a*a);
			g.drawLine((int)p.getX()-(int)X, cursor.y, (int)p.getX()+(int)X, cursor.y);
		}
		Font font;
		Color couleur;
		if(!m_focus){
			font = prefs.getFont(PGroupe.RELATION, PCle.FONT);
			couleur = (Color) prefs.get(PGroupe.RELATION, PCle.FONT_COLOR);
		}
		else{
			font = prefs.getFont(PGroupe.RELATION, PCle.FONT_FOCUS);
			couleur = (Color) prefs.get(PGroupe.RELATION, PCle.FONT_COLOR_FOCUS);
		}
		Dimension dimEC;
		cursor.y-=3;
		for(int i=0;i<this.m_lastPropsNumber;++i){
			dimEC = this.m_proprietes.get(i).getDimension(g, font, m_relation.getNom());
			cursor.y+= dimEC.height;
			cursor.x = pos.x+(dim.width/2)-(dimEC.width/2);
			this.m_proprietes.get(i).dessiner(g, font, couleur, cursor, m_relation.getNom());
		}

	}
	public Boolean isLinkable() {
		return true;
	}
	public void setMcd(McdGraph mcd) {
		if(m_mcd!=null)
			m_mcd.removeLogic(m_relation);
		m_mcd=mcd;
		m_mcd.registerLogic(m_relation, this);
	}

	public void prepareDelete() {
		m_mcd.removeLogic(m_relation);
	}
	
	public void setFocus(Boolean f){
		super.setFocus(f);
		m_calculerTaille=true;
	}
	
	public List<ProprieteGraph> getProprietesGraphList(){
		return m_proprietes;
	}
	public String getCommentaire(){
		return m_relation.getCommentaire();
	}
	public void setPropriete(ArrayList<ProprieteGraph> prop){
		m_proprietes = prop;
	}
	public String getName(){
		return m_relation.getNom();
	}
	public void dessinerOmbre(Graphics g) {
		McdPreferencesManager prefs = McdPreferencesManager.getInstance();
		if ((Boolean)prefs.get(PGroupe.RELATION, PCle.OMBRE)){
			g.setColor((Color)prefs.get(PGroupe.RELATION, PCle.OMBRE_COLOR));
			g.fillOval(this.getPosition().x+3, this.getPosition().y+3, this.getDimension().width, this.getDimension().height);
		}
	}
	private void updateCif(){
		if(!(Boolean)McdPreferencesManager.getInstance().get(PGroupe.RELATION, PCle.CIF)){
			m_relation.setCif(false);
			return;
		}
		if(!m_relation.getProprietes().isEmpty()){
			m_relation.setCif(false);
			return;
		}
		ArrayList<CardinaliteGraph> cards = m_mcd.getCardinalitesGraph();
		int nombre=0;
		int max[]=new int[2];
		for(CardinaliteGraph c : cards){
			if(c.getCardinalite().getRelation()==m_relation){
				if(++nombre>2){
					m_relation.setCif(false);
					return;
				}
				max[nombre-1]=c.getCardinalite().getMax();
			}
		}
		if(((max[0]==0||max[0]==1)&&(max[1]==-1||max[1]==2))||
				((max[1]==0||max[1]==1)&&(max[0]==-1||max[0]==2))){
			m_relation.setCif(true);
		}
		else
			m_relation.setCif(false);
	}
	public boolean collision() {
		Rectangle rect1, rect2;
		rect1 = this.getRectangle();
		rect2 = new Rectangle();
		int i=1;
		for (McdComposentGraphique c : m_mcd.getMcdComponents()){
			if (!(c instanceof Collisable)){
				++i;
				continue;
			}
			
			rect2 = ((Collisable)c).getRectangle();

			if (rect1.x > rect2.x + rect2.width
					|| rect1.x + rect1.width < rect2.x
					|| rect1.y > rect2.y + rect2.height
					|| rect1.y + rect1.height < rect2.y)
				++i;
		}
		if (i == m_mcd.getMcdComponents().size())
			return false;
		else
			return true;
	}
}
