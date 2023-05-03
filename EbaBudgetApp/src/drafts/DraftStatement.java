package drafts;

import java.util.Date;

import actions.BalanceActions;
import actions.EnvelopeActions;
import actions.StatementActions;
import dataAccess.BalanceAccess;
import dataAccess.EnvelopeAccess;
import dataAccess.StatementAccess;
import dataAccess.VendorAccess;
import dataObjects.Balance;
import dataObjects.Envelope;
import dataObjects.Statement;
import dataObjects.Vendor;
import moduleObjects.StatementModule;
import tickets.ResponseTicket;

public class DraftStatement {

	//CREATES A STATEMENT AND ADJUSTS ENVELOPE AMOUNTS
	public static void createStatement(ResponseTicket response, StatementModule module) {

		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(module == null) {
			response.addErrorMessage("Statement module cannot be null");
			return;
		}

		//validate module
		//vendor cannot be null
		if(module.vendor == null || module.vendor.equals("")) {
			response.addErrorMessage("Vendor cannot be null or empty");
			return;
		}
		//if module < 0, it is a deposit
		if(module.amount < 0) {

		}
		//module amount cannot be 0
		if(module.amount == 0) {
			response.addErrorMessage("Amount cannot be 0");
			return;
		}
		//if date is null it is current date
		if(module.date == null) {
			module.date = new Date();
		}

		//valid vendor
		Vendor vendor = VendorAccess.getVendorByName(module.vendor);
		if(vendor == null) {
			vendor = VendorAccess.addVendor(new Vendor(module.vendor));
		}
		String prefName = vendor.getPrefName();
		String prefEnv = vendor.getPrefEnvelope();



		//create statement
		Statement statement = new Statement(0, (prefName.equals("")? vendor.getName(): prefName), vendor.getName(), module.amount, 0, module.pending, null, module.date);
		statement = StatementAccess.addStatement(statement);


		//start draft -- adjust envelopes
		if(!prefEnv.equals("")) {
			Envelope preferredEnvelope = EnvelopeAccess.getEnvelopeByName(prefName);
			if(preferredEnvelope == null) {
				response.addErrorMessage("Illegal state -- preferred envelope is null");
				return;
			}
			draftAmount(response, statement, preferredEnvelope, module.amount);		
		}

		//adjust account balance
		Balance balance = BalanceAccess.getBalance();
		BalanceActions.setAmount(response, balance, balance.getBalance() - module.amount);

		
		//return statement copy in response
		

	}
	public static void removeStatement(ResponseTicket response, StatementModule module) {

		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(module == null) {
			response.addErrorMessage("Statement module cannot be null");
			return;
		}

		if(!StatementAccess.removeStatement(module.id)) {
			response.addErrorMessage("Statement does not exist");
		}



	}	
	public static void editStatement(ResponseTicket response, StatementModule module) {

		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(module == null) {
			response.addErrorMessage("Statement module cannot be null");
			return;
		}

		//get statement
		Statement statement = StatementAccess.getStatementByID(module.id);

		if(statement == null) {
			response.addErrorMessage("Statement does not exist");
			return;
		}

		//validate edit
		//statement is not pending
		if(!statement.isPending()) {
			response.addErrorMessage("Invalid change, statement cannot be changed");
			return;
		}
		if(module.amount < 0) {
			response.addErrorMessage("Invalid change, amount cannot be negative");
			return;
		}
		if(module.pending && module.amount == 0) {
			response.addErrorMessage("Invalid change, statement amount cannot be 0");
		}

		StatementActions.change(response, statement, module.pending, module.amount);

		//return statement copy in response


	}	


	public static void getStatement(ResponseTicket response, StatementModule module) {
		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(module == null) {
			response.addErrorMessage("Statement module cannot be null");
			return;
		}


		//get statement
		Statement statement = StatementAccess.getStatementByID(module.id);

		//validate statement
		if(statement == null) {
			response.addErrorMessage("Statement does not exist");
			return;
		}

		//return statement copy in response





	}


