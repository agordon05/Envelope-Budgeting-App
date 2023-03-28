package tickets;

import java.util.ArrayList;

import dataObjects.Envelope;
import dataObjects.Statement;
import dataObjects.Vendor;

public class ResponseTicket {

	private ArrayList<String> errorMessages = new ArrayList<>();
	private ArrayList<String> infoMessages = new ArrayList<>();
	
	private Statement statement;
	private Envelope envelope;
	private Vendor vendor;
	private double balance;
	
	
	
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
	/**
	 * @return the statement
	 */
	public Statement getStatement() {
		return statement;
	}
	/**
	 * @return the envelope
	 */
	public Envelope getEnvelope() {
		return envelope;
	}
	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}
	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}
	/**
	 * @param statement the statement to set
	 */
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	/**
	 * @param envelope the envelope to set
	 */
	public void setEnvelope(Envelope envelope) {
		this.envelope = envelope;
	}
	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}
	

	
	
	
}
