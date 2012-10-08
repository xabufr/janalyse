package com.mcd_graph.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import com.mcd_composent_graph.auth.EntiteGraph;
import com.mcd_composent_graph.auth.McdComposentGraphique;

public class McdGraph extends JPanel{
	private EntiteGraph entite;
	private McdComposentGraphique focus;
	public McdGraph() {
		focus = null;
		entite = new EntiteGraph(new Rectangle(20 , 30, 120, 130));
		this.setSize(new Dimension(80, 80));
		this.addMouseMotionListener(new mouseMove(this));
		this.addMouseListener(new mouseClick());
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
			if (focus != null){
				entite.setX(e.getX());
				entite.setY(e.getY());
				parent.repaint();
			}
		}		
	}
	
	private class mouseClick implements MouseListener{

		public void mouseClicked(MouseEvent e) {
			if (entite.contient(e.getPoint()))
				focus = entite;
			else
				focus = null;
			
		}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {}

		public void mouseReleased(MouseEvent e) {}
	
	}
}
