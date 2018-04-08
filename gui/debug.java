package gui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

public class debug extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JButton startreg;
	public JButton endreg;
	public JButton startvote;
	public JButton endvote;
	public JButton btnQueryMy;
	public JButton btnLogin;
	public JButton btnBullet;

	/**
	 * Create the panel.
	 */
	public debug() {
		setLayout(null);
		
		startreg = new JButton("start regist");
		startreg.setBounds(10, 16, 89, 23);
		add(startreg);
		
		endreg = new JButton("end regist");
		endreg.setBounds(109, 16, 89, 23);
		add(endreg);
		
		startvote = new JButton("start voting");
		startvote.setBounds(209, 16, 89, 23);
		add(startvote);
		
		endvote = new JButton("end voting");
		endvote.setBounds(306, 16, 89, 23);
		add(endvote);
		
		
		setSize(new Dimension(436, 103));

	}

}
