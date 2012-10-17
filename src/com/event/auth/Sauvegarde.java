package com.event.auth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.mcd_composent_graph.auth.CardinaliteGraph;
import com.mcd_composent_graph.auth.ContrainteGraph;
import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.HeritageGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.RelationGraph;
import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Cardinalite;
import com.mcd_log.auth.Contrainte;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Heritage;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;

public class Sauvegarde {
	private ArrayList<McdComposentGraphique> m_components;
	private String m_mcdNom;
	
	public Sauvegarde(McdGraph mcd) {
		m_components = mcd.getMcdComponents();
		m_mcdNom = mcd.getLogicName();
		
		if(mcd.getFile() == null){
			if (m_components != null){
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileFilter(){
					public boolean accept(File arg0) {
						if(arg0.isDirectory())
							return true;
						String ext = getExtension(arg0);
						if(ext==null)
							return false;
						if(ext.equals("xml"))
							return true;
						return false;
					}
	
					public String getDescription() {
						return "XML Only";
					}
					
				});
				if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
					File file = chooser.getSelectedFile();
					if(getExtension(file)==null||!getExtension(file).equals("xml"))
						file = new File(file.getAbsolutePath()+".xml");
						try {
							Document doc = arborescence(m_mcdNom);
							XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
						    sortie.output(doc, new FileOutputStream(file));
						    mcd.setFile(file);
						    mcd.setSaved(true);
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		}
		else{
			try {
				File file = mcd.getFile();
				Document doc = arborescence(m_mcdNom);
				XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			    sortie.output(doc, new FileOutputStream(file));
			    mcd.setSaved(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Document arborescence(String nom){
		List<EntiteGraph> entiteGraph = new ArrayList<EntiteGraph>();
		List<RelationGraph> relationGraph = new ArrayList<RelationGraph>();
		List<CardinaliteGraph> cardinaliteGraph = new ArrayList<CardinaliteGraph>();
		List<ContrainteGraph> contrainteGraph = new ArrayList<ContrainteGraph>();
		List<HeritageGraph> heritageGraph = new ArrayList<HeritageGraph>();
		
		Element mcd = new Element("Mcd");
		Attribute mcdNom = new Attribute("nom", nom);
		mcd.setAttribute(mcdNom);
		Document document = new Document(mcd);
			
		for (McdComposentGraphique mcg : m_components){
			if (mcg instanceof EntiteGraph){
				entiteGraph.add((EntiteGraph) mcg);
			}
			else if (mcg instanceof RelationGraph){
				relationGraph.add((RelationGraph) mcg);
			}
			else if (mcg instanceof ContrainteGraph){
				contrainteGraph.add((ContrainteGraph) mcg);
			}
			else if (mcg instanceof HeritageGraph){
				heritageGraph.add((HeritageGraph) mcg);
			}
			else if (mcg instanceof CardinaliteGraph){
				cardinaliteGraph.add((CardinaliteGraph) mcg);
			}
		}
		
		int i = 0; 
		Element allEntite = new Element("All-entite");
		for (EntiteGraph eg : entiteGraph){
			Entite e = eg.getEntite();
			Element entite = new Element("Entite");
			
			Attribute id = new Attribute("id", String.valueOf(i));
			entite.setAttribute(id);
			
			Attribute posx = new Attribute("x", String.valueOf(eg.getPosition().x));
			entite.setAttribute(posx);
			
			Attribute posy = new Attribute("y", String.valueOf(eg.getPosition().y));
			entite.setAttribute(posy);

			Attribute nomE = new Attribute("nom", e.getName());
			entite.setAttribute(nomE);

			Attribute commentaireE = new Attribute("commentaire", e.getCommentaire());
			entite.setAttribute(commentaireE);
			
			for (Propriete p : e.getProprietes()){
				Element propriete = new Element("Propriete");
				
				Attribute nomP = new Attribute("nom", p.getName());
				propriete.setAttribute(nomP);

				Attribute commentaireP = new Attribute("commentaire", p.getCommentaire());
				propriete.setAttribute(commentaireP);
				
				
				Attribute typeP = new Attribute("type", p.getType().toString());
				propriete.setAttribute(typeP);
				
				Attribute tailleP = new Attribute("taille", String.valueOf(p.getTaille()));
				propriete.setAttribute(tailleP);
				
				Attribute cleP = new Attribute("clé_primaire", String.valueOf(p.isClePrimaire()));
				propriete.setAttribute(cleP);
				
				Attribute nullP = new Attribute("null", String.valueOf(p.isNull()));
				propriete.setAttribute(nullP);
				
				Attribute AutoP = new Attribute("auto-incrémenté", String.valueOf(p.isAutoIncrement()));
				propriete.setAttribute(AutoP);
				
				entite.addContent(propriete);
			}
			allEntite.addContent(entite);
			++i;
		}
		mcd.addContent(allEntite);
		
		Element allRelation = new Element("All-relation");
		for (RelationGraph rg : relationGraph){
			Relation e = rg.getRelation();
			Element relation = new Element("Relation");
			
			Attribute id = new Attribute("id", String.valueOf(i));
			relation.setAttribute(id);
			
			Attribute posx = new Attribute("x", String.valueOf(rg.getPosition().x));
			relation.setAttribute(posx);
			
			Attribute posy = new Attribute("y", String.valueOf(rg.getPosition().y));
			relation.setAttribute(posy);

			Attribute nomR = new Attribute("nom", e.getNom());
			relation.setAttribute(nomR);

			Attribute commentaireR = new Attribute("commentaire", e.getCommentaire());
			relation.setAttribute(commentaireR);
			
			for (Propriete p : e.getProprietes()){
				Element propriete = new Element("Propriete");
				
				if (!p.getName().equals(null)){
					Attribute nomP = new Attribute("nom", p.getName());
					propriete.setAttribute(nomP);
				}
				
				if (!p.getCommentaire().equals(null)){
					Attribute commentaireP = new Attribute("commentaire", p.getCommentaire());
					propriete.setAttribute(commentaireP);
				}
				
				Attribute typeP = new Attribute("type", p.getType().toString());
				propriete.setAttribute(typeP);
				
				Attribute tailleP = new Attribute("taille", String.valueOf(p.getTaille()));
				propriete.setAttribute(tailleP);
				
				Attribute cleP = new Attribute("clé_primaire", String.valueOf(p.isClePrimaire()));
				propriete.setAttribute(cleP);
				
				Attribute nullP = new Attribute("null", String.valueOf(p.isNull()));
				propriete.setAttribute(nullP);
				
				Attribute AutoP = new Attribute("auto-incrémenté", String.valueOf(p.isAutoIncrement()));
				propriete.setAttribute(AutoP);
				
				relation.addContent(propriete);
			}
			++i;
			allRelation.addContent(relation);
		}
		mcd.addContent(allRelation);
		
		Element allCardinalite = new Element("All-cardinalite");
		for (CardinaliteGraph cg : cardinaliteGraph){
			Cardinalite c = cg.getCardinalite();
			Element cardinalite = new Element("Cardinalite");
			
			Attribute min = new Attribute("min", String.valueOf(c.getMin()));
			cardinalite.setAttribute(min);
			
			Attribute max = new Attribute("max", String.valueOf(c.getMax()));
			cardinalite.setAttribute(max);
			
			Attribute relatif = new Attribute("relatif", String.valueOf(c.isRelatif()));
			cardinalite.setAttribute(relatif);
			
			for (int j=0; j<entiteGraph.size(); ++j)
				if (entiteGraph.get(j).getEntite() == c.getEntite()){
					Element entite = new Element("Entite");
					entite.setAttribute(new Attribute("id", String.valueOf(j)));
					
					cardinalite.addContent(entite);
				}
			
			for (int j=0; j<relationGraph.size(); ++j)
				if (relationGraph.get(j).getRelation() == c.getRelation()){
					Element relation = new Element("Relation");
					relation.setAttribute(new Attribute("id", String.valueOf(j+entiteGraph.size())));
					
					cardinalite.addContent(relation);
				}
			
			allCardinalite.addContent(cardinalite);
		}
		mcd.addContent(allCardinalite);
		
		Element allHeritage = new Element("All-héritage");
		for (HeritageGraph hg : heritageGraph){
			Heritage h = hg.getHeritage();
			Element heritage = new Element("Héritage");
			
			Attribute posx = new Attribute("x", String.valueOf(hg.getPosition().x));
			heritage.setAttribute(posx);
			
			Attribute posy = new Attribute("y", String.valueOf(hg.getPosition().y));
			heritage.setAttribute(posy);
			
			Attribute type = new Attribute("type", h.getType().toString());
			heritage.setAttribute(type);
			
			for (int j=0; j<entiteGraph.size(); ++j)
				for (int u=0; u<h.getEnfants().size(); ++u)
					if (entiteGraph.get(j).getEntite() == h.getEnfants().get(u)){
						Element entite = new Element("Entite");
						entite.setAttribute(new Attribute("id", String.valueOf(j)));
						
						heritage.addContent(entite);
					}
			
			allHeritage.addContent(heritage);
		}
		mcd.addContent(allHeritage);
		
		Element allContrainte = new Element("All-contrainte");
		for (ContrainteGraph cg : contrainteGraph){
			Contrainte c = cg.getContrainte();
			Element contrainte = new Element("Contrainte");
			
			Attribute posx = new Attribute("x", String.valueOf(cg.getPosition().x));
			contrainte.setAttribute(posx);
			
			Attribute posy = new Attribute("y", String.valueOf(cg.getPosition().y));
			contrainte.setAttribute(posy);
			
			Attribute type = new Attribute("type", c.getNom());
			contrainte.setAttribute(type);
			
			for (int j=0; j<entiteGraph.size(); ++j)
				for (int u=0; u<c.getEntites().size(); ++u)
					if (entiteGraph.get(j).getEntite() == c.getEntites().get(u)){
						Element entite = new Element("Entite");
						entite.setAttribute(new Attribute("id", String.valueOf(j)));
						
						contrainte.addContent(entite);
					}
			
			for (int j=0; j<relationGraph.size(); ++j)
				for (int u=0; u<c.getRelations().size(); ++u)
					if (relationGraph.get(j).getRelation() == c.getRelations().get(u)){
						Element relation = new Element("Relation");
						relation.setAttribute(new Attribute("id", String.valueOf(j+entiteGraph.size())));
						
						contrainte.addContent(relation);
					}
			allContrainte.addContent(contrainte);
		}
		mcd.addContent(allContrainte);
		
		return document;
	}
	
	private String getExtension(File f){
		String ext=f.getName();
		
		int index = ext.lastIndexOf(".");
		String ret = null;
		if(index>0&&index<ext.length()-1){
			ret = ext.substring(index+1).toLowerCase();
		}
		
		return ret;
	}
}
