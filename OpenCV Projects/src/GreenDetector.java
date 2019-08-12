import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;       

public class GreenDetector extends JPanel{
	private boolean array [][]; 
    private BufferedImage image;
    private JFrame frame0;
	
    public GreenDetector() {
    }

    public GreenDetector(BufferedImage img) {
        image = img;
    }   



    public static void main (String args[]) throws InterruptedException{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        GreenDetector t = new GreenDetector();
        org.opencv.videoio.VideoCapture camera = new org.opencv.videoio.VideoCapture(0);

        if(!camera.isOpened()){
            System.out.println("REEEEEEEEEEEEEEEE");
        }
        
        else {   
            Mat frame = new Mat();
            camera.read(frame); 
        	BufferedImage image = t.MatToBufferedImage(frame);
            t.window(image, "look at this dood", 0, 0);
            
            while(true){
            	Thread.sleep(250);
            	frame = new Mat();  
                camera.read(frame);
                image = t.MatToBufferedImage(frame);
                t.drawImage(image);
            }   
        }
        camera.release();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
        

        g.setColor(new Color(findGreen(image)));
        g.fillRect(image.getWidth()/2, image.getHeight()/2, 15, 15);
        
        boolean array[][] = new boolean[image.getHeight()][image.getWidth()];
        for(int i = 1 ; i < image.getHeight()-1; i++) {
    		for(int j = 1; j < image.getWidth()-1; j++ ) {

    			if(new Color(image.getRGB(j,i)).getGreen() >= 160 && new Color(image.getRGB(j,i)).getRed() <= 150 && new Color(image.getRGB(j,i)).getBlue() <= 120 ) {
    		        g.setColor(Color.RED);
    				g.drawRect(j, i, 1, 1);
    				float sum = gv(image, j-1, i-1) + gv(image, j, i-1) +gv(image, j+1, i-1)+
    		    			gv(image, j-1, i)+ gv(image, j, i) +gv(image, j+1, i)+
    		    			gv(image, j-1, i+1)+gv(image, j, i+1)+gv(image, j+1, i+1);
    		    			float val = gv(image, j, i)*9 - sum;
    		    			if (val > 10) {
    		    				g.setColor(Color.RED);
    		    				array[i][j] = true;
    		    				g.drawRect(j, i, 1, 1);
    		    			}
    			}
    		}
    	}
        int start = 1;
        for(int i = 1 ; i < image.getHeight()-1; i++) {
    		for(int j = 1; j < image.getWidth()-1; j++ ) {
    			if(array[i][j] == true) {
    				start = j;
    				break;
    			}
    		}
    		int end = start;
    		for(int j = start+10; j < image.getWidth()-1; j++ ) {
     		if(array[i][j] == true) {
    				end = j;
    				break;
    			}
    		}
    		if(start != 1 || end != 1) {
    		g.setColor(Color.RED);
    		g.fillRect(start, i-1, end-start, 3);
    		//g.drawLine(start, i, end, i);
    		}
        }
        start = 1;
        for(int j = 1; j < image.getWidth()-1; j++) {
    		for(int i = 1 ; i < image.getHeight()-1; i++) {
    			if(array[i][j] == true) {
    				start = i;
    				break;
    			}
    		}
    		int end = start;
    		for(int i = start+10 ; i < image.getHeight()-1; i++ ) {
     		if(array[i][j] == true) {
    				end = i;
    				break;
    			}
    		}
    		if(start != 1 || end != 1) {
    		g.setColor(Color.RED);
    		g.fillRect(j-1, start, 3, end-start);
    		//g.drawLine(j, start, j, end);
    		}
        }
        
    }
    
    public int gv(BufferedImage image, int x, int y) {
    	return new Color(image.getRGB(x, y)).getGreen();
    }

    public void window(BufferedImage img, String text, int x, int y) {
        frame0 = new JFrame();
        frame0.getContentPane().add(new GreenDetector(img));
        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setTitle(text);
        frame0.setSize(img.getWidth(), img.getHeight() + 30);
        frame0.setLocation(x, y);
        frame0.setVisible(true);
    }
    
    
    public void drawImage (BufferedImage img) {
    frame0.getContentPane().add(new GreenDetector(img));
    frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame0.setSize(img.getWidth(), img.getHeight() + 30);
    frame0.setVisible(true);
    }
    


    public BufferedImage MatToBufferedImage(Mat frame) {
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }
    
    public int findGreen(BufferedImage image) {
    	//array = new boolean[image.getHeight()][image.getWidth()];
    	
    	int arr = image.getRGB(image.getWidth()/2, image.getHeight()/2);
    	Color colour = new Color(arr);
    	System.out.println("COLOUR AT CENTER " + colour.getBlue());
    	return arr;
    }

}
