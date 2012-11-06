package com.mcd_graph.auth;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.preferences_mcd_logique.auth.McdPreferencesManager;
import com.preferences_mcd_logique.auth.PCle;
import com.preferences_mcd_logique.auth.PGroupe;
import com.version.auth.Updater;

public class mainClass {
	private static FenetrePrincipale m_fenetre;

	public static void main(String[] args) {
		if(args.length > 0){
			if(!args[0].equals("nothingtodo")){
				Updater.replace(args[0]);
			}
		}
		try {
			UIManager.setLookAndFeel((String)McdPreferencesManager.getInstance().get(PGroupe.GUI, PCle.LOOK));
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
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
