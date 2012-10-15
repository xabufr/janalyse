package com.preferences_mcd_logique.auth;

import java.awt.Font;
import java.util.Enumeration;
import java.util.Hashtable;

public class McdPreferencesManager {

	private McdPreferencesManager() {
		m_proprietes = new Hashtable<PGroupe, Hashtable<PCle, Object>>();
		m_proprietesPush = new Hashtable<PGroupe, Hashtable<PCle,Object>>();
	}
	public static McdPreferencesManager getInstance(){
		if(m_instance==null){
			synchronized(McdPreferencesManager.class){
				if(m_instance==null){
					m_instance = new McdPreferencesManager();
				}
			}
		}
		return m_instance;
	}
	
	public void set(PGroupe g, PCle c, Object o){
		if(!m_proprietes.containsKey(g)){
			m_proprietes.put(g, new Hashtable<PCle, Object>());
		}
		m_proprietes.get(g).put(c, o);
	}
	public void setFont(PGroupe g, PCle c, String name, int style, int size){
		set(g,c,PoliceManager.getInstance().getFont(name, style, size));
	}
	public Object get(PGroupe g, PCle c){
		if(!m_proprietes.containsKey(g))
			return null;
		if(!m_proprietes.get(g).containsKey(c))
			return null;
		return m_proprietes.get(g).get(c);
	}
	public Font getFont(PGroupe g, PCle c){
		return (Font) get(g, c);
	}
	public void push(){
		copyPreferences(m_proprietes, m_proprietesPush);
	}
	public void pop(){
		copyPreferences(m_proprietesPush, m_proprietes);
	}
	private void copyPreferences(Hashtable<PGroupe, Hashtable<PCle, Object>> from, Hashtable<PGroupe, Hashtable<PCle, Object>> to){
		to.clear();
		Enumeration<PGroupe> groupes = from.keys();
		while(groupes.hasMoreElements()){
			PGroupe g = groupes.nextElement();
			to.put(g, new Hashtable<PCle, Object>());
			Enumeration<PCle> cles = from.get(g).keys();
			while(cles.hasMoreElements()){
				PCle c = cles.nextElement();
				to.get(g).put(c, from.get(g).get(c));
			}
		}
	}
	private static McdPreferencesManager m_instance;
	private Hashtable<PGroupe, Hashtable<PCle, Object>> m_proprietes;
	private Hashtable<PGroupe, Hashtable<PCle, Object>> m_proprietesPush;
}

