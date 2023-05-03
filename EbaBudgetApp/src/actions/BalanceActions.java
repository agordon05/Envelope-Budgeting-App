package actions;

import dataObjects.Balance;
import dataObjects.Statement;
import tickets.ResponseTicket;

public class BalanceActions {
	
	//redundant
	public static void setAmount(ResponseTicket response, Balance balance, double amount) {
		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(balance == null) {
			response.addErrorMessage("Balance cannot be null");
			return;
		}
		balance.setBalance(amount);
	}
	

	
	

}
