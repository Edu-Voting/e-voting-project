package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class QueryMyVotePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtRegCode;
	private JTextField txtYourVote;
	private String regCode;
	public JButton btnCheckMyVote;
	
	
	/**
	 * Create the panel.
	 */
	public QueryMyVotePanel() {
		setBackground(SystemColor.inactiveCaptionBorder);
		setLayout(null);
		setSize(new Dimension(800, 800));
		
		JPanel surrodingPanel = new JPanel();
		surrodingPanel.setBackground(SystemColor.inactiveCaptionBorder);
		surrodingPanel.setBounds(33, 152, 684, 236);
		add(surrodingPanel);
		surrodingPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Registration Code:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(58, 14, 140, 27);
		surrodingPanel.add(lblNewLabel);
		
		txtRegCode = new JTextField();
		txtRegCode.setBounds(225, 11, 400, 27);
		surrodingPanel.add(txtRegCode);
		txtRegCode.setColumns(10);
		
		txtYourVote = new JTextField();
		txtYourVote.setEnabled(false);
		txtYourVote.setBounds(225, 177, 400, 27);
		surrodingPanel.add(txtYourVote);
		txtYourVote.setColumns(10);
		
		btnCheckMyVote = new JButton("Check My Vote");
		btnCheckMyVote.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCheckMyVote.setBounds(225, 65, 400, 46);
		surrodingPanel.add(btnCheckMyVote);
		
		JLabel lblYourVote = new JLabel("Your Vote:");
		lblYourVote.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblYourVote.setBounds(58, 177, 140, 27);
		surrodingPanel.add(lblYourVote);
		
		JLabel lblNewLabel_4 = new JLabel("Query My Vote");
		lblNewLabel_4.setBounds(33, 25, 268, 65);
		add(lblNewLabel_4);
		lblNewLabel_4.setForeground(new Color(0, 0, 51));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 30));

	}
	
	public void checkMyVoteClick() {
		if (txtRegCode.getText().equals("") || txtRegCode.getText()==null ) {
			JOptionPane.showMessageDialog(null, "Registration Code Is Not Entered!" );			
		} else {
			regCode = txtRegCode.getText(); 
		}
	}

	public String getRegCode() {
		return regCode;
	}
}
