package UI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import actions.Actions;
import data.Database;
//import dataAccess.EnvelopeAccess;
import dataObjects.Envelope;
import settings.EnvelopeSettings;
import settings.UISettings;
import settings.textFilters;
import tickets.ResponseTicket;

public class editUI extends JFrame implements ActionListener, UISettings{

	private static editUI frame;
	private String[] fillSettings  = {"Fill Amount", "Fill Percentage", "Fill"};

	int priority, capAmount, fillSetting, fillAmount;
	String name;
	boolean cap, extra, Default;
	Envelope envelope;
	JComboBox priorityList;
	TextField nameText;
	Checkbox capBox;
	TextField capAmountText;
	JComboBox fillList;
	TextField fillAmountText;
	Checkbox extraBox;
	Checkbox defaultBox;
	
	public editUI(Envelope e, int x, int y) {
		envelope = e;
		this.priority = e.getPriority();
		this.name = e.getName();
		this.cap = e.hasCap();
		this.capAmount = e.getCapAmount();
		this.fillSetting = e.getFillSetting();
		this.fillAmount = e.getFillAmount();
		this.extra = e.isExtra();
		this.Default = e.isDefault();
		
		
		setup();

		//frame
		frame = this;
		frame.setTitle("Edit");
		frame.setBounds(x + EUIx, y + EUIy, EUIWidth, EUIHeight);
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
		
		addInfo();
	}



	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Panel setPanel() {

		//priority label
		Label priorityLabel = new Label("Priority: ");
		
		//priority
		String[] priorities = new String[Database.getEnvelopes().size()];
		for(int index = 0; index < priorities.length; index++) {
			priorities[index] = "" + (index + 1);
		}
		 priorityList = new JComboBox(priorities);
		priorityList.setSelectedIndex(this.priority - 1);
		priorityList.setSize(priorityDropListWidth, dropListHeight);
		priorityList.setMaximumSize(new Dimension(priorityDropListWidth, dropListHeight));
		priorityList.setPreferredSize(new Dimension(priorityDropListWidth, dropListHeight));
		
		//name label
		Label nameLabel = new Label("Name: ");
		//name
		 nameText = new TextField(/*this.name*/);
		nameText.setSize(TextboxWidth, TextboxHeight);
		nameText.setMaximumSize(new Dimension(TextboxWidth, TextboxHeight));
		nameText.setPreferredSize(new Dimension(TextboxWidth, TextboxHeight));
		nameText.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

				textFilters.letterFilter(e, nameText, frame);
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
		
		//cap
		 capBox = new Checkbox("Cap");
		capBox.setState(this.cap);
		
		//cap amount
		 capAmountText = new TextField(/*"" + this.capAmount*/);
		capAmountText.setSize(TextboxWidth, TextboxHeight);
		capAmountText.setMaximumSize(new Dimension(TextboxWidth, TextboxHeight));
		capAmountText.setPreferredSize(new Dimension(TextboxWidth, TextboxHeight));
		capAmountText.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if(!capBox.getState()) {
					e.consume();
					return;
				}
				textFilters.IntegerFilter(e, capAmountText, frame);
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
		

		//fill setting
		 fillList = new JComboBox(fillSettings);
		fillList.setSize(fillDropListWidth, dropListHeight);
		fillList.setMaximumSize(new Dimension(fillDropListWidth, dropListHeight));
		fillList.setPreferredSize(new Dimension(fillDropListWidth, dropListHeight));
		switch(this.fillSetting) {
		case EnvelopeSettings.amount: fillList.setSelectedIndex(0);	break;
		case EnvelopeSettings.percentage: fillList.setSelectedIndex(1);	break;
		case EnvelopeSettings.fill: fillList.setSelectedIndex(2);	break;		
		}
		
		
		//fill amount
		 fillAmountText = new TextField(/*"" + this.fillAmount*/);
		fillAmountText.setSize(TextboxWidth, TextboxHeight);
		fillAmountText.setMaximumSize(new Dimension(TextboxWidth, TextboxHeight));
		fillAmountText.setPreferredSize(new Dimension(TextboxWidth, TextboxHeight));
		fillAmountText.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if(fillList.getSelectedItem().toString().equals("Fill")) {
					e.consume();
					return;
				}
				textFilters.IntegerFilter(e, fillAmountText, frame);
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
		
		//extra
		extraBox = new Checkbox("Extra");
		extraBox.setState(this.extra);
		//default
		defaultBox = new Checkbox("Default");
		defaultBox.setState(this.Default);
		
		//submit
		Button submit = new Button("Submit");
		submit.addActionListener(this);
		
		//remove
		Button remove = new Button("Remove");
		remove.addActionListener(this);
		
		//priority
		Panel p2 = new Panel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.LINE_AXIS));
		p2.add(priorityLabel);
		p2.add(Box.createHorizontalGlue());
		p2.add(priorityList);
		
		//name
		Panel p3 = new Panel();
		p3.setLayout(new BoxLayout(p3, BoxLayout.LINE_AXIS));
		p3.add(nameLabel);
		p3.add(Box.createHorizontalStrut(55));
		p3.add(nameText);
		//p3.add(Box.createHorizontalGlue());
		
		//cap
		Panel p4 = new Panel();
		p4.setLayout(new BoxLayout(p4, BoxLayout.LINE_AXIS));
		p4.add(capBox);
		p4.add(Box.createHorizontalStrut(10));
		p4.add(capAmountText);
		//p4.add(Box.createHorizontalGlue());
		
		//fill
		Panel p5 = new Panel();
		p5.setLayout(new BoxLayout(p5, BoxLayout.LINE_AXIS));
		p5.add(fillList);
		p5.add(Box.createHorizontalStrut(60));
		p5.add(fillAmountText);
		//p5.add(Box.createHorizontalGlue());


		
		//extra/default
		Panel p6 = new Panel();
		p6.setLayout(new BoxLayout(p6, BoxLayout.LINE_AXIS));
		p6.add(Box.createHorizontalGlue());
		p6.add(extraBox);
		p6.add(defaultBox);

		//submit/remove button
		Panel p7 = new Panel();
		p7.setLayout(new BoxLayout(p7, BoxLayout.LINE_AXIS));
		p7.add(Box.createHorizontalGlue());
		p7.add(submit);
		p7.add(remove);
		p7.add(Box.createHorizontalGlue());



		//panel
		Panel panel = new Panel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(p2);
		panel.add(p3);
		panel.add(p4);
		panel.add(p5);
		panel.add(p6);
		panel.add(p7);




		return panel;
	}
	
	private void addInfo() {
		
		nameText.setText(name);
		capAmountText.setText("" + capAmount);
		fillAmountText.setText("" + fillAmount);
		
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getActionCommand().equals("Remove")) {
			System.out.println("Remove button pressed");
			//check user's decision
			//remove
			ResponseTicket response = Actions.Remove(envelope);
			response.printMessages();
			if(response.getErrorMessages().size() != 0) return;
			
		}
		else if(e.getActionCommand().equals("Submit")) {
			System.out.println("Submit button pressed");
			
			int p = Integer.parseInt(priorityList.getSelectedItem().toString());
			String name = nameText.getText();
			boolean cap = capBox.getState();
			int capAmount = Integer.parseInt(capAmountText.getText());
			int fillSetting;
			switch(fillList.getSelectedItem().toString()) {
				default: return;
				case "Fill Amount": fillSetting = EnvelopeSettings.amount; break;
				case "Fill Percentage": fillSetting = EnvelopeSettings.percentage; break;
				case "Fill": fillSetting = EnvelopeSettings.fill; break;
			}
			int fillAmount = Integer.parseInt(fillAmountText.getText());
			boolean extra = extraBox.getState();
			boolean Default = defaultBox.getState();
			
			
			ResponseTicket response = Actions.Edit(envelope, p, name, envelope.getAmount(), cap, capAmount, fillSetting, fillAmount, extra, Default);
			response.printMessages();
			
			
		}
		else {
			System.out.println(e.getActionCommand());
		}
		
		PrototypeUI.update();
		dispose();
		
	}

}
