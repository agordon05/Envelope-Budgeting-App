package actions;

import dataObjects.Envelope;
import dataObjects.Statement;
import dataObjects.StatementSplits;
import tickets.ResponseTicket;

public class StatementActions {

	//split a splitstatement in Statement object
	public static ResponseTicket Split(ResponseTicket response, Statement statement, StatementSplits splitAmount) {
		return null;
	}
	
	//merge 2 split statements
	public static ResponseTicket merge(ResponseTicket response, Statement statement, Envelope envelope1, Envelope envelope2) {
		return null;
	}
	
	//statement no longer pending, change statement amount to actual amount
	public static ResponseTicket change(ResponseTicket response, Statement statement, double amount) {
		return null;
	}
	
	
}
