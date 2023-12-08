package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import actions.EnvelopeActions;
import dataAccess.BalanceAccess;
import dataAccess.EnvelopeAccess;
import dataObjects.Balance;
import dataObjects.Envelope;
import settings.EnvelopeSettings;
import tickets.ResponseTicket;

class EnvelopeActionTests {

	private ResponseTicket initial() {
		EnvelopeAccess.Initialize();
		ResponseTicket response = new ResponseTicket();
		
		
		//int priority, String name, double amount, int fillSetting, double fillAmount, boolean cap, double capAmount
		Envelope e1 = new Envelope(0, "e1", 50, EnvelopeSettings.amount, 0, false, 0, false, false);
		Envelope e2 = new Envelope(0, "e2", 50, EnvelopeSettings.amount, 0, false, 0, false, false);
		Envelope e3 = new Envelope(0, "e3", 50, EnvelopeSettings.amount, 0, false, 0, false, false);
		Envelope e4 = new Envelope(0, "e4", 50, EnvelopeSettings.amount, 0, false, 0, false, false);
		EnvelopeAccess.addEnvelope(e1);
		EnvelopeAccess.addEnvelope(e2);
		EnvelopeAccess.addEnvelope(e3);
		EnvelopeAccess.addEnvelope(e4);
		
		assertTrue(EnvelopeAccess.getEnvelopes().size() == 4, "Envelope Access is not set up correctly");
		
		
		
		//BalanceAccess.addBalance(new Balance(250));
	
		return response;
	}
	
	
	@Test
	void SetPriority() {
		ResponseTicket response = initial();

		Envelope e1 = EnvelopeAccess.getEnvelopeByName("e1");
		Envelope e2 = EnvelopeAccess.getEnvelopeByName("e2");
		Envelope e3 = EnvelopeAccess.getEnvelopeByName("e3");
		Envelope e4 = EnvelopeAccess.getEnvelopeByName("e4");
		
		
		
		assertTrue(e1.getPriority() == 1, "Envelope Access did not set priority correctly");
		assertTrue(e2.getPriority() == 2, "Envelope Access did not set priority correctly");
		assertTrue(e3.getPriority() == 3, "Envelope Access did not set priority correctly");
		assertTrue(e4.getPriority() == 4, "Envelope Access did not set priority correctly");
		
		
		EnvelopeActions.setPriority(response, e4, 1);
		assertTrue(e1.getPriority() == 2, "Priority was not set correctly1");
		assertTrue(e2.getPriority() == 3, "Priority was not set correctly2");
		assertTrue(e3.getPriority() == 4, "Priority was not set correctly3");
		assertTrue(e4.getPriority() == 1, "Priority was not set correctly4");
		
		EnvelopeActions.setPriority(response, e4, 4);
		assertTrue(e1.getPriority() == 1, "Priority was not set correctly5");
		assertTrue(e2.getPriority() == 2, "Priority was not set correctly6");
		assertTrue(e3.getPriority() == 3, "Priority was not set correctly7");
		assertTrue(e4.getPriority() == 4, "Priority was not set correctly8");
		
		EnvelopeActions.setPriority(response, e4, 2);
		assertTrue(e1.getPriority() == 1, "Priority was not set correctly9");
		assertTrue(e2.getPriority() == 3, "Priority was not set correctly10");
		assertTrue(e3.getPriority() == 4, "Priority was not set correctly11");
		assertTrue(e4.getPriority() == 2, "Priority was not set correctly12");
		
		EnvelopeActions.setPriority(response, e4, 4);
		assertTrue(e1.getPriority() == 1, "Priority was not set correctly13");
		assertTrue(e2.getPriority() == 2, "Priority was not set correctly14");
		assertTrue(e3.getPriority() == 3, "Priority was not set correctly15");
		assertTrue(e4.getPriority() == 4, "Priority was not set correctly16");
				
		assertTrue(response.getErrorMessages().size() == 0, "There should be no error messages");
		
		
		EnvelopeActions.setPriority(response, e4, 5);
		assertTrue(response.getErrorMessages().size() == 1, "There should be one error messages");
		assertTrue(response.getErrorMessages().get(0).equals("Invalid envelope edit, prority outside of range"), "Incorrect error message");
		
		EnvelopeActions.setPriority(response, e4, 0);
		assertTrue(response.getErrorMessages().size() == 2, "There should be two error messages");
		assertTrue(response.getErrorMessages().get(1).equals("Invalid envelope edit, prority outside of range"), "Incorrect error message");

		Envelope e5 = new Envelope(0, "e5", 0, EnvelopeSettings.amount, 0, false, 0, false, false);

		EnvelopeActions.setPriority(response, e5, 2);
		assertTrue(response.getErrorMessages().size() == 3, "There should be three error messages");
		assertTrue(response.getErrorMessages().get(2).equals("Envelope is not in Envelope Access"), "Incorrect error message");
		
		EnvelopeActions.setPriority(response, null, 2);
		assertTrue(response.getErrorMessages().size() == 4, "There should be four error messages");
		assertTrue(response.getErrorMessages().get(3).equals("Envelope cannot be null"), "Incorrect error message: " + response.getErrorMessages().get(2));
		
		response = new ResponseTicket();
		EnvelopeActions.setPriority(response, e4, 4);
		assertTrue(response.getInfoMessages().size() == 1, "There should be one info messages");
		assertTrue(response.getInfoMessages().get(0).equals("No change to envelope priority"), "Incorrect info message");
		
	}
	
