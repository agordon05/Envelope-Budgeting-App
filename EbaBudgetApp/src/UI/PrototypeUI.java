package UI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dataAccess.BalanceAccess;
import dataAccess.EnvelopeAccess;
import dataObjects.Envelope;
import settings.EnvelopeSettings;
import settings.UISettings;



public class PrototypeUI extends JFrame implements ActionListener, UISettings{


	//frames
	private static PrototypeUI frame;
	private static withdrawUI WUI;
	private static addEnvelopeUI AUI;
	private static editUI EUI;
	private static depositUI DUI;
	private static transferUI TUI;
	
		
	//components
	private static Container container;
	private static Panel centerPanel;
	private static Button edit;

	public PrototypeUI() {

		//create container
		container = this.getContentPane();
		container.setLayout(new BorderLayout());
		
		//top panel
		Panel p1 = createTopPanel();
		container.add(p1, BorderLayout.NORTH);
		
		//center panel
		centerPanel = createCenterPanel();
		container.add(centerPanel, BorderLayout.CENTER);


	}
	

	//frame setup
	public static void main(String[] args) {

		//create data
		new tempInfo();
		
		//frame
		frame = new PrototypeUI();
		frame.setTitle("eba prototype");
		frame.setBounds(PUIx, PUIy, PUIWidth, PUIHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.validate();

	}
	
	private Panel createTopPanel(){
		
		//panel
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(2,1,1,1));

		
		//buttons
		Button withdraw = new Button("Withdraw");
		withdraw.addActionListener(this);
		Button deposit = new Button("Deposit");
		deposit.addActionListener(this);
		Button transfer = new Button("Transfer");
		transfer.addActionListener(this);
		
		
		//withdraw/deposit/transfer
		Panel p2 = new Panel();
		p2.setLayout(new GridLayout(1,3,1,1));
		p2.add(withdraw);
		p2.add(deposit);
		p2.add(transfer);
		
		
		//Balance label
		Panel p3 = new Panel();
		Label balance = new Label("Balance: $" + String.format("%.2f", BalanceAccess.getBalance().getBalance()));
		p3.add(Box.createHorizontalGlue());
		p3.add(balance);
		p3.add(Box.createHorizontalGlue());
		
		
		//add components to panel
		panel.add(p2);
		panel.add(p3);
		
		
		//validate
		panel.validate();
		
		return panel;
	}
	
	
	
	private static Panel createCenterPanel() {
		
		//panel
		Panel panel = new Panel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBackground(Color.gray);		
		panel.add(createBody());
		
		
		
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
				AUI = new addEnvelopeUI();
			}
			
		});
		
		if(EnvelopeAccess.getEnvelopes().size() < 10) {
			//panel 2
			Panel p2 = new Panel();
			p2.setLayout(new BoxLayout(p2, BoxLayout.LINE_AXIS));
			p2.add(Box.createHorizontalGlue());
			p2.add(addEnvelope);
			p2.add(Box.createHorizontalGlue());

			//add components to panel
			panel.add(p2);
			panel.add(Box.createVerticalGlue());
		}
		panel.validate();
		
		return panel;
	}

	
	private static Panel createBody() {
		
		int numOfEnvelopes = EnvelopeAccess.getEnvelopes().size();

		//panel
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(EnvelopeAccess.getEnvelopes().size(),5,0,0));
		panel.setSize(PUIWidth, envelopeHeight * numOfEnvelopes);
		panel.setMaximumSize(new Dimension(PUIWidth, envelopeHeight * maxNumOfEnvelopes));
		panel.setPreferredSize(new Dimension(PUIWidth, envelopeHeight * numOfEnvelopes));
		
		
		//create row for each existing envelope
		for(int index = 0; index < EnvelopeAccess.getEnvelopes().size(); index++) {
			
			//envelope
			Envelope envelope = EnvelopeAccess.getEnvelopeByPriority(index + 1);
			
			//priority
			Label priority = new Label("\t" + envelope.getPriority());
			priority.setSize(5, 5);
			priority.setPreferredSize(new Dimension(5, 5));
			priority.setMaximumSize(new Dimension(5, 5));
			priority.setAlignment(Label.LEFT);
			
			//name
			Label name = new Label(envelope.getName());
			
			//amount
			Label amount = new Label("$" + envelope.getAmount() + 
					(envelope.hasCap() ? "/$" + envelope.getCapAmount(): ""));
			
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

				int priority = envelope.getPriority();
				String name = envelope.getName();
				boolean cap = envelope.hasCap();
				int capAmount = envelope.getCapAmount();
				int fillSetting = envelope.getFillSetting();
				int fillAmount = envelope.getFillAmount();
				boolean extra = envelope.isExtra();
				boolean Default = envelope.isDefault();
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					
					disposeOtherWindows();
					EUI = new editUI(priority, name, cap, capAmount, fillSetting, fillAmount, extra, Default);
				}
				
			});


			//add components to panel
			panel.add(priority);
			panel.add(name);
			panel.add(amount);
			panel.add(fill);
			panel.add(edit);
			
		}
		return panel;
	}


	//saves info and updates center panel
	public static void update() {
		tempInfo.save();
		container.remove(centerPanel);
		centerPanel = createCenterPanel();

		container.add(centerPanel, BorderLayout.CENTER);
		frame.validate();
	}


	//opens secondary window after disposing existing secondary windows
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if(e.getActionCommand().equals("Withdraw")) {
			disposeOtherWindows();
			WUI = new withdrawUI();
		}
		else if(e.getActionCommand().equals("Deposit")) {
			System.out.println("Deposit button pressed");
			disposeOtherWindows();
			DUI = new depositUI();

		}
		else if(e.getActionCommand().equals("Transfer")) {
			System.out.println("Transfer button pressed");
			disposeOtherWindows();
			TUI = new transferUI();

		}
	
		
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


}





	
	


