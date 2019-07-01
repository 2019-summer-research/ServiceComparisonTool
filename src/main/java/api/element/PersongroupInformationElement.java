package api.element;

import api.ServiceType;

public class PersongroupInformationElement {

	/**
	 * Defines which service has been used to fill out this information element
	 */
	ServiceType service;

	/**
	 * Used in Azure services to hold an ID for this list. (Maybe you can use AWS collection ID on this var too?)
	 */
	String persongroupId;
	// .. Put whatever else you might need in here for Amazon
}
