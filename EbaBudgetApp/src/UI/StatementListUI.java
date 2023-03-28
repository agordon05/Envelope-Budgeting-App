package UI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dataAccess.StatementAccess;
import dataObjects.Statement;

public class StatementListUI extends JFrame{

	StatementListUI frame;
	private statementPanel sp;
	
	
	
	public StatementListUI() {
		//create container
		
		frame = this;
		frame.setTitle("Statement List");
		//set bounds
		frame.setBounds(150, 100, 500, 400);

		//set close operation
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		//set visible
		frame.setVisible(true);
		
		
		
		
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());


		sp = new statementPanel();



	

		container.add(sp, BorderLayout.CENTER);
	}

}

class statementPanel extends JPanel{

	
	//ArrayList<Statement> statements = StatementAccess.getStatements();
	
	public statementPanel() {
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
}
