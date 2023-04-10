package dataAccess;

import java.util.ArrayList;

import dataObjects.Envelope;


public class EnvelopeAccess {

	private static ArrayList<Envelope> envelopes;


	public static void Initialize() {
		envelopes = new ArrayList<Envelope>();
	}

	private static int getNextPriority() {
		return envelopes.size() + 1;
	}

	public static ArrayList<Envelope> getEnvelopes(){
		return envelopes;
	}

	public static Envelope getEnvelopeByName(String name) {
		for(int index = 0; index < envelopes.size(); index++) {
			if(envelopes.get(index).getName().equals(name)) return envelopes.get(index);
		}
		return null;
	}

	public static Envelope addEnvelope(Envelope e) {
		Envelope temp = new Envelope(getNextPriority(), e.getName(), e.getAmount(), e.getFillSetting(), e.getFillAmount(), e.hasCap(), e.getCapAmount(), e.isExtra(), e.isDefault());
		envelopes.add(temp);
		return temp;
	}

	public static boolean removeEnvelope(String name) {
		Envelope temp = getEnvelopeByName(name);
		if(temp == null) return false;
		envelopes.remove(temp);
		return true;
	}

}
