package com.ui_help.auth;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;

public class HelpDialog {

	public HelpDialog() {
		
	}
	public void show(ActionEvent e){
		HelpSet hs = getHelpSet("JanalyseHelp");
		HelpBroker hb = hs.createHelpBroker();
		new CSH.DisplayHelpFromSource(hb).actionPerformed(e);
	}
	private HelpSet getHelpSet(String name){
		URL url = HelpSet.findHelpSet(this.getClass().getClassLoader(), name);
		try {
			return new HelpSet(null, url);
		} catch (HelpSetException e) {
			return null;
		}
	}

}
