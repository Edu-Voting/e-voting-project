package gui;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class Datepicker extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox dday;
	private JComboBox dMon;
	private JComboBox dYear;
	private TimePicker timePicker_1;
	/**
	 * Create the panel.
	 */
	public Datepicker() {
		setBackground(SystemColor.inactiveCaptionBorder);
		setLayout(null);
		
		dday = new JComboBox();
		dday.setBackground(SystemColor.inactiveCaptionBorder);
		dday.setBounds(0, 0, 66, 20);
		dday.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"}));
		dday.setSelectedIndex(23);
		add(dday);
		
		dMon = new JComboBox();
		dMon.setModel(new DefaultComboBoxModel(new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec"}));
		dMon.setBounds(76, 0, 67, 20);
		add(dMon);
		
		dYear = new JComboBox();
		dYear.setModel(new DefaultComboBoxModel(new String[] {"1940", "1941", "1942", "1943", "1944", "1945", "1946", "1947", "1948", "1949", "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957", "1958", "1959", "1960", "1961", "1962", "1963", "1964", "1965", "1966", "1967", "1968", "1969", "1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989", "1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030"}));
		dYear.setSelectedIndex(77);
		dYear.setBounds(153, 0, 67, 20);
		add(dYear);
		
		timePicker_1 = new TimePicker();
		timePicker_1.setBounds(230, 0, 112, 20);
		add(timePicker_1);
		
		setSize(new Dimension(342, 20));

	}
	
	public Date getDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String strDate = dday.getSelectedItem().toString() + "-" + Integer.toString(dMon.getSelectedIndex()+1);
		strDate += "-" + dYear.getSelectedItem().toString() + " " + timePicker_1.getTimeAsStr();
		Date date = null; 
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return date;
	}
	
	/*
	public Date getDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
		String strDate = dday.getSelectedItem().toString() + Integer.toString(dMon.getSelectedIndex()+1);
		strDate += dYear.getSelectedItem().toString() + timePicker_1.getTimeAsStr();
		Date date = null; 
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return date;
	}*/
}
