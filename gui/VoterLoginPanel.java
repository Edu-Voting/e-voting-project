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


public class VoterLoginPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtEmail;
	private JTextField txtRegCode;
	public JButton btnLogin;
	private String email;
	private String regCode;
	

	/**
	 * Create the panel.
	 */
	public VoterLoginPanel() {
		setBackground(SystemColor.inactiveCaptionBorder);
		setLayout(null);
		setSize(new Dimension(800, 800));
		
		JPanel surrodingPanel = new JPanel();
		surrodingPanel.setBackground(SystemColor.inactiveCaptionBorder);
		surrodingPanel.setBounds(33, 152, 684, 236);
		add(surrodingPanel);
		surrodingPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("E-mail Adress:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(58, 14, 140, 27);
		surrodingPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Registration Code:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(58, 76, 140, 27);
		surrodingPanel.add(lblNewLabel_1);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(225, 11, 400, 27);
		surrodingPanel.add(txtEmail);
		txtEmail.setColumns(10);
		
		txtRegCode = new JTextField();
		txtRegCode.setBounds(225, 75, 400, 27);
		surrodingPanel.add(txtRegCode);
		txtRegCode.setColumns(10);
		
		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLogin.setBounds(513, 139, 112, 46);
		surrodingPanel.add(btnLogin);
		
		JLabel lblNewLabel_4 = new JLabel("Voter Login");
		lblNewLabel_4.setBounds(33, 25, 268, 65);
		add(lblNewLabel_4);
		lblNewLabel_4.setForeground(new Color(0, 0, 51));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 30));

	}
	
	public void loginClick() {
		if (txtEmail.getText().equals("") || txtEmail.getText()==null ) {
			JOptionPane.showMessageDialog(null, "Email Is Not Entered!" );			
		} else {
			email = txtEmail.getText();
			regCode = txtRegCode.getText();
		}
	}

	public String getEmail() {
		return email;
	}

	public String getRegCode() {
		return regCode;
	}
}
