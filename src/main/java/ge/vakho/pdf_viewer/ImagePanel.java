package ge.vakho.pdf_viewer;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = -8483797305070521030L;
	
	private Image image;
	private int width;
	private int height;

	public ImagePanel(Image image, int width, int height) {
		this.image = image;
		this.width = width;
		this.height = height;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, width, height, null);
	}

}