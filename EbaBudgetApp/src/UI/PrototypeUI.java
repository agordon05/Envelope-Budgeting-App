package UI;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import javax.swing.*;

import actions.Actions;
import actions.precisionOperations;
//import dataAccess.BalanceAccess;
//import dataAccess.EnvelopeAccess;
import dataObjects.Envelope;
import settings.EnvelopeSettings;
import settings.UISettings;
import tickets.ResponseTicket;
import data.Database;


public class PrototypeUI extends JFrame implements UISettings{


	//frames
	private static PrototypeUI frame;
	private static withdrawUI WUI;
	private static addEnvelopeUI AUI;
	private static editUI EUI;
	private static depositUI DUI;
	private static transferUI TUI;
	
		
	//components
	private static Container container;
	//top panels
	private static Panel topPanel = new Panel();
	private static Panel buttonPanel = new Panel();
	private static Panel balancePanel = new Panel();
	
	//center panels
	private static Panel centerPanel = new Panel();
	private static Panel addEnvPanel = new Panel();
	private static Panel bodyPanel = new Panel();


	private static Button edit;

	public PrototypeUI() {

		//create container
		container = this.getContentPane();
		container.setLayout(new BorderLayout());
		
		//top panel
		createTopPanel();
		container.add(topPanel, BorderLayout.NORTH);
		
		//center panel
		createCenterPanel();
		container.add(centerPanel, BorderLayout.CENTER);


	}
	

