package actions;

import java.math.BigDecimal;
import java.util.ArrayList;

import dataAccess.EnvelopeAccess;
import dataObjects.Balance;
import dataObjects.Envelope;
import settings.EnvelopeSettings;
import tickets.ResponseTicket;

public class EnvelopeActions extends precisionOperations{

	//sets the priority of envelope, changes the rest accordingly
	public static void setPriority(ResponseTicket response, Envelope envelope, int priority) {

		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(envelope == null) {
			response.addErrorMessage("Envelope cannot be null");
			return;
		}


		ArrayList<Envelope> envelopes = EnvelopeAccess.getEnvelopes();

		if(priority <= 0 || priority > envelopes.size()) {
			response.addErrorMessage("Invalid envelope edit, prority outside of range");
			return;
		}
		int prevPriority = envelope.getPriority();

		if(priority == prevPriority) {
			response.addInfoMessage("No change to envelope priority");
			return;
		}


		for(int index = 0; index < envelopes.size(); index++) {
			//current envelope
			Envelope temp = envelopes.get(index);
			//if current envelope is envelope given
			if(temp.getName().equals(envelope.getName())) {
				temp.setPriority(priority);
				continue;
			}
			//if current envelope should be moved up by priority change
			if(temp.getPriority() >= priority && temp.getPriority() < prevPriority) {
				temp.setPriority( temp.getPriority() + 1);
			}
			//if current envelope should be moved down by priority change
			else if(temp.getPriority() <= priority && temp.getPriority() > prevPriority) {
				temp.setPriority( temp.getPriority() - 1);

			}
		}

		response.addInfoMessage("Envelope " + envelope.getName() + " priority changed to " + priority);


		return;
	}

	//changes the name of the envelope
	public static void EditName(ResponseTicket response, Envelope e, String name) {

		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(e == null) {
			response.addErrorMessage("Envelope cannot be null");
			return;
		}
		if(name == null) {
			response.addErrorMessage("Envelope name cannot be changed to null");
			return;
		}
		if(e.getName().equals(name)) {
			response.addInfoMessage("No change to envelope name");
			return;
		}

		ArrayList<Envelope> envelopes = EnvelopeAccess.getEnvelopes();


		for(int index = 0; index < envelopes.size(); index++) {
			if(e.equals(envelopes.get(index))) continue;
			if(name.equals(envelopes.get(index).getName())) {
				response.addErrorMessage("Invalid envelope edit, name is not unique");
				return;
			}
		}

		String prevName = e.getName();

		e.setName(name);

		response.addInfoMessage("Envelope " + prevName + " is now " + name);

	}

	/* NOTE: CAP SETTING IS NOT CHECKED IN TRANSFER IT IS ONLY CHECKED FOR DEPOSITS INTO BANK ACCOUNT I.E DEPOSIT BANK STATEMENT*/
	//Transfers amount from e1 to e2
	public static void Transfer(ResponseTicket response, Envelope e1, Envelope e2, double amount) {


		//if e1 is null, it is a deposit
		if(e1 == null) {
			deposit(response, e2, amount);
			return;
		}
		//if e2 is null, it is a withdrawal
		if(e2 == null) {
			withdrawal(response, e1, amount);
			return;
		}

		if(e1.equals(e2)) {
			response.addErrorMessage("Cannot transfer to the same envelope");
			return;
		}

		if(amount <= 0) {
			response.addErrorMessage("Invalid envelope transfer, amount cannot be less than or equal to 0");
			return;
		}
		if(e1.getAmount() < amount) {
			response.addErrorMessage("Invalid envelope transfer, amount cannot be more than envelope amount");
			return;
		}
		
		double fromAmount = subtract(e1.getAmount(), amount);
		double toAmount = add(e2.getAmount(), amount);
		
		e1.setAmount(fromAmount);
		e2.setAmount(toAmount);

		
		response.addInfoMessage("$" + amount + " has been transferred from " + e1.getName() + " to " + e2.getName());

	}
	
