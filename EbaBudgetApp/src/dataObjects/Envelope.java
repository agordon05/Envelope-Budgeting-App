package dataObjects;

public class Envelope {
	
	//order that envelopes will be filled, exeption is if fillSetting is set to percentage
	private int priority;
	//unique name of envelope
	private String name;
	//amount inside envelope
	private double amount;
	//setting for how an envelope will be filled
	private int fillSetting;
	//setting for amount to fill an envelope depending on fillSetting
	private int fillAmount;
	//if there is a cap for how much an envelope can be filled
	private boolean cap;
	//amount an envelope can be filled if there is a cap
	private int capAmount;
	//if envelope is extra, when desposit fills all envelopes, put the left over amount into this envelope
	private boolean extra;
	//if envelope is Default, when a withdraw amount is more than what an envelope has, take amount from here before other envelopes
	private boolean Default;
	
	public Envelope(int priority, String name, double amount, int fillSetting, int fillAmount, boolean cap, int capAmount, boolean extra, boolean Default) {
		this.priority = priority;
		this.name = name;
		this.amount = amount;
		this.fillSetting = fillSetting;
		this.fillAmount = fillAmount;
		this.cap = cap;
		this.capAmount = capAmount;
		this.extra = extra;
		this.Default = Default;
	}
	
	
	
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @return the fillSetting
	 */
	public int getFillSetting() {
		return fillSetting;
	}
	/**
	 * @return the fillAmount
	 */
	public int getFillAmount() {
		return fillAmount;
	}
	/**
	 * @return the cap
	 */
	public boolean hasCap() {
		return cap;
	}
	/**
	 * @return the capAmount
	 */
	public int getCapAmount() {
		return capAmount;
	}
	public boolean isExtra() {
		return this.extra;
	}
	public boolean isDefault() {
		return this.Default;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	/**
	 * @param fillSetting the fillSetting to set
	 */
	public void setFillSetting(int fillSetting) {
		this.fillSetting = fillSetting;
	}
	/**
	 * @param fillAmount the fillAmount to set
	 */
	public void setFillAmount(int fillAmount) {
		this.fillAmount = fillAmount;
	}
	/**
	 * @param cap the cap to set
	 */
	public void setCap(boolean cap) {
		this.cap = cap;
	}
	/**
	 * @param capAmount the capAmount to set
	 */
	public void setCapAmount(int capAmount) {
		this.capAmount = capAmount;
	}
	public void setExtra(boolean extra) {
		this.extra = extra;
	}
	public void setDefault(boolean Default) {
		this.Default = Default;
	}
	
	
	

}
