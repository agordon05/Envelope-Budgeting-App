package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;

import actions.StatementActions;
import dataObjects.Envelope;
import dataObjects.Statement;
import dataObjects.StatementSplits;
import tickets.ResponseTicket;

class StatementActionTests {


	@Test
	//valid tests
	void Transfer1() {
		ResponseTicket response = new ResponseTicket();
		
		//int id, String name, String vendor, double amount, double tip, boolean pending, ArrayList<StatementSplits> envAmount, Date date)
		Statement s1 = new Statement(0, "s1", null, 50, 0, true, null, null);
		Statement s2 = new Statement(0, "s2", null, 50, 50, true, null, null);
		//int priority, String name, double amount, int fillSetting, double fillAmount, boolean cap, double capAmount
		Envelope e1 = new Envelope(0, "e1", 0, 0, 0, false, 0, false, false);
		Envelope e2 = new Envelope(0, "e2", 0, 0, 0, false, 0, false, false);
		
		ArrayList<StatementSplits> splits = new ArrayList<>();
		
		StatementActions.Transfer(response, s1, null, e2.getName(), 10);
		assertTrue(response.getErrorMessages().size() == 0, "transfer 1 should be valid");
		splits = s1.getEnvAmount();
		assertTrue(splits.size() == 1, "s1 should have a statement split");
		StatementSplits split = splits.get(0);
		assertTrue(split.envelope.equals("e2"), "s1 split should have e2 envelope");
		assertTrue(split.amount == 10, "s1 split should have $10");
		
		
		StatementActions.Transfer(response, s1, e2.getName(), e1.getName(), 10);
		assertTrue(response.getErrorMessages().size() == 0, "transfer 2 should be valid");
		splits = s1.getEnvAmount();
		assertTrue(splits.size() == 1, "s1 should have only 1 statement split");
		split = splits.get(0);
		assertTrue(split.envelope.equals("e1"), "s1 split should have e1 envelope");
		assertTrue(split.amount == 10, "s1 split should have $10");
		
		
		StatementActions.Transfer(response, s2, null, e1.getName(), 65);
		assertTrue(response.getErrorMessages().size() == 0, "transfer 3 should be valid");
		splits = s2.getEnvAmount();
		assertTrue(splits.size() == 1, "s2 should have only 1 statement split");
		split = splits.get(0);
		assertTrue(split.envelope.equals("e1"), "s2 split should have e1 envelope");
		assertTrue(split.amount == 65, "s1 split should have $65");
		
		
		StatementActions.Transfer(response, s2, e1.getName(), e2.getName(), 60);
		assertTrue(response.getErrorMessages().size() == 0, "transfer 4 should be valid");
		splits = s2.getEnvAmount();
		assertTrue(splits.size() == 2, "s2 should have 2 statement splits");
		split = splits.get(0);
		StatementSplits split2 = splits.get(1);
		assertTrue(split.envelope.equals("e1"), "s2 split should have e1 envelope");
		assertTrue(split2.envelope.equals("e2"), "s2 split should have e2 envelope");

		assertTrue(split.amount == 5, "s1 first split should have $5");
		assertTrue(split2.amount == 60, "s1 second split should have $60");
		
		StatementActions.Transfer(response, s2, e2.getName(), null, 50);
		assertTrue(response.getErrorMessages().size() == 0, "transfer 5 should be valid");
		splits = s2.getEnvAmount();
		assertTrue(splits.size() == 2, "s2 should have 2 statement splits");
		split = splits.get(0);
		split2 = splits.get(1);
		assertTrue(split.envelope.equals("e1"), "s2 split should have e1 envelope");
		assertTrue(split2.envelope.equals("e2"), "s2 split2 should have e2 envelope");
		assertTrue(split.amount == 5, "s1 split should have $5");
		assertTrue(split2.amount == 10, "s1 split2 should have $10");
		
		StatementActions.Transfer(response, s2, e2.getName(), null, 10);
		assertTrue(response.getErrorMessages().size() == 0, "transfer 6 should be valid");
		splits = s2.getEnvAmount();
		assertTrue(splits.size() == 1, "s2 should have only have 1 statement splits");
		split = splits.get(0);
		assertTrue(split.envelope.equals("e1"), "s2 split should have e1 envelope");
		assertTrue(split.amount == 5, "s1 split should have $5");				
	}
	
