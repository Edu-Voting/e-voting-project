package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class AdminPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JButton btnCreateElection;
	private JButton btnStopElection;
	public Datepicker endRegDp;
	public Datepicker startElDp;
	public Datepicker endElDp;
	public Datepicker startRegDp;
	public JTextField txtElectionTitle;
	//private Controller controller=null;
	
	
	/**
	 * Create the panel.
	 * @throws Exception 
	 */
	public AdminPanel() throws Exception {
		//controller = Controller.getInstance();
		
		setBackground(SystemColor.inactiveCaptionBorder);
		setLayout(null);
		setSize(new Dimension(800, 800));
		
		JPanel surrodingPanel = new JPanel();
		surrodingPanel.setBackground(SystemColor.inactiveCaptionBorder);
		surrodingPanel.setBounds(33, 125, 712, 554);
		add(surrodingPanel);
		surrodingPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Voter Registration");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(58, 64, 140, 27);
		surrodingPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Start Date:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(58, 113, 140, 27);
		surrodingPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Election");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(58, 284, 140, 27);
		surrodingPanel.add(lblNewLabel_2);
		
		/*
		JLabel lblElTitle = new JLabel("Election Title");
		lblElTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblElTitle.setBounds(58, 234, 140, 27);
		surrodingPanel.add(lblElTitle);
		*/
		
		startRegDp = new Datepicker();
		startRegDp.setBounds(174, 113, 361, 27);
		surrodingPanel.add(startRegDp);
		
		JLabel lblEndDate = new JLabel("End Date:");
		lblEndDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEndDate.setBounds(58, 154, 105, 27);
		surrodingPanel.add(lblEndDate);
		
		btnStopElection = new JButton("Reset Election");
		btnStopElection.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnStopElection.setBounds(395, 501, 275, 42);
		surrodingPanel.add(btnStopElection);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(21, 257, 681, 16);
		surrodingPanel.add(separator);
		
		btnCreateElection = new JButton("Create Election");
		btnCreateElection.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCreateElection.setBounds(58, 501, 275, 42);
		surrodingPanel.add(btnCreateElection);
		
		endRegDp = new Datepicker();
		endRegDp.setBounds(174, 154, 361, 27);
		surrodingPanel.add(endRegDp);
		
		JLabel label = new JLabel("Start Date:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setBounds(58, 325, 140, 27);
		surrodingPanel.add(label);
		
		startElDp = new Datepicker();
		startElDp.setBounds(174, 325, 361, 27);
		surrodingPanel.add(startElDp);
		
		endElDp = new Datepicker();
		endElDp.setBounds(174, 366, 361, 27);
		surrodingPanel.add(endElDp);
		
		JLabel label_1 = new JLabel("End Date:");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_1.setBounds(58, 366, 105, 27);
		surrodingPanel.add(label_1);
		
		JLabel lblElectionTitle = new JLabel("Election Title:");
		lblElectionTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblElectionTitle.setBounds(58, 11, 140, 27);
		surrodingPanel.add(lblElectionTitle);
		
		txtElectionTitle = new JTextField();
		txtElectionTitle.setColumns(10);
		txtElectionTitle.setBounds(208, 11, 399, 27);
		surrodingPanel.add(txtElectionTitle);
		
	
		
		JLabel lblNewLabel_4 = new JLabel("Admin Panel");
		lblNewLabel_4.setBounds(33, 25, 268, 65);
		add(lblNewLabel_4);
		lblNewLabel_4.setForeground(new Color(0, 0, 51));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 30));

	}
}
