package dataObjects;

public class Vendor {

	//vendor name
	private String name;
	//preferred name user set for vendor
	private String prefName;
	//preferred envelope for statements made by this envelope
	private String prefEnvelope;
	
	public Vendor(String name) {
		this.name = name;
		this.prefName = "";
		this.prefEnvelope = "";
	}
	
	
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
