/**
 * 
 */
package graph;

import java.awt.Color;
import java.awt.Graphics;

import material.SimpleDrawable;

/**
 * @author Bairon
 *
 */
public class Point2D implements SimpleDrawable {

	int x=0;
	int y=0;
	
	public Point2D(int x1, int y1) {
		x=x1;
		y=y1;
	}
	
	
	/**
	 * 
	 */
	public Point2D() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see graph.SimpleDrawable#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.blue);
		g.fillOval(x-1, y-1, 10, 10);
		g.setColor(Color.black);
		g.drawOval(x-1, y-1, 10, 10);
	}


	@Override
	public void paint(Graphics g, int width, int height) {
		g.setColor(Color.blue);
		g.fillOval(x-1, y-1, width, height);
		g.setColor(Color.black);
		g.drawOval(x-1, y-1, width, height);
	}

}
