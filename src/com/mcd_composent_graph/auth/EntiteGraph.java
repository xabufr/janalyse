package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class EntiteGraph extends FormeGeometriqueRectangle implements McdComposentGraphique{
	public EntiteGraph(Rectangle r) {
		super(r);
	}

	public void Dessiner(Graphics g) {
		Rectangle rect = getRectangle();
		g.setColor(Color.YELLOW);
		g.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		g.drawLine((int)rect.getX(), ((int)rect.getY()+30), ((int)rect.getX()+(int)rect.getWidth()), ((int)rect.getY()+30));
	}
}
