package gui;

import java.awt.Dimension;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JPanel;

public class pelinx extends JPanel {

	
	/**
	 * Create the panel.
	 */
	private static final long serialVersionUID = 1L;
	public JButton btnNewButton;
	public JButton btnNewButton_1;
	public JButton btnNewButton_2;
	public JButton btnNewButton_4;
	public pelinx() {
		setBackground(SystemColor.inactiveCaptionBorder);
		setLayout(null);
		setSize(new Dimension(800, 200));
		
		btnNewButton = new JButton("Election Panel");
		btnNewButton.setBounds(30, 11, 132, 23);
		add(btnNewButton);
		
		btnNewButton_1 = new JButton("Admin Panel");
		btnNewButton_1.setBounds(30, 57, 132, 23);
		add(btnNewButton_1);
		
		btnNewButton_2 = new JButton("Voter Registration");
		btnNewButton_2.setBounds(30, 112, 132, 23);
		add(btnNewButton_2);
		
		btnNewButton_4 = new JButton("Voter Login");
		btnNewButton_4.setBounds(30, 166, 132, 23);
		add(btnNewButton_4);

	}

}
