package UI;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

import javax.swing.*;
import javax.swing.text.*;

import actions.Actions;
import actions.EnvelopeActions;
import data.Database;
//import dataAccess.EnvelopeAccess;
//import dataAccess.VendorAccess;
import dataObjects.Envelope;
//import dataObjects.Vendor;
import settings.UISettings;
import settings.textFilters;
import tickets.ResponseTicket;

public class withdrawUI extends JFrame implements ActionListener, UISettings {

	//frame
	private static withdrawUI frame;
		
	
	//components
	private TextField amountText;
	//private TextField name;
	private JComboBox envelopeList;
	
	
	public withdrawUI(int x, int y) {

		setup();

		//frame
		frame = this;
		frame.setTitle("Withdraw");
		frame.setBounds(x + WUIx, y + WUIy, WUIWidth, WUIHeight);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.validate();
	}

	private void setup() {
		//container
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		//panel
		Panel panel = setPanel();
		container.add(panel);
	}



	private Panel setPanel() {

		//panel
		Panel panel = new Panel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


		//add components to panel
		panel.add(Box.createVerticalGlue());
		addStatementPanel(panel);


		return panel;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addStatementPanel(Panel panel) {

		//amount label
		Label amountLabel = new Label("Amount:");


		//amount text field
		amountText = new TextField();
		amountText.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				textFilters.DoubleFilter(e, amountText, frame);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});


		//envelope list
		int envelopeSize = Database.getEnvelopes().size();
		String[] envelopeNames = new String[envelopeSize];

		for(int index = 0; index < envelopeSize; index++) {
			envelopeNames[index] = Database.getEnvelopeByPriority(index + 1).getName();
		}
		envelopeList = new JComboBox(envelopeNames);
		envelopeList.setSelectedIndex(0);


		//submit button
		Button submit = new Button("Submit");
		submit.addActionListener(this);
		submit.setSize(submitButtonWidth, submitButtonHeight);


		//panel 2
		Panel p2 = new Panel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.PAGE_AXIS));


		//statement name and amount
		Panel labelPanel = new Panel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
		labelPanel.add(Box.createHorizontalGlue());
		labelPanel.add(Box.createHorizontalStrut(40));
		labelPanel.add(amountLabel);
		labelPanel.add(Box.createHorizontalGlue());

		
		
		//statement name and amount
		Panel textPanel = new Panel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.LINE_AXIS));
		textPanel.add(Box.createHorizontalGlue());
		textPanel.add(amountText);
		textPanel.add(Box.createHorizontalGlue());

		

		//add components to panel 2
		p2.add(Box.createHorizontalGlue());
		p2.add(labelPanel);
		p2.add(textPanel);
		p2.add(Box.createHorizontalGlue());


		//envelope list
		Panel listPanel = new Panel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.LINE_AXIS));
		listPanel.add(Box.createHorizontalGlue());
		listPanel.add(envelopeList);
		listPanel.add(Box.createHorizontalGlue());


		//submit button
		Panel buttonPanel = new Panel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(submit);
		buttonPanel.add(Box.createHorizontalGlue());


		//add components to panel
		panel.add(p2);
		panel.add(listPanel);
		panel.add(Box.createVerticalGlue());
		panel.add(buttonPanel);
		panel.add(Box.createVerticalGlue());
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//validate
		//if(name.getText().equals("")) return;
		if(amountText.getText().equals("")) return;

		//vendor
		//Vendor vendor = VendorAccess.getVendorByName(name.getText());
		//if(vendor == null) VendorAccess.addVendor(new Vendor(name.getText(), "", envelopeList.getSelectedItem().toString()));


		//withdraw		
		BigDecimal amount = new BigDecimal(amountText.getText());
		Envelope envelope = Database.getEnvelope(envelopeList.getSelectedItem().toString());
		ResponseTicket response = Actions.Withdraw("", envelope, amount);

		response.printMessages();

		//update
		PrototypeUI.update();

		//close
		this.dispose();
	}

}

