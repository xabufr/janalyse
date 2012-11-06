package com.export.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ParserSql {
	private File m_file;
	private Hashtable<String, List<List<String>>> m_entites;
	public ParserSql(String f) {
		m_file = new File(f);
		setEntites(new Hashtable<String, List<List<String>>>());
		if (m_file.exists()){
			try {
				BufferedReader buffer = new BufferedReader(new FileReader(m_file));
				seekEntites(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void seekEntites(BufferedReader b) throws IOException{
		String ligne, tmp, entite="", prop;
		int debut=0;
		while ((ligne = b.readLine()) != null){
			tmp = ligne.toLowerCase();
			if (tmp.matches("create table.*")){
				tmp = showWord(13, ligne, '(');
				entite = tmp;
			}
			else if(tmp.matches("	.*")){
				List<String> values = new ArrayList<String>();
				String nom;
				debut = debutLigne(tmp);
				nom = showWord(debut, ligne, ',');
				values.add(nom);
				debut += nom.length()+1;
				if (!tmp.contains("constraint") || !tmp.contains("constraint")){
					while (tmp != ""){
						tmp = showWord(debut, ligne, ',');
						values.add(tmp);
						debut += tmp.length()+1;
					}
					if (!m_entites.containsKey(entite))
						m_entites.put(entite, new ArrayList());
					
					m_entites.get(entite).add(values);
				}
				else {
					//if (nom != null)
						//m_proprietes.get(nom).add("tutu");
				}
			}
		}
		System.out.println(m_entites);
	}
	private int debutLigne(String ligne){
		for (int i=0; i<ligne.toCharArray().length; ++i){
			char c = ligne.toCharArray()[i];
			if (c>='a' && c<='z'){
				return i;
			}
		}
		return 0;
	}
	private String showWord(int debut, String ligne, char fin){
		String resultat="";
		for (int i=debut; i<ligne.toCharArray().length; ++i){
			char c = ligne.toCharArray()[i];
			if (c != ' ' && c != fin){
				resultat += c;
				if (resultat.toLowerCase().equals("not")){
					resultat += " ";
					++i;
				}
				if (resultat.toLowerCase().equals("constraint") || resultat.toLowerCase().equals("add"))
					 return showIndex(debut, ligne);
			}
			else
				return resultat;
		}
		
		return resultat;
	}
	private String showIndex(int debut, String ligne){
		String resultat="";
		if (ligne.toLowerCase().contains("primary key")){
			resultat = ligne.substring(ligne.indexOf("(")+1, ligne.indexOf(")"));
		}
			
		
		return resultat;
	}
	public Hashtable<String, List<List<String>>> getEntites() {
		return m_entites;
	}
	public void setEntites(Hashtable<String, List<List<String>>> hashtable) {
		m_entites = hashtable;
	}

}
