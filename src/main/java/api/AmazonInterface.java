package api;

import api.element.CompareFaceElement;
import api.element.PersongroupInformationElement;

import java.io.File;

public class AmazonInterface implements ViewportInterface{

	@Override
	public void trainNetwork(String groupId) {

	}

	@Override
	public CompareFaceElement compareFace(File localFaceToCompare, String personGroupId) {
		return null;
	}

	@Override
	public PersongroupInformationElement createPersongroup(String persongroupName) {
		return null;
	}
}
