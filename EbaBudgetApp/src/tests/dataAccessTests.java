package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import data.Database;
import dataObjects.*;
import settings.EnvelopeSettings;


class dataAccessTests {


	//testing remove method to ensure it works properly
	@Test
	void EnvelopeAccess() {
		
//		EnvelopeAccess.Initialize();
		Envelope e1 = new Envelope(0, "e1", BigDecimal.ZERO, EnvelopeSettings.amount, 20, true, 50, false, false);
		Envelope e2 = new Envelope(0, "e2", BigDecimal.ZERO, EnvelopeSettings.amount, 20, true, 50, false, false);
		Envelope e3 = new Envelope(0, "e3", BigDecimal.ZERO, EnvelopeSettings.amount, 20, true, 50, false, false);
		Database.addEnvelope(e1);
		Database.addEnvelope(e2);
		Database.addEnvelope(e3);
		
		assertTrue(Database.getEnvelopes().size() == 3, "not all envelopes were added properly");
		boolean check = Database.removeEnvelope("e1");
		assertTrue(check, "remove envelope method did not find e1");
		List<Envelope> envList = Database.getEnvelopes();
		assertTrue(envList.size() == 2, "envelope not removed");
		for(int index = 0; index < envList.size(); index++) {
			assertFalse(envList.get(index).getName().equals("e1"), "e1 is still in list");
		}
		
	}
	
}
