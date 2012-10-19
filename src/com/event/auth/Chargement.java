package com.event.auth;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.mcd_composent_graph.auth.CardinaliteGraph;
import com.mcd_composent_graph.auth.CardinaliteGraphType;
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
import com.mcd_log.auth.ProprieteType;
import com.mcd_log.auth.ProprieteTypeE;
import com.mcd_log.auth.Relation;
import com.mcd_log.auth.HeritageType;
import com.mcd_log.auth.ContrainteType;

public class Chargement{

	public Chargement(McdGraph mcd) {
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
		if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			if(getExtension(file)==null||!getExtension(file).equals("xml"))
				file = new File(file.getAbsolutePath()+".xml");
			
			SAXBuilder sax = new SAXBuilder();
			try {
				Document doc = sax.build(file);
				if (mcd != null && doc != null){
					charger(mcd, doc);
					mcd.setFile(file);
				    mcd.setSaved(true);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JDOMException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void charger(McdGraph mcd, Document doc){
		Element racine = doc.getRootElement();
		List<Element> entites = racine.getChild("All-entite").getChildren("Entite");
		List<Element> relations = racine.getChild("All-relation").getChildren("Relation");
		List<Element> cardinalites = racine.getChild("All-cardinalite").getChildren("Cardinalite");
		List<Element> heritages = racine.getChild("All-héritage").getChildren("Héritage");
		List<Element> contraintes = racine.getChild("All-contrainte").getChildren("Contrainte");
		Hashtable <Integer, McdComposentGraphique> ids = new Hashtable();
		int i=0;

		mcd.setName(racine.getAttributeValue("nom"));
		
		for (Element courant : entites){
			EntiteGraph eg = new EntiteGraph();
			Entite e = new Entite(null);
			List<Propriete> props = new ArrayList();
			Point pos = new Point();
			
			pos.x = Integer.parseInt(courant.getAttributeValue("x"));
			pos.y = Integer.parseInt(courant.getAttributeValue("y"));
			eg.setPosition(pos);
			
			e.setName(courant.getAttributeValue("nom"));
			e.setCommentaire(courant.getAttributeValue("commentaire"));
			
			for (Element p : courant.getChildren("Propriete")){
				Propriete prop = new Propriete(null, null);
				
				prop.setName(p.getAttributeValue("nom"));
				prop.setCommentaire(p.getAttributeValue("commentaire"));
				ProprieteType type = new ProprieteType(ProprieteTypeE.valueOf(p.getAttributeValue("type")));
				prop.setType(type);
				prop.setTaille(Integer.parseInt(p.getAttributeValue("taille")));
				prop.setClePrimaire(Boolean.getBoolean(p.getAttributeValue("clé_primaire")));
				prop.setNull(Boolean.getBoolean(p.getAttributeValue("null")));
				prop.setAutoIncrement(Boolean.getBoolean(p.getAttributeValue("auto-incrémenté")));
				
				props.add(prop);
			}
			e.setProprietes(props);
			eg.setEntite(e);
			eg.setMcd(mcd);
			mcd.addMcdComponents(eg);
			ids.put(i, eg);
			++i;
		}
		
		for (Element courant : relations){
			RelationGraph eg = new RelationGraph();
			Relation e = new Relation();
			Point pos = new Point();
			
			pos.x = Integer.parseInt(courant.getAttributeValue("x"));
			pos.y = Integer.parseInt(courant.getAttributeValue("y"));
			eg.setPosition(pos);
			
			e.setNom(courant.getAttributeValue("nom"));
			e.setCommentaire(courant.getAttributeValue("commentaire"));
			
			for (Element p : courant.getChildren("Propriete")){
				Propriete prop = new Propriete(null, null);
				
				prop.setName(p.getAttributeValue("nom"));
				prop.setCommentaire(p.getAttributeValue("commentaire"));
				prop.setType(new ProprieteType(ProprieteTypeE.valueOf(p.getAttributeValue("type"))));
				prop.setTaille(Integer.parseInt(p.getAttributeValue("taille")));
				prop.setClePrimaire(Boolean.parseBoolean(p.getAttributeValue("clé_primaire")));
				prop.setNull(Boolean.parseBoolean(p.getAttributeValue("null")));
				prop.setAutoIncrement(Boolean.parseBoolean(p.getAttributeValue("auto-incrémenté")));
				
				e.addPropriete(prop);
			}
			eg.setRelation(e);
			eg.setMcd(mcd);
			mcd.addMcdComponents(eg);
			mcd.addMcdComponents(eg);
			ids.put(i, eg);
			++i;
		}
		
		for (Element courant : cardinalites){
			CardinaliteGraph eg = new CardinaliteGraph();
			Cardinalite e = new Cardinalite();
			
			e.setMin(Integer.valueOf(courant.getAttributeValue("min")));
			e.setMax(Integer.valueOf(courant.getAttributeValue("max")));
			e.setRelatif(Boolean.valueOf(courant.getAttributeValue("relatif")));
			
			EntiteGraph ent  = (EntiteGraph) ids.get(Integer.valueOf(courant.getChild("Entite").getAttributeValue("id")));
			e.setEntite(ent.getEntite());
			
			RelationGraph rel  = (RelationGraph) ids.get(Integer.valueOf(courant.getChild("Relation").getAttributeValue("id")));;
			e.setRelation(rel.getRelation());
			
			for (CardinaliteGraphType t : CardinaliteGraphType.values())
				if (t.toString().equals(courant.getAttributeValue("style")))
					eg.setTypeDessin(t);

			eg.setCardinalite(e);
			eg.setMcd(mcd);
			mcd.addMcdComponents(eg);
		}
		
		for (Element courant : heritages){
			HeritageGraph eg = new HeritageGraph();
			Heritage e = new Heritage(null);
			Point pos = new Point();
			
			pos.x = Integer.parseInt(courant.getAttributeValue("x"));
			pos.y = Integer.parseInt(courant.getAttributeValue("y"));
			eg.setPosition(pos);
			
			e.setType(HeritageType.valueOf(courant.getAttributeValue("type")));
			
			for (Element entite : courant.getChildren("Entite")){
				EntiteGraph ent  = (EntiteGraph)ids.get(Integer.valueOf(entite.getAttributeValue("id")));;
				e.addEnfant(ent.getEntite());
			}
			
			eg.setHeritage(e);
			eg.setMcd(mcd);
			mcd.addMcdComponents(eg);
		}
		
		for (Element courant : contraintes){
			ContrainteGraph eg = new ContrainteGraph();
			Contrainte e = new Contrainte(null);
			Point pos = new Point();
			
			pos.x = Integer.parseInt(courant.getAttributeValue("x").toString());
			pos.y = Integer.parseInt(courant.getAttributeValue("y").toString());
			eg.setPosition(pos);
			
			e.setNom(ContrainteType.valueOf(courant.getAttributeValue("type")));
			
			for (Element entite : courant.getChildren("Entite")){
				EntiteGraph ent  = (EntiteGraph) ids.get(Integer.valueOf(entite.getAttributeValue("id")));;
				e.addEntite(ent.getEntite());
			}
				
			for (Element relation : courant.getChildren("Relation")){
				RelationGraph rel  = (RelationGraph) ids.get(Integer.valueOf(relation.getAttributeValue("id")));;
				e.addRelation(rel.getRelation());
			}
			
			eg.setContrainte(e);
			eg.setMcd(mcd);
			mcd.addMcdComponents(eg);
		}
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
