package com.mcd_graph.auth;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

import com.mcd_composent_graph.auth.CardinaliteGraph;
import com.mcd_composent_graph.auth.FormeGeometrique;
import com.mcd_composent_graph.auth.HeritageGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_log.auth.Entite;

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
		placerComponents(groupesTries);
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
							g.components.add(m_mcd.getGraphicComponent(card.getCardinalite().getEntite()));
							registedInvertedGroupe(m_mcd.getGraphicComponent(card.getCardinalite().getEntite()), g);
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
		int i=0;
		for(Groupe g : m_groupes){
			tri[i++][0] = g;
		}
		return tri;
	}
	private void placerComponents(Groupe[][] groupes){
		for(int i=0;i<groupes.length;++i){
			if(groupes[i]==null)
				continue;
			for(int j=0;j<groupes[i].length;++j){
				if(groupes[i][j]==null)
					continue;
				for(int k=0;k<groupes[i][j].components.size();++k){
					 ((FormeGeometrique) groupes[i][j].components.get(k)).setPosition(new Point(100*i+20*k,100*j+20*k));
				}
			}
		}
	}
	class Groupe{
		public ArrayList<McdComposentGraphique> components;
		public LinkedList<Groupe> groupes;
		public Groupe(){
			components = new ArrayList<McdComposentGraphique>();
			groupes = new LinkedList<McdGraphOrganizer.Groupe>();
		}
	}
}
