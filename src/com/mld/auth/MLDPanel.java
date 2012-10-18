package com.mld.auth;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Cardinalite;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;

import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;

public class MLDPanel extends JPanel {
	private McdGraph m_mcd;
	public MLDPanel(McdGraph mcd) {
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JTextPane textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		JScrollPane sp = new JScrollPane(textPane);
		add(sp, "cell 0 0,grow");
		m_mcd=mcd;
		textPane.setText(analyser(m_mcd));
	}
	static private String analyser(McdGraph mcd){
		ArrayList<Entite> entites = analyserCreerMldLogique(mcd);
		String analyse="";
		for(Entite e : entites){
			analyse+=e.getName();
			if(!e.getProprietes().isEmpty()){
				analyse+="(";
				Boolean hasPrec=false;
				for(Propriete p : e.getProprietes()){
					if(hasPrec)
						analyse+=", ";
					if(p.isClePrimaire())
						analyse+="<span style='text-decoration: underline'>";
					analyse+=p.getName();
					if(p.isClePrimaire())
						analyse+="</span>";
					hasPrec=true;
				}
				analyse+=")";
			}
			analyse+="<br />";
		}
		return analyse;
	}
	static private ArrayList<Entite> analyserCreerMldLogique(McdGraph mcd){
		ArrayList<Entite> entites = new ArrayList<Entite>();
		ArrayList<RelationMld> relationsMld = new ArrayList<RelationMld>();
		Hashtable<Object, Object> m_correspondances = new Hashtable<Object, Object>(); //<ancien,nouveau>
		ArrayList<Object> logicObject = mcd.getLogic();
				
		for(Object o : logicObject){
			if(o instanceof Entite){
				try {
					Entite e = ((Entite) o).clone();
					entites.add(e);
					m_correspondances.put(o, e);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
		for(Object o : logicObject){
			if(o instanceof Relation){
				RelationMld r = new RelationMld((Relation) o);
				relationsMld.add(r);
				m_correspondances.put(o, r);
			}
		}
		for(Object o : logicObject){
			if(o instanceof Cardinalite){
				Relation r = ((Cardinalite) o).getRelation();
				Entite e = ((Cardinalite) o).getEntite();
				((RelationMld)m_correspondances.get(r)).addCardinalite(
						(Entite)m_correspondances.get(e),
						((Cardinalite) o).getMin(),
						((Cardinalite) o).getMax(),
						((Cardinalite) o).isRelatif());
			}
		}
		for(RelationMld r : relationsMld){
			if(!r.needToCreateNewEntity()){
				r.migrer();
				System.out.println("Migrer");
			}
		}
		return entites;
	}
}
