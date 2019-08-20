package letters;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class LetterWindow{
	private BufferedImage image;
	public LetterWindow() throws IOException {
		JFrame f = new JFrame();
		image = ImageIO.read((new File("Images/words.png")));
		f.add(new DetectLetters(image));
		f.setSize(new Dimension(image.getWidth()+100,image.getHeight()+100));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	
	
	public static void main(String[] args) throws IOException {
		new LetterWindow();
	}

}
