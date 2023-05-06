package UI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import actions.Actions;
import actions.EnvelopeActions;
import dataAccess.BalanceAccess;
import dataAccess.EnvelopeAccess;
import dataAccess.StatementAccess;
import dataAccess.VendorAccess;
import dataObjects.Balance;
import dataObjects.Envelope;
import dataObjects.Statement;
import dataObjects.Vendor;
import settings.EnvelopeSettings;
import tickets.ResponseTicket;

public class tempInfo {

	//directory
	private static String currentDirectory = "";
	
	//settings
	private static final String envelopeSymbol = "#";
	private static final String statementSplitSymbol = "!";
	private static final String vendorSymbol = "@";

	public tempInfo() {

		load();
		ResponseTicket response = Actions.validate();
		response.printMessages();
	}
	
	public void Initialize() {
		EnvelopeAccess.Initialize();
		StatementAccess.Initialize();
		BalanceAccess.Initialize();
		VendorAccess.Initialize();
		BalanceAccess.addBalance(new Balance(0));
	}
	

	public void load() {
		//reset dataAccess
		Initialize();
		
		//current directory
		if(this.currentDirectory.equals("")) getCurrentDirectory();
		
		//file
		File file = new File(this.currentDirectory + "info.txt");

		//try to load file
		try {
			
			//file exists, load file
			if(file.exists()) {
				
				Scanner scanner = new Scanner(file);
				
				//load Objects
				loadBalance(scanner);
				String currentLine = loadEnvelopes(scanner);
				
				
				scanner.close();
				
			}
			//file does not exist, create file
			else {
				createFile(file);
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

	private void loadBalance(Scanner scanner) {
		//get info
		double balance = Double.parseDouble(scanner.nextLine());
		System.out.println("" + balance);
		//create info
		BalanceAccess.getBalance().setBalance(balance);
		//BalanceAccess.addBalance(new Balance(balance));
	}
	private String loadEnvelopes(Scanner scanner) {
		
		String currentLine = "";
		
		while(scanner.hasNextLine()) {
			
			//get info
			currentLine = scanner.nextLine();
			String[] tokens = currentLine.split("" + envelopeSymbol);
			if(tokens.length != 9) return currentLine;
			
			//create info
			int priority = Integer.parseInt(tokens[0]);
			String name = tokens[1];
			double amount = Double.parseDouble(tokens[2]);
			int fillSetting = Integer.parseInt(tokens[3]);
			int fillAmount = Integer.parseInt(tokens[4]);
			boolean cap = Boolean.parseBoolean(tokens[5]);
			int capAmount = Integer.parseInt(tokens[6]);
			boolean extra = Boolean.parseBoolean(tokens[7]);
			boolean Default = Boolean.parseBoolean(tokens[8]);
			
			//create envelope
			Envelope e = new Envelope(priority, name, amount, fillSetting, fillAmount, cap, capAmount, extra, Default);
			EnvelopeAccess.addEnvelope(e);
		}
		
		return null;
	}
	
	/*TO BE WRITTEN*/
	private void loadStatements(Scanner scanner, String currentLine) {

	}
	private void loadStatementSplits(Scanner scanner, String currentLine) {

	}
	private void loadVendors(Scanner scanner, String currentLine) {

	}
	
	
	
	public static void save() {

		try {
			//directory
			if(currentDirectory.equals("")) getCurrentDirectory();
			
			//open file
			File file = new File(currentDirectory + "info.txt");			
			FileWriter fw = new FileWriter(file, false);
			BufferedWriter bw = new BufferedWriter(fw);

			//save objects
			saveBalance(bw);
			saveEnvelopes(bw);
			saveStatements(bw);
			saveStatementSplits(bw);
			saveVendors(bw);
			
			bw.newLine();
			bw.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private static void saveBalance(BufferedWriter bw) throws IOException {
		bw.write( String.format("%.2f", BalanceAccess.getBalance().getBalance()) + "\n");
	}
	private static void saveEnvelopes(BufferedWriter bw) throws IOException {
		
		for(int index = 0; index < EnvelopeAccess.getEnvelopes().size(); index++) {
			Envelope e = EnvelopeAccess.getEnvelopeByPriority(index + 1);
			
			String envString = e.getPriority() + envelopeSymbol +
					e.getName() + envelopeSymbol +
					e.getAmount() + envelopeSymbol +
					e.getFillSetting() + envelopeSymbol +
					e.getFillAmount() + envelopeSymbol +
					e.hasCap() + envelopeSymbol +
					e.getCapAmount() + envelopeSymbol +
					e.isExtra() + envelopeSymbol +
					e.isDefault() + "\n";
			
			bw.write(envString);
			
		}
	}
	static char statementSymbol = '$';
	private static void saveStatements(BufferedWriter bw) throws IOException {
		for(int index = 0; index < StatementAccess.getStatements().size(); index++) {
			Statement s = StatementAccess.getStatementByID(index + 1);
			
			String statementString = s.getID() + statementSymbol +
					s.getName() + statementSymbol +
					s.getVendor() + statementSymbol +
					s.getAmount() + statementSymbol +
					s.getTip() + statementSymbol +
					s.isPending() + "\n";
			
			bw.write(statementString);
			
		}
	}
	/* --TO BE WRITTEN--*/
	private static void saveStatementSplits(BufferedWriter bw) throws IOException {
		
	}
	
	private static void saveVendors(BufferedWriter bw) throws IOException {
		
		for(int index = 0; index < VendorAccess.getVendors().size(); index++) {
			Vendor v = VendorAccess.getVendors().get(index);
			
			String statementString = v.getName() + vendorSymbol +
					v.getPrefName() + vendorSymbol +
					v.getPrefEnvelope() + "\n";
			bw.write(statementString);
			
		}
	}
	
	
	private void createFile(File file) throws Exception{
		file.createNewFile();
		   FileWriter fw = new FileWriter(file, false);
		   BufferedWriter bw = new BufferedWriter(fw);		    
		    bw.close();
		
	}
	
	private static String getCurrentDirectory() {
		
		String currentPath = "";
		
		try {
			currentPath = new java.io.File(".").getCanonicalPath();
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		currentDirectory = currentPath + "/src/data/";
		return currentDirectory;
	}
	
	
	
	
	
	
}
