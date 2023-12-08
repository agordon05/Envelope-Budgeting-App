package tickets;

import java.util.ArrayList;

import dataObjects.Envelope;
import dataObjects.Statement;
import dataObjects.Vendor;

public class ResponseTicket {

	private ArrayList<String> errorMessages = new ArrayList<>();
	private ArrayList<String> infoMessages = new ArrayList<>();
	
	public void add(ResponseTicket response) {
		errorMessages.addAll(response.getErrorMessages());
		infoMessages.addAll(response.getInfoMessages());
	}
	
	public boolean hasErrorMessage() {
		if(errorMessages.size() == 0) return false;
		return true;
	}
	public ArrayList<String> getErrorMessages(){
		return errorMessages;
	}
	public boolean hasInfoMessage() {
		if(infoMessages.size() == 0) return false;
		return true;
	} 
	public ArrayList<String> getInfoMessages(){
		return infoMessages;
	}
	public void addErrorMessage(String message) {
		if(message == null) return;
		errorMessages.add(message);
	}
	public void addInfoMessage(String message) {
		if(message == null) return;
		infoMessages.add(message);
	}
	
	public void printMessages() {
		
		System.out.println("Error messages: " + errorMessages.size());


		for(int index = 0; index < errorMessages.size(); index++) {
			System.out.println(errorMessages.get(index));
		}
		System.out.println();
		System.out.println("Info messages: " + infoMessages.size());


		for(int index = 0; index < infoMessages.size(); index++) {
			System.out.println(infoMessages.get(index));
		}
		System.out.println("\n");
	}
		
	
}
