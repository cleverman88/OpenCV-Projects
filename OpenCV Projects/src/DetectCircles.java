import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DetectCircles extends JPanel{
	//Change these to find different sized circles
	final static int LB = 30;
	final static int UB = 50;

	//Change this to find stuff that are not as circular
	final static int HOWCIRCLE = 100;


	BufferedImage image = null;
	BufferedImage binary = null;

	public DetectCircles() throws IOException {
		image = ImageIO.read((new File("Images/download.jpg")));
		binary = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
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

		//Detect circles
		//Looks for red pixels around a circle of the middle
		boolean array2[][] = new boolean[image.getHeight()][image.getWidth()];
		array2 = reset(array2, array);
		for(int RADIUS = LB; RADIUS <= UB; RADIUS++) {
			System.out.println("On radius "+ RADIUS);


			for(int y = 0; y < binary.getHeight()-1; y++) {
				for(int x = 0; x < binary.getWidth()-1; x++) {
					Set<Integer> vals = new HashSet<Integer>();
					int counter = 0;
					double angle,x1,y1;
					boolean carry = true;
					for(double i = 0; i <= 360; i+= 1) {
						angle = i;
						x1 = RADIUS * (Math.cos(angle * Math.PI/180));
						y1 = RADIUS * (Math.sin(angle * Math.PI/180));


						if(Math.round(x+x1) > binary.getWidth()-1 || Math.round(x+x1) < 0 || Math.round(y+y1) > Math.round(binary.getHeight()-1) || Math.round(y + y1) < 0) {
							carry = false;
							array2 = reset(array2, array);
							break;
						}
						vals.add((int)Math.round(x+x1));
						vals.add((int)Math.round(y+y1));
						if(array2[(int)Math.round(y+y1)][(int)Math.round(x+x1)]) {
							array2[(int)Math.round(y+y1)][(int)Math.round(x+x1)] = false;
							counter++;
						}



					}
					if(carry) {
						//If a certain percentage of them exists then draw the circle
						if((double)counter/(double)vals.size() * 100 >= HOWCIRCLE) {
							g.setColor(Color.blue);
							array = reset(array, array2);
							Graphics2D g2 = (Graphics2D)g;
							g2.setStroke(new BasicStroke(3));
							g2.drawOval(x-RADIUS, y-RADIUS, RADIUS*2, RADIUS*2);
						}
					}

					array2 = reset(array2, array);

				}	

			}
		}

	}

	public int gv(BufferedImage image, int x, int y) {
		return new Color(image.getRGB(x, y)).getRed();
	}

	public boolean[][] reset(boolean[][] changed, boolean[][] stay){
		for(int i = 0; i < stay.length;i++) {
			for(int j = 0; j < stay[i].length; j++) {
				boolean value = stay[i][j];
				changed[i][j] = value;
			}
		}
		return changed;
	}
	
	
	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame();
		DetectCircles d = new DetectCircles();
		frame.setSize(new Dimension(d.image.getWidth()+100,d.image.getHeight()+100));
		frame.add(d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