	//frame setup
	public static void main(String[] args) {

//		//create data
//		new tempInfo();
//		
		//frame
		Database.createNewDatabase();
		Database.printAllEnvelopes();
//		Database.removeEnvelope("Puppy Supplies");
		frame = new PrototypeUI();
		frame.setTitle("eba prototype");
		frame.setBounds(PUIx, PUIy, PUIWidth, PUIHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.validate();
		
		updateUI();
//		updateFrame();
		
//        Database.createNewDatabase();
//        System.out.println(Database.tableExists("envelopes"));
//        Database.addEnvelope("Groceries", 1, new BigDecimal(200.0), 1, 50, false, 0, false, true);
//        Database.printAllEnvelopes();
//        Database.removeEnvelope("Groceries");
		
		
	}
	

	
	private static void createTopPanel(){
		removeAll(topPanel);
		removeAll(buttonPanel);
		removeAll(balancePanel);
		//panel
		topPanel.setLayout(new GridLayout(2,1,1,1));

		
		//buttons
		Button withdraw = new Button("Withdraw");
		withdraw.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				disposeOtherWindows();
				WUI = new withdrawUI(frame.getX(), frame.getY());
			}
			
		});
		Button deposit = new Button("Deposit");
		deposit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				disposeOtherWindows();
				DUI = new depositUI(frame.getX(), frame.getY());
			}
			
		});
		Button transfer = new Button("Transfer");
		transfer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				disposeOtherWindows();
				TUI = new transferUI(frame.getX(), frame.getY());
			}
			
		});
		
		
		//withdraw/deposit/transfer
		buttonPanel.setLayout(new GridLayout(1,3,1,1));
		buttonPanel.add(withdraw);
		buttonPanel.add(deposit);
		buttonPanel.add(transfer);
		validate(buttonPanel);
		
		//Balance label
		BigDecimal b = Database.getBalance();
		Label balance;
		if(b == null) {
			balance = new Label("Balance: $" + String.format("%.2f", 0));
		}
		else {
			 balance = new Label("Balance: $" + String.format("%.2f", b));
		}
		balancePanel.add(Box.createHorizontalGlue());
		balancePanel.add(balance);
		balancePanel.add(Box.createHorizontalGlue());
		validate(balancePanel);
		
		//add components to panel
		topPanel.add(buttonPanel);
		topPanel.add(balancePanel);
		
		
		//validate
		validate(topPanel);		
	}
	
	
	private static void createCenterPanel() {
		removeAll(centerPanel);
		//panel
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		centerPanel.setBackground(Color.gray);
		
		createBody();
		centerPanel.add(bodyPanel);
		
		
		
		//add button
		Button addEnvelope = new Button("Add Envelope");
		addEnvelope.setSize(addButtonWidth, addButtonHeight);
		addEnvelope.setMaximumSize(new Dimension(addButtonWidth, addButtonHeight));
		addEnvelope.setPreferredSize(new Dimension(addButtonWidth, addButtonHeight));
		addEnvelope.setActionCommand("Add");
		addEnvelope.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Add Envelope button pressed");
				disposeOtherWindows();
				AUI = new addEnvelopeUI(frame.getX(), frame.getY());
			}
			
		});
		
		if(Database.getEnvelopes().size() < 10) {
			removeAll(addEnvPanel);
			//panel 2
			addEnvPanel.setLayout(new BoxLayout(addEnvPanel, BoxLayout.LINE_AXIS));
			addEnvPanel.add(Box.createHorizontalGlue());
			addEnvPanel.add(addEnvelope);
			addEnvPanel.add(Box.createHorizontalGlue());
			validate(addEnvPanel);

			//add components to panel
			centerPanel.add(addEnvPanel);
			centerPanel.add(Box.createVerticalGlue());
		}
		
		validate(centerPanel);
	}

	private static void createBody() {
		
		int numOfEnvelopes = Database.getEnvelopes().size();
		removeAll(bodyPanel);
		//panel
		bodyPanel.setLayout(new GridLayout(numOfEnvelopes,5,0,0));
		bodyPanel.setSize(PUIWidth, envelopeHeight * numOfEnvelopes);
		bodyPanel.setMaximumSize(new Dimension(PUIWidth, envelopeHeight * maxNumOfEnvelopes));
		bodyPanel.setPreferredSize(new Dimension(PUIWidth, envelopeHeight * numOfEnvelopes));
		
		
		//create row for each existing envelope
		for(int index = 1; index <= numOfEnvelopes; index++) {
//			System.out.println("" + index);
			//envelope
			Envelope envelope = Database.getEnvelopeByPriority(index);
			
			//priority
			Label priority;
			if(envelope.getFillSetting() == EnvelopeSettings.percentage) {
				priority = new Label("\t-");
			}
			else priority = new Label("\t" + envelope.getPriority());
			priority.setSize(5, 5);
			priority.setPreferredSize(new Dimension(5, 5));
			priority.setMaximumSize(new Dimension(5, 5));
			priority.setAlignment(Label.LEFT);
			
			//name
			Label name = new Label(envelope.getName());
			
			//amount
			Label amount;
			//amount is a whole number
			if(envelope.getAmount().intValue() == envelope.getAmount().doubleValue() ) {
				amount = new Label("$" + (int)envelope.getAmount().intValue() + 
						(envelope.hasCap() ? "/$" + envelope.getCapAmount() : ""));
			}
			//amount is not a whole number
			else {
				amount = new Label("$" + String.format("%.2f", envelope.getAmount()) + 
						(envelope.hasCap() ? "/$" + envelope.getCapAmount() : ""));
			}
			
			
			//amount
			
			
			//fill
			Label fill;
			switch(envelope.getFillSetting()) {
			default: throw new IllegalArgumentException("Envelope setting does not exist");
			case EnvelopeSettings.amount: fill = new Label("($" + envelope.getFillAmount() +")"); break;
			case EnvelopeSettings.fill: fill = new Label("($Fill)"); break;
			case EnvelopeSettings.percentage: fill = new Label("(" + envelope.getFillAmount() +"%)"); break;
			}
		
			//edit
			 edit = new Button("Edit");
			edit.setActionCommand("Edit");
			edit.setName(envelope.getName());
			edit.setSize(editButtonWidth, editButtonHeight);
			edit.setPreferredSize(new Dimension(editButtonWidth, editButtonHeight));
			edit.setMaximumSize(new Dimension(editButtonWidth, editButtonHeight));
			edit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					
					disposeOtherWindows();
					EUI = new editUI(envelope, frame.getX(), frame.getY());
				}
				
			});


			//add components to panel
			bodyPanel.add(priority);
			bodyPanel.add(name);
			bodyPanel.add(amount);
			bodyPanel.add(fill);
			bodyPanel.add(edit);
			
		}
		validate(bodyPanel);
	}


	//saves info and updates center panel
	public static void update() {
		ResponseTicket response = Actions.validate();
		response.printMessages();
		
//		tempInfo.save();
		
		updateFrame();

	}
	
	public static void updateFrame() {
		// remove and validate
		container.remove(topPanel);
		container.remove(centerPanel);
		container.validate();
		container.repaint();
		
		// recreate panels
		createTopPanel();
		createCenterPanel();

		// add panels back
		container.add(topPanel, BorderLayout.NORTH);
		container.add(centerPanel, BorderLayout.CENTER);
		
		// Revalidated and repaint
		container.revalidate();
		container.repaint();
		
//		Point location = frame.getLocation();
//		
//		frame.setBounds(location.x, location.y, PUIWidth, PUIHeight + 1);
//		
//		frame.validate();
//		frame.setBounds(location.x, location.y, PUIWidth, PUIHeight);
	}




	//gets rid of all secondary windows
	private static void disposeOtherWindows() {
		
		
		if(TUI != null) {
			TUI.dispose();
		}
		if(WUI != null) {
			WUI.dispose();
		}
		if(EUI != null) {
			EUI.dispose();
		}
		if(AUI != null) {
			AUI.dispose();
		}
		if(DUI != null) {
			DUI.dispose();
		}
	}

    private static void removeAll(Panel panel) {
        panel.removeAll();
        validate(panel);
    }

    private static void validate(Panel panel) {
        panel.revalidate();
        panel.repaint();
    }
    
    private static void updateUI() {
    	
    	
    	ActionListener taskPerformer = new ActionListener() {
    		int count = 0;
    		public void actionPerformed(ActionEvent evt) {
    			
    			updateFrame();
//    			System.out.println(count++);
    		}
    	};
    	
    	Timer timer = new Timer(1000, taskPerformer);
    	timer.setRepeats(true);
    	timer.start();
    }

}





	
	


