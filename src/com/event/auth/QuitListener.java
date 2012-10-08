package com.event.auth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.mcd_graph.auth.FenetrePrincipale;

public class QuitListener implements ActionListener{
	private FenetrePrincipale m_parent;
	public QuitListener(FenetrePrincipale parent) {
		m_parent = parent;
	}

	public void actionPerformed(ActionEvent e) {
		m_parent.quitter();
	}

}
