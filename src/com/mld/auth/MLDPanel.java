package com.mld.auth;

import java.io.IOException;
import java.io.StringReader;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;

import com.mcd_graph.auth.McdGraph;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;


@SuppressWarnings("serial")
public class MLDPanel extends JScrollPane {
	private MldLog m_mld;
	private HtmlPanel m_panel;
	public MLDPanel(McdGraph mcd) {
		super();
		m_mld=new MldLog(mcd);
		m_panel = new HtmlPanel();
		getViewport().setView(m_panel);
		UserAgentContext ucontext = new SimpleUserAgentContext();
		SimpleHtmlRendererContext rcontext = new SimpleHtmlRendererContext(m_panel, ucontext);
		DocumentBuilderImpl dbi = new DocumentBuilderImpl(ucontext, rcontext);
		Document document = null;
		try {
			document = dbi.parse(new InputSourceImpl(new StringReader(m_mld.getString()), "http://null.com/"));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		m_panel.setDocument(document, rcontext);
		/*JTextPane p = new JTextPane();
		
		p.setContentType("text/html");
		p.setText(m_mld.getString());
		getViewport().setView(p);*/

		System.setErr(null);
	}
}