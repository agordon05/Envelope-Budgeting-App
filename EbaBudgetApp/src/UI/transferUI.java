package UI;

import java.awt.BorderLayout;
import java.awt.Button;
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

import dataAccess.EnvelopeAccess;
import settings.UISettings;
import settings.textFilters;

public class transferUI extends JFrame implements ActionListener, UISettings{

	//frame
	private static transferUI frame;


	//components
	private JComboBox envList;
	private TextField amount;
	private JComboBox envList2;



	public transferUI() {
		setup();

		//frame
		frame = this;
		frame.setTitle("Transfer");
		frame.setBounds(TUIx, TUIy, TUIWidth, TUIHeight);
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


		//envelope list data
		String[] envelopes = new String[EnvelopeAccess.getEnvelopes().size() + 1];
		envelopes[0] = "";
		for(int index = 1; index < EnvelopeAccess.getEnvelopes().size() + 1; index++) {
			envelopes[index] = EnvelopeAccess.getEnvelopeByPriority(index).getName();
		}

		//from label
		Label fromLabel = new Label("From:");

		//envelope list
		envList = new JComboBox(envelopes);
		envList.setSelectedIndex(0);
		envList.setSize(dropListWidth, dropListHeight);
		envList.setMaximumSize(new Dimension(dropListWidth, dropListHeight));
		envList.setPreferredSize(new Dimension(dropListWidth, dropListHeight));


		//amount label
		Label amountLabel = new Label("Amount:");


		//amount text field
		amount = new TextField();
		amount.setSize(TextboxWidth, TextboxHeight);
		amount.setMaximumSize(new Dimension(TextboxWidth, TextboxHeight));
		amount.setPreferredSize(new Dimension(TextboxWidth, TextboxHeight));
		amount.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

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


		//to label
		Label toLabel = new Label("To:");


		//envelope list 2
		JComboBox envList2 = new JComboBox(envelopes);
		envList2.setSelectedIndex(0);
		envList2.setSize(dropListWidth, dropListHeight);
		envList2.setMaximumSize(new Dimension(dropListWidth, dropListHeight));
		envList2.setPreferredSize(new Dimension(dropListWidth, dropListHeight));


		//submit button
		Button submit = new Button("Submit");
		submit.addActionListener(this);


		//from label
		Panel p2 = new Panel();
		p2.setLayout(new BoxLayout(p2, BoxLayout.LINE_AXIS));
		p2.add(Box.createHorizontalStrut(labelOffset));
		p2.add(Box.createHorizontalGlue());
		p2.add(fromLabel);
		p2.add(Box.createHorizontalGlue());


		//from envelope
		Panel p3 = new Panel();
		p3.setLayout(new BoxLayout(p3, BoxLayout.LINE_AXIS));
		p3.add(Box.createHorizontalStrut(15));
		p3.add(envList);
		p3.add(Box.createHorizontalGlue());


		//to label
		Panel p4 = new Panel();
		p4.setLayout(new BoxLayout(p4, BoxLayout.LINE_AXIS));
		p4.add(Box.createHorizontalStrut(labelOffset));
		p4.add(Box.createHorizontalGlue());
		p4.add(toLabel);
		p4.add(Box.createHorizontalGlue());


		//to envelope
		Panel p5 = new Panel();
		p5.setLayout(new BoxLayout(p5, BoxLayout.LINE_AXIS));
		p5.add(Box.createHorizontalStrut(15));
		p5.add(envList2);
		p5.add(Box.createHorizontalGlue());


		//amount label
		Panel p6 = new Panel();
		p6.setLayout(new BoxLayout(p6, BoxLayout.LINE_AXIS));
		p6.add(Box.createHorizontalStrut(labelOffset));
		p6.add(Box.createHorizontalGlue());
		p6.add(amountLabel);
		p6.add(Box.createHorizontalGlue());


		//amount text field
		Panel p7 = new Panel();
		p7.setLayout(new BoxLayout(p7, BoxLayout.LINE_AXIS));
		p7.add(Box.createHorizontalGlue());
		p7.add(amount);
		p7.add(Box.createHorizontalGlue());


		//submit button
		Panel p8 = new Panel();
		p8.setLayout(new BoxLayout(p8, BoxLayout.LINE_AXIS));
		p8.add(Box.createHorizontalGlue());
		p8.add(submit);
		p8.add(Box.createHorizontalGlue());


		//panel
		Panel panel = new Panel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));


		//add components to panel
		panel.add(Box.createVerticalGlue());
		panel.add(p2);
		panel.add(p3);
		panel.add(Box.createVerticalGlue());
		panel.add(p4);
		panel.add(p5);
		panel.add(Box.createVerticalGlue());
		panel.add(p6);
		panel.add(p7);
		panel.add(Box.createVerticalGlue());
		panel.add(p8);


		return panel;
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		//validate
		if(envList.getSelectedItem().toString().equals(envList2.getSelectedItem().toString())) return;

		//if from amount is greater than from envelope's amount -- return
		//transfer amount


		//update
		PrototypeUI.update();


		//dispose
		this.dispose();
	}

}