	public static void TransferStatement(ResponseTicket response, StatementModule module) {

		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(module == null) {
			response.addErrorMessage("Statement module cannot be null");
			return;
		}

		if(module.amount <= 0) {
			response.addErrorMessage("cannot transfer an amount of 0");
			return;
		}


		Statement statement = StatementAccess.getStatementByID(module.id);
		if(statement == null) {
			response.addErrorMessage("Statement does not exist");
			return;
		}


		//validate envelopes
		Envelope e1 = null;
		Envelope e2 = null;

		if(module.envelope1 != null) {
			e1 = EnvelopeAccess.getEnvelopeByName(module.envelope1);
			if(e1 == null) {
				response.addErrorMessage("Envelope does not exist");
				return;
			}
		}
		if(module.envelope2 != null) {
			e2 = EnvelopeAccess.getEnvelopeByName(module.envelope2);
			if(e2 == null) {
				response.addErrorMessage("Envelope does not exist");
				return;
			}
		}

		if(e1 == null && e2 == null) {
			response.addErrorMessage("Both envelopes do not exist");
			return;
		}

		//check that e2 or null has enough to transfer to e1
		
		
		//transfer
		StatementActions.Transfer(response, statement, module.envelope1, module.envelope2, module.amount);
		EnvelopeActions.Transfer(response, e2, e1, module.amount);
		

		//return statement copy and envelope copies in response?


	}	

	private static boolean lastChance = false;
	private static boolean extraEnvelopeCalled = false;

	private static void draftAmount(ResponseTicket response, Statement statement, Envelope envelope, double amount) {

		if(statement == null) {
			response.addErrorMessage("Statement not found for transfer");
			return;
		}
		if(envelope == null) {
			response.addErrorMessage("Envelope not found for transfer");
			return;
		}

		//last chance was called
		if(lastChance) {
			lastDraft(response, statement, envelope, amount);
			lastChance = false;
		}
		//		//envelope cannot handle transfer, go ahead and transfer to next envelope
		//		else if(envelope.getAmount() == 0 && !extraEnvelopeCalled) {
		//
		//		}
		//Envelope cannot handle transfer
		else if(envelope.getAmount() <= amount) {
			overDraft(response, statement, envelope, amount);
		}
		//Envelope can handle transfer
		else {
			draftTransfer(response, statement, envelope, amount);
		}
	}
	private static void lastDraft(ResponseTicket response, Statement statement, Envelope envelope, double amount) {

		if(amount <= 0) {
			response.addErrorMessage("Error: cannot have transfer of a negative amount");
			return;
		}


		//envelope is not empty
		if(envelope.getAmount() != 0) {

			//envelope did not over draft
			if(envelope.getAmount() >= amount) {
				draftTransfer(response, statement, envelope, amount);
				return;
			}

			//envelope over drafted on last chance
			double transferAmount = envelope.getAmount();
			draftTransfer(response, statement, envelope, transferAmount);
			amount -= transferAmount;


		}



		Envelope prevPriority = EnvelopeAccess.getEnvelopeByPriority(envelope.getPriority() - 1);
		if(prevPriority == null) {
			//throw new Exception("ACCOUNT HAS OVERDRAFTED");
			response.addErrorMessage("Account has overdrafted");
			return;
		}
		draftAmount(response, statement, prevPriority, amount);




	}

	//confirmed that amount is greater than envelope amount
	private static void overDraft(ResponseTicket response, Statement statement, Envelope envelope, double amount) {


		double transferAmount = envelope.getAmount();
		if(transferAmount != 0) {
			draftTransfer(response, statement, envelope, transferAmount);
			amount -= transferAmount;
		}

		//extra envelope over drafted -- start lastdraft with last priority envelope
		if(extraEnvelopeCalled) {

			extraEnvelopeCalled = false;
			lastChance = true;
			Envelope lastPriority = EnvelopeAccess.getEnvelopeByPriority(EnvelopeAccess.getEnvelopes().size());
			if(lastPriority == null) throw new IllegalStateException("Invalid envelope priorities are set");
			draftAmount(response, statement, lastPriority, amount);
			return;
		}




		Envelope nextPriority = EnvelopeAccess.getEnvelopeByPriority(envelope.getPriority() + 1);

		//reached the end of the list
		if(nextPriority == null) {
			extraEnvelopeCalled = true;
			nextPriority = EnvelopeAccess.getExtraEnvelope();

			//there was no extra envelope -- start lastDraft algorithm
			if(nextPriority == null) {
				lastChance = true;
				draftAmount(response, statement, envelope, amount);
				return;
			}

		}

		//draft amount for next envelope
		draftAmount(response, statement, nextPriority, amount);



	}

	private static void draftTransfer(ResponseTicket response, Statement statement, Envelope envelope, double amount) {
		//		if(statement == null || envelope == null || amount <= 0) {
		//			response.addErrorMessage("cannot complete transfer");
		//			return;
		//		}
		StatementActions.Transfer(response, statement, null, envelope.getName(), amount);
		EnvelopeActions.Transfer(response, envelope, null, amount);
	}


}
