package dataAccess;

import java.util.ArrayList;

import dataObjects.Balance;

public class BalanceAccess {
	
	private static ArrayList<Balance> balances;
	
	public static void Initialize() {
		balances = new ArrayList<Balance>();
	}
	public static boolean addBalance(Balance balance) {
		if(balances.size() == 1) return false;
		balances.add(balance);
		return true;
	}
	
	public static Balance getBalance() {
		return balances.get(0);
	}
	
	

}