	@Test
	void EditName() {
		ResponseTicket response = initial();
		
		Envelope e1 = EnvelopeAccess.getEnvelopeByName("e1");

		
		
		Envelope e5 = new Envelope(0, "e5", 0, EnvelopeSettings.amount, 0, false, 0, false, false);

		EnvelopeActions.EditName(response, e1, "e20");
		assertTrue(e1.getName().equals("e20"));
		assertTrue(response.getErrorMessages().size() == 0);		

		EnvelopeActions.EditName(response, e1, "e1");
		assertTrue(response.getErrorMessages().size() == 0);		
		assertTrue(e1.getName().equals("e1"), "name should be e1 but was: " + e1.getName());

		
		
		EnvelopeActions.EditName(response, e1, "e2");
		assertTrue(e1.getName().equals("e1"), "name should be e1 but was: " + e1.getName());
		assertTrue(response.getErrorMessages().size() == 1, "There should be one error messages");
		assertTrue(response.getErrorMessages().get(0).equals("Invalid envelope edit, name is not unique"), "Incorrect error message");
				
		EnvelopeActions.EditName(response, e5, "e2");
		assertTrue(e5.getName().equals("e5"));
		assertTrue(response.getErrorMessages().size() == 2, "There should be two error messages");
		assertTrue(response.getErrorMessages().get(1).equals("Invalid envelope edit, name is not unique"), "Incorrect error message");
				
		
		
		EnvelopeActions.EditName(response, null, "e2");
		assertTrue(response.getErrorMessages().size() == 3, "There should be three error messages");
		assertTrue(response.getErrorMessages().get(2).equals("Envelope cannot be null"), "Incorrect error message");
				
		EnvelopeActions.EditName(response, e1, null);
		assertTrue(e1.getName().equals("e1"));
		assertTrue(response.getErrorMessages().size() == 4, "There should be four error messages");
		assertTrue(response.getErrorMessages().get(3).equals("Envelope name cannot be changed to null"), "Incorrect error message");
		
		response = new ResponseTicket();
		EnvelopeActions.EditName(response, e1, "e1");
		assertTrue(e1.getName().equals("e1"));
		assertTrue(response.getInfoMessages().size() == 1, "There should be one info messages");
		assertTrue(response.getInfoMessages().get(0).equals("No change to envelope name"), "Incorrect info message");
				
		
		
		
		
		
	
	
	}
	
