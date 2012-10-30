package com.mcd_graph.auth;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

import com.mcd_composent_graph.auth.CardinaliteGraph;
import com.mcd_composent_graph.auth.ContrainteGraph;
import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.FormeGeometrique;
import com.mcd_composent_graph.auth.HeritageGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Relation;

public class McdGraphOrganizer {
	private McdGraph m_mcd;
	private LinkedList<Groupe> m_groupes;
	private Hashtable<McdComposentGraphique, LinkedList<Groupe>> m_invertedGroupes;
	public McdGraphOrganizer(McdGraph mcd) {
		m_mcd=mcd;
		Groupe groupesTries[][]=null;
		m_invertedGroupes = new Hashtable<McdComposentGraphique, LinkedList<Groupe>>();
		m_groupes = new LinkedList<McdGraphOrganizer.Groupe>();
		creerGroupes();
		lierGroupes();
		groupesTries = trierGroupes();
		placerComponents(trierComposants(groupesTries));
	}
	private void creerGroupes(){
		ArrayList<McdComposentGraphique> components = m_mcd.getMcdComponents();
		/*Création des groupes par héritage et relation*/
		Groupe g = null;
		for(McdComposentGraphique comp : components){
			g=null;
			if(comp instanceof HeritageGraph || comp instanceof RelationGraph){
				g = new Groupe();
				if(comp instanceof HeritageGraph){
					HeritageGraph her = (HeritageGraph) comp;
					if(her.getHeritage().getMere()!=null){
						g.components.add(m_mcd.getGraphicComponent(her.getHeritage().getMere()));
						registedInvertedGroupe(m_mcd.getGraphicComponent(her.getHeritage().getMere()), g);
					}
					for(Entite e : her.getHeritage().getEnfants()){
						g.components.add(m_mcd.getGraphicComponent(e));
						registedInvertedGroupe(m_mcd.getGraphicComponent(e), g);
					}
				}
				else{
					RelationGraph rel = (RelationGraph) comp;
					g.components.add(rel);
					registedInvertedGroupe(rel, g);
					for(CardinaliteGraph card : m_mcd.getCardinalitesGraph()){
						if(card.getCardinalite().getRelation() == rel.getRelation()){
							if(!g.components.contains(m_mcd.getGraphicComponent(card.getCardinalite().getEntite())))
							{
								g.components.add(m_mcd.getGraphicComponent(card.getCardinalite().getEntite()));
								registedInvertedGroupe(m_mcd.getGraphicComponent(card.getCardinalite().getEntite()), g);
							}
						}
					}
				}
				m_groupes.add(g);
			}
		}
	}
	private void lierGroupes(){
		for(Groupe g1 : m_groupes){
			for(McdComposentGraphique comp : g1.components){
				for(Groupe g2 : m_invertedGroupes.get(comp)){
					lierGroupe(g1, g2);
				}
			}
		}
	}
	private void registedInvertedGroupe(McdComposentGraphique comp, Groupe g){
		if(!m_invertedGroupes.containsKey(comp)){
			m_invertedGroupes.put(comp, new LinkedList<McdGraphOrganizer.Groupe>());
		}
		if(!m_invertedGroupes.get(comp).contains(g))
			m_invertedGroupes.get(comp).add(g);
	}
	private void lierGroupe(Groupe g1, Groupe g2){
		if(g1==g2)
			return;
		if(!g1.groupes.contains(g2))
			g1.groupes.add(g2);
		if(!g2.groupes.contains(g1))
			g2.groupes.add(g1);
	}
	private Groupe[][] trierGroupes(){
		Groupe tri[][] = new Groupe[m_groupes.size()][m_groupes.size()];
		int min=999, max=0;
		for(Groupe g : m_groupes){
			if(min>g.groupes.size())
				min=g.groupes.size();
			if(max<g.groupes.size())
				max=g.groupes.size();
		}
		//Première étape: trouver un groupe de départ
		Groupe depart = m_groupes.get(0);
		System.out.println(min+" "+max);
		for(Groupe g : m_groupes){
			if(g.groupes.size()==min){
				depart=g;
				break;
			}
		}
		tri[1][1] = depart;
		depart.estPlace=true;
		//Placer tous les autres composants en récursif
		placerAutresGroupes(1,1, tri);
		return tri;
	}
	private McdComposentGraphique[][] trierComposants(Groupe[][] groupes){		
		ArrayList<McdComposentGraphique> placed = new ArrayList<McdComposentGraphique>();
		McdComposentGraphique composantsTries[][] = new McdComposentGraphique[3*m_groupes.size()][3*m_groupes.size()];
		for(int i=0;i<groupes.length;++i){
			for(int j=0;j<groupes[i].length;++j){
				if(groupes[i][j]==null)
					continue;
				for(McdComposentGraphique comp : groupes[i][j].components){
					if(placed.contains(comp))
						continue;
					placed.add(comp);
					if(comp instanceof RelationGraph||
							comp instanceof HeritageGraph){ //On le place au centre
						composantsTries[1+i*3][1+j*3] = comp;
					}
					else{ //Cas entité, il faut déterminer sa position par rapport aux points communs avec les groupes voisins
						placerComposantGrille(i,j,groupes, comp, composantsTries, 1+i*3, 1+j*3);
					}
				}
			}
		}
		return composantsTries;
	}
	private void placerComposantGrille(int x, int y, Groupe[][] groupes, McdComposentGraphique comp, McdComposentGraphique composants[][], int centreXgroupe, int centreYgroupe){
		ArrayList<McdComposentGraphique> ptsCommuns;
		for(int i=-1;i<2;++i){
			if(x+i<0||x+i>=groupes.length)
				continue;
			for(int j=0;j<2;++j){
				if(j==0&&i==0)
					continue;
				if(j+y<0||j+y>=groupes[x+i].length)
					continue;
				ptsCommuns = pointsCommun(groupes[x][y], groupes[x+i][y+j]);
				if(ptsCommuns==null)
					continue;
				if(composants[centreXgroupe+i][centreYgroupe+j]==null){
					composants[centreXgroupe+i][centreYgroupe+j]=comp;
					return;
				}
			}
		}
	}
	private ArrayList<McdComposentGraphique> pointsCommun(Groupe g1, Groupe g2){
		if(g1==null|g2==null)
			return null;
		ArrayList<McdComposentGraphique> composants = new ArrayList<McdComposentGraphique>();
		for(McdComposentGraphique comp : g1.components){
			if(g2.components.contains(comp))
				composants.add(comp);
		}
		return composants.isEmpty()?null:composants;
	}
	private void placerAutresGroupes(int x, int y, Groupe tableau[][]){
		Groupe groupe = tableau[x][y];
		for(Groupe g : groupe.groupes){
			if(g.estPlace)
				continue;
			g.estPlace=true;
			Position pos = trouverPlaceLibre(x,y,tableau);
			System.out.println(x+" "+y);
			if(pos!=null){
				System.out.println(pos.x+" "+pos.y);
				tableau[pos.x][pos.y] = g;
				placerAutresGroupes(pos.x, pos.y, tableau);
			}
		}
	}
	private Position trouverPlaceLibre(int x, int y, Groupe tableau[][]){
		for(int i=-1;i<2;++i){
			if(x+i<0||x+i>=tableau.length)
				continue;
			for(int j=-1;j<2;++j){
				if(y+j<0||y+j>=tableau[x+i].length)
					continue;
				if(tableau[x+i][y+j]!=null)
					continue;
				return new Position(x+i,y+j);
			}
		}
		//On en trouve un autre, mal placé
		for(int i=tableau.length-1;i>=0;--i){
			for(int j=tableau[i].length-1;j>=0;--j){
				if(tableau[i][j]!=null)
					continue;
				return new Position(i,j);
			}
		}
		return null;
	}
	private void placerComponents(McdComposentGraphique[][] composants){
		Dimension max = new Dimension();
		for(McdComposentGraphique comp : m_mcd.getMcdComponents()){
			Dimension dim = null;
			if(comp instanceof EntiteGraph){
				EntiteGraph e = (EntiteGraph) comp;
				dim=e.getDimension();
			}
			else if(comp instanceof RelationGraph){
				dim=((RelationGraph) comp).getDimension();
			}
			if(dim==null)
				continue;
			if(max.width<dim.width)
				max.width=dim.width;
			if(max.height<dim.height)
				max.height=dim.height;
		}
		for(int i=0;i<composants.length;++i){
			for(int j=0;j<composants[i].length;++j){
				if(composants[i][j]==null)
					continue;
				 ((FormeGeometrique) composants[i][j]).setPosition(new Point((max.width+50)*i,(max.height+50)*j));
			}
		}
		//On peut maintenant placer les contraintes
		for(McdComposentGraphique comp : m_mcd.getMcdComponents()){
			if(!(comp instanceof ContrainteGraph))
				continue;
			Point moyenne = new Point();
			int nb=0;
			ContrainteGraph contrainte = (ContrainteGraph) comp;
			for(Relation r : contrainte.getContrainte().getRelations()){
				RelationGraph rg = (RelationGraph) m_mcd.getGraphicComponent(r);
				moyenne.x+=rg.getPosition().x;
				moyenne.y+=rg.getPosition().y;
				++nb;
			}
			for(Entite e : contrainte.getContrainte().getEntites()){
				EntiteGraph eg = (EntiteGraph) m_mcd.getGraphicComponent(e);
				moyenne.x+=eg.getPosition().x;
				moyenne.y+=eg.getPosition().y;
				++nb;
			}
			moyenne.x/=nb;
			moyenne.y/=nb;
			contrainte.setPosition(moyenne);
		}
	}
	class Groupe{
		public ArrayList<McdComposentGraphique> components;
		public LinkedList<Groupe> groupes;
		public boolean estPlace;
		public Groupe(){
			components = new ArrayList<McdComposentGraphique>();
			groupes = new LinkedList<McdGraphOrganizer.Groupe>();
			estPlace=false;
		}
	}
	class Position{
		public int x, y;
		public Position(int x, int y){
			this.x=x;
			this.y=y;
		}
		public Position(){
			x=0;
			y=0;
		}
	}
}
