package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dataAccess.*;
import dataObjects.*;
import settings.EnvelopeSettings;


class dataAccessTests {


	//testing remove method to ensure it works properly
	@Test
	void EnvelopeAccess() {
		EnvelopeAccess.Initialize();
		Envelope e1 = new Envelope(0, "e1", 0, EnvelopeSettings.amount, 20, true, 50);
		Envelope e2 = new Envelope(0, "e2", 0, EnvelopeSettings.amount, 20, true, 50);
		Envelope e3 = new Envelope(0, "e3", 0, EnvelopeSettings.amount, 20, true, 50);
		EnvelopeAccess.addEnvelope(e1);
		EnvelopeAccess.addEnvelope(e2);
		EnvelopeAccess.addEnvelope(e3);
		
		assertTrue(EnvelopeAccess.getEnvelopes().size() == 3, "not all envelopes were added properly");
		boolean check = EnvelopeAccess.removeEnvelope("e1");
		assertTrue(check, "remove envelope method did not find e1");
		ArrayList<Envelope> envList = EnvelopeAccess.getEnvelopes();
		assertTrue(envList.size() == 2, "envelope not removed");
		for(int index = 0; index < envList.size(); index++) {
			assertFalse(envList.get(index).getName().equals("e1"), "e1 is still in list");
		}
		
	}
	
}
