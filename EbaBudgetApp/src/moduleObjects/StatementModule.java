package moduleObjects;

import java.util.Date;

import dataObjects.StatementSplits;

public class StatementModule {
	
	public int id;
	
	public String vendor;
	public double amount;
	public boolean pending;
	public Date date;

	public String envelope1;
	public String envelope2;
	

	//CREATE
	public StatementModule(String vendor, double amount, Date date, boolean pending) {
		this.vendor = vendor;
		this.amount = amount;
		this.date = date;
		this.pending = pending;
	}
	
	//REMOVE/GET
	public StatementModule(int id) {
		this.id = id;
	}
	
	//TRANSFER
	public StatementModule(int id, String e1, String e2, double amount) {
		this.id = id;
		this.envelope1 = e1;
		this.envelope2 = e2;
		this.amount = amount;
	}
	
	//EDIT
	public StatementModule(int id, boolean pending, double amount) {
		this.id = id;
		this.pending = pending;
		this.amount = amount;
	}
	
	

}