	@Test
	void Transfer() {
		
		ResponseTicket response = initial();

		Envelope e1 = EnvelopeAccess.getEnvelopeByName("e1");
		Envelope e2 = EnvelopeAccess.getEnvelopeByName("e2");
		Envelope e3 = EnvelopeAccess.getEnvelopeByName("e3");
		Envelope e4 = EnvelopeAccess.getEnvelopeByName("e4");
		
		//valid transfer
		EnvelopeActions.Transfer(response, e1, e2, 10);
		assertTrue(e1.getAmount() == 40, "e1 Envelope should have $40");
		assertTrue(e2.getAmount() == 60, "e2 Envelope should have $60");
		assertTrue(response.getErrorMessages().size() == 0, "There should be no error messages");
		assertTrue(response.getInfoMessages().size() == 1, "There should be one info message");

		//valid withdrawal
		EnvelopeActions.Transfer(response, e1, null, 10);
		assertTrue(e1.getAmount() == 30, "e1 Envelope should have $30");
		assertTrue(response.getErrorMessages().size() == 0, "There should be no error messages");
		assertTrue(response.getInfoMessages().size() == 2, "There should be two info message");
		
		
		/*NOT TESTED -- TESTED IN DRAFT -- needs balance checked*/
		//EnvelopeActions.Transfer(response, null, e2, 10);
		
		//invalid transfer
		EnvelopeActions.Transfer(response, null, null, 10);
		assertTrue(response.getErrorMessages().size() == 1, "There should be one error messages");
		assertTrue(response.getInfoMessages().size() == 2, "There should be two info message");
		assertTrue(response.getErrorMessages().get(0).equals("Both envelopes cannot be null"), "Incorrect error message");

		//invalid transfer
		EnvelopeActions.Transfer(response, e1, e1, 10);
		assertTrue(e1.getAmount() == 30, "e1 Envelope should have $30");
		assertTrue(response.getErrorMessages().size() == 2, "There should be two error messages");
		assertTrue(response.getInfoMessages().size() == 2, "There should be two info message");
		assertTrue(response.getErrorMessages().get(1).equals("Cannot transfer to the same envelope"), "Incorrect error message");

		
		//invalid transfer
		EnvelopeActions.Transfer(response, e1, e2, -10);
		assertTrue(e1.getAmount() == 30, "e1 Envelope should have $30");
		assertTrue(response.getErrorMessages().size() == 3, "There should be three error messages but has " + response.getErrorMessages().size());
		assertTrue(response.getInfoMessages().size() == 2, "There should be two info message");
		assertTrue(response.getErrorMessages().get(2).equals("Cannot withdraw an amount less than or equal to 0"), "Incorrect error message");

		
		//invalid transfer
		EnvelopeActions.Transfer(response, e1, e2, 0);
		assertTrue(e1.getAmount() == 30, "e1 Envelope should have $30");
		assertTrue(e2.getAmount() == 60, "e2 Envelope should have $60");
		assertTrue(response.getErrorMessages().size() == 4, "There should be four error messages");
		assertTrue(response.getInfoMessages().size() == 2, "There should be two info message");
		assertTrue(response.getErrorMessages().get(3).equals("Cannot withdraw an amount less than or equal to 0"), "Incorrect error message");

		
		//invalid transfer
		EnvelopeActions.Transfer(response, e3, e4, 60);
		assertTrue(e3.getAmount() == 50, "e3 Envelope should have $50");
		assertTrue(e4.getAmount() == 50, "e4 Envelope should have $50");
		assertTrue(response.getErrorMessages().size() == 5, "There should be five error messages");
		assertTrue(response.getInfoMessages().size() == 2, "There should be two info message");
		assertTrue(response.getErrorMessages().get(4).equals("Invalid envelope transfer, amount cannot be more than envelope amount"), "Incorrect error message");

		response.printMessages();
		
		//invalid withdrawal
		EnvelopeActions.Transfer(response, e3, null, 60);
		assertTrue(e3.getAmount() == 50, "e3 Envelope should have $50 but has " + e3.getAmount());
		assertTrue(response.getErrorMessages().size() == 6, "There should be six error messages");
		assertTrue(response.getInfoMessages().size() == 2, "There should be two info message");
		assertTrue(response.getErrorMessages().get(5).equals("Invalid Envelope Transfer, Insufficient funds"), "Incorrect error message");

		System.out.println(e3.getAmount() + "");
		//invalid withdrawal
		EnvelopeActions.Transfer(response, e3, null, -10);
		assertTrue(e3.getAmount() == 50, "e3 Envelope should have $50 but has " + e3.getAmount());
		assertTrue(response.getErrorMessages().size() == 7, "There should be seven error messages");
		assertTrue(response.getInfoMessages().size() == 2, "There should be two info message");
		assertTrue(response.getErrorMessages().get(6).equals("Cannot withdraw an amount less than or equal to 0"), "Incorrect error message");

		
		
		/*NOT TESTED -- TESTED IN DRAFT -- needs balance checked*/ 
		//invalid deposit
		//EnvelopeActions.Transfer(response, null, e4, 60);
		
	}
	@Test
	void EditSettings() {
		
		ResponseTicket response = initial();

		Envelope e1 = EnvelopeAccess.getEnvelopeByName("e1");
		Envelope e2 = EnvelopeAccess.getEnvelopeByName("e2");
		Envelope e3 = EnvelopeAccess.getEnvelopeByName("e3");
		Envelope e4 = EnvelopeAccess.getEnvelopeByName("e4");
		
		/*AMOUNT*/
		//valid amount setting
		EnvelopeActions.EditSettings(response, e1, EnvelopeSettings.amount, 0);
		assertTrue(response.getErrorMessages().size() == 0, "response should have zero error messages");
		assertTrue(e1.getFillSetting() == EnvelopeSettings.amount, "e1 fill settings should be set to amount");
		assertTrue(e1.getFillAmount() == 0, "e1 fill amount should be 0");
		
		//invalid amount setting
		EnvelopeActions.EditSettings(response, e1, EnvelopeSettings.amount, -21);
		assertTrue(response.getErrorMessages().size() == 1, "response should have one error message");
		assertTrue(response.getErrorMessages().get(0).equals("Invalid envelope edit, fill amount cannot be less than 0"), "Incorrect error message");
		assertTrue(e1.getFillSetting() == EnvelopeSettings.amount, "e1 fill settings should be set to amount");
		assertTrue(e1.getFillAmount() == 0, "e1 fill amount should be 0");
		
		//valid amount setting
		EnvelopeActions.EditSettings(response, e1, EnvelopeSettings.amount, 50);
		assertTrue(response.getErrorMessages().size() == 1, "response should have one error message");
		assertTrue(e1.getFillSetting() == EnvelopeSettings.amount, "e1 fill settings should be set to amount");
		assertTrue(e1.getFillAmount() == 50, "e1 fill amount should be 50 but was " + e1.getFillAmount());
		
		/*FILL*/
		//valid fill setting
		EnvelopeActions.EditSettings(response, e2, EnvelopeSettings.fill, 0);
		assertTrue(response.getErrorMessages().size() == 1, "response should have one error message");
		assertTrue(e2.getFillSetting() == EnvelopeSettings.fill, "e2 fill settings should be set to fill");
		assertTrue(e2.getFillAmount() == 0, "e2 fill amount should be 0");
		
		
		//valid fill setting
		EnvelopeActions.EditSettings(response, e2, EnvelopeSettings.fill, 25);
		assertTrue(response.getErrorMessages().size() == 1, "response should have one error message");
		assertTrue(e2.getFillSetting() == EnvelopeSettings.fill, "e2 fill settings should be set to fill");
		assertTrue(e2.getFillAmount() == 0, "e2 fill amount should be 0");
		
		
		//valid fill setting
		EnvelopeActions.EditSettings(response, e2, EnvelopeSettings.fill, -10);
		assertTrue(response.getErrorMessages().size() == 1, "response should have one error message");
		assertTrue(e2.getFillSetting() == EnvelopeSettings.fill, "e2 fill settings should be set to fill");
		assertTrue(e2.getFillAmount() == 0, "e1 fill amount should be 0");
		
		/*PERCENTAGE*/
		//valid percentage settings
		EnvelopeActions.EditSettings(response, e1, EnvelopeSettings.percentage, 20);
		assertTrue(response.getErrorMessages().size() == 1, "response should have one error message");
		assertTrue(e1.getFillSetting() == EnvelopeSettings.percentage, "e1 fill settings should be set to percentage");
		assertTrue(e1.getFillAmount() == 20, "e1 fill amount should be 20");
		
		EnvelopeActions.EditSettings(response, e2, EnvelopeSettings.percentage, 30);
		assertTrue(response.getErrorMessages().size() == 1, "response should have one error message");
		assertTrue(e2.getFillSetting() == EnvelopeSettings.percentage, "e2 fill settings should be set to percentage");
		assertTrue(e2.getFillAmount() == 30, "e2 fill amount should be 30");
		
		EnvelopeActions.EditSettings(response, e3, EnvelopeSettings.percentage, 50);
		assertTrue(response.getErrorMessages().size() == 1, "response should have one error message");
		assertTrue(e3.getFillSetting() == EnvelopeSettings.percentage, "e3 fill settings should be set to percentage");
		assertTrue(e3.getFillAmount() == 50, "e3 fill amount should be 50");
		
		//invalid percentage settings
		EnvelopeActions.EditSettings(response, e1, EnvelopeSettings.percentage, 25);
		assertTrue(response.getErrorMessages().size() == 2, "response should have two error messages");
		assertTrue(response.getErrorMessages().get(1).equals("Invalid envelope edit, total fill percentage cannot be greater than 100%"), "Incorrect error message");
		assertTrue(e1.getFillSetting() == EnvelopeSettings.percentage, "e1 fill settings should be set to amount");
		assertTrue(e1.getFillAmount() == 20, "e1 fill amount should be 20");
		
		
		//invalid percentage setting
		EnvelopeActions.EditSettings(response, e1, EnvelopeSettings.percentage, -10);
		assertTrue(response.getErrorMessages().size() == 3, "response should have three error messages");
		assertTrue(response.getErrorMessages().get(2).equals("Invalid envelope edit, fill amount cannot be less than 0"), "Incorrect error message");
		assertTrue(e1.getFillSetting() == EnvelopeSettings.percentage, "e1 fill settings should be set to amount");
		assertTrue(e1.getFillAmount() == 20, "e1 fill amount should be 20");
		
		
		/*UNKNOWN*/
		EnvelopeActions.EditSettings(response, e1, 43, 10);
		assertTrue(response.getErrorMessages().size() == 4, "response should have four error messages");
		assertTrue(response.getErrorMessages().get(3).equals("Invalid envelope edit, Invalid fill setting"), "Incorrect error message");
		assertTrue(e1.getFillSetting() == EnvelopeSettings.percentage, "e1 fill settings should be set to amount");
		assertTrue(e1.getFillAmount() == 20, "e1 fill amount should be 20");

		//invalid
		EnvelopeActions.EditSettings(response, null, EnvelopeSettings.fill, 0);
		assertTrue(response.getErrorMessages().size() == 5, "response should have five error messages");
		assertTrue(response.getErrorMessages().get(4).equals("Envelope cannot be null"), "Incorrect error message");
		
				
	}
	@Test
	void EditCap() {
		
		ResponseTicket response = initial();

		Envelope e1 = EnvelopeAccess.getEnvelopeByName("e1");
		Envelope e2 = EnvelopeAccess.getEnvelopeByName("e2");
		Envelope e3 = EnvelopeAccess.getEnvelopeByName("e3");
		Envelope e4 = EnvelopeAccess.getEnvelopeByName("e4");
		
		
		//valid cap
		EnvelopeActions.EditCap(response, e1, true, 150);
		assertTrue(response.getErrorMessages().size() == 0, "There should be no error messages");
		assertTrue(e1.hasCap() == true, "e1 cap should be true");
		assertTrue(e1.getCapAmount() == 150, "e1 cap amount should be 150");
		

		//valid cap
		EnvelopeActions.EditCap(response, e1, false, 150);
		assertTrue(response.getErrorMessages().size() == 0, "There should be no error messages");
		assertTrue(e1.hasCap() == false, "e1 cap should be false");
		assertTrue(e1.getCapAmount() == 0, "e1 cap amount should be 0");
		
		//valid cap
		EnvelopeActions.EditCap(response, e1, false, 0);
		assertTrue(response.getErrorMessages().size() == 0, "There should be no error messages");
		assertTrue(e1.hasCap() == false, "e1 cap should be false");
		assertTrue(e1.getCapAmount() == 0, "e1 cap amount should be 0");
		
		//valid cap
		EnvelopeActions.EditCap(response, e1, false, -150);
		assertTrue(response.getErrorMessages().size() == 0, "There should be no error messages");
		assertTrue(e1.hasCap() == false, "e1 cap should be false");
		assertTrue(e1.getCapAmount() == 0, "e1 cap amount should be 0");
		
		//invalid cap
		EnvelopeActions.EditCap(response, e1, true, 0);
		assertTrue(response.getErrorMessages().size() == 1, "response should have one error message");
		assertTrue(response.getErrorMessages().get(0).equals("Invalid envelope edit, cap amount cannot be less than or equal to 0"), "Incorrect error message");
		assertTrue(e1.hasCap() == false, "cap boolean should not have changed");
		
				//invalid cap
		EnvelopeActions.EditCap(response, e1, true, -150);
		assertTrue(response.getErrorMessages().size() == 2, "response should have two error message");
		assertTrue(response.getErrorMessages().get(1).equals("Invalid envelope edit, cap amount cannot be less than or equal to 0"), "Incorrect error message");
		assertTrue(e1.hasCap() == false, "cap boolean should not have changed");
		assertTrue(e1.getCapAmount() == 0, "e1 cap amount should be 0");
		
		
		//invalid edit
		EnvelopeActions.EditCap(response, null, false, 0);
		assertTrue(response.getErrorMessages().size() == 3, "response should have three error message");
		assertTrue(response.getErrorMessages().get(2).equals("Envelope cannot be null"), "Incorrect error message");

		
	}


}
