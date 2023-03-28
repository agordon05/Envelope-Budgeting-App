package dataObjects;

import java.util.ArrayList;
import java.util.Date;

public class Statement {

	private int ID;
	private String name;
	private String vendor;
	private double amount;
	private double tip;
	private boolean pending;
	private ArrayList<StatementSplits> envAmount;
	private Date date;

	public Statement(int id, String name, String vendor, double amount, double tip, boolean pending, ArrayList<StatementSplits> envAmount, Date date) {
	
		this.ID = id;
		this.name = name;
		this.vendor = vendor;
		this.amount = amount;
		this.tip = tip;
		this.pending = pending;
		this.envAmount = envAmount;
		this.date = date;
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return the tip
	 */
	public double getTip() {
		return tip;
	}

	/**
	 * @return the pending
	 */
	public boolean isPending() {
		return pending;
	}

	/**
	 * @return the envAmount
	 */
	public ArrayList<StatementSplits> getEnvAmount() {
		return envAmount;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
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
	 * @param tip the tip to set
	 */
	public void setTip(double tip) {
		this.tip = tip;
	}

	/**
	 * @param pending the pending to set
	 */
	public void setPending(boolean pending) {
		this.pending = pending;
	}

	/**
	 * @param envAmount the envAmount to set
	 */
	public void setEnvAmount(ArrayList<StatementSplits> envAmount) {
		this.envAmount = envAmount;
	}
	
	

}
