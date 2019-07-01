package gui;

import api.ViewportInterface;
import com.datasetinterface.DatasetInterface;
import com.datasetinterface.exceptions.DirectorySelectionException;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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

	}

	/**
	 * Feed the AWS interface a face, and have it do identification, returning some sort of thing.
	 * @param face
	 */
	private void identifyFaceAws(File face) {

	}

	/**
	 * Feed the Azure interface a face, and have it do identification, returning some sort of thing
	 * @param face
	 */
	private void identifyFaceAzure(File face) {

	}
}