	public static void depositIntoAll(ResponseTicket response, double amount) {
				
		
		double fullAmount = amount;
		
		//deposit into those with percent fill setting
		for(int index = 1; index <= EnvelopeAccess.getEnvelopes().size(); index++) {
			
			Envelope envelope = EnvelopeAccess.getEnvelopeByPriority(index);
			
			if(envelope.getFillSetting() == EnvelopeSettings.percentage) {
				
				// EX: 10 / 100 = 0.1
				double percentAmount = divide(envelope.getFillAmount(), 100);
				
				// EX: 500 * 0.1 = 50
				double tempAmount = multiply(fullAmount, percentAmount);
				
				//format temp amount
				int formattedAmount = (int)multiply(tempAmount, 100);
				tempAmount = divide(formattedAmount, 100);
				
				//deposit tempAmount
				EnvelopeActions.deposit(response, envelope, tempAmount);
				
				//subtract tempAmount from amount
				amount = subtract(amount, tempAmount);

				response.addInfoMessage("deposited " + tempAmount + " into " + envelope.getName());
			}

			if(amount == 0) return;
		}
		
		
		
		//deposit rest -- index == priority
		for(int index = 1; index <= EnvelopeAccess.getEnvelopes().size(); index++) {
			
			Envelope e = EnvelopeAccess.getEnvelopeByPriority(index);
			
			double amountToDeposit;
			
			switch(e.getFillSetting()) {
				default: throw new IllegalStateException("envelope has an invalid fill setting");
				case EnvelopeSettings.percentage: continue;
				
				case EnvelopeSettings.fill: {
					
					//envelope has a cap
					if(e.hasCap()) {

						//get amounts needed
						int capAmount = e.getCapAmount();
						double envelopeAmount = e.getAmount();

						//skip if envelope is full
						if(envelopeAmount >= capAmount) continue;

						//get max amount envelope can be deposited
						amountToDeposit = subtract(capAmount, envelopeAmount);

						//check if deposit amount is less than max amount
						if(amountToDeposit > amount) amountToDeposit = amount;

					}
					//envelope does not have a cap
					else {
						amountToDeposit = amount;
					}
					
					
				} break;
				
				case EnvelopeSettings.amount: {
					
					amountToDeposit = e.getFillAmount();
					//if envelope fill amount is greater than amount available, change amountToDeposit to amount available
					if(amountToDeposit > amount) amountToDeposit = amount;
					
					//checks cap amount
					if(e.hasCap()) {
						
						double amountTillFull = subtract(e.getCapAmount(), e.getAmount());
						//if envelope is full continue
						if(amountTillFull <= 0) continue;
						//if amount to deposit is bigger than amount for envelope to be full, amount to deposit is the amount till full
						if(amountToDeposit > amountTillFull) {
							amountToDeposit = amountTillFull;
						}
					}
					
				} break;
			}
			
			
			EnvelopeActions.deposit(response, e, amountToDeposit);
			amount = subtract(amount, amountToDeposit);
			//amount -= amountToDeposit;
			response.addInfoMessage("deposited $" + amountToDeposit + " into " + e.getName());
			
			if(amount == 0) return;	
		}
		
		
		//deposit left over amount into envelope marked extra if there is one, otherwise validate will put it into the 1st priority envelope
		ArrayList<Envelope> envelopes = EnvelopeAccess.getEnvelopes();
		for(int index = 0; index < envelopes.size(); index++) {
			if(!envelopes.get(index).isExtra()) continue;
			EnvelopeActions.deposit(response, envelopes.get(index), amount);
			response.addInfoMessage("deposited $" + amount + " into " + envelopes.get(index).getName());

		}
		
	}
	
	public static void deposit(ResponseTicket response, Envelope e, double amount) {
		e.setAmount(add(e.getAmount(), amount));
		//e.setAmount(e.getAmount() + amount);
		response.addInfoMessage("Envelope " + e.getName() + " has been deposited $" + amount);
		
	}

	
	public static void withdrawal(ResponseTicket response, Envelope e, double amount) {
		e.setAmount(subtract(e.getAmount(), amount));
		//e.setAmount(e.getAmount() - amount);
		response.addInfoMessage("$" + amount + " has been withdrawn from Envelope " + e.getName());

	}

	public static double withdrawFromEnvelope(ResponseTicket response, Envelope envelope, double amount) {
		
		if(envelope == null) {
			response.addInfoMessage("Cannot withdraw from envelope, envelope does not exist");
			return amount;
		}
		if(amount == 0) {
			response.addInfoMessage("Cannot withdraw $0 from " + envelope.getName());
			return 0;
		}
		
		//pointless to continue if envelope is empty
		if(envelope.getAmount() == 0) {
			response.addInfoMessage(envelope.getName() + " is already empty, cannot be withdrawn from");
			return amount;
		}
		
		
		
		//if envelope does not have enough to cover amount, withdraw what's possible
		if(envelope.getAmount() < amount) {
			amount = subtract(amount, envelope.getAmount());			
			withdrawal(response, envelope, envelope.getAmount());
		}
		//withdraw all of the amount
		else {
			withdrawal(response, envelope, amount);
			return 0;
		}
		
		
		return amount;
	}


	
	public static void withdrawFromAll(ResponseTicket response, double amount) {



		//loop from lowest to highest priority until looped through all envelopes or until amount is 0
		for(int index = EnvelopeAccess.getEnvelopes().size(); index > 0 && amount > 0; index--) {
			
			Envelope envelope = EnvelopeAccess.getEnvelopeByPriority(index);
			
			amount = withdrawFromEnvelope(response, envelope, amount);
			
		}
		
		if(amount == 0)
			response.addInfoMessage("Withdraw was successfull");
		else {
			response.addErrorMessage("Withdraw overdrafted account");
		}
		
	}

	

	//changes the fillSettings in envelope
	public static void EditSettings(ResponseTicket response, Envelope envelope, int fillSetting, int fillAmount) {

		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}

		if(envelope == null) {
			response.addErrorMessage("Envelope cannot be null");
			return;
		}

