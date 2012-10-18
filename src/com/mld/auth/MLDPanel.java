package com.mld.auth;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Cardinalite;
import com.mcd_log.auth.Entite;
import com.mcd_log.auth.Propriete;
import com.mcd_log.auth.Relation;

import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;

public class MLDPanel extends JPanel {
	private MldLog m_mld;
	public MLDPanel(McdGraph mcd) {
		m_mld=new MldLog(mcd);
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JTextPane textPane = new JTextPane();
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		JScrollPane sp = new JScrollPane(textPane);
		add(sp, "cell 0 0,grow");
		textPane.setText(m_mld.getString());
	}
}
