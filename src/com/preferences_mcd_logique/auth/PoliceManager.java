package com.preferences_mcd_logique.auth;

import java.awt.Font;
import java.util.Hashtable;

public class PoliceManager {

	private PoliceManager() {
		// TODO Auto-generated constructor stub
		this.m_polices = new Hashtable<String, Font> ();
	}
	public static PoliceManager getInstance(){
		if(m_instance==null)
		{
			synchronized(PoliceManager.class){
				if(m_instance==null){
					m_instance = new PoliceManager();
				}
			}
		}
		return m_instance;
	}
	public static PoliceManager get(){
		return getInstance();
	}
	
	public Font getFont(String policeName, int style, int size){
		Font font;
		String key = policeName + "-" + String.valueOf(style) + "-" + String.valueOf(size);
		if(m_polices.containsKey(key))
			font = m_polices.get(key);
		else
		{
			font = new Font(policeName, style, size);
			m_polices.put(key, font);
		}
		return font;
	}
	public Font getFont(Font f){
		String fontName=f.getFontName();
		int style = f.getStyle();
		int size = f.getSize();
		return getFont(fontName,style,size);
	}
	public void clearFonts(){
		m_polices.clear();
	}
	public void eraseFont(String policeName, int style, int size){
		String key = policeName + "-" + String.valueOf(style) + "-" + String.valueOf(size);
		m_polices.remove(key);
	}
	public Boolean contains(Font f){
		String key = f.getFontName() + "-" + String.valueOf(f.getStyle()) + "-" + String.valueOf(f.getSize());
		return m_polices.containsKey(key);
	}
	Hashtable<String, Font> m_polices;
	
	private static PoliceManager m_instance;
}
