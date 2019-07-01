package gui;

import api.ViewportInterface;

import com.apientry.api.DetectFaces;
import com.apientry.api.collections.CreateCollection;
import com.apientry.api.faces.IndexFaces;
import com.apientry.api.faces.SearchFacesByImage;
import com.datasetinterface.DatasetInterface;
import com.datasetinterface.elements.PersonElement;
import com.datasetinterface.exceptions.DirectorySelectionException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static gui.Utilities.generateString;

public class GuiMain {
	private JPanel panel1;
	private JLabel origImageLabel;
	private JLabel topImageLabel;
	private JLabel bottomImageLabel;
	private JToolBar toolbar;
	private JLabel datasetStatusDialog;
	private JButton nextImageButton;
	private JButton StartupFormButton;
	private JButton trainButton;

	static GuiMain instance;

	ViewportInterface AwsInterface;
	ViewportInterface AzureInterface;
	String ClassID = "NOCLASS";

	/**
	 * Directory File which contains the dataset which is being handled by the system at the moment
	 */
	File datasetDirectory;

	DatasetInterface dataInterface;

	/**
	 * Are the APIs currently trained?
	 */
	boolean isTrained = false;

	public static void main(String[] args) {
		JFrame frame = new JFrame("GuiMain");
		GuiMain.instance = new GuiMain();
		frame.setContentPane(GuiMain.instance.panel1);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		// Init things
		GuiMain.instance.init();
		GuiMain.instance.addButtonHandlers();
	}

	public void init() {

		// Init button text
		this.nextImageButton = new JButton("Next");
		this.StartupFormButton = new JButton("Setup");

		// Add toolbar options
		this.toolbar.add(StartupFormButton);
		this.toolbar.add(nextImageButton);

	}

	private void addButtonHandlers() {

		// Setup the button which displays and handles saving the dataset directory
		this.StartupFormButton.addActionListener(e -> {
			try {
				this.dataInterface = new DatasetInterface();
				this.datasetStatusDialog.setText("Dataset Loaded");
				this.datasetStatusDialog.setForeground(Color.YELLOW);
			} catch (DirectorySelectionException e1) {
				e1.printStackTrace();
				this.datasetStatusDialog.setText("Failed loading dataset");
				this.datasetStatusDialog.setForeground(Color.RED);
			}
		});

		this.trainButton.addActionListener(e -> {
			if(this.isTrained) {
				System.out.println("System is already trained");
				return;
			}

			// Azure requires

		});

	}

	/**
	 * Trains the Azure API with the loaded DatasetInterface information
	 */
	private void trainAzure() {

	}

	/**
	 * Trains the Aws API with the loaded DatasetInterface information
	 */
	private void trainAws() {

		ClassID = generateString(6);
		CreateCollection CC = new CreateCollection();
		String[] args = {"",ClassID};
		CC.run(args);
		IndexFaces IF = new IndexFaces();

		for (PersonElement pe : dataInterface.getDatasetList())
		{
			pe.getImages().get(0);
			String[] arg = {"",pe.getImages().get(0).getAbsolutePath()};
			IF.run(arg);
		}
	}

	/**
	 * Feed the AWS interface a face, and have it do identification, returning some sort of thing.
	 * @param face
	 */
	private void identifyFaceAws(File face) {
		DetectFaces DF = new DetectFaces();
		String[] args = {"",face.getAbsolutePath()};
		BufferedImage FaceWithBounding = DF.run(args);
		Float Confindence = 0.0f;
		String NameGuess = "NoName";
		SearchFacesByImage SF = new SearchFacesByImage();
		String[] args2 = {"",ClassID,face.getAbsolutePath()};
		NameGuess = SF.run(args2);
		Confindence = SF.confidence;



	}

	/**
	 * Feed the Azure interface a face, and have it do identification, returning some sort of thing
	 * @param face
	 */
	private void identifyFaceAzure(File face) {

	}
}
