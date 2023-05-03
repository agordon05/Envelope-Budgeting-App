package dataAccess;

import java.util.ArrayList;

import dataObjects.Statement;

public class StatementAccess {
	
	private static ArrayList<Statement> statements;
	
	
	public static void Initialize() {
		statements = new ArrayList<Statement>();
	}
	
	private static int getNextID() {
		return statements.size() + 1;
	}
	
	public static ArrayList<Statement> getStatements(){
		return statements;
	}
	
	public static Statement getStatementByID(int id) {
		for(int index = 0; index < statements.size(); index++) {
			if(statements.get(index).getID() == id) return statements.get(index);
		}
		return null;
	}
		
	public static Statement addStatement(Statement e) {
		Statement temp = new Statement(getNextID(), e.getName(), e.getVendor(), e.getAmount(), e.getTip(), e.isPending(), e.getEnvAmount(), e.getDate());
		statements.add(temp);
		return temp;
	}
	
	public static boolean removeStatement(int id) {
		Statement temp = getStatementByID(id);
		if(temp == null) return false;
		statements.remove(temp);
		return true;
	}
	
}
