package graph;

import java.awt.Color;
import java.awt.Graphics;

import material.SimpleDrawable;

public class Door2D implements SimpleDrawable {

	int x=0;
	int y=0;
	private int dimX = 0;
	private int dimY = 0;
	
	public Door2D(int x1, int y1, int dimX, int dimY) {
		x=x1;
		y=y1;
		this.dimX = dimX;
		this.dimY = dimY;
	}
	
	
	/**
	 * 
	 */
	public Door2D() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see graph.SimpleDrawable#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g, int width, int height) {
		g.setColor(Color.green);
		g.fillRect(x-1, y-1, width, height);
		g.setColor(Color.green);
		g.drawRect(x-1, y-1, width, height);
	}


	@Override
	public void paint(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(x-1, y-1, dimX, dimY);
		g.setColor(Color.green);
		g.drawRect(x-1, y-1, dimX, dimY);
	}
}