package settings;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;

public class textFilters {

	private static int maxTextLength = 15;
	private static int maxNumberLength = 10;
	
	public static void DoubleFilter(KeyEvent e, TextField amount, JFrame frame) {
		try {
			char c = e.getKeyChar();
			if(amount.getText().length() >= maxNumberLength && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) throw new Exception();

			if((c != KeyEvent.VK_BACK_SPACE) &&
					(c != KeyEvent.VK_DELETE)) {

				double number = Double.parseDouble("" + amount.getText() + c);
				//System.out.println("" + number);
				if(!String.format("%.2f", number).equals("" + number) && !String.format("%.1f", number).equals("" + number)) {
					//System.out.println(String.format("%0.2f", number));
					//System.out.println(String.format("%0.1f", number));

					throw new Exception();
				}
				

			}
			
		}
		catch(Exception ex){
			char c = e.getKeyChar();
			if(amount.getText().length() != 1 && (c != KeyEvent.VK_BACK_SPACE) &&
					(c != KeyEvent.VK_DELETE) ) {
				frame.getToolkit().beep();
			}
			e.consume();

		}


		char c = e.getKeyChar();
		if (!( (c >= '0') && (c <= '9') ||
				(c == KeyEvent.VK_BACK_SPACE) ||
				(c == KeyEvent.VK_DELETE) )
				&& c != '.'

				) {
			frame.getToolkit().beep();
			e.consume();
		}
	}
	
	public static void IntegerFilter(KeyEvent e, TextField amount, JFrame frame) {
		try {
			char c = e.getKeyChar();
			if(amount.getText().length() >= maxNumberLength && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) throw new Exception();

			if((c != KeyEvent.VK_BACK_SPACE) &&
					(c != KeyEvent.VK_DELETE)) {

				Integer.parseInt("" + amount.getText() + c);
				//System.out.println("" + number);
//				if(!String.format("%.0f", number).equals("" + number) && !String.format("%.1f", number).equals("" + number)) {
//					//System.out.println(String.format("%0.2f", number));
//					//System.out.println(String.format("%0.1f", number));
//
//					throw new Exception();
//				}
				

			}
			
		}
		catch(Exception ex){
			char c = e.getKeyChar();
			if(amount.getText().length() != 1 && (c != KeyEvent.VK_BACK_SPACE) &&
					(c != KeyEvent.VK_DELETE) ) {
				frame.getToolkit().beep();
			}
			e.consume();

		}


		char c = e.getKeyChar();
		if (!( (c >= '0') && (c <= '9') ||
				(c == KeyEvent.VK_BACK_SPACE) ||
				(c == KeyEvent.VK_DELETE) )
				&& c != '.'

				) {
			frame.getToolkit().beep();
			e.consume();
		}
	}
	public static void letterFilter(KeyEvent e, TextField text, JFrame frame) {
		
		try {
			char c = e.getKeyChar();
			if(text.getText().length() >= maxTextLength && 
					c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) throw new Exception();
			if(
					c == '!' || c == '@' || c == '#' ||	c == '$' ||
					c == '%' || c == '^' || c == '&' || c == '*' ||
					c == '(' || c == ')' || c == '+' ||	c == '=' ||
					c == '~' ||	c == '{' ||	 c == '}' || c == '[' ||
					c == ']' || c == ':' || c == ';' ||	c == '\'' ||
					c == '\"' || c == ',' || c == '.' || c == '/' ||
					c == '<' || c == '>' || c == '?' || c == '/' ||
					c == '|' || c == '\\'

					) throw new Exception();
			
			
		}
		catch(Exception ex) {
			System.out.println("Exception for " + e.getKeyChar());
			frame.getToolkit().beep();
			e.consume();
		}
		
	}


	
	
}
