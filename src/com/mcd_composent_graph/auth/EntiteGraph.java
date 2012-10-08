package com.mcd_composent_graph.auth;

import java.awt.Color;
import java.awt.Graphics;

public class EntiteGraph extends Object implements McdComposentGraphique{
	private int x;
	private int y;
	
	public EntiteGraph(int x, int y) {
		setX(x);
		setY(y);
	}

	public void Dessiner(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillRect(x, y, 120, 130);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, 120, 130);
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
}
