package com.mcd_log.auth;

import java.util.LinkedList;

import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;

public class Propriete implements Cloneable{
	private String m_name;
	private String m_commentaire;
	private ProprieteTypeE m_type;
	private int m_taille[];
	private boolean m_clePrimaire;
	private boolean m_autoIncrement;
	private boolean m_null;
	
	public Propriete(String name, ProprieteTypeE proprieteType){
		setType(proprieteType);	
		setName(name);
		setClePrimaire(false);
		setAutoIncrement(false);
		setNull(false);
	}
	
	public Propriete(String name, ProprieteTypeE proprieteType, int taille, boolean clePrimaire, boolean autoIncrement, boolean zero){
		setType(proprieteType);	
		setName(name);
		setClePrimaire(clePrimaire);
		setAutoIncrement(autoIncrement);
		setNull(zero);
	}
	public Propriete(Propriete p){
		setType(p.m_type);
		setCommentaire(p.m_commentaire);
		setName(p.m_name);
		setTaille(p.m_taille);
		setClePrimaire(p.m_clePrimaire);
		setAutoIncrement(p.m_autoIncrement);
		setNull(p.m_autoIncrement);
	}

	public String getName() {
		return m_name;
	}
	public String getVirtualName(String proprietaire){
		return ProprieteProcessor.process(proprietaire, m_name);
	}
	public void setName(String m_name) {
		this.m_name = m_name;
	}
	
	public String getCommentaire() {
		return m_commentaire;
	}

	public void setCommentaire(String commentaire) {
		m_commentaire = commentaire;
	}

	public ProprieteTypeE getType() {
		return m_type;
	}

	public void setType(ProprieteTypeE type) {
		if(type.getNombreTaille()>0)
			m_taille = new int[type.getNombreTaille()];
		else
			m_taille=null;
		this.m_type = type;
	}

	public int getTaille(int i) {
		return m_taille[i];
	}

	public void setTaille(int i,int m_taille) {
		this.m_taille[i] = m_taille;
	}
	public void setTaille(int taille[]) {
		m_taille = new int[taille.length];
		System.arraycopy(taille, 0, m_taille, 0, taille.length);
	}

	public boolean isClePrimaire() {
		return m_clePrimaire;
	}

	public void setClePrimaire(boolean m_clePrimaire) {
		this.m_clePrimaire = m_clePrimaire;
	}

	public boolean isAutoIncrement() {
		return m_autoIncrement;
	}

	public void setAutoIncrement(boolean m_autoIncrement) {
		this.m_autoIncrement = m_autoIncrement;
	}

	public boolean isNull() {
		return m_null;
	}

	public void setNull(boolean m_null) {
		this.m_null = m_null;
	}
	public String toString(){
		return m_name;
	}
	
	public Propriete clone() throws CloneNotSupportedException{
		Propriete p = (Propriete) super.clone();
		p.m_type = m_type;
		return p;
	}
}
class ProprieteProcessor{
	public static String schema;
	static String process(String proprietaireName, String p){
		schema = (String) McdPreferencesManager.getInstance().get(PGroupe.PROPRIETE, PCle.SCHEMA);
		LinkedList<ProprieteProcessorCommand> commands = new LinkedList<ProprieteProcessorCommand>();
		String name="";
		Boolean startCommand=false;
		Boolean startLongCommand=false;
		int stateLong=0; //0->min, 1-> command, 2->max
		ProprieteProcessorCommand currentCommand=null;
		
		for(int i=0;i<schema.length();++i){
			if(schema.charAt(i) == '%'){
				startCommand=true;
				currentCommand = new ProprieteProcessorCommand();
			}
			else if(schema.charAt(i)== '['&&startCommand){
				startLongCommand=true;
				stateLong=0;
			}
			else if(startCommand||startLongCommand){
				if(!startLongCommand){
					startCommand=false;
					currentCommand.command=schema.charAt(i);
					commands.add(currentCommand);
				}
				else{
					char carac = schema.charAt(i);
					if(Character.isLetter(carac)){
						stateLong=1;
						currentCommand.command=carac;
					}
					else{
						if(carac==']'){
							startLongCommand=false;
							startCommand=false;
							commands.add(currentCommand);
						}
						else{
							if(stateLong==0){
								currentCommand.minC+=carac;
							}
							else{
								currentCommand.maxC+=carac;
							}
						}
					}
				}
			}
			else{
				currentCommand = new ProprieteProcessorCommand();
				currentCommand.command='i';
				currentCommand.carac=schema.charAt(i);
				commands.add(currentCommand);
			}
		}
		
		for(ProprieteProcessorCommand c : commands){
			switch(c.command){
			case 'i':
				name+=c.carac;
				break;
			case 'p':
				name+=c.getPortion(p);
				break;
			case 'q':
				name+=c.getPortion(p.toLowerCase());
				break;
			case 'P':
				name+=c.getPortion(p.toUpperCase());
				break;
			case 'e':
				name+=c.getPortion(proprietaireName);
				break;
			case 'r':
				name+=c.getPortion(proprietaireName.toLowerCase());
				break;
			case 'E':
				name+=c.getPortion(proprietaireName.toUpperCase());
				break;
			default:
				name+=c.command;
			}
		}
		return name;
	}
}
class ProprieteProcessorCommand{
	public int min=0, max=0;
	public String minC="", maxC="";
	public char command, carac;
	public String getPortion(String ch){
		if(minC.equals(""))
			minC="0";
		if(maxC.equals(""))
			maxC="0";
		min=Integer.parseInt(minC);
		max=Integer.parseInt(maxC);
		int imax;
		if(min!=0&&min>=ch.length())
			return "";
		if(max==0||max>ch.length())
			imax=ch.length();
		else
			imax=max;
		return ch.substring(min, imax);
	}
}
