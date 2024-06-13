//package actions;
//
//import dataObjects.Balance;
//
//import java.math.BigDecimal;
//
//import data.Database;
//import dataObjects.Statement;
//import tickets.ResponseTicket;
//
//public class BalanceActions extends precisionOperations{
//	
//	//redundant
//	public static void setAmount(ResponseTicket response, Balance balance, double amount) {
//		if(response == null) {
//			throw new IllegalArgumentException("Response is null");
//		}
//		if(balance == null) {
//			response.addErrorMessage("Balance cannot be null");
//			return;
//		}
//		balance.setBalance(amount);
//	}
//	
//	public static void deposit(ResponseTicket response, Balance balance, BigDecimal amount) {
//		balance.setBalance(add(balance.getBalance(), amount));
//	}
//
//	public static void withdraw(ResponseTicket response, Balance balance, BigDecimal amount) {
//		balance.setBalance(subtract(balance.getBalance(), amount));
//	}
//
//	
//	
//
//}
