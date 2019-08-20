package letters;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DetectLetters extends JPanel {
	private BufferedImage image;
	private BufferedImage binary;
	
	public DetectLetters(BufferedImage image) {
		this.image = image;
		binary = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D g2d = binary.createGraphics();
		g2d.drawImage(image, 0, 0, this);
		g2d.dispose();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(binary,0,0,this);
		
		boolean array[][] = new boolean[image.getHeight()][image.getWidth()];

		//Detect edges
		array = detectEdges(array,g);
		for(boolean[] x :array) {
			for(boolean y : x) {
				if(y) {
					System.out.print("@");
				}
				else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}

	}
	
	
	
	
	public boolean[][] detectEdges(boolean array[][], Graphics g){
		for(int i = 1 ; i < binary.getHeight()-1; i++) {
			for(int j = 1; j < binary.getWidth()-1; j++ ) {
				float sum = gv(binary, j-1, i-1) + gv(binary, j, i-1) +gv(binary, j+1, i-1)+
						gv(binary, j-1, i)+ gv(binary, j, i) +gv(binary, j+1, i)+
						gv(binary, j-1, i+1)+gv(binary, j, i+1)+gv(binary, j+1, i+1);
				float val = gv(binary, j, i)*9 - sum;
				if (val > 0) {
					g.setColor(Color.RED);
					array[i][j] = true;
					g.drawRect(j, i, 1, 1);
				}
			}
		}
		return array;
	}
	
	
	
	public int gv(BufferedImage image, int x, int y) {
		return new Color(image.getRGB(x, y)).getRed();
	}

}
