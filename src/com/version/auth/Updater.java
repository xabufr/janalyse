package com.version.auth;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.utils.auth.Utils;

public class Updater {
	static private Document getVersionDocument(){
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
				return null;
			}
			return xml;
			
		} catch (MalformedURLException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	static public boolean hasNewVersion(){
		Document xml = getVersionDocument();
		if(xml==null)
			return false;
		Element racine = xml.getRootElement();
		Element version = racine.getChild("numero");
		int iversion = Integer.parseInt(version.getText());
		return iversion > m_currentVersion;
	}
	static public String downloadUpdate(){
		start();
		Document xml = getVersionDocument();
		if(xml==null)
			return null;
		try {
			URL url = new URL(xml.getRootElement().getChild("url").getText());

			URLConnection connexion = url.openConnection();
			connexion.setUseCaches(false);
			int length = connexion.getContentLength();
			if(length==-1)
				throw new IOException("Fichier vide");
			
			java.io.InputStream is = new BufferedInputStream(connexion.getInputStream());
			byte data[] = new byte[length];
			
			int currByte=0, deplacement = 0;
			while(deplacement<length){
				currByte = is.read(data, deplacement, data.length-deplacement);
				if(currByte==-1||!isStarted())
					break;
				deplacement+=currByte;
				setPercentComplete(deplacement*100/length);
			}

			if(deplacement==length){
				String fichier = File.createTempFile("janalyse", null).getAbsolutePath();
	
				FileOutputStream destinationFile = new FileOutputStream(fichier);
				destinationFile.write(data);
				destinationFile.flush();
				destinationFile.close();
				
				String checksum = xml.getRootElement().getChildText("checksum");
				if(checksum.equals(Utils.checksum(new File(fichier)))){
					stop();
					return fichier;
				}
			}
			stop();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		stop();
		return null;
	}
	static public void setPercentComplete(int p){
		synchronized (m_sync) {
			m_avancement = p;
		}
	}
	static public int getPercentComplete(){
		synchronized (m_sync) {
			return m_avancement;
		}
	}
	static public void restart(String to){
		restart(to, true);
	}
	static public void restart(String to, boolean update){
		String currentPath =  getJarPath();
		try {
			if(update)
				launch(to, currentPath, true);
			else
				launch(to, "", false);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.exit(0);
	}
	static public String getJarPath(){
		String path = new File("").getAbsolutePath();
		path +=File.separator+System.getProperty("java.class.path");
		if(System.getProperty("java.class.path").startsWith("/"))	//Systèmes linux
			return System.getProperty("java.class.path");
		if(System.getProperty("java.class.path").length()>3&&	//Windows
				System.getProperty("java.class.path").startsWith(":\\", 1))
			return System.getProperty("java.class.path");
		return path;
	}
	static public void replace(String to){
		java.io.InputStream in;
		OutputStream out;
		int c=0;
		try {
			in=new FileInputStream(getJarPath());
			out=new FileOutputStream(to);
			while ((c=in.read())!=-1) out.write(c);
			out.flush();
			in.close();
			out.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		restart(to, false);
	}

	public static void launch(String jarFileName, String from, boolean replace) throws IOException {
		boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
 
		// On récupère le chemin depuis "java.home" :
		File java = new File(System.getProperty("java.home"),
			isWindows ? "bin/javaw.exe" : "bin/java" );
 
		// On lance le jar :
		Process p = null;
		if(replace)
			p = new ProcessBuilder( java.getAbsolutePath(), "-jar", jarFileName, "--internal-update",from).start();
		else
			p = new ProcessBuilder( java.getAbsolutePath(), "-jar", jarFileName, "nothingtodo",from).start();
		p.getInputStream().close();
		p.getOutputStream().close();
		p.getErrorStream().close();
 
	}
	public static int getVersionMinor(){
		return m_currentVersion%10;
	}
	public static int getVersionMajor(){
		return (int) m_currentVersion/100;
	}
	public static int getVersionMedium(){
		return (int) (m_currentVersion%100)/10;
	}
	public static String getVersionString(){
		String version = String.valueOf(getVersionMajor());
		if(getVersionMinor()!=0){
			version += "."+getVersionMedium();
			version += "."+getVersionMinor();
		}
		else if(getVersionMedium()!=0){
			version += "."+getVersionMedium();
		}
		return version;
	}
	public static void start(){
		synchronized (m_sync) {
			m_started=true;
		}
	}
	public static void stop(){
		synchronized (m_sync) {
			m_started=false;
		}
	}
	public static boolean isStarted(){
		synchronized (m_sync) {
			return m_started;
		}
	}
	static private boolean m_started;
	static private Object m_sync = new Object();
	static private int m_avancement;
	static private int m_currentVersion = 27;
	static private String m_urlVersion="https://www.assembla.com/code/janalyse/git/node/blob/master/version.xml";
}
