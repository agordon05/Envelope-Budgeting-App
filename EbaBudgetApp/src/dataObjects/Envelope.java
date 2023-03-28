package dataObjects;

public class Envelope {
	
	private int priority;
	private String name;
	private double amount;
	private int fillSetting;
	private double fillAmount;
	private boolean cap;
	private double capAmount;
	
	public Envelope(int priority, String name, double amount, int fillSetting, double fillAmount, boolean cap, double capAmount) {
		this.priority = priority;
		this.name = name;
		this.amount = amount;
		this.fillSetting = fillSetting;
		this.fillAmount = fillAmount;
		this.cap = cap;
		this.capAmount = capAmount;
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
	public double getFillAmount() {
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
	public double getCapAmount() {
		return capAmount;
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
	public void setFillAmount(double fillAmount) {
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
	public void setCapAmount(double capAmount) {
		this.capAmount = capAmount;
	}
	
	
	
	

}
