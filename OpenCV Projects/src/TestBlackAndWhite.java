import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.opencv.core.Core; 
import org.opencv.core.Mat; 
import org.opencv.imgcodecs.Imgcodecs; 
import org.opencv.imgproc.Imgproc; 
public class TestBlackAndWhite {
	
	public static void main(String[] args) {
	    new TestBlackAndWhite();
	}

public TestBlackAndWhite() {
    EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
            }

            JFrame frame = new JFrame("Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new TestPane());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        }
    });
}

public class TestPane extends JPanel {

    private BufferedImage master;
    private BufferedImage grayScale;
    private BufferedImage blackWhite;

    public TestPane() {
        try {
            master = ImageIO.read(new File("Fingerkreis.jpg"));
            grayScale = ImageIO.read(new File("Fingerkreis.jpg"));
            ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
            op.filter(grayScale, grayScale);

            blackWhite = new BufferedImage(master.getWidth(), master.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
            Graphics2D g2d = blackWhite.createGraphics();
            g2d.drawImage(master, 0, 0, this);
            g2d.dispose();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        if (master != null) {
            size = new Dimension(master.getWidth()+50, master.getHeight()+50);
        }
        return size;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (master != null) {
        	
        	//g.drawImage(grayScale, 10,10,this);
            g.drawImage(blackWhite,10,10,this);

        }
    }


}
}