package actions;

import dataAccess.EnvelopeAccess;
import dataObjects.Vendor;
import tickets.ResponseTicket;

public class VendorActions {

	//changes prefname of vendor
	public static void setName(ResponseTicket response, Vendor vendor, String name) {
		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(vendor == null) {
			response.addErrorMessage("Vendor cannot be null");
			return;
		}
		if(name == null) {
			response.addErrorMessage("Invalid vendor edit, name cannot be null");
			return;
		}
		if(name.equals("")) {
			response.addErrorMessage("Invalid vendor edit, name cannot be empty");
			return;
		}
		vendor.setPrefName(name);
		

	}
	
	//changes prefEnvelope of vendor
	public static void setEnvelope(ResponseTicket response, Vendor vendor, String Envelope) {
		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(vendor == null) {
			response.addErrorMessage("Vendor cannot be null");
			return;
		}
		if(Envelope == null) {
			response.addErrorMessage("Invalid vendor edit, envelope cannot be null");
			return;
		}
		if(Envelope.equals("")) {
			response.addErrorMessage("Invalid vendor edit, envelope cannot be empty");
			return;
		}
		
		if(EnvelopeAccess.getEnvelopeByName(Envelope) == null) {
			response.addErrorMessage("Invalid vendor edit, envelope does not exist");
			return;
		}
		
		
		vendor.setPrefEnvelope(Envelope);
	}
	
	
}
