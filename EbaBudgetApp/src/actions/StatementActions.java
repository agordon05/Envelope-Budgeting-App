package actions;

import java.util.ArrayList;

import dataObjects.Envelope;
import dataObjects.Statement;
import dataObjects.StatementSplits;
import tickets.ResponseTicket;

public class StatementActions {

	//Transfer amount -- transfer amount from envelope 1 to envelope 2 in the statement splits
	public static void Transfer(ResponseTicket response, Statement statement, Envelope e1, Envelope e2, double amount) {

		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(statement == null) {
			response.addErrorMessage("Statement does not exist");
			return;
		}
		if(e1 == null && e2 == null) {
			response.addErrorMessage("Invalid transfer, both envelopes cannot be null");
			return;
		}
		if(e1 != null && e2 != null && e1.equals(e2)) {
			response.addErrorMessage("Invalid transfer, cannot transfer to same envelope");
			return;
		}
		if(amount <= 0) {
			response.addErrorMessage("Invalid transfer, amount cannot be less than or equal to 0");
			return;
		}

		ArrayList<StatementSplits> splits = statement.getEnvAmount();
		StatementSplits split1 = null;
		StatementSplits split2 = null;
		
		if(e1 != null) split1 = getSplitByEnvelope(splits, e1);
		if(e2 != null) split2 = getSplitByEnvelope(splits, e2);
		
		//if envelope given exists but split for it doesn't exist
		//if split1 does not exist then there is no split to transfer from
		if(split1 == null && e1 != null) {
			response.addErrorMessage("Invalid transfer, split statement for " + e1.getName() + " does not exist");
			//removing split if its amount is 0
			if(split2 != null && split2.amount == 0) statement.getEnvAmount().remove(split2);
			return;
		}
		//if split2 does not exist then a split will be made
		if(split2 == null && e2 != null) {
			split2 = new StatementSplits(e2.getName(), 0);
			statement.getEnvAmount().add(split2);
		}

		
		
		
		
		//gets total amount not allocated towards any envelopes
		double totalAmount = statement.getAmount() + statement.getTip() - getTotalSplitAmount(splits);

		//transferring money not associated with any envelopes
		if(split1 == null) {

			if(amount > totalAmount) {
				response.addErrorMessage("Invalid transfer, insufficient funds in statement");
				//removing split if its amount is 0
				if(split2.amount == 0) statement.getEnvAmount().remove(split2);
				return;
			}
			split2.amount += amount;


		}
		//removing money from envelope
		else if(split2 == null) {

			if(amount > split1.amount) {
				response.addErrorMessage("Invalid transfer, insufficient funds allocated to " + split1.envelope + " envelope");
				return;
			}
			split1.amount -= amount;

		}
		//Transferring money allocated from one envelope to another
		else {

			//amount to transfer is larger than the amount in the statement split
			if(amount > split1.amount) {
				response.addErrorMessage("Invalid transfer, insufficient funds allocated to " + split1.envelope + " envelope");
				//removing split if its amount is 0
				if(split2.amount == 0) statement.getEnvAmount().remove(split2);
				return;
			}

			split1.amount -= amount;
			split2.amount += amount;

		}



		//removing split if its amount is 0
		if(split1 != null && split1.amount == 0) {
			splits.remove(split1);
		}

	}



	//change Statement -- 2 actions -- set tip or set pending and actual amount
	public static void change(ResponseTicket response, Statement statement, boolean pending, double amount) {

		if(response == null) {
			throw new IllegalArgumentException("Response is null");
		}
		if(statement == null) {
			response.addErrorMessage("Statement does not exist");
			return;
		}

		//statement is not pending
		if(!statement.isPending()) {
			response.addErrorMessage("Invalid change, statement cannot be changed");
			return;
		}
		
		if(amount < 0) {
			response.addErrorMessage("Invalid change, amount cannot be negative");
			return;
		}



		//the pending for the statement does not change, amount is the tip
		if(pending) {
			statement.setTip(amount);
		}
		//the statement is no longer pending, remove tip and set true amount
		else {
			statement.setPending(false);
			statement.setTip(0);
			statement.setAmount(amount);
		}

		
	}




	private static double getTotalSplitAmount(ArrayList<StatementSplits> splits) {
		if(splits == null) throw new NullPointerException("Splits cannot be null");
		double sum = 0;

		for(int index = 0; index < splits.size(); index++) {
			sum += splits.get(index).amount;
		}

		return sum;
	}
	private static StatementSplits getSplitByEnvelope(ArrayList<StatementSplits> splits, Envelope e) {
		if(e == null) return null;
		return getSplitByEnvelope(splits, e.getName());
	}
	
	
	private static StatementSplits getSplitByEnvelope(ArrayList<StatementSplits> splits, String envelope) {
		if(envelope == null) return null;
		if(splits == null) throw new NullPointerException("Splits cannot be null");

		for(int index = 0; index < splits.size(); index++) {
			if(splits.get(index).envelope.equals(envelope)) return splits.get(index);
		}

		return null;
	}


	
}
