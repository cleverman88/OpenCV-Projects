import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DetectCircles extends JPanel{
	final static int RADIUS = 25;
	
	BufferedImage image = null;
	BufferedImage binary = null;
	
	public DetectCircles() throws IOException {
		image = ImageIO.read((new File("Fingerkreis.jpg")));
		
		binary = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = binary.createGraphics();
		g2d.drawImage(image, 0, 0, this);
		g2d.dispose();
		
		repaint();
	}
	
	public BufferedImage getImage() {
		return image;
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
        
        boolean array2[][] = new boolean[image.getHeight()][image.getWidth()];
        System.arraycopy( array, 0, array2, 0, array.length );
        for(int y = 0; y < binary.getHeight()-1; y++) {
        	for(int x = 0; x < binary.getWidth()-1; x++) {
        		
        		double angle,x1,y1;
        		boolean carry = true;
        		int counter = 0;
        		for(double i = 0; i < 360; i+= 1) {
        			angle = i;
        			x1 = RADIUS * (Math.cos(angle * Math.PI/180));
        			y1 = RADIUS * (Math.cos(angle * Math.PI/180));
        			
        			
        			if(Math.round(x+x1) > binary.getWidth()-1 || Math.round(x+x1) < 0 || Math.round(y+y1) > Math.round(binary.getHeight()-1) || Math.round(y + y1) < 0) {
        				carry = false;
        				break;
        			}
        			
        			else {
        				
        				if(array[(int)Math.round(y+y1)][(int)Math.round(x+x1)]) {
        					counter++;
        				}
        			}
        		}
        		if(carry) {
        			System.out.println(counter);
        			if(counter > 100) {
        				g.drawOval(x-RADIUS, y-RADIUS, RADIUS*2, RADIUS*2);
        			}
        		}
        		
        	}
        }
        
        
        
        
	}
	
    public int gv(BufferedImage image, int x, int y) {
    	return new Color(image.getRGB(x, y)).getRed();
    }
	
	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame();
		DetectCircles d = new DetectCircles();
		frame.setSize(new Dimension(d.getImage().getWidth()+100,d.getImage().getHeight()+100));
		frame.add(d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
