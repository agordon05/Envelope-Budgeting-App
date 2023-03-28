package UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



public class budgetUI extends JFrame{


	private static budgetUI frame;
	private MyPanel mp;

	StatementListUI statementWindow;


	
	public budgetUI() {

		//create container
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());

		
		JButton statementButton = new JButton("Statements");
		statementButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(statementWindow == null) {
					statementWindow = new StatementListUI();
				}
				else {
					statementWindow.dispose();
					statementWindow = new StatementListUI();
				}
				
			}
			
		});

		mp = new MyPanel();



	
		container.add(statementButton, BorderLayout.NORTH);
		container.add(mp, BorderLayout.CENTER);

	}


	
	
	


	//frame setup
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//create frame
		frame = new budgetUI();
		frame.setTitle("eba prototype");
		//set bounds
		frame.setBounds(100, 60, 500, 400);

		//set close operation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//set visible
		frame.setVisible(true);



	}

}

class MyPanel extends JPanel{

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawLine(0, 0, 100, 100);


	} 
}







