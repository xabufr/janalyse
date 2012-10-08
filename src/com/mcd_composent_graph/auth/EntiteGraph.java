package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class EntiteGraph extends FormeGeometriqueRectangle implements McdComposentGraphique{

	private int x;
	private int y;
	private int width;
	private int height;
	
	public EntiteGraph(Rectangle r) {
		super(r);
		x = r.x;
		y = r.y;
		width = r.width;
		height = r.height;
	}

	public void Dessiner(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		g.drawLine(x, (y+30), (x+120), (y+30));
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int w) {
		this.width = w;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int h) {
		this.height = h;
	}
}
