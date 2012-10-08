package com.mcd_graph.auth;
import java.awt.Dimension;
import javax.swing.JFrame;


public class Fenetre extends JFrame{
	public Fenetre(){
		this.setTitle("JAnalyse");
		this.setMinimumSize(new Dimension(800, 480));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setVisible(true);
	}
}
