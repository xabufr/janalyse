package com.mcd_composent_graph.auth;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;


import com.mcd_graph.auth.McdGraph;
import com.mcd_log.auth.Contrainte;

public class ContrainteGraph extends FormeGeometriqueRectangle implements McdComposentGraphique{
	private Contrainte m_contrainte;
	private EntiteGraph m_entiteGraph;
	private RelationGraph m_relationGraph;
	private RelationGraph m_relationGraph2;
	private Boolean m_needUpdateGraphic;
	private McdGraph m_mcd;
	
	public ContrainteGraph(){
		super(new Rectangle());
		m_needUpdateGraphic = true;
		
	}
	
	public void dessiner(Graphics g, Font f, Color c) {
		Dimension dim = new Dimension(0,0);
		Point pos = getPosition();
		FontMetrics font = g.getFontMetrics();
		Graphics2D g2 = (Graphics2D) g;
		float style[] = {5.0f};
		BasicStroke dashed = new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f,
                style,
                0); 
		
		if(m_needUpdateGraphic&&m_mcd!=null){
			m_relationGraph = (RelationGraph) m_mcd.getGraphicComponent(m_contrainte.getRelations().get(0));
			m_relationGraph2 = (RelationGraph) m_mcd.getGraphicComponent(m_contrainte.getRelations().get(1));
			m_entiteGraph = (EntiteGraph) m_mcd.getGraphicComponent(m_contrainte.getEntites().get(0));
			m_needUpdateGraphic=false;
		}
		if(m_relationGraph==null||m_relationGraph2==null||m_entiteGraph==null)
			return;
		
		g.setFont(f);
		g.setColor(Color.BLACK);
		
		dim.width = dim.height = 30;
		
		Point centreContrainte = getPosition();
		Point centreObjet;
		
		if (m_contrainte.getNom().equals("T") || m_contrainte.getNom().equals("+") || m_contrainte.getNom().equals("1")){
			centreContrainte.x += dim.width / 2;
			centreContrainte.y += dim.height / 2;
			centreObjet = m_relationGraph.getPosition();
			centreObjet.x += m_relationGraph.getDimension().width / 2;
			centreObjet.y += m_relationGraph.getDimension().height / 2;
			
			g.drawLine(centreContrainte.x, centreContrainte.y, centreObjet.x, centreObjet.y);
			
			centreObjet = m_relationGraph2.getPosition();
			centreObjet.x += m_relationGraph2.getDimension().width / 2;
			centreObjet.y += m_relationGraph2.getDimension().height / 2;
			
			g.drawLine(centreContrainte.x, centreContrainte.y, centreObjet.x, centreObjet.y);
			
			g2.setStroke(dashed);
			
			centreObjet = m_entiteGraph.getPosition();
			centreObjet.x += m_entiteGraph.getDimension().width / 2;
			centreObjet.y += m_entiteGraph.getDimension().height / 2;
			
			Rectangle obj = m_entiteGraph.getRectangle();
			Line2D l = new Line2D.Double(centreContrainte, centreObjet),
					haut = new Line2D.Double(obj.x, obj.y, obj.x+obj.width, obj.y),
					bas = new Line2D.Double(obj.x, obj.y+obj.height, obj.x+obj.width, obj.y+obj.height),
					gauche = new Line2D.Double(obj.x, obj.y, obj.x, obj.y+obj.height),
					droite = new Line2D.Double(obj.x+obj.width, obj.y, obj.x+obj.width, obj.y+obj.height);
			
			if (l.intersectsLine(haut)){
				centreObjet.y -= obj.height/2; 
			}
			else if (l.intersectsLine(bas)){
				centreObjet.y += obj.height/2;			
			}
			else if (l.intersectsLine(gauche)){
				centreObjet.x -= obj.width/2;
			}
			else if (l.intersectsLine(droite)){
				centreObjet.x += obj.height/2;
			}
			
			g2.draw(new Line2D.Double(centreContrainte.x, centreContrainte.y, centreObjet.x, centreObjet.y));
			g2.setStroke(new BasicStroke(1.0f));
		}
		
		g.setColor(c);
		
		g.fillOval(pos.x, pos.y, dim.width, dim.height);
		g.setColor(Color.BLACK);
		g.drawOval(pos.x, pos.y, dim.width, dim.height);
		
		this.setDimension(dim);
		
		if (m_contrainte.getNom().equals("T") || m_contrainte.getNom().equals("X") || m_contrainte.getNom().equals("1")){
			pos.x += (dim.width/2)-(font.getStringBounds(m_contrainte.getNom(), g).getWidth()/3);
			pos.y += (dim.height/2)+(font.getStringBounds(m_contrainte.getNom(), g).getHeight()/2)-1;
		}
		else if (m_contrainte.getNom().equals("I") || m_contrainte.getNom().equals("=")){
			pos.x += (dim.width/2)-(font.getStringBounds(m_contrainte.getNom(), g).getWidth()/3)+1;
			pos.y += (dim.height/2)+(font.getStringBounds(m_contrainte.getNom(), g).getHeight()/2)-1;
		}
		else if (m_contrainte.getNom().equals("+") || m_contrainte.getNom().equals("1")){
			pos.x += (dim.width/2)-(font.getStringBounds(m_contrainte.getNom(), g).getWidth()/3)-1;
			pos.y += (dim.height/2)+(font.getStringBounds(m_contrainte.getNom(), g).getHeight()/2)-1;
		}
		
		g.drawString(m_contrainte.getNom(), pos.x, pos.y);
	}

	public void setMcd(McdGraph mcd) {
		if(m_mcd!=null)
			m_mcd.removeLogic(m_contrainte);
		m_mcd=mcd;
		m_mcd.registerLogic(m_contrainte, this);
		m_needUpdateGraphic=true;
	}

	public Contrainte getContrainte() {
		return m_contrainte;
	}

	public void setContrainte(Contrainte contrainte) {
		m_contrainte = contrainte;
		m_needUpdateGraphic=true;
	}

	public void dessiner(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	public Boolean isLinkable() {
		return true;
	}
}
