package com.version.auth;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.Dimension;

public class FenetreMiseAJour extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JProgressBar m_progressBar;
	private Timer m_timer;

	/**
	 * Create the dialog.
	 */
	public FenetreMiseAJour() {
		setMinimumSize(new Dimension(0, 120));
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		final Thread downloadThread = new Thread(){
			public String fichier=null;
			public void run() {
				fichier=Updater.downloadUpdate();
			}
			public String toString(){
				return fichier;
			}
		};
		
		setBounds(100, 100, 444, 120);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[114px,grow,fill]", "[19px][]"));
		{
			JLabel lblTlchargementDeLa = new JLabel("Téléchargement de la nouvelle version");
			contentPanel.add(lblTlchargementDeLa, "cell 0 0");
		}
		{
			m_progressBar = new JProgressBar();
			m_progressBar.setStringPainted(true);
			m_progressBar.setValue(0);
			m_timer = new Timer();

			m_timer.schedule(new TimerTask() {
				public void run() {
					m_progressBar.setValue(Updater.getPercentComplete());
					if(!Updater.isStarted()){
						m_timer.cancel();
						String fichier = downloadThread.toString();
						if(fichier!=null){
							Updater.restart(fichier);
							FenetreMiseAJour.this.dispose();
						}
						else{							
							JOptionPane.showMessageDialog(FenetreMiseAJour.this, "Téléchargement interrompu", "Erreur de téléchargement", JOptionPane.ERROR_MESSAGE);
						}
						FenetreMiseAJour.this.dispose();
					}
				}
			}, 1000, 100);
			contentPanel.add(m_progressBar, "cell 0 1");
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Annuler");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener() {					
					public void actionPerformed(ActionEvent arg0) {
						Updater.stop();
					}
				});
				buttonPane.setLayout(new MigLayout("", "[89px,grow,fill]", "[25px]"));
				buttonPane.add(cancelButton, "cell 0 0,alignx left,aligny top");
			}
		}
		addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent arg0) {				
			}
			public void windowIconified(WindowEvent arg0) {				
			}
			public void windowDeiconified(WindowEvent arg0) {
			}
			public void windowDeactivated(WindowEvent arg0) {
			}
			public void windowClosing(WindowEvent arg0) {
			}
			public void windowClosed(WindowEvent arg0) {
				Updater.stop();
			}
			public void windowActivated(WindowEvent arg0) {
			}
		});
		downloadThread.start();
	}
}
