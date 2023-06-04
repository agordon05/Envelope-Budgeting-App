package UI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import dataAccess.EnvelopeAccess;
import dataObjects.Envelope;
import settings.EnvelopeSettings;
import settings.UISettings;
import settings.textFilters;

public class addEnvelopeUI extends JFrame implements ActionListener, UISettings{

	//frame
	private static addEnvelopeUI frame;

	

	//components
	private TextField nameText;
	private String[] fillSettings  = {"Fill Amount", "Fill Percentage", "Fill"};
	@SuppressWarnings("rawtypes")
	private JComboBox fillList;
	private TextField fillAmount;
	private Checkbox cap;
	private TextField capAmount;
	private Checkbox extra;
	private Checkbox Default;

	
	public addEnvelopeUI(int x, int y) {

		setup();

		//frame
		frame = this;
		frame.setTitle("Add Envelope");
		frame.setBounds(x + AUIx, y + AUIy, AUIWidth, AUIHeight);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Panel setPanel() {

	
		//name label
		Label nameLabel = new Label("Name: ");
		
		//name text field
		nameText = new TextField("Savings");
		nameText.setSize(TextboxWidth, TextboxHeight);
		nameText.setMaximumSize(new Dimension(TextboxWidth, TextboxHeight));
		nameText.setPreferredSize(new Dimension(TextboxWidth, TextboxHeight));
		nameText.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
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


		//fill setting list
		fillList = new JComboBox(fillSettings);
		fillList.setSize(dropListWidth, dropListHeight);
		fillList.setMaximumSize(new Dimension(dropListWidth, dropListHeight));
		fillList.setPreferredSize(new Dimension(dropListWidth, dropListHeight));
		fillList.setSelectedIndex(0);

		
		//fillAmount
		fillAmount = new TextField();
		fillAmount.setSize(TextboxWidth, TextboxHeight);
		fillAmount.setMaximumSize(new Dimension(TextboxWidth, TextboxHeight));
		fillAmount.setPreferredSize(new Dimension(TextboxWidth, TextboxHeight));
		fillAmount.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				boolean check = false;
				if(fillList.getSelectedItem().equals("Fill Amount")) check = true;
				if(fillList.getSelectedItem().equals("Fill Percentage")) check = true;

				fillAmount.setEditable(check);

				if(check) {
					textFilters.IntegerFilter(e, fillAmount, frame);
				}
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
		cap = new Checkbox("Cap");
		
		
		//cap amount
		capAmount = new TextField();
		capAmount.setSize(TextboxWidth, TextboxHeight);
		capAmount.setMaximumSize(new Dimension(TextboxWidth, TextboxHeight));
		capAmount.setPreferredSize(new Dimension(TextboxWidth, TextboxHeight));
		capAmount.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub


				boolean check = false;
				if(cap.getState()) check = true;

				capAmount.setEditable(check);

				if(check) {
					textFilters.IntegerFilter(e, capAmount, frame);
				}

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
		extra = new Checkbox("Extra");

		
		//default
		Default = new Checkbox("Default");

		
		//submit button
		Button submit = new Button("Submit");
		submit.addActionListener(this);

		
		//panel
		Panel panel = new Panel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		
		//panel 2
		Panel p2 = new Panel();
		p2.setLayout(new GridLayout(4,2,1,1));

		//panel 3
		Panel p3 = new Panel();
		p3.setLayout(new BoxLayout(p3, BoxLayout.LINE_AXIS));


		//add components to panel 2
		p2.add(nameLabel);
		p2.add(nameText);
		p2.add(fillList);
		p2.add(fillAmount);
		p2.add(cap);
		p2.add(capAmount);
		p2.add(extra);
		p2.add(Default);

		//add components to panel 3
		p3.add(Box.createHorizontalGlue());
		p3.add(submit);
		p3.add(Box.createHorizontalGlue());

		
		//add components to panel
		panel.add(p2);
		panel.add(p3);


		return panel;
	}



	@Override
	public void actionPerformed(ActionEvent e) {

		//name
		String name = this.nameText.getText();

		
		//fill amount
		int fillAmount;
		if(!this.fillAmount.getText().equals("")) fillAmount = Integer.parseInt(this.fillAmount.getText());
		else fillAmount = 0;
		
		
		//cap
		boolean cap = this.cap.getState();
		
		
		//cap amount
		int capAmount;
		if(!this.capAmount.getText().equals("")) capAmount = Integer.parseInt(this.capAmount.getText());
		else capAmount = 0;
		
		
		//extra
		boolean extra = this.extra.getState();
		
		
		//default
		boolean Default = this.Default.getState();

		
		//validate name
		if(name == null || name.equals("")) return;
		Envelope env = EnvelopeAccess.getEnvelopeByName(name);
		if(env != null) return;


		//fill setting
		int fill = 0;
		String fillSetting = this.fillList.getSelectedItem().toString();
		switch(fillSetting) {
		default: return;
		case "Fill Amount": fill = EnvelopeSettings.amount; break;
		case "Fill Percentage": fill = EnvelopeSettings.percentage; break;
		case "Fill": fill = EnvelopeSettings.fill; break;
		}

		
		//create envelope
		Envelope envelope = new Envelope(0, name, 0, fill, fillAmount, cap, capAmount, extra, Default);
		EnvelopeAccess.addEnvelope(envelope);

		
		//update
		PrototypeUI.update();
		
		
		//dispose
		this.dispose();
	}

	
	
}
