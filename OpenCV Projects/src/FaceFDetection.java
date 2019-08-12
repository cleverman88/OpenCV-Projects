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

public class FaceFDetection extends JPanel{
    private BufferedImage image;
    private JFrame frame0;
	
    public FaceFDetection() {
    }

    public FaceFDetection(BufferedImage img) {
        image = img;
    }   


    private static final String xmlFile = "C:/Users/sohai/Desktop/opencv/sources/modules/java/test/common_test/res/raw/lbpcascade_frontalface.xml"; 

    public static void main (String args[]) throws InterruptedException{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        CascadeClassifier classifier = new CascadeClassifier(xmlFile); 
        
        FaceFDetection t = new FaceFDetection();
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
            	frame = new Mat();  
                camera.read(frame);
            	MatOfRect faceDetections = new MatOfRect(); 
                classifier.detectMultiScale(frame, faceDetections); 
                for (Rect rect : faceDetections.toArray()) { 
                    Imgproc.rectangle(frame,        
                    new Point(rect.x, rect.y),   
                    new Point(rect.x + rect.width, rect.y + rect.height),  
                    new Scalar(0, 255, 255), 
                    3); 
                }
                image = t.MatToBufferedImage(frame);
                t.drawImage(image);
            }   
        }
        camera.release();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public void window(BufferedImage img, String text, int x, int y) {
        frame0 = new JFrame();
        frame0.getContentPane().add(new FaceFDetection(img));
        frame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame0.setTitle(text);
        frame0.setSize(img.getWidth(), img.getHeight() + 30);
        frame0.setLocation(x, y);
        frame0.setVisible(true);
    }
    
    public void drawImage (BufferedImage img) {

    frame0.getContentPane().add(new FaceFDetection(img));
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

}
