package dataObjects;

public class Vendor {

	private String name;
	private String prefName;
	private String prefEnvelope;
	
	public Vendor(String name, String prefName, String prefEnvelope) {
		this.name = name;
		this.prefName = prefName;
		this.prefEnvelope = prefEnvelope;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the prefName
	 */
	public String getPrefName() {
		return prefName;
	}

	/**
	 * @return the prefEnvelope
	 */
	public String getPrefEnvelope() {
		return prefEnvelope;
	}

	/**
	 * @param prefName the prefName to set
	 */
	public void setPrefName(String prefName) {
		this.prefName = prefName;
	}

	/**
	 * @param prefEnvelope the prefEnvelope to set
	 */
	public void setPrefEnvelope(String prefEnvelope) {
		this.prefEnvelope = prefEnvelope;
	}
	
	
}
