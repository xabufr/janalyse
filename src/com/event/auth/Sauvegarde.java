package com.event.auth;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.mcd_composent_graph.auth.CardinaliteGraph;
import com.mcd_composent_graph.auth.ContrainteGraph;
import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.HeritageGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_log.auth.Cardinalite;
import com.mcd_log.auth.Contrainte;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Heritage;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;

public class Sauvegarde {
	private ArrayList<McdComposentGraphique> m_components;
	
	public Sauvegarde(ArrayList<McdComposentGraphique> components) {
		m_components = components;
		if (m_components != null)
			initialize();
		
	}
	
	private void initialize(){
		String path = System.getProperty("user.dir")+"/src/save/";
		path += JOptionPane.showInputDialog(null, "Nom du fichier:", "Sauvegarde", JOptionPane.PLAIN_MESSAGE);
		path += ".save";
		String text;
		File f = new File(path);
		
		try {
			if (!f.exists())
				f.createNewFile();
			
			FileWriter fw = new FileWriter(f);
			BufferedWriter output = new BufferedWriter(fw);
			
			for (McdComposentGraphique mcg : m_components){
				if (mcg instanceof EntiteGraph){
					Entite e = ((EntiteGraph) mcg).getEntite();
					
					text = "[Entité]\n";
					output.write(text);
					
					text = "position.x="+((EntiteGraph) mcg).getPosition().x+"\n";
					output.write(text);
					
					text = "position.y="+((EntiteGraph) mcg).getPosition().y+"\n";
					output.write(text);
					
					text = "nom="+e.getName()+"\n";
					output.write(text);
					
					text = "commentaire="+e.getCommentaire()+"\n";
					output.write(text);
					
					text = "mère="+e.isMere()+"\n";
					output.write(text);
					
					for (Propriete p : e.getProprietes()){
						text = "[Entité-Propriété]";
						output.write(text);
						
						text = "nom="+p.getName()+"\n";
						output.write(text);
						
						text = "commentaire="+p.getCommentaire()+"\n";
						output.write(text);
						
						text = "type="+p.getType().toString()+"\n";
						output.write(text);
						
						text = "taille="+p.getTaille()+"\n";
						output.write(text);
						
						text = "clé primaire="+p.isClePrimaire()+"\n";
						output.write(text);
						
						text = "null="+p.isNull()+"\n";
						output.write(text);
						
						text = "auto-incrémenté="+p.isAutoIncrement()+"\n";
						output.write(text);
					}
				}
				else if (mcg instanceof RelationGraph){
					Relation r = ((RelationGraph) mcg).getRelation();
					
					text = "[Relation]\n";
					output.write(text);
					
					text = "position.x="+((RelationGraph) mcg).getPosition().x+"\n";
					output.write(text);
					
					text = "position.y="+((RelationGraph) mcg).getPosition().y+"\n";
					output.write(text);
					
					text = "nom="+r.getNom()+"\n";
					output.write(text);
					
					text = "commentaire="+r.getCommentaire()+"\n";
					output.write(text);
					for (Propriete p : r.getProprietes()){
						text = "[Relation-Propriété]";
						output.write(text);
						
						text = "nom="+p.getName()+"\n";
						output.write(text);
						
						text = "commentaire="+p.getCommentaire()+"\n";
						output.write(text);
						
						text = "type="+p.getType().toString()+"\n";
						output.write(text);
						
						text = "taille="+p.getTaille()+"\n";
						output.write(text);
						
						text = "clé primaire="+p.isClePrimaire()+"\n";
						output.write(text);
						
						text = "null="+p.isNull()+"\n";
						output.write(text);
						
						text = "auto-incrémenté="+p.isAutoIncrement()+"\n";
						output.write(text);
					}
				}
				else if (mcg instanceof ContrainteGraph){
					Contrainte c = ((ContrainteGraph) mcg).getContrainte();
					
					text = "[Contrainte]";
					output.write(text);
					
					text = "type="+c.getNom()+"\n";
					output.write(text);
					
					for (Entite e : c.getEntites()){
						text = "[Contrainte-Entité]";
						output.write(text);
						
						text = "nom="+e.getName()+"\n";
						output.write(text);
					}
					
					for (Relation r : c.getRelations()){
						text = "[Contrainte-Relation]";
						output.write(text);
						
						text = "nom="+r.getNom()+"\n";
						output.write(text);
					}
				}
				else if (mcg instanceof HeritageGraph){
					Heritage h = ((HeritageGraph) mcg).getHeritage();
					
					text = "[Héritage]";
					output.write(text);
					
					text = "type="+h.getType().toString()+"\n";
					output.write(text);
					
					for (Entite e : h.getEnfants()){
						text = "[Héritage-Entite]";
						output.write(text);
						
						text = "nom="+e.getName()+"\n";
						output.write(text);
					}
				}
				else if (mcg instanceof CardinaliteGraph){
					Cardinalite c = ((CardinaliteGraph) mcg).getCardinalite();
					
					text = "[Relation]\n";
					output.write(text);
					
					text = "min="+c.getMin()+"\n";
					output.write(text);
					
					text = "max="+c.getMax()+"\n";
					output.write(text);
					
					text = "relatif="+c.isRelatif()+"\n";
					output.write(text);
					
					text = "entite="+c.getEntite().getName()+"\n";
					output.write(text);
					
					text = "relation="+c.getRelation().getNom()+"\n";
					output.write(text);
				}
			}
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
