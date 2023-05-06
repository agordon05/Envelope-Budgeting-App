package actions;

import java.math.BigDecimal;
import java.util.ArrayList;

import UI.tempInfo;
import dataAccess.BalanceAccess;
import dataAccess.EnvelopeAccess;
import dataObjects.Balance;
import dataObjects.Envelope;
import settings.EnvelopeSettings;
import tickets.ResponseTicket;

public class Actions extends precisionOperations{
	
	public static ResponseTicket validate() {
		
		ResponseTicket response = new ResponseTicket();
		response.addInfoMessage("validate action called");

		//formatting balance
		int tempBalance = (int)(BalanceAccess.getBalance().getBalance() * 100);
		double amount = (double)(tempBalance / 100.0);
		
		ArrayList<Envelope> envelopes = EnvelopeAccess.getEnvelopes();
		
		//check for discrepancy
		boolean check = false;
		
		//formatting envelope amounts
		for(int index = 0; index < envelopes.size(); index++) {
			double checkAmount = envelopes.get(index).getAmount();
			int tempAmount = (int)(envelopes.get(index).getAmount() * 100);
			envelopes.get(index).setAmount((double)(tempAmount / 100.0));	
			
			//discrepancy found
			if(checkAmount != envelopes.get(index).getAmount()) check = true;
		}
		
		//formatting envelopes -- validating balance
		for(int index = 0; index < envelopes.size(); index++) {
			if(envelopes.get(index).getAmount() < 0) envelopes.get(index).setAmount(0);
//			System.out.println("amount -- " + amount + " -- envelope: " + envelopes.get(index).getName() + " -- envelope amount: " + envelopes.get(index).getAmount() );
			
			//amount = BigDecimal.valueOf(amount).subtract(BigDecimal.valueOf(envelopes.get(index).getAmount())).doubleValue();
			amount = subtract(amount, envelopes.get(index).getAmount());
			/*IS NOT PRECISE ENOUGH FOR CALCULATION*/
			//amount -= envelopes.get(index).getAmount();
			
//			System.out.println("amount -- " + amount + " -- envelope: " + envelopes.get(index).getName() + " -- envelope amount: " + envelopes.get(index).getAmount() );
		}
		
		//balance does not match with envelope balances
		if(amount < 0) {
			amount = Math.abs(amount);
			response.addErrorMessage("Account validation failed. Removing extra");
			//start withdraw algorithm from last priority
			check = true;
			EnvelopeActions.defaultEnvelopeCalled = true;
			EnvelopeActions.withdrawFromAll(response, EnvelopeAccess.getDefault(), amount);
		}
		
		//balance does not match with envelope balances
		else if(amount > 0) {
			response.addErrorMessage("Account validation failed. adding unnaccounted funds");
			EnvelopeActions.deposit(response, EnvelopeAccess.getEnvelopeByPriority(1), amount);
			check = true;
		}
		
		//if discrepancy found, save
		if(check) tempInfo.save();
		
		return response;
	}
	

	public static ResponseTicket Withdraw(String name, Envelope envelope, Double amount) {
		ResponseTicket response = new ResponseTicket();
		
		response.addInfoMessage("withdraw action called");

		//currently only one balance is used
		Balance balance = BalanceAccess.getBalance();
				
		if(amount <= 0) {
			response.addErrorMessage("amount cannot be less than or equal to 0");
			return response;
		}
		
		//withdraw starting from default envelope
		if(envelope == null) {
			response.addInfoMessage("Withdrawing $" + amount + " from default envelope");
			amount = EnvelopeActions.withdrawFromDefault(response, amount);
			Envelope e = EnvelopeAccess.getEnvelopeByPriority(EnvelopeAccess.getEnvelopes().size());
			EnvelopeActions.withdrawFromAll(response, e, amount);
		}
		//withdraw starting from envelope given
		else {
			response.addInfoMessage("Withdrawing $" + amount + " from " + envelope.getName());
			EnvelopeActions.withdrawFromAll(response, envelope, amount);
		}
		
		BalanceActions.withdraw(response, balance, amount);
		
		return response;
		
	}
	
	
	public static ResponseTicket Deposit(Envelope e, Double amount) {
		
		ResponseTicket response = new ResponseTicket();
		
		response.addInfoMessage("deposit action called");

		//currently only one balance is used
		Balance balance = BalanceAccess.getBalance();
		
		if(amount <= 0) {
			response.addErrorMessage("amount cannot be less than or equal to 0");
			return response;
		}
		
		if(e == null) {
			response.addInfoMessage("Depositing $" + amount + " into all envelopes");
			EnvelopeActions.depositIntoAllEnvelopes(response, amount);		
		}
		else {
			response.addInfoMessage("Depositing $" + amount + " into " + e.getName());
			EnvelopeActions.Transfer(response, null, e, amount);
		}
		
		BalanceActions.deposit(response, balance, amount);
		
		return response;
	}

	
	
