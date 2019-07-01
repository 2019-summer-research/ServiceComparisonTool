package gui;

import com.AzureInterface.api.ApiAdapter;
import com.AzureInterface.api.endpoint.PersonInfo.response.PersonResponseElement;

import java.util.Map;

public class AzurePackage {

	public AzurePackage() {
		this.azureAdapter = new ApiAdapter();
	}

	ApiAdapter azureAdapter;

	String currentPersonGroupId = "";

	Map<String, PersonResponseElement> personList;

	public String getCurrentPersonGroupId() {
		return currentPersonGroupId;
	}

	public void setCurrentPersonGroupId(String currentPersonGroupId) {
		this.currentPersonGroupId = currentPersonGroupId;
	}

	public ApiAdapter getAzureAdapter() {
		return azureAdapter;
	}
}