	@Test
	//invalid tests
	void Transfer2() {
		ResponseTicket response = new ResponseTicket();
		
		//int id, String name, String vendor, double amount, double tip, boolean pending, ArrayList<StatementSplits> envAmount, Date date)
		Statement s1 = new Statement(0, "s1", null, 50, 0, true, null, null);
		Statement s2 = new Statement(0, "s2", null, 50, 50, true, null, null);
		//int priority, String name, double amount, int fillSetting, double fillAmount, boolean cap, double capAmount
		Envelope e1 = new Envelope(0, "e1", 0, 0, 0, false, 0, false, false);
		Envelope e2 = new Envelope(0, "e2", 0, 0, 0, false, 0, false, false);
		
				
		//no statement split for e1
		StatementActions.Transfer(response, s1, e1.getName(), e2.getName(), 50);
		assertTrue(response.getErrorMessages().size() == 1, "cannot transfer from envelope with no split");
		assertTrue(response.getErrorMessages().get(0).equals("Invalid transfer, split statement for e1 does not exist"), "Incorrect error message");
		assertTrue(s1.getEnvAmount().size() == 0, "there should be no statement split");
		
		//amount negative
		StatementActions.Transfer(response, s1, null, e1.getName(), -20);
		assertTrue(response.getErrorMessages().size() == 2, "cannot transfer a negative amount");
		assertTrue(response.getErrorMessages().get(1).equals("Invalid transfer, amount cannot be less than or equal to 0"), "Incorrect error message");
		assertTrue(s1.getEnvAmount().size() == 0, "there should be no statement split");

		//amount 0
		StatementActions.Transfer(response, s1, null, e1.getName(), 0);
		assertTrue(response.getErrorMessages().size() == 3, "cannot transfer a zero amount");
		assertTrue(response.getErrorMessages().get(2).equals("Invalid transfer, amount cannot be less than or equal to 0"), "Incorrect error message");
		assertTrue(s1.getEnvAmount().size() == 0, "there should be no statement split");

		
		//amount above statement split
		StatementActions.Transfer(response, s1, null, e1.getName(), 10);
		StatementActions.Transfer(response, s1, e1.getName(), e2.getName(), 15);
		assertTrue(response.getErrorMessages().size() == 4, "cannot transfer above amount in envelope");
		assertTrue(response.getErrorMessages().get(3).equals("Invalid transfer, insufficient funds allocated to e1 envelope"), "Incorrect error message");
		assertTrue(s1.getEnvAmount().size() == 1, "there should be 1 statement split");

		
		
		//amount above statement amount
		StatementActions.Transfer(response, s2, null, e1.getName(), 110);
		assertTrue(response.getErrorMessages().size() == 5, "cannot transfer above amount in statement");
		assertTrue(response.getErrorMessages().get(4).equals("Invalid transfer, insufficient funds in statement"), "Incorrect error message");
		assertTrue(s2.getEnvAmount().size() == 0, "there should be no statement split");

		//both envelopes null
		StatementActions.Transfer(response, s2, null, null, 20);
		assertTrue(response.getErrorMessages().size() == 6, "cannot transfer with envelopes both being null");
		assertTrue(response.getErrorMessages().get(5).equals("Invalid transfer, both envelopes cannot be null"), "Incorrect error message");

		//statement null
		StatementActions.Transfer(response, null, e1.getName(), e2.getName(), 20);
		assertTrue(response.getErrorMessages().size() == 7, "cannot transfer with statement being null");
		assertTrue(response.getErrorMessages().get(6).equals("Statement does not exist"), "Incorrect error message");

		//response null
		
		
		
		
		
		
		
	}

	
	@Test
	void Change() {
		
		ResponseTicket response = new ResponseTicket();
		
		//int id, String name, String vendor, double amount, double tip, boolean pending, ArrayList<StatementSplits> envAmount, Date date)
		Statement s1 = new Statement(0, "s1", null, 50, 0, true, null, null);

		
		StatementActions.change(response, s1, true, 25);
		assertTrue(response.getErrorMessages().size() == 0, "There should be no error messages");
		assertTrue(s1.getAmount() == 50, "Statement amount should not have been changed");
		assertTrue(s1.getTip() == 25, "Statement tip should have been changed to 25");
		assertTrue(s1.isPending(), "Statement pending should still be true");
		
		StatementActions.change(response, s1, true, 0);
		assertTrue(response.getErrorMessages().size() == 0, "There should be no error messages");
		assertTrue(s1.getAmount() == 50, "Statement amount should not have been changed");
		assertTrue(s1.getTip() == 0, "Statement tip should have been changed to 0");
		assertTrue(s1.isPending(), "Statement pending should still be true");
		
		StatementActions.change(response, s1, true, -10);
		assertTrue(response.getErrorMessages().size() == 1, "There should be 1 error message");
		assertTrue(response.getErrorMessages().get(0).equals("Invalid change, amount cannot be negative"), "Incorrect error message");
		assertTrue(s1.getAmount() == 50, "Statement amount should not have been changed");
		assertTrue(s1.getTip() == 0, "Statement tip should have been changed to 0");
		assertTrue(s1.isPending(), "Statement pending should still be true");
		
		
		StatementActions.change(response, s1, true, 25);
		StatementActions.change(response, s1, false, 75);
		assertTrue(response.getErrorMessages().size() == 1, "There should be 1 error message");
		assertTrue(s1.getAmount() == 75, "Statement amount should have been changed to $75");
		assertTrue(s1.getTip() == 0, "Statement tip should have been changed to 0");
		assertTrue(!s1.isPending(), "Statement pending should be false");
		
		StatementActions.change(response, s1, true, 25);
		assertTrue(response.getErrorMessages().size() == 2, "There should be 2 error message");
		assertTrue(response.getErrorMessages().get(1).equals("Invalid change, statement cannot be changed"), "Incorrect error message");
		assertTrue(s1.getAmount() == 75, "Statement amount should not have been changed");
		assertTrue(s1.getTip() == 0, "Statement tip should still be 0");
		assertTrue(!s1.isPending(), "Statement pending should still be false");
		
		
		StatementActions.change(response, s1, false, 50);
		assertTrue(response.getErrorMessages().size() == 3, "There should be 3 error message");
		assertTrue(response.getErrorMessages().get(2).equals("Invalid change, statement cannot be changed"), "Incorrect error message");
		assertTrue(s1.getAmount() == 75, "Statement amount should not have been changed");
		assertTrue(s1.getTip() == 0, "Statement tip should still be 0");
		assertTrue(!s1.isPending(), "Statement pending should still be false");
		
		
		
		StatementActions.change(response, null, true, 15);
		assertTrue(response.getErrorMessages().size() == 4, "There should be 4 error message");
		assertTrue(response.getErrorMessages().get(3).equals("Statement does not exist"), "Incorrect error message");

			
	}

}
