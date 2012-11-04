package com.mcd_graph.auth;

import javax.swing.JOptionPane;

import com.version.auth.Updater;

public class main {
	public static void main(String[] args) {
		if(args.length > 0){
			if(!args[0].equals("nothingtodo")){
				Updater.replace(args[0]);
			}
		}
		FenetrePrincipale fenetre = new FenetrePrincipale();
	}
}
