package api;

import api.element.CompareFaceElement;
import api.element.PersongroupInformationElement;

import java.io.File;

public interface ViewportInterface {

	/**
	 * Sends a signal back to the cloud provider telling it to start training the model
	 * @param groupId - The groupId which is to be trained
	 */
	public void trainNetwork(String groupId);

	/**
	 * Compares two faces and returns back information about the two faces
	 * @param localFaceToCompare The filepath to the file which is to be compared to a group
	 * @param personGroupId The group which the face is to be compared against
	 */
	public CompareFaceElement compareFace(File localFaceToCompare, String personGroupId);

	/**
	 * Creates a persongroup with a specified service, and returns some sort of identification
	 * @param persongroupName
	 * @return
	 */
	public PersongroupInformationElement createPersongroup(String persongroupName);
}
