package moduleObjects;

import java.util.Date;

import dataObjects.StatementSplits;

public class StatementModule {
	
	public int id;
	public String vendor;
	public double amount;
	public double tip;
	public boolean pending;
	public StatementSplits split1;
	public StatementSplits split2;
	public Date date;

	
	public StatementModule(int id, String vendor, double amount, double tip, boolean pending, StatementSplits split1, StatementSplits split2, Date date) {
		this.id = id;
		this.vendor = vendor;
		this.amount = amount;
		this.tip = tip;
		this.pending = pending; 
		this.split1 = split1;
		this.split2 = split2;
		this.date = date;
	}
	

}
