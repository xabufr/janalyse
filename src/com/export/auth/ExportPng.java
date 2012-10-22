package com.export.auth;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.mcd_graph.auth.McdGraph;

public class ExportPng {

	public static void ExporterMcd(McdGraph mcd){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileFilter(){
			public boolean accept(File arg0) {
				if(arg0.isDirectory())
					return true;
				String ext = getExtension(arg0);
				if(ext==null)
					return false;
				if(ext.equals("png"))
					return true;
				return false;
			}
	
			public String getDescription() {
				return "PNG Only";
			}
			
		});
		if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
			BufferedImage outImage = getImage(mcd);
			
			File outFile = chooser.getSelectedFile();
			if(getExtension(outFile)==null||!getExtension(outFile).equals("png"))
				outFile = new File(outFile.getAbsolutePath()+".png");
			try {
				ImageIO.write(outImage, "png", outFile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	public static BufferedImage getImage(McdGraph mcd){
		BufferedImage outImage = new BufferedImage(
				mcd.getPreferredSize().width+1, mcd.getPreferredSize().height+1, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphic = outImage.createGraphics();
		mcd.paint(graphic);
		return outImage;
	}
	public static String getExtension(File f){
		String ext=f.getName();
		
		int index = ext.lastIndexOf(".");
		String ret = null;
		if(index>0&&index<ext.length()-1){
			ret = ext.substring(index+1).toLowerCase();
		}
		
		return ret;
	}
}
