package com.dico.auth;

import java.util.ArrayList;

import com.mcd_composent_graph.auth.CommentableComponent;
import com.mcd_composent_graph.auth.McdComposentGraphique;
import com.mcd_composent_graph.auth.ProprieteGraph;
import com.mcd_graph.auth.McdGraph;

public class DicoLog {
	private ArrayList<CommentableComponent> m_components;
	
	public DicoLog(McdGraph mcd) {
		m_components = new ArrayList<CommentableComponent>();
		for (McdComposentGraphique c : mcd.getMcdComponents()){
			if (c instanceof CommentableComponent){
				m_components.add((CommentableComponent) c);				
			}
		}
	}
	
	public String toString(){
		String dico = "";
		
		for(CommentableComponent c : m_components){
			if(c.getProprietesGraphList().isEmpty())
				continue;
			dico += "<p class='entite'><span class='nom'>"+c.getName();
			dico += "</span><hr/>";
			
			for (ProprieteGraph p : c.getProprietesGraphList()){
				dico += "<span class='propriete'>"+p.getPropriete().getName()+"</span><br />";
			}
			dico += "</p>";
		}
		return dico;
	}
	public String toHTML(boolean commentaires){
		String dico = "";
		for(CommentableComponent c : m_components){
			if(c.getProprietesGraphList().isEmpty())
				continue;
			dico += "<div class='entite'><p class='nom'>"+c.getName();
			if(!c.getCommentaire().trim().isEmpty())
				dico+="<span class='commentaire'> ("+c.getCommentaire()+")</span>";
			dico += "</p>";
			
			for (ProprieteGraph p : c.getProprietesGraphList()){
				dico += "<span class='propriete'>"+p.getPropriete().getName()+"</span>";
				if(!p.getPropriete().getCommentaire().trim().isEmpty())
					dico+="<span class='commentaire'> ("+p.getPropriete().getCommentaire()+")</span>";
				dico+="<br />";
			}
			dico += "</div>";
		}
		return dico;
	}
}
