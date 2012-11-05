package com.preferences_mcd_logique.auth;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.UIManager;

public class McdPreferencesManager {

	private McdPreferencesManager() {
		m_proprietes = new Hashtable<PGroupe, Hashtable<PCle, Object>>();
		m_proprietesPush = new Hashtable<PGroupe, Hashtable<PCle,Object>>();
		loadDefault();
		load();
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
	public void save(){
		Enumeration<PGroupe> groupes = m_proprietes.keys();
		while(groupes.hasMoreElements()){
			PGroupe g = groupes.nextElement();
			Enumeration<PCle> cles = m_proprietes.get(g).keys();
			while(cles.hasMoreElements()){
				PCle c = cles.nextElement();
				saveObject(g.toString()+":"+c.toString(), m_proprietes.get(g).get(c));
			}
		}
	}
	public void load(){
		for(PGroupe g  : PGroupe.values()){
			for(PCle c : PCle.values()){
				Object o = loadObject(g.toString()+":"+c.toString());
				if(o!=null){
					if(!m_proprietes.containsKey(g))
						m_proprietes.put(g, new Hashtable<PCle, Object>());
					if(o instanceof Font){
						Font f = (Font) o;
						f = PoliceManager.get().getFont(f); //Éviter les doublons
						m_proprietes.get(g).put(c, f);
					}
					else{
						m_proprietes.get(g).put(c, o);
					}
				}
			}
		}
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
	private void saveObject(String key, Object o){
		key=key.replace("_", "-");
		byte objectParts[][] = null;
		try {
			objectParts = splitByteArray(object2bytes(o));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		Preferences node = m_preferences.node(key);
		for(int i=0;i<objectParts.length;++i)
			node.putByteArray(""+i, objectParts[i]);
		node.putInt("partNum", objectParts.length);
	}
	private Object loadObject(String key) {
		key=key.replace("_", "-");
		try {
			if(!m_preferences.nodeExists(key))
				return null;
		} catch (BackingStoreException e1) {
			e1.printStackTrace();
			return null;
		}
		Preferences node = m_preferences.node(key);
		int partNum = node.getInt("partNum", 0);
		if(partNum == 0)
			return null;
		byte parts[][] = new byte[partNum][];
		for(int i=0;i<partNum;++i){
			parts[i] = node.getByteArray(""+i, null);
		}
		try {
			return byte2object(combineBytesArray(parts));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	private byte[] object2bytes(Object o) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		return baos.toByteArray();
	}
	private Object byte2object(byte[] array) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bais = new ByteArrayInputStream(array);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object o = ois.readObject();
		return o;
	}
	private byte[][] splitByteArray(byte[] bytes){
		int partLength = (int) ((int)Preferences.MAX_VALUE_LENGTH*0.75);
		int nbPart = (bytes.length + partLength - 1) / partLength;
		byte parts[][] = new byte[nbPart][];
		for(int i=0;i<nbPart;++i){
			int startByte=i*partLength;
			int endByte = startByte + partLength;
			if(endByte>bytes.length) endByte = bytes.length;
			int length = endByte-startByte;
			parts[i] = new byte[length];
			System.arraycopy(bytes, startByte, parts[i], 0, length);
		}
		return parts;
	}
	private byte[] combineBytesArray(byte[][] parts){
		int length=0;
		for(int i=0;i<parts.length;++i)
			length+=parts[i].length;
		byte bytes[] = new byte[length];
		int curseur = 0;
		for(int i=0;i<parts.length;++i){
			System.arraycopy(parts[i], 0, bytes, curseur, parts[i].length);
			curseur+=parts[i].length;
		}
		return bytes;
	}
	private static McdPreferencesManager m_instance;
	private Hashtable<PGroupe, Hashtable<PCle, Object>> m_proprietes;
	private Hashtable<PGroupe, Hashtable<PCle, Object>> m_proprietesPush;
	final private Preferences m_preferences = Preferences.userNodeForPackage(getClass());
	
	
	//Rien de bien interessant au delà, juste du travail de chinois...
	public void loadDefault(){
		setFont(PGroupe.HERITAGE, PCle.FONT, "Serif", Font.PLAIN, 10);
		setFont(PGroupe.HERITAGE, PCle.FONT_FOCUS, "Serif", Font.PLAIN, 10);
		set(PGroupe.HERITAGE,  PCle.COLOR, Color.GREEN);
		set(PGroupe.HERITAGE,  PCle.COLOR_FOCUS, Color.WHITE);
		set(PGroupe.HERITAGE, PCle.COLOR_2, Color.LIGHT_GRAY);
		set(PGroupe.HERITAGE, PCle.COLOR_2_FOCUS, Color.GREEN);
		set(PGroupe.HERITAGE, PCle.COLOR_CONTOUR, Color.BLACK);
		set(PGroupe.HERITAGE, PCle.COLOR_CONTOUR_FOCUS, Color.RED);
		set(PGroupe.HERITAGE, PCle.COLOR_LINE, Color.BLACK);
		set(PGroupe.HERITAGE, PCle.FONT_COLOR, Color.BLACK);
		set(PGroupe.HERITAGE, PCle.FONT_COLOR_FOCUS, Color.BLACK);
		set(PGroupe.HERITAGE, PCle.OMBRE, false);
		set(PGroupe.HERITAGE, PCle.OMBRE_COLOR, Color.LIGHT_GRAY);
		set(PGroupe.HERITAGE, PCle.GRADIANT_COLOR, true);
		
		
		setFont(PGroupe.CONTRAINTE, PCle.FONT, "Serif", Font.PLAIN, 10);
		setFont(PGroupe.CONTRAINTE, PCle.FONT_FOCUS, "Serif", Font.PLAIN, 10);
		set(PGroupe.CONTRAINTE,  PCle.COLOR, Color.CYAN);
		set(PGroupe.CONTRAINTE,  PCle.COLOR_FOCUS, Color.WHITE);
		set(PGroupe.CONTRAINTE, PCle.COLOR_CONTOUR, Color.BLACK);
		set(PGroupe.CONTRAINTE, PCle.COLOR_CONTOUR_FOCUS, Color.RED);
		set(PGroupe.CONTRAINTE, PCle.COLOR_LINE, Color.BLACK);
		set(PGroupe.CONTRAINTE, PCle.FONT_COLOR, Color.BLACK);
		set(PGroupe.CONTRAINTE, PCle.FONT_COLOR_FOCUS, Color.BLACK);
		set(PGroupe.CONTRAINTE, PCle.OMBRE, false);
		set(PGroupe.CONTRAINTE, PCle.OMBRE_COLOR, Color.LIGHT_GRAY);
		
		
		setFont(PGroupe.CARDINALITE, PCle.FONT, "Serif", Font.PLAIN, 10);
		setFont(PGroupe.CARDINALITE, PCle.FONT_FOCUS, "Serif", Font.PLAIN, 10);
		set(PGroupe.CARDINALITE, PCle.FONT_COLOR, Color.BLACK);
		set(PGroupe.CARDINALITE, PCle.FONT_COLOR_FOCUS, Color.BLACK);
		set(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR, Color.BLACK);
		set(PGroupe.CARDINALITE, PCle.COLOR_CONTOUR_FOCUS, Color.RED);
		
		setFont(PGroupe.RELATION, PCle.FONT, "Serif", Font.PLAIN, 10);
		setFont(PGroupe.RELATION, PCle.FONT_NOM, "Serif", Font.PLAIN, 10);
		setFont(PGroupe.RELATION, PCle.FONT_FOCUS, "Serif", Font.PLAIN, 10);
		setFont(PGroupe.RELATION, PCle.FONT_NOM_FOCUS, "Serif", Font.PLAIN, 10);
		set(PGroupe.RELATION, PCle.FONT_COLOR, Color.GRAY);
		set(PGroupe.RELATION, PCle.FONT_COLOR_FOCUS, Color.BLACK);
		set(PGroupe.RELATION, PCle.FONT_NOM_COLOR, Color.BLACK);
		set(PGroupe.RELATION, PCle.FONT_NOM_COLOR_FOCUS, Color.BLACK);
		set(PGroupe.RELATION, PCle.COLOR, Color.GREEN);
		set(PGroupe.RELATION, PCle.COLOR_2, Color.LIGHT_GRAY);
		set(PGroupe.RELATION, PCle.COLOR_FOCUS, Color.WHITE);
		set(PGroupe.RELATION, PCle.COLOR_2_FOCUS, Color.GREEN);
		set(PGroupe.RELATION, PCle.COLOR_CONTOUR, Color.BLACK);
		set(PGroupe.RELATION, PCle.COLOR_CONTOUR_FOCUS, Color.RED);
		set(PGroupe.RELATION, PCle.OMBRE, false);
		set(PGroupe.RELATION, PCle.OMBRE_COLOR, Color.LIGHT_GRAY);
		set(PGroupe.RELATION, PCle.CIF, true);
		set(PGroupe.RELATION, PCle.GRADIANT_COLOR, true);
		
		setFont(PGroupe.ENTITE, PCle.FONT, "Serif", Font.PLAIN, 10);
		setFont(PGroupe.ENTITE, PCle.FONT_NOM, "Serif", Font.PLAIN, 12);
		setFont(PGroupe.ENTITE, PCle.FONT_FOCUS, "Serif", Font.PLAIN, 10);
		setFont(PGroupe.ENTITE, PCle.FONT_NOM_FOCUS, "Serif", Font.PLAIN, 12);
		set(PGroupe.ENTITE,  PCle.COLOR, Color.GREEN);
		set(PGroupe.ENTITE,  PCle.COLOR_2, Color.LIGHT_GRAY);
		set(PGroupe.ENTITE,  PCle.COLOR_FOCUS, Color.LIGHT_GRAY);
		set(PGroupe.ENTITE,  PCle.COLOR_2_FOCUS, Color.GREEN);
		set(PGroupe.ENTITE, PCle.COLOR_CONTOUR, Color.BLACK);
		set(PGroupe.ENTITE, PCle.COLOR_CONTOUR_FOCUS, Color.RED);
		set(PGroupe.ENTITE, PCle.FONT_COLOR, Color.GRAY);
		set(PGroupe.ENTITE, PCle.FONT_COLOR_FOCUS, Color.BLACK);
		set(PGroupe.ENTITE, PCle.FONT_NOM_COLOR, Color.BLACK);
		set(PGroupe.ENTITE, PCle.FONT_NOM_COLOR_FOCUS, Color.RED);
		set(PGroupe.ENTITE, PCle.OMBRE, false);
		set(PGroupe.ENTITE, PCle.OMBRE_COLOR, Color.LIGHT_GRAY);
		set(PGroupe.ENTITE, PCle.GRADIANT_COLOR, true);
		
		set(PGroupe.PROPRIETE, PCle.SCHEMA, "%[0e1]%[1E]_%p");
		
		set(PGroupe.SELECTEUR, PCle.COLOR_CONTOUR, Color.BLACK);
		set(PGroupe.SELECTEUR, PCle.FORME_ARRONDIE, true);
		set(PGroupe.SELECTEUR, PCle.LIGNE_CONTINUE, false);
		
		set(PGroupe.ETAT, PCle.SAVE, new ArrayList<String>());

		set(PGroupe.HTML, PCle.CSS, "h1, div {\ntext-align: center;\n}\ndiv {\n border: 1px solid black;\n    margin-bottom: -1px;\n    padding: 1px;\n}\ndiv.entite p \n{\n    background-color: black;\n    margin: 0;\n    color: white;\n}\n.mld. cleEtrangere \n{\n    border-bottom: 1px black dashed;\n}\ndiv.entite \n{\n    margin-bottom: 5px;\n    background-color: silver;\n}\n .mld. clePrimaire{\n text-decoration: underline; \n}");
	
		set(PGroupe.COMMENTAIRE, PCle.SHOW, true);
		setFont(PGroupe.COMMENTAIRE, PCle.FONT, "Serif", Font.PLAIN, 10);
		set(PGroupe.COMMENTAIRE,  PCle.COLOR, Color.CYAN);
		set(PGroupe.COMMENTAIRE,  PCle.COLOR_2, Color.LIGHT_GRAY);
		set(PGroupe.COMMENTAIRE, PCle.COLOR_CONTOUR, Color.BLACK);
		set(PGroupe.COMMENTAIRE, PCle.FONT_COLOR, Color.BLACK);
		set(PGroupe.COMMENTAIRE, PCle.OMBRE, true);
		set(PGroupe.COMMENTAIRE, PCle.OMBRE_COLOR, Color.LIGHT_GRAY);
		set(PGroupe.COMMENTAIRE, PCle.GRADIANT_COLOR, true);
		
		set(PGroupe.GUI, PCle.LOOK, UIManager.getSystemLookAndFeelClassName());
	}
}

