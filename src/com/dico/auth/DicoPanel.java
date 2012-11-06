package com.dico.auth;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import com.mcd_graph.auth.McdGraph;

public class DicoPanel extends JScrollPane{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DicoLog m_dico;
	
	public DicoPanel(McdGraph mcd) {
		super();
		m_dico = new DicoLog(mcd);
		
		JTextPane p = new JTextPane();
		
		p.setContentType("text/html");
		p.setText(m_dico.toString());
		getViewport().setView(p);
		p.setEditable(false);

		System.setErr(null);
		
	}

}