	public static ResponseTicket Transfer(Envelope e1, Envelope e2, Double amount) {
		ResponseTicket response = new ResponseTicket();

		response.addInfoMessage("transfer action called");

		if(amount <= 0) {
			response.addErrorMessage("amount cannot be less than or equal to 0");
			return response;
		}

		
		
		if(e1 == null && e2 == null) {
			response.addErrorMessage("Both envelopes cannot be null");
			return response;
		}
		
		if(e1 == null) {
			response.addInfoMessage("checking for amounts outside of envelopes");
			double unaccountedFunds = getUnaccountedFunds();
			
			if(amount > unaccountedFunds) {
				response.addErrorMessage("amount is greater than unaccounted funds");
				return response;
			}
			
			EnvelopeActions.deposit(response, e2, amount);
			
			return response;
		}
		
		if(amount > e1.getAmount()) {
			response.addErrorMessage("Envelope does not have sufficient funds for transfer");
			return response;
		}
		
		
		if(e2 == null) {
			response.addInfoMessage("removing amount from envelope");
			EnvelopeActions.withdrawal(response, e2, amount);
			return response;
		}
		

		EnvelopeActions.Transfer(response, e1, e2, amount);
		
		return response;
	}
	
	
	private static double getUnaccountedFunds() {
		
		double amount = BalanceAccess.getBalance().getBalance();
		ArrayList<Envelope> envelopes = EnvelopeAccess.getEnvelopes();
		for(int index = 0; index < envelopes.size(); index++) {
			amount -= envelopes.get(index).getAmount();
		}
		if(amount < 0) {
			throw new IllegalStateException("Envelopes have more funds than balance"); 
		}
		
		return amount;
	}
	
	
	
	public static ResponseTicket Edit(Envelope e, int priority, String name, boolean cap, int capAmount, int fillSetting, int fillAmount, boolean extra, boolean Default) {
		
		ResponseTicket response = new ResponseTicket();
		response.addInfoMessage("edit action called");

		
		//validate envelope
		if(e == null) {
			response.addErrorMessage("Envelope Edit -- Envelope cannot be null");
			return response;
		}
		
		
		//validate priority
		if(priority <= 0 || priority > EnvelopeAccess.getEnvelopes().size()) {
			response.addErrorMessage("Envelope Edit -- Priority cannot be less than 0 or greater than " + EnvelopeAccess.getEnvelopes().size());
			return response;
		}
		
		//validate name
		if(name == null) {
			response.addErrorMessage("Envelope Edit -- Name cannot be null");
			return response;
		}
		Envelope temp = EnvelopeAccess.getEnvelopeByName(name);
		if(temp != null && !temp.equals(e)) {
			response.addErrorMessage("Envelope Edit -- Another envelope uses this name");
			return response;
		}
		
		//validate cap
			//cap amount
		if(!cap) capAmount = 0;
		else if(capAmount <= 0) {
			response.addErrorMessage("Envelope Edit -- Cap amount cannot be less than or equal to 0");
			return response;
		}
		
		//validate fillsetting
			//validate fillAmount
		switch(fillSetting) {
			default: {
				response.addErrorMessage("Envelope Edit -- invalid fill setting");
				return response;
			}
			case EnvelopeSettings.amount: {
				if(fillAmount < 0) {
					response.addErrorMessage("Envelope Edit -- fill amount cannot be less than or equal to 0");
					return response;
				}
			} break;
			case EnvelopeSettings.fill: {
				fillAmount = 0;
			} break;
			case EnvelopeSettings.percentage: {
				int percentage = EnvelopeActions.getTotalFillPercentage(temp);
				if(percentage == 100) {
					response.addErrorMessage("Envelope Edit -- Invalid percentage, total percentage used by other envelopes is 100%");
					return response;
				}
				if(fillAmount > percentage) {
					response.addErrorMessage("Envelope Edit -- Invalid percentage, cannot be greater than " + percentage);
					return response;
				}
				if(fillAmount < 0) {
					response.addErrorMessage("Envelope Edit -- Invalid percentage, cannot be less than 0");
					return response;
				}
				
				
				
			} break;

		}
		
		
		
		//edit envelope
		EnvelopeActions.setPriority(response, e, priority);
		EnvelopeActions.EditName(response, e, name);
		EnvelopeActions.EditCap(response, e, cap, capAmount);
		EnvelopeActions.EditSettings(response, e, fillSetting, fillAmount);
		EnvelopeActions.EditExtra(response, e, extra);
		EnvelopeActions.EditDefault(response, e, Default);
		
		
		
		return response;
	}
	
	
}
