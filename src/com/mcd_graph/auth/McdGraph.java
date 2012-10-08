package com.mcd_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import com.mcd_composent_graph.auth.EntiteGraph;

public class McdGraph extends JPanel{
	private EntiteGraph entite;
	public McdGraph() {
		entite = new EntiteGraph(30, 50);
		this.setSize(new Dimension(80, 80));
		this.addMouseMotionListener(new mouseMove(this));
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		entite.Dessiner(g);
	}
	
	private class mouseMove implements MouseMotionListener{
		private McdGraph parent;
		
		public mouseMove(McdGraph p){
			parent = p;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			entite.setX(e.getX());
			entite.setY(e.getY());
			parent.repaint();
		}		
	}
	
	private class mouseClick implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			
			
		}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}
	
	}
}