		if(envelope.getFillSetting() == fillSetting && envelope.getFillAmount() == fillAmount) {
			response.addInfoMessage("No change to Envelope fill settings");
			return;
		}

		if(fillAmount < 0 && fillSetting != EnvelopeSettings.fill) {
			response.addErrorMessage("Invalid envelope edit, fill amount cannot be less than 0");
			return;
		}

		//Checks and changes fill setting according to fillSetting requirements
		switch(fillSetting) {
			default:{
				response.addErrorMessage("Invalid envelope edit, Invalid fill setting");
				return;
			}
			case EnvelopeSettings.amount:{
	
				envelope.setFillSetting(fillSetting);
				envelope.setFillAmount(fillAmount);
				response.addInfoMessage("Fill setting has been changed to amount for " + envelope.getName());
				response.addInfoMessage("Fill amount has been set to " + fillAmount + " for " + envelope.getName());
	
	
			} break;
			case EnvelopeSettings.fill: {
				envelope.setFillSetting(fillSetting);
				envelope.setFillAmount(0);
				response.addInfoMessage("Fill setting has been changed to fill for " + envelope.getName());
	
			} break;
			case EnvelopeSettings.percentage: {
	
				int totalPercentage = getTotalFillPercentage(envelope);
	
				if(fillAmount > (100 - totalPercentage)) {
					response.addErrorMessage("Invalid envelope edit, total fill percentage cannot be greater than 100%");
					return;
				}
				envelope.setFillSetting(fillSetting);
				envelope.setFillAmount(fillAmount);
				response.addInfoMessage("Fill setting has been changed to percentage for " + envelope.getName());
				response.addInfoMessage("Fill percentage has been set to " + fillAmount + " for " + envelope.getName());
			}break;
		}

	}

	
	//changes the cap settings in envelope
	public static void EditCap(ResponseTicket response, Envelope envelope, boolean cap, int capAmount) {
		
		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(envelope == null) {
			response.addErrorMessage("Envelope cannot be null");
			return;
		}
		
		//no change happens
		if(envelope.hasCap() == cap && envelope.getCapAmount() == capAmount) {
			response.addInfoMessage("No change to envelope cap settings");
			return;
		}
		
		//invalid cap amount
		if(capAmount <= 0 && cap) {
			response.addErrorMessage("Invalid envelope edit, cap amount cannot be less than or equal to 0");
			return;
		}
		
		envelope.setCap(cap);
		
		if(cap) {
			envelope.setCapAmount(capAmount);
		}
		else {
			envelope.setCapAmount(0);
		}
		
		response.addInfoMessage("Envelope " + envelope.getName() + " now " + (cap?"has":"does not have") + " a cap");
		if(cap) {
			response.addInfoMessage("Envelope " + envelope.getName() + " now has a cap of " + capAmount);
		}
		
	}
	public static void EditExtra(ResponseTicket response, Envelope e, boolean extra) {
		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(e == null) {
			response.addErrorMessage("Envelope cannot be null");
			return;
		}
		if(e.isExtra() == extra) {
			response.addInfoMessage(e.getName() + " envelope's extra did not change");
			return;
		}
		//if extra given is not true
		if(!extra) {
			e.setExtra(extra);
		}
		//if extra given is true
		else {
			//setting envelope given to extra and rest to false
			ArrayList<Envelope> envelopes = EnvelopeAccess.getEnvelopes();
			for(int index = 0; index < envelopes.size(); index++) {
				if(envelopes.get(index).equals(e)) {
					envelopes.get(index).setExtra(extra);
				}
				else {
					envelopes.get(index).setExtra(false);
				}
			}
		}
		
	}
	public static void EditDefault(ResponseTicket response, Envelope e, boolean Default) {
		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(e == null) {
			response.addErrorMessage("Envelope cannot be null");
			return;
		}
		if(e.isDefault() == Default) {
			response.addInfoMessage(e.getName() + " envelope's Default did not change");
			return;
		}
		//if default given is not true
		if(!Default) {
			e.setDefault(Default);
		}
		//if default given is true
		else {
			//setting envelope given to extra and rest to false
			ArrayList<Envelope> envelopes = EnvelopeAccess.getEnvelopes();
			for(int index = 0; index < envelopes.size(); index++) {
				if(envelopes.get(index).equals(e)) {
					envelopes.get(index).setDefault(Default);
				}
				else {
					envelopes.get(index).setDefault(false);
				}
			}
		}
	}
	
	
	//does not count envelope given
	public static int getTotalFillPercentage(Envelope e) {
		ArrayList<Envelope> envelopes = EnvelopeAccess.getEnvelopes();
		
		
		int sum = 0;
		for(int index = 0; index < envelopes.size(); index++) {
			if(e.equals(envelopes.get(index))) continue;
			if(envelopes.get(index).getFillSetting() == EnvelopeSettings.percentage) {
				sum += envelopes.get(index).getFillAmount();
			}
		}
		
		return sum;
	}
	
}
