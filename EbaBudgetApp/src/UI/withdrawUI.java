package UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import actions.Actions;
import actions.EnvelopeActions;
import dataAccess.EnvelopeAccess;
import dataAccess.VendorAccess;
import dataObjects.Envelope;
import dataObjects.Vendor;
import settings.UISettings;
import settings.textFilters;
import tickets.RequestTicket;
import tickets.ResponseTicket;

public class withdrawUI extends JFrame implements ActionListener, UISettings {

	//frame
	private static withdrawUI frame;
		
	
	//components
	private TextField amountText;
	private TextField name;
	private JComboBox envelopeList;
	
	
	public withdrawUI() {

		setup();

		//frame
		frame = this;
		frame.setTitle("Withdraw");
		frame.setBounds(WUIx, WUIy, WUIWidth, WUIHeight);
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

		//name label
		Label nameLabel = new Label("Statement name:");


		//name text field
		name = new TextField();
		name.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				textFilters.letterFilter(e, name, frame);
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
		int envelopeSize = EnvelopeAccess.getEnvelopes().size();
		String[] envelopeNames = new String[envelopeSize];

		for(int index = 0; index < envelopeSize; index++) {
			envelopeNames[index] = EnvelopeAccess.getEnvelopeByPriority(index + 1).getName();
		}
		envelopeList = new JComboBox(envelopeNames);
		envelopeList.setSelectedIndex(0);


		//submit button
		Button submit = new Button("Submit");
		submit.addActionListener(this);
		submit.setSize(submitButtonWidth, submitButtonHeight);


		//panel 2
		Panel p2 = new Panel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.LINE_AXIS));


		//statement name and amount
		Panel p3 = new Panel();
		p3.setLayout(new GridLayout(2,2,1,1));
		p3.add(nameLabel);
		p3.add(name);
		p3.add(amountLabel);
		p3.add(amountText);


		//add components to panel 2
		p2.add(Box.createHorizontalGlue());
		p2.add(p3);
		p2.add(Box.createHorizontalGlue());


		//envelope list
		Panel p4 = new Panel();
		p4.setLayout(new BoxLayout(p4, BoxLayout.LINE_AXIS));
		p4.add(Box.createHorizontalGlue());
		p4.add(envelopeList);
		p4.add(Box.createHorizontalGlue());


		//submit button
		Panel p5 = new Panel();
		p5.setLayout(new BoxLayout(p5, BoxLayout.LINE_AXIS));
		p5.add(Box.createHorizontalGlue());
		p5.add(submit);
		p5.add(Box.createHorizontalGlue());


		//add components to panel
		panel.add(p2);
		panel.add(p4);
		panel.add(Box.createVerticalGlue());
		panel.add(p5);
		panel.add(Box.createVerticalGlue());
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		//validate
		if(name.getText().equals("")) return;
		if(amountText.getText().equals("")) return;

		//vendor
		Vendor vendor = VendorAccess.getVendorByName(name.getText());
		if(vendor == null) VendorAccess.addVendor(new Vendor(name.getText(), "", envelopeList.getSelectedItem().toString()));


		//withdraw		
		double amount = Double.parseDouble(amountText.getText());
		Envelope envelope = EnvelopeAccess.getEnvelopeByName(envelopeList.getSelectedItem().toString());
		ResponseTicket response = Actions.Withdraw(name.getText(), envelope, amount);

		response.printMessages();

		//update
		PrototypeUI.update();

		//close
		this.dispose();
	}

}

