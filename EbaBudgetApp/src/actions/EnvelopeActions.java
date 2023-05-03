package actions;

import java.util.ArrayList;

import dataAccess.EnvelopeAccess;
import dataObjects.Envelope;
import settings.EnvelopeSettings;
import tickets.ResponseTicket;

public class EnvelopeActions {

	//sets the priority of envelope, changes the rest accordingly
	public static void setPriority(ResponseTicket response, Envelope envelope, int priority) {

		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(envelope == null) {
			response.addErrorMessage("Envelope cannot be null");
			return;
		}
//		Envelope e = EnvelopeAccess.getEnvelopeByName(envelope.getName());
//		if(e == null) {
//			response.addErrorMessage("Envelope is not in Envelope Access");
//			return;
//		}

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

		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(e1 == null && e2 == null) {
			response.addErrorMessage("Both envelopes cannot be null");
			return;
		}


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

		e1.setAmount(e1.getAmount() - amount);
		e2.setAmount(e2.getAmount() + amount);

		response.addInfoMessage("$" + amount + " has been transferred from " + e1.getName() + " to " + e2.getName());

	}

	private static void deposit(ResponseTicket response, Envelope e, double amount) {
		if(amount <= 0) {
			response.addErrorMessage("Cannot deposit an amount less than or equal to 0");
			return;
		}

		e.setAmount(e.getAmount() + amount);

		response.addInfoMessage("Envelope " + e.getName() + "has been deposited $" + amount);
	}

	private static void withdrawal(ResponseTicket response, Envelope e, double amount) {

		if(amount <= 0) {
			response.addErrorMessage("Cannot withdraw an amount less than or equal to 0");
			return;
		}

		if(e.getAmount() < amount) {
			response.addErrorMessage("Invalid Envelope Transfer, Insufficient funds");
			return;
		}

		e.setAmount(e.getAmount() - amount);

		response.addInfoMessage("$" + amount + " has been withdrawn from Envelope " + e.getName());

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
	
	
	private static int getTotalFillPercentage(Envelope e) {
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
