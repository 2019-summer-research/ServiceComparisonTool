package gui;

import com.datasetinterface.ImageFileFilter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

public class Utilities {

	public static String generateString(int len) {

		final String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();

		StringBuilder builder = new StringBuilder(len);
		for(int i = 0; i < len; i++) {
			builder.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		return builder.toString();
	}

	public static File requestImageFile() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		jfc.setDialogTitle("Select a file to compare");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnValue = jfc.showOpenDialog(null);

		if(returnValue == JFileChooser.APPROVE_OPTION) {
			return jfc.getSelectedFile();
		}

		else {
			System.err.println("Error with JFileChooser");
			return null;
		}

	}

	public static File drawSquare(File inputFile, int left, int top, int width, int height) {
		BufferedImage img = null;
		File output = null;
		try {
			img = ImageIO.read(inputFile);

			Graphics2D graph = img.createGraphics();
			graph.setColor(Color.RED);
			graph.drawRect(left, top, width, height);

			output = new File(inputFile.getName() + "_rect.png");
			ImageIO.write(img, "png", output);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return output;
	}

}
