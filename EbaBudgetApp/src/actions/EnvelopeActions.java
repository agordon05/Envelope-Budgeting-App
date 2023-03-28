package actions;

import dataObjects.Envelope;
import tickets.ResponseTicket;

public class EnvelopeActions {

	//sets the priority of envelope, changes the rest accordingly
	public static ResponseTicket setPriority(ResponseTicket response, Envelope envelope, int priority) {
		return null;
	}
	
	//changes the name of the envelope
	public static ResponseTicket changeName(ResponseTicket response, Envelope envelope, String name) {
		return null;
	}
	
	//changes the amount in envelope1, takes the amount from envelope2
	public static ResponseTicket changeAmount(ResponseTicket response, Envelope envelope1, Envelope envelope2, double amount) {
		return null;
	}
	
	//changes the fillSettings in envelope
	public static ResponseTicket changeSettings(ResponseTicket response, Envelope envelope, int fillSetting, double fillAmount) {
		return null;
	}
	
	//changes the cap settings in envelope
	public static ResponseTicket changeCap(ResponseTicket response, Envelope envelope, boolean cap, int capAmount) {
		return null;
	}
	
	
}
