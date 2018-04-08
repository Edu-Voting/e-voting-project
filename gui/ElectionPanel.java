package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ElectionPanel extends JPanel {
	/**
	 * Create the panel.
	 */
	private static final long serialVersionUID = 1L;
	public JButton btnCastVote;
	JButton btnQueryVote;
	public ElectionPanel() {
		setBackground(SystemColor.inactiveCaptionBorder);
		setLayout(null);
		setSize(new Dimension(800, 800));
		
		JPanel surrodingPanel = new JPanel();
		surrodingPanel.setBackground(SystemColor.inactiveCaptionBorder);
		surrodingPanel.setBounds(33, 129, 684, 533);
		add(surrodingPanel);
		surrodingPanel.setLayout(null);
		
		btnCastVote = new JButton("Cast A Vote");
		btnCastVote.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCastVote.setBounds(231, 11, 261, 217);
		surrodingPanel.add(btnCastVote);
		
		btnQueryVote = new JButton("Query My Vote");
		btnQueryVote.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnQueryVote.setBounds(231, 268, 261, 217);
		surrodingPanel.add(btnQueryVote);
		
		JLabel lblNewLabel_4 = new JLabel("Election Panel");
		lblNewLabel_4.setBounds(33, 25, 268, 65);
		add(lblNewLabel_4);
		lblNewLabel_4.setForeground(new Color(0, 0, 51));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 30));

	}
}
