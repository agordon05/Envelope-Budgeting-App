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
		int tempBalance = (int)(multiply(BalanceAccess.getBalance().getBalance(), 100));
		double amount = divide(tempBalance, 100.0);
		response.addInfoMessage("Formatted balance, was $" + BalanceAccess.getBalance().getBalance() + " and is now $" + amount);
		BalanceAccess.getBalance().setBalance(amount);
		
		
		ArrayList<Envelope> envelopes = EnvelopeAccess.getEnvelopes();
		
		//check for discrepancy
		boolean check = false;
		
		//formatting envelope amounts
		for(int index = 0; index < envelopes.size(); index++) {
			
			
			double checkAmount = envelopes.get(index).getAmount();
			
			int tempAmount = (int)(multiply(envelopes.get(index).getAmount(), 100));
			envelopes.get(index).setAmount(divide(tempAmount, 100));
			
			//discrepancy found
			if(checkAmount != envelopes.get(index).getAmount()) {
				response.addInfoMessage("Envelope " + envelopes.get(index).getName() + "'s amount had a discrepancy");
				response.addInfoMessage("\t was " + checkAmount + " and is now " + envelopes.get(index).getAmount());
				check = true;
			}
				
		}
		
		
		//formatting envelopes -- validating balance
		for(int index = 0; index < envelopes.size(); index++) {
			if(envelopes.get(index).getAmount() < 0) {
				response.addInfoMessage(envelopes.get(index).getName() + "'s amount was less than 0, setting to 0");
				envelopes.get(index).setAmount(0);
			}
			
			response.addInfoMessage("subtracting $" + envelopes.get(index).getAmount() + " from " + envelopes.get(index).getName() + " from remaining balance");
			amount = subtract(amount, envelopes.get(index).getAmount());
			response.addInfoMessage("remaining balance is $" + amount);
			
		}
		
		
		//balance does not match with envelope balances
		if(amount < 0) {
			amount = Math.abs(amount);
			response.addErrorMessage("Account validation failed. Removing extra amount: $" + amount);
			//start withdraw algorithm from last priority
			check = true;
			
			amount = EnvelopeActions.withdrawFromEnvelope(response, EnvelopeAccess.getDefault(), amount);
			EnvelopeActions.withdrawFromAll(response, amount);

		}
		
		
		//balance does not match with envelope balances
		else if(amount > 0) {
			response.addErrorMessage("Account validation failed. adding unnaccounted funds");
			EnvelopeActions.deposit(response, EnvelopeAccess.getEnvelopeByPriority(1), amount);
			
			check = true;
		}
		
		//if discrepancy found, save
		if(check) {
			response.addInfoMessage("Saving new info");
			tempInfo.save();
		}
		
		return response;
	}
	

	public static ResponseTicket Withdraw(String name, Envelope envelope, Double amount) {
		ResponseTicket response = new ResponseTicket();
		
		response.addInfoMessage("withdraw action called");

		if(amount <= 0) {
			response.addErrorMessage("amount cannot be less than or equal to 0");
			return response;
		}
		
		//currently only one balance is used
		Balance balance = BalanceAccess.getBalance();
		BalanceActions.withdraw(response, balance, amount);
	
		
		
		
		
		//withdraw from envelope given
		amount = EnvelopeActions.withdrawFromEnvelope(response, envelope, amount);
		//withdraw from Default envelope
		amount = EnvelopeActions.withdrawFromEnvelope(response, EnvelopeAccess.getDefault(), amount);
		//withdraw from all starting from lowest priority
		EnvelopeActions.withdrawFromAll(response, amount);
		
		
		
		return response;
		
	}
	
	
	public static ResponseTicket Deposit(Envelope e, Double amount) {
		
		ResponseTicket response = new ResponseTicket();
		
		response.addInfoMessage("deposit action called");

		//currently only one balance is used
		Balance balance = BalanceAccess.getBalance();
		BalanceActions.deposit(response, balance, amount);

		
		if(amount <= 0) {
			response.addErrorMessage("amount cannot be less than or equal to 0");
			return response;
		}
		
		if(e == null) {
			response.addInfoMessage("Depositing $" + amount + " into all envelopes");
			EnvelopeActions.depositIntoAll(response, amount);		
		}
		else {
			response.addInfoMessage("Depositing $" + amount + " into " + e.getName());
			//EnvelopeActions.Transfer(response, null, e, amount);
			EnvelopeActions.deposit(response, e, amount);
		}
		
		
		return response;
	}

	
	
	public static ResponseTicket Transfer(Envelope e1, Envelope e2, Double amount) {
		ResponseTicket response = new ResponseTicket();

		response.addInfoMessage("transfer action called");

		if(amount <= 0) {
			response.addErrorMessage("amount cannot be less than or equal to 0");
			return response;
		}

		
		
		if(e1 == null || e2 == null) {
			response.addErrorMessage("Envelopes cannot be null");
			return response;
		}
		
		
		if(amount > e1.getAmount()) {
			response.addErrorMessage("Envelope does not have sufficient funds for transfer");
			return response;
		}
		
		

		EnvelopeActions.Transfer(response, e1, e2, amount);
		
		return response;
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
				//total percantage used for envelopes is 100%
				if(percentage == 100) {
					response.addErrorMessage("Envelope Edit -- Invalid percentage, total percentage used by other envelopes is 100%");
					return response;
				}
				//total percentage used for envelopes is not 100%, but the fill amount given will make it more than 100%
				if(fillAmount > (100 - percentage)) {
					response.addErrorMessage("Envelope Edit -- Invalid percentage, cannot be greater than " + (100 - percentage));
					return response;
				}
				//fill amount cannot be less than 0
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
