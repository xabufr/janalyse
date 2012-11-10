package com.utils.auth;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class Utils {
	public static String getExtension(File f){
		String ext=f.getName();
		
		int index = ext.lastIndexOf(".");
		String ret = null;
		if(index>0&&index<ext.length()-1){
			ret = ext.substring(index+1).toLowerCase();
		}
		
		return ret;
	}
	public static double angle(Point p1, Point p2, Point p3){
		double a, a1, a2;
		
		a1 = Math.atan2(p1.y-p3.y, p1.x-p3.x);
		a2 = Math.atan2(p2.y-p3.y, p2.x-p3.x);
		a = a2 - a1;
		
		return a;
	}
	public static File getFile4Save(String type){
		ArrayList<String> params = new ArrayList<String>();
		params.add(type);
		return getFile(params, true);
	}
	public static File getFile4Load(String type){
		ArrayList<String> params = new ArrayList<String>();
		params.add(type);
		return getFile(params, false);
	}
	private static File getFile(final ArrayList<String> types, boolean save){
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter(){
			public boolean accept(File f) {
				if(types.isEmpty())
					return true;
				if(f.isDirectory())
					return true;
				String ext = getExtension(f);
				if(ext==null)
					return false;
				return types.contains(ext);
			}
			public String getDescription() {
				if(types.isEmpty())
					return "*";
				String ret="";
				boolean first=true;
				for(String s : types){
					if(!first)
						ret+=", ";
					first=false;
					ret+="*."+s;
				}
				return ret;
			}
		});
		if(save){
			if(chooser.showSaveDialog(null)!=JFileChooser.APPROVE_OPTION){
				return null;
			}
		}
		else{
			if(chooser.showOpenDialog(null)!=JFileChooser.APPROVE_OPTION){
				return null;
			}
		}
		File f = chooser.getSelectedFile();
		
		if(save&&!types.isEmpty()&&!types.contains(getExtension(f))){
			f = new File(f.getAbsolutePath()+"."+types.get(0));
		}
		if(!save&&!f.exists()||f.isDirectory())
			return null;
		if(save&&f.exists()){
			if(JOptionPane.showConfirmDialog(null, "Le fichier existe, écraser ?")!=JOptionPane.OK_OPTION)
				return null;
		}
		return f;
	}
	static public String checksum(File f){
		
		try {
			FileInputStream is = new FileInputStream(f);
			MessageDigest digest = MessageDigest.getInstance("MD5");
			
			int length = (int) f.length();
			byte[] bytes = new byte[length];
			new BufferedInputStream(is).read(bytes, 0, length);
			is.close();

			byte[] bDigest = digest.digest(bytes);
			String sDigest = "",
					HEX_DIGIT="0123456789abcdef";
			for(int i=0;i<bDigest.length;++i){
				int b = (int) (bDigest[i] & 0xFF);
				sDigest+=HEX_DIGIT.charAt(b>>>4);
				sDigest+=HEX_DIGIT.charAt(b & 0xF);
			}
			return sDigest;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
