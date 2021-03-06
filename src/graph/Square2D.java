package graph;

import material.SimpleDrawable;

import java.awt.*;

public class Square2D implements SimpleDrawable {

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	int x=0;
	int y=0;
	int dimX = 0;
	int dimY = 0;
	
	public Square2D(int x1, int y1, int dimX, int dimY) {
		x=x1;
		y=y1;
		this.dimX = dimX;
		this.dimY = dimY;
	}
	
	
	/**
	 * 
	 */
	public Square2D() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see graph.SimpleDrawable#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g, int width, int height) {
		g.setColor(Color.gray);
		g.fillRect(x-1, y-1, width, height);
		g.setColor(Color.gray);
		g.drawRect(x-1, y-1, width, height);
	}


	@Override
	public void paint(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(x-1, y-1, dimX, dimY);
		g.setColor(Color.gray);
		g.drawRect(x-1, y-1, dimX, dimY);
	}

    public void resize(int i, int i1, int dimX, int dimY) {
		this.x = i;
		this.y = i1;
		this.dimX = dimX;
		this.dimY = dimY;
    }
}
