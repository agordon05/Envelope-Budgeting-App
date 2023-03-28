package actions;

import dataObjects.Balance;
import tickets.ResponseTicket;

public class BalanceActions {
	
	//redundant
	public static ResponseTicket setAmount(ResponseTicket response, Balance balance, double amount) {
		balance.setBalance(amount);
		return response;
	}
	
	
	

}
