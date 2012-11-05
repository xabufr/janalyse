package com.export.auth;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.mcd_graph.auth.McdGraph;
import com.utils.auth.Utils;

public class ExportPng {

	public static void ExporterMcd(McdGraph mcd){
		BufferedImage outImage = getImage(mcd);
		File outFile = Utils.getFile4Save("png");
		if(outFile==null)
			return;
		try {
			ImageIO.write(outImage, "png", outFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static BufferedImage getImage(McdGraph mcd){
		BufferedImage outImage = new BufferedImage(
				mcd.getPreferredSize().width+1, mcd.getPreferredSize().height+1, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphic = outImage.createGraphics();
		mcd.paint(graphic);
		return outImage;
	}
}
