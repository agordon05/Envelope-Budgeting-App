package UI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import actions.Actions;
import dataAccess.EnvelopeAccess;
import dataObjects.Envelope;
import settings.UISettings;
import settings.textFilters;
import tickets.ResponseTicket;

public class depositUI extends JFrame implements ActionListener, UISettings{

	//frame
	private static depositUI frame;

	//components
	@SuppressWarnings("rawtypes")
	private JComboBox envList;
	private TextField amount;

	
	public depositUI() {
		
		setup();

		//frame
		frame = this;
		frame.setTitle("Deposit");
		frame.setBounds(DUIx, DUIy, DUIWidth, DUIHeight);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Panel setPanel() {

		//panel
		Panel panel = new Panel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));


		//deposit label
		Label depositLabel = new Label("Deposit into:");


		//envelope list
		String[] envelopes = new String[EnvelopeAccess.getEnvelopes().size() + 1];
		envelopes[0] = "";
		for(int index = 1; index < EnvelopeAccess.getEnvelopes().size() + 1; index++) {
			envelopes[index] = EnvelopeAccess.getEnvelopeByPriority(index).getName();
		}
		envList = new JComboBox(envelopes);
		envList.setSelectedIndex(0);
		envList.setSize(dropListWidth, dropListHeight);
		envList.setMaximumSize(new Dimension(dropListWidth, dropListHeight));
		envList.setPreferredSize(new Dimension(dropListWidth, dropListHeight));


		//amount text field
		amount = new TextField();
		amount.setSize(TextboxWidth, TextboxHeight);
		amount.setMaximumSize(new Dimension(TextboxWidth, TextboxHeight));
		amount.setPreferredSize(new Dimension(TextboxWidth, TextboxHeight));
		amount.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

				textFilters.DoubleFilter(e, amount, frame);
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


		//submit button
		Button submit = new Button("Submit");
		submit.addActionListener(this);


		//deposit label
		Panel p2 = new Panel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.LINE_AXIS));
		p2.add(Box.createHorizontalStrut(5));
		p2.add(depositLabel);
		p2.add(Box.createHorizontalGlue());


		//envelope list and amount
		Panel p3 = new Panel();
		p3.setLayout(new BoxLayout(p3, BoxLayout.LINE_AXIS));
		p3.add(Box.createHorizontalGlue());
		p3.add(envList);
		p3.add(Box.createHorizontalStrut(30));
		p3.add(amount);
		p3.add(Box.createHorizontalGlue());


		//submit button
		Panel p4 = new Panel();
		p4.setLayout(new BoxLayout(p4, BoxLayout.LINE_AXIS));
		p4.add(Box.createHorizontalGlue());
		p4.add(submit);
		p4.add(Box.createHorizontalGlue());


		//add components to panel
		panel.add(Box.createVerticalGlue());
		panel.add(p2);
		panel.add(p3);
		panel.add(Box.createVerticalGlue());
		panel.add(p4);


		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//envelope
		Envelope envelope = EnvelopeAccess.getEnvelopeByName(envList.getSelectedItem().toString());

		ResponseTicket response = Actions.Deposit(envelope, Double.parseDouble(amount.getText().toString()));
		response.printMessages();

		//update
		PrototypeUI.update();


		//dispose
		this.dispose();
	}



}
