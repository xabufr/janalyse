package com.preferences_mcd_logique.auth;

import java.awt.Font;
import java.util.Hashtable;

public class McdPreferencesManager {

	private McdPreferencesManager() {
		m_proprietes = new Hashtable<PGroupe, Hashtable<PCle, Object>>();
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
	
	private static McdPreferencesManager m_instance;
	private Hashtable<PGroupe, Hashtable<PCle, Object>> m_proprietes;
}

