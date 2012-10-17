package com.ui_help.auth;

import javax.swing.JDialog;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("serial")
public class APropos extends JDialog {
	public APropos() {
		
		KeyListener keylist = new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
					APropos.this.setVisible(false);
			}
			public void keyReleased(KeyEvent e) {				
			}
			public void keyTyped(KeyEvent e) {
				
			}
		};
		MouseListener mouselist = new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {				
			}
			public void mousePressed(MouseEvent arg0) {	
				APropos.this.setVisible(false);
			}
			public void mouseExited(MouseEvent arg0) {				
			}
			public void mouseEntered(MouseEvent arg0) {				
			}
			public void mouseClicked(MouseEvent arg0) {				
			}
		};
		setTitle("À Propos");
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[grow]", "[grow][]"));
		
		JTextPane txtpnJanalyseCrateurDe = new JTextPane();
		txtpnJanalyseCrateurDe.setEditable(false);
		txtpnJanalyseCrateurDe.setContentType("text/html");
		txtpnJanalyseCrateurDe.setText("JAnalyse, créateur de MCD Merise.\n<br />\nÉcrit par:<br />\n<ul>\n<li>Alexandre Rame(<a href=\"mailto:aramel@epsi.fr\">aramel@epsi.fr</a>)</li>\n<li>Thomas Loubiou(<a href=\"mailto:tloubiou@epsi.fr\">tloubiou@epsi.fr</a>)</li>\n</ul>\nVous pouvez consulter l'avancement du projet sur:<br />\n<a href=\"https://www.assembla.com/code/janalyse/\">https://www.assembla.com/code/janalyse/</a>");
		panel.add(txtpnJanalyseCrateurDe, "cell 0 0,grow");
		
		txtpnJanalyseCrateurDe.addHyperlinkListener(new HyperlinkListener() {
			
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if(!Desktop.isDesktopSupported())
					return;
				if(e.getEventType()!=HyperlinkEvent.EventType.ACTIVATED)
					return;
				try {
					Desktop.getDesktop().browse(new URI(e.getURL().getPath()));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JLabel lblEscOuClick = new JLabel("Esc. ou click pour quitter");
		lblEscOuClick.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEscOuClick.setForeground(Color.GRAY);
		panel.add(lblEscOuClick, "cell 0 1,alignx right");
		setFocusable(true);
		
		addKeyListener(keylist);
		addMouseListener(mouselist);
		txtpnJanalyseCrateurDe.addKeyListener(keylist);
		panel.addKeyListener(keylist);
		panel.addMouseListener(mouselist);
		lblEscOuClick.addKeyListener(keylist);
		lblEscOuClick.addMouseListener(mouselist);
	}

}
