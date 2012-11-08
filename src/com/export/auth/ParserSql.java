package com.export.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.utils.auth.Utils;

public class ParserSql {
	private static File m_file;
	private static Hashtable<String, List<List<String>>> m_entites;
	
	public static void parse() {
		m_file = Utils.getFile4Load("sql");
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
	private static void seekEntites(BufferedReader b) throws IOException{
		String ligne, tmp, entite="", nom="";
		int debut=0;
		while ((ligne = b.readLine()) != null){
			tmp = ligne.toLowerCase();
			if (tmp.matches("create table.*")){
				tmp = showWord(13+debutLigne(tmp), ligne, '(');
				entite = tmp;
				while (!(ligne = b.readLine()).contains(");")){
					List<String> values = new ArrayList<String>();
					tmp = ligne.toLowerCase();
					debut = debutLigne(tmp);
					nom = showWord(debut, ligne, ',');
					values.add(nom);
					debut += nom.length()+1;
					if (!tmp.contains("constraint")){
						while (tmp != ""){
							tmp = showWord(debut, ligne, ',');
							if (tmp != "")
								values.add(tmp);
							debut += tmp.length()+1;
						}
						if (!m_entites.containsKey(entite))
							m_entites.put(entite, new ArrayList<List<String>>());
						
						m_entites.get(entite).add(values);
					}
					else {
						if (tmp.contains("primary key"))
							setPrimaryKey(m_entites.get(entite), nom);
						else if (tmp.contains("foreign key"))
							setForeignKey(ligne, nom);
					}
				}
			}
			else if(tmp.matches("alter table.*")){
				entite = showWord(12+debutLigne(tmp), ligne, ' ');
				ligne = b.readLine();
				tmp = ligne.toLowerCase();
				if (tmp != null && tmp.contains("primary key"))
					setPrimaryKey(m_entites.get(entite), 
							ligne.substring(ligne.indexOf("(")+1, ligne.indexOf(")")));
				else if (tmp != null && tmp.contains("foreign key"))
					setForeignKey(ligne, entite);
			}
		}
	}
	private static void setPrimaryKey(List<List<String>> props, String cle){
		for (List<String> lst : props){
			if (lst.contains(cle)){
				lst.add("primary");
			}
		}
	}
	private static void setForeignKey(String ligne, String entite){
		for (List<String> lst : m_entites.get(entite)){
			if (lst.contains(ligne.substring(ligne.indexOf("(")+1, ligne.indexOf(")")))){
				lst.add("foreign_"+showWord(ligne.indexOf(")")+13, ligne, '('));
			}
		}
	}
	private static int debutLigne(String ligne){
		for (int i=0; i<ligne.toCharArray().length; ++i){
			char c = ligne.toCharArray()[i];
			if (c>='a' && c<='z')
				return i;
		}
		return 0;
	}
	private static String showWord(int debut, String ligne, char fin){
		String resultat="";
		for (int i=debut; i<ligne.toCharArray().length; ++i){
			char c = ligne.toCharArray()[i];
			if (resultat.toLowerCase().equals("identity"))
				return resultat += ligne.substring(i, ligne.lastIndexOf(")")+1);
			else if (c != ' ' && c != fin){
				resultat += c;
				if (resultat.toLowerCase().equals("not")){
					resultat += " ";
					++i;
				}
				if (resultat.toLowerCase().equals("constraint"))
					 return ligne.substring(ligne.indexOf("(")+1, ligne.indexOf(")"));;
			}
			else
				return resultat;
		}
		
		return resultat;
	}
	public static Hashtable<String, List<List<String>>> getEntites() {
		return m_entites;
	}
	public static void setEntites(Hashtable<String, List<List<String>>> hashtable) {
		m_entites = hashtable;
	}

}
