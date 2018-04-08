package gui;

import javax.swing.JPanel;

import controller.Controller;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.standard.Chromaticity;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class StatusPanel extends JPanel {

	public JLabel status;

	/**
	 * Create the panel.
	 */
	public StatusPanel() {
		
		status = new JLabel("##");
		add(status);
		setSize(new Dimension(800, 800));

	}
	public void publishStatus(String str){
		status.setText(str);
	}
	public void announceResult() throws Exception{
		Map<Integer,String> a = Controller.getInstance().getResultMap();
		Set<Integer> s = a.keySet();
		Object[] array = s.toArray();
		String str="";
		for(int i=0; i < a.size(); i++){
			String candidate = array[i].toString();
			String value = (new ArrayList<String>(a.values())).get(i);
			str+="Candidate: " + candidate +" value: " + value + "\n";
		}
		status.setText(str);
	}

}
