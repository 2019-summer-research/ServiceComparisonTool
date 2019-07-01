package gui;

import com.AzureInterface.api.endpoint.FacialDetection.FaceDetectionResponseElement;
import com.AzureInterface.api.endpoint.Identify.IdentifyResponseElement;
import api.ViewportInterface;

import com.apientry.api.DetectFaces;
import com.apientry.api.collections.CreateCollection;
import com.apientry.api.faces.IndexFaces;
import com.apientry.api.faces.SearchFacesByImage;
import com.datasetinterface.DatasetInterface;
import com.datasetinterface.elements.PersonElement;
import com.datasetinterface.exceptions.DirectorySelectionException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static gui.Utilities.generateString;

public class GuiMain {
	private JPanel panel1;
	private JLabel origImageLabel;
	private JLabel topImageLabel;
	private JLabel bottomImageLabel;
	private JToolBar toolbar;
	private JLabel datasetStatusDialog;
	private JLabel azureTraining;
	private JLabel azureConfidence;
	private JButton IdentifyImage;
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
	 * Package of Azure API functions
	 */
	AzurePackage azurePackage;

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
		GuiMain.instance.azurePackage = new AzurePackage();
	}

	public void init() {

		// Init button text
		this.IdentifyImage = new JButton("Identify");
		this.StartupFormButton = new JButton("Setup");
		this.trainButton = new JButton("Train");

		// Add toolbar options
		this.toolbar.add(StartupFormButton);
		this.toolbar.add(IdentifyImage);
		this.toolbar.add(trainButton);

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

			try {
				this.azureTraining.setText("Azure Training..");
				this.azureTraining.setForeground(Color.BLUE);
				trainAzure();
				this.azureTraining.setText("Azure Trained");
				this.azureTraining.setForeground(Color.GREEN);
				this.isTrained = true;
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		});

		this.IdentifyImage.addActionListener(e -> {
			if(!this.isTrained) {
				System.err.println("Train the system first");
				return;
			}

			try {
				File inputFile = Utilities.requestImageFile();
				this.identifyFaceAzure(inputFile);
				this.identifyFaceAws(inputFile);

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

	}

	/**
	 * Trains the Azure API with the loaded DatasetInterface information
	 */
	private void trainAzure() throws IOException {

		if(dataInterface == null) {
			System.err.println("Please select a directory first");
			return;
		}

		// First, to train Azure we need to create a person group. Generate a group ID and do such.
		String personGroupId = Utilities.generateString(6);
		azurePackage.setCurrentPersonGroupId(personGroupId);
		azurePackage.azureAdapter.createPersonGroup(personGroupId, "Test Group", "");
		azurePackage.setCurrentPersonGroupId(personGroupId);

		// Now we create a person for each entry in the dataset that we have.
		azurePackage.personList = new HashMap<>();

		for(PersonElement person : dataInterface.getDatasetList()) {
			azurePackage.personList.put(person.getPersonId(),
					azurePackage.azureAdapter.addPersonToPersonGroup(personGroupId, person.getPersonId().toLowerCase(), ""));
		}

		//Now that we have loaded a person for each entry, add a face to each person.
		for(PersonElement person : dataInterface.getDatasetList()) {
			for(File image : person.getImages()) {
				azurePackage.azureAdapter.addFaceToPersongroup(personGroupId, azurePackage.personList.get(person.getPersonId()).GetID(),
						Files.readAllBytes(Paths.get(image.getAbsolutePath())));
			}
		}

		// Now that we've added a face entry for each person, run the train method
		azurePackage.azureAdapter.trainPersongroup(personGroupId);

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
	 */
	private void identifyFaceAzure(File input) throws IOException {

		// Upload the face to Azure
		ArrayList<FaceDetectionResponseElement> elements = (ArrayList<FaceDetectionResponseElement>) azurePackage.azureAdapter.getFaces(Files.readAllBytes(Paths.get(input.getAbsolutePath())));

		// Display the face in the Azure section of the box
		FaceDetectionResponseElement element = elements.get(0);
		HashMap<String, Integer> recLandmarks = element.getFaceRectangle();
		Icon icon = new ImageIcon(ImageIO.read(Utilities.drawSquare(input, recLandmarks.get("left"),
				recLandmarks.get("top"), recLandmarks.get("width"), recLandmarks.get("height"))));

		bottomImageLabel.setIcon(icon);

		// Run Azure identification to get a confidence value
		ArrayList<IdentifyResponseElement> identity = (ArrayList<IdentifyResponseElement>) azurePackage.azureAdapter
				.identifyFaces(element.getFaceId(), azurePackage.currentPersonGroupId);

		// Set confidence
		azureConfidence.setText("Azure Image Confidence - " + identity.get(0).getConfidence());
	}
}
