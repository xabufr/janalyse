package com.mld.auth;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import com.mcd_graph.auth.McdGraph;

@SuppressWarnings("serial")
public class MLDPanel extends JScrollPane {
	private MldLog m_mld;
	public MLDPanel(McdGraph mcd) {
		super();
		m_mld=new MldLog(mcd);

		JTextPane p = new JTextPane();
		
		p.setContentType("text/html");
		if(m_mld.isValid())
			p.setText(m_mld.getString());
		else
			p.setText("<p>MCD invalide :</p>"+m_mld.getErreurs());
		getViewport().setView(p);

		System.setErr(null);
	}
}