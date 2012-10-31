package com.version.auth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.export.auth.Base64.InputStream;

public class Updater {
	static public boolean hasNewVersion(){
		try {
			URL url = new URL(m_urlVersion);
			Document xml = null;
			URLConnection connexion = url.openConnection();
			connexion.setUseCaches(false);
			
			connexion.connect();
			
			java.io.InputStream is = connexion.getInputStream();
			SAXBuilder sxb = new SAXBuilder();
			
			try {
				xml = sxb.build(is);
			} catch (JDOMException e) {
				e.printStackTrace();
			}
			
			Element racine = xml.getRootElement();
			Element version = racine.getChild("version");
			int iversion = Integer.parseInt(version.getText());
			return iversion > m_currentVersion;
			
		} catch (MalformedURLException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	static public void update(){
		
	}
	static private int m_currentVersion;
	static private String m_urlVersion="https://www.assembla.com/code/janalyse/git/nodes/master/src/com/version/auth/version.xml";

}
