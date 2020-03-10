package material;

import java.awt.Graphics;

public interface SimpleDrawable {
	void paint(Graphics g);
	
	void paint(Graphics g, int width, int height);
}
