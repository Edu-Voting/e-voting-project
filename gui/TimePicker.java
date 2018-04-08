package gui;

import java.awt.Dimension;
import java.awt.SystemColor;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;


public class TimePicker extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSpinner hour;
	private JSpinner minute;
	/**
	 * Create the panel.
	 */
	public TimePicker() {
		
		setBackground(SystemColor.inactiveCaptionBorder);
		setLayout(null);
		setSize(new Dimension(110, 22));
		hour = new JSpinner();
		hour.setModel(new SpinnerListModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));
		hour.setBounds(0, 0, 45, 20);
		add(hour);
		
		minute = new JSpinner();
		minute.setModel(new SpinnerListModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"}));
		minute.setBounds(65, 0, 42, 20);
		add(minute);
		
		JLabel lblNewLabel = new JLabel(":");
		lblNewLabel.setBounds(55, 3, 21, 14);
		add(lblNewLabel);
		

	}
	public Date getDate(){
		return new Date();
	}
	
	public String getTimeAsStr() {		
		String strTime = hour.getValue().toString() + ":" + minute.getValue().toString();
		return strTime;
	}
	/*
	public String getTimeAsStr() {		
		String strTime = hour.getValue().toString() + ":" + minute.getValue().toString();
		return strTime;
	}
	*/
}
