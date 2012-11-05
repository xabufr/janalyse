package com.mcd_graph.auth;

import com.version.auth.Updater;

public class mainClass {
	private static FenetrePrincipale m_fenetre;

	public static void main(String[] args) {
		if(args.length > 0){
			if(!args[0].equals("nothingtodo")){
				Updater.replace(args[0]);
			}
		}
		setFenetre(new FenetrePrincipale());
	}

	public static FenetrePrincipale getFenetre() {
		return m_fenetre;
	}

	public static void setFenetre(FenetrePrincipale m_fenetre) {
		mainClass.m_fenetre = m_fenetre;
	}
}
